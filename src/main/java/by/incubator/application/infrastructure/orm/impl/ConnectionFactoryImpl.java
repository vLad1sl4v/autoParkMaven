package by.incubator.application.infrastructure.orm.impl;

import by.incubator.application.infrastructure.core.annotations.InitMethod;
import by.incubator.application.infrastructure.core.annotations.Property;
import by.incubator.application.infrastructure.orm.ConnectionFactory;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryImpl implements ConnectionFactory {
    @Property("url")
    private String url;
    @Property("username")
    private String username;
    @Property("password")
    private String password;
    private Connection connection;

    public ConnectionFactoryImpl() {

    }

    @SneakyThrows
    @InitMethod
    public void initConnection() {
        connection = DriverManager.getConnection(url, username, password);
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        if (!connection.isClosed()) {
            return connection;
        }

        return DriverManager.getConnection(url,username,password);
    }
}
