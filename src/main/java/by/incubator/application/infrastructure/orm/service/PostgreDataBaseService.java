package by.incubator.application.infrastructure.orm.service;

import by.incubator.application.infrastructure.core.Context;
import by.incubator.application.infrastructure.core.annotations.Autowired;
import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.orm.ConnectionFactory;
import by.incubator.application.infrastructure.orm.annotations.Column;
import by.incubator.application.infrastructure.orm.annotations.ID;
import by.incubator.application.infrastructure.orm.annotations.Table;
import by.incubator.application.infrastructure.orm.enums.SqlFieldType;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class PostgreDataBaseService {
    @Autowired
    private ConnectionFactory connectionFactory;
    private Map<String, String> classToSql;
    private Map<String, String> insertPatternByClass;
    @Autowired
    private Context context;
    private static final String SEQ_NAME = "id_seq";
    private static final String CHECK_SEQ_SQL_PATTERN =
            "SELECT EXISTS (\n" +
                    "   SELECT FROM information_schema.sequences \n" +
                    "   WHERE sequence_schema = 'public'\n" +
                    "   AND   sequence_name   = '%s'\n" +
                    ");";
    private static final String CREATE_ID_SEQ_PATTERN =
            "CREATE SEQUENCE %S\n" +
                    "   INCREMENT 1\n" +
                    "   START 1;";
    private static final String CHECK_TABLE_SQL_PATTERN =
            "SELECT EXISTS (\n" +
                    "   SELECT FROM information_schema.tables \n" +
                    "   WHERE table_schema = 'public' \n" +
                    "   AND   table_name   = '%s'\n" +
                    ");";
    private static final String CREATE_TABLE_SQL_PATTERN =
            "CREATE TABLE %s (\n" +
                    "    %s integer PRIMARY KEY DEFAULT nextval('%s')" +
                    "    %s\n);";
    private static final String INSERT_SQL_PATTERN =
            "INSERT INTO %s(%s)\n" +
                    "   VALUES (%s)\n" +
                    "   RETURNING %s;";
    private Map<String, String> insertByClassPattern;

    public PostgreDataBaseService() {
    }

    @InitMethod
    public void init() throws Exception {
        classToSql = Arrays.stream(SqlFieldType.values())
                .collect(Collectors.toMap(sqlFieldType -> sqlFieldType.getType().getName(),
                        SqlFieldType::getSqlType));
        insertPatternByClass = Arrays.stream(SqlFieldType.values())
                .collect(Collectors.toMap(sqlFieldType -> sqlFieldType.getType().getName(),
                        SqlFieldType::getInsertPattern));

        if (!checkSeq()) {
            createSeq();
        }

        Set<Class<?>> entities = context.getConfig().
                getScanner().getReflections().getTypesAnnotatedWith(Table.class);

        validateEntities(entities);

        for (Class<?> entity : entities) {
            if (!isTableExist(entity)) {
                createTableByEntity(entity);
            }
        }

        insertByClassPattern = new HashMap<>();
        entities.stream().forEach(entity -> insertByClassPattern.put(entity.getName(), getQueryByClass(entity)));
    }

    @SneakyThrows
    public Long save(Object obj) {
        Long id;
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field idField = getIdField(fields);
        Object[] values = getObjectFieldValues(obj);
        String sql = String.format(insertByClassPattern.get(clazz.getName()), values);

        try (Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            resultSet.next();
            id = resultSet.getLong(getIdFieldName(idField));
            setId(obj, idField, id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }

        return id;
    }

    public <T> Optional<T> get(Long id, Class<T> clazz) {
        checkTableAnnotation(clazz);
        String sql = "SELECT * FROM " + clazz.getDeclaredAnnotation(Table.class).name() +
                " WHERE " + getIdFieldName(clazz.getDeclaredFields()) + " = " + id;

        try(Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            return Optional.of(makeObject(resultSet, clazz));
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    @SneakyThrows
    public <T> List<T> getAll(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        checkTableAnnotation(clazz);
        String sql = "SELECT * FROM " + clazz.getDeclaredAnnotation(Table.class).name();

        try (Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            while(resultSet.next()) {
                list.add(makeObject(resultSet, clazz));
            }
        }

        return list;
    }

    @SneakyThrows
    private <T> T makeObject(ResultSet resultSet, Class<T> clazz) {
        T object = clazz.getConstructor().newInstance();
        Field[] objectFields = clazz.getDeclaredFields();
        resultSet.next();

        for (Field field : objectFields) {
            String column;
            if (field.isAnnotationPresent(ID.class)) {
                column = field.getDeclaredAnnotation(ID.class).name();
            } else {
                column = field.getDeclaredAnnotation(Column.class).name();
            }

            field.setAccessible(true);

            if (field.getType().equals(Integer.class)) {
                field.set(object, resultSet.getInt(column));
            } else if (field.getType().equals(Long.class)) {
                field.set(object, resultSet.getLong(column));
            } else if (field.getType().equals(Date.class)) {
                field.set(object, resultSet.getDate(column));
            } else if (field.getType().equals(Double.class)) {
                field.set(object, resultSet.getDouble(column));
            } else {
                field.set(object, resultSet.getString(column));
            }
        }

        return object;
    }

    private void checkTableAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new RuntimeException("Table annotation not present");
        }
    }

    private void setId(Object obj, Field idField, Long id) {
        idField.setAccessible(true);
        try {
            idField.set(obj, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private String getIdFieldName(Field... fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(ID.class)) {
                return field.getAnnotation(ID.class).name();
            }
        }

        throw new RuntimeException("Table hasn't got 'ID' field");
    }

    private Object[] getObjectFieldValues(Object obj) {
        List<Object> valuesList = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                try {
                    valuesList.add(field.get(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return valuesList.toArray();
    }

    private Field getIdField(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(ID.class)) {
                return field;
            }
        }

        throw new RuntimeException("Table hasn't got 'ID' field");
    }

    private String getQueryByClass(Class<?> clazz) {
        String tableNAme = clazz.getDeclaredAnnotation(Table.class).name();
        Field[] entityDeclaredFields = clazz.getDeclaredFields();
        String insertFields = Arrays.stream(entityDeclaredFields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> field.getDeclaredAnnotation(Column.class).name())
                .reduce((name1, name2) -> name1 + ", " + name2).get();

        String values = Arrays.stream(entityDeclaredFields)
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> insertPatternByClass.get(field.getType().getName()))
                .reduce((name1, name2) -> name1 + ", " + name2).get();

        String idFieldName = getIDFieldName(entityDeclaredFields);
        String sql = String.format(INSERT_SQL_PATTERN, tableNAme, insertFields, values, idFieldName);

        return sql;
    }

    private String getValues(Field[] entityDeclaredFields) {
        StringBuilder values = new StringBuilder();

        for (Field field : entityDeclaredFields) {
            if (field.isAnnotationPresent(Column.class)) {
                values.append(insertPatternByClass
                        .get(field.getType().getName()));
                values.append(",");
            }
        }

        values.deleteCharAt(values.length() - 1);
        return values.toString();
    }

    private String getInsertFields(Field[] entityDeclaredFields) {
        StringBuilder insertFields = new StringBuilder();

        for (Field field : entityDeclaredFields) {
            if (field.isAnnotationPresent(Column.class)) {
                insertFields.append(field.getDeclaredAnnotation(Column.class).name());
                insertFields.append(",");
            }
        }

        insertFields.deleteCharAt(insertFields.length() - 1);
        return insertFields.toString();
    }

    private void createTableByEntity(Class<?> entity){
        String tableName = entity.getDeclaredAnnotation(Table.class).name();
        Field[] entityDeclaredFields = entity.getDeclaredFields();
        String idField = getIDFieldName(entityDeclaredFields);
        String fields = buildFieldsString(entityDeclaredFields);
        String sql = String.format(CREATE_TABLE_SQL_PATTERN,
                tableName, idField, SEQ_NAME, fields);

        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()){
             statement.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String buildFieldsString(Field[] entityDeclaredFields) {
        StringBuilder fields = new StringBuilder();

        for (Field field : entityDeclaredFields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getDeclaredAnnotation(Column.class);
                fields.append(",\n");
                fields.append(column.name());
                fields.append(" ");
                fields.append(classToSql.get(field.getType().getName()));
                fields.append(" ");
                fields.append(column.unique() ? "UNIQUE " : "");
                fields.append(column.nullable() ? "" : "NOT NULL");
            }
        }

        return fields.toString();
    }

    private String getIDFieldName(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(ID.class)) {
                return field.getAnnotation(ID.class).name();
            }
        }

        throw new RuntimeException("No @ID field found");
    }

    private boolean isTableExist(Class<?> entity) {
        String tableName = entity.getDeclaredAnnotation(Table.class).name();

        try (Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement();
             ResultSet resultSet = statement
                     .executeQuery(String.format(CHECK_TABLE_SQL_PATTERN, tableName))) {
            if (resultSet.next()) {
                return resultSet.getBoolean("exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    private void validateEntities(Set<Class<?>> entities) {
        for (Class<?> entity : entities) {
            Field[] fields = entity.getDeclaredFields();
            checkIDFields(fields);
            checkColumnFields(fields);
        }
    }

    private void checkColumnFields(Field[] fields)  {
        boolean correct = true;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class) &&
                    field.getType().isPrimitive()) {
                throw new RuntimeException("Field with @Column annotation is primitive");
            }
        }

        Set<Field> uniqueFields = new HashSet<>();

        for (Field field : fields) {
            if (!uniqueFields.add(field)) {
                throw new RuntimeException("Fields have the same name");
            }
        }

    }

    private void checkIDFields(Field[] fields) {
        boolean correct = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(ID.class) && field.getType().equals(Long.class)) {
                correct = true;
            }
        }

        if (!correct) {
            throw new RuntimeException("No @ID field with Long type");
        }
    }

    private void createSeq() {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()){
            String sql = String.format(CREATE_ID_SEQ_PATTERN, SEQ_NAME);
            statement.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean checkSeq() {
        boolean result = false;

        try  (Connection connection = connectionFactory.getConnection();
              Statement statement = connection.createStatement()){
            String sql = String.format(CHECK_SEQ_SQL_PATTERN, SEQ_NAME);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getBoolean("exists");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }
}
