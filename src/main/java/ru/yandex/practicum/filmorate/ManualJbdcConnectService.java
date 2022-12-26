package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ManualJbdcConnectService {
    public static final String JDBC_DRIVER="org.h2.Driver";
    public static final String JDBC_URL="jdbc:h2:file:./db/filmorate";
    public static final String JDBC_USERNAME="sa";
    public static final String JDBC_PASSWORD="password";

    public JdbcTemplate getTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(JDBC_DRIVER);
        dataSource.setUrl(JDBC_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);
        return new JdbcTemplate(dataSource);
    }
}