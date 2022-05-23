package by.incubator.application.infrastructure.orm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public enum SqlFieldType {
    INTEGER(Integer.class, "integer", "%s"),
    LONG(Long.class, "integer", "%s"),
    DOUBLE(Double.class, "decimal", "%s"),
    STRING(String.class, "varchar(255)", "'%s'"),
    DATE(Date.class, "date", "'%s'");

    private final Class<?> type;
    private final String sqlType;
    private final String insertPattern;
}
