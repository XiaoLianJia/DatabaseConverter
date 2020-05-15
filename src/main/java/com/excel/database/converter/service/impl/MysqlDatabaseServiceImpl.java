package com.excel.database.converter.service.impl;

import com.excel.database.converter.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     MySQL数据库服务实现类
 * </p>
 * @author zhangbin
 * @date 2020-05-15
 */
@Slf4j
@Service("Mysql")
public class MysqlDatabaseServiceImpl implements IDatabaseService {

    @Value("${datasource.mysql.jdbc-url}")
    private String jdbcUrl;

    @Value("${datasource.mysql.username}")
    private String username;

    @Value("${datasource.mysql.password}")
    private String password;

    @Value("${datasource.mysql.driver-class-name}")
    private String driverClassName;

    @Override
    public String createDatabase(String databaseName) throws SQLException, ClassNotFoundException {
        String url = jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/") + 1) + databaseName + jdbcUrl.substring(jdbcUrl.indexOf("?"));
        log.info("创建数据库：{}。", url);

        Class.forName(driverClassName);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s", databaseName));
            DatabaseMetaData meta = connection.getMetaData();
            log.info("驱动：{}，版本：{}。", meta.getDriverName(), meta.getDriverVersion());
        }
        return url;
    }

    @Override
    public boolean crateTable(String databaseUrl, String tableName, @NotNull Map<Integer, String> fields) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, String> entry : fields.entrySet()) {
            stringBuilder.append(String.format(", `%s` varchar(255)", entry.getValue()));
        }

        String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?"));
        String sql = String.format("CREATE TABLE IF NOT EXISTS `%s`.`%s`(`id` int(0) NOT NULL AUTO_INCREMENT%s, PRIMARY KEY (`id`)) CHARSET=utf8;",
                databaseName, tableName, stringBuilder.toString());
        log.info("创建数据表：{}。", tableName);
        log.info("SQL：{}。", sql);

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        return true;
    }

    @Override
    public boolean dropTable(String databaseUrl, String tableName) throws SQLException {
        String sql = "DROP TABLE if EXISTS " + tableName;
        log.info("删除数据表：{}。", tableName);
        log.info("SQL：{}。", sql);

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
        return true;
    }

    @Override
    public boolean insert(String databaseUrl, String tableName,
                          @NotNull Map<Integer, String> fields, Map<Integer, String> data) throws SQLException {
        StringBuilder stringBuilderFields = new StringBuilder();
        StringBuilder stringBuilderValues = new StringBuilder();
        for (Map.Entry<Integer, String> entry : fields.entrySet()) {
            if (stringBuilderFields.toString().isEmpty()) {
                stringBuilderFields.append(String.format("`%s`", entry.getValue()));
                stringBuilderValues.append("?");
            } else {
                stringBuilderFields.append(String.format(", `%s`", entry.getValue()));
                stringBuilderValues.append(", ?");
            }
        }

        String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?"));
        String sql = String.format("INSERT INTO `%s`.`%s`(%s) VALUES (%s);",
                databaseName, tableName, stringBuilderFields.toString(), stringBuilderValues.toString());
        log.info("插入数据表：{}。", tableName);
        log.info("SQL：{}。", sql);

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, String> entry : data.entrySet()) {
                preparedStatement.setString(entry.getKey() + 1, entry.getValue());
            }
            preparedStatement.executeUpdate();
        }
        return true;
    }

    @Override
    public boolean insert(String databaseUrl, String tableName,
                          @NotNull Map<Integer, String> fields, List<Map<Integer, String>> list) throws SQLException {
        StringBuilder stringBuilderFields = new StringBuilder();
        StringBuilder stringBuilderValues = new StringBuilder();
        for (Map.Entry<Integer, String> entry : fields.entrySet()) {
            if (stringBuilderFields.toString().isEmpty()) {
                stringBuilderFields.append(String.format("`%s`", entry.getValue()));
                stringBuilderValues.append("?");
            } else {
                stringBuilderFields.append(String.format(", `%s`", entry.getValue()));
                stringBuilderValues.append(", ?");
            }
        }

        String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?"));
        String sql = String.format("INSERT INTO `%s`.`%s`(%s) VALUES (%s);",
                databaseName, tableName, stringBuilderFields.toString(), stringBuilderValues.toString());
        log.info("插入数据表：{}。", tableName);
        log.info("SQL：{}。", sql);

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Map<Integer, String> data : list) {
                for (Map.Entry<Integer, String> entry : data.entrySet()) {
                    preparedStatement.setString(entry.getKey() + 1, entry.getValue());
                }
                preparedStatement.executeUpdate();
            }
        }
        return true;
    }
}
