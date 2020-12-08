package com.excel.database.converter.service.impl;

import com.excel.database.converter.service.IDatabaseExportService;
import com.excel.database.converter.service.IDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
public class MysqlDatabaseServiceImpl implements IDatabaseService, IDatabaseExportService {

    @Value("${datasource.mysql.jdbc-url}")
    private String jdbcUrl;

    @Value("${datasource.mysql.username}")
    private String username;

    @Value("${datasource.mysql.password}")
    private String password;

    @Override
    public String createDatabase(String databaseName) throws SQLException {
        String url = jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/") + 1) + databaseName + jdbcUrl.substring(jdbcUrl.indexOf("?"));
        log.info("创建数据库：{}。", url);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s", databaseName));
            DatabaseMetaData meta = connection.getMetaData();
            log.info("驱动：{}，版本：{}。", meta.getDriverName(), meta.getDriverVersion());
        }
        return url;
    }

    @Override
    public boolean dropDatabase(String databaseUrl, String databaseName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format("DROP DATABASE IF EXISTS %s", databaseName));
            log.info("删除数据库：{}。", databaseName);
        }
        return true;
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
                    String value = entry.getValue();
                    if (null == value) {
                        value = "";
                    }
                    if (value.length() > 255) {
                        log.warn("数据长度超限，{}:{}。", entry.getKey(), value);
                        value = value.substring(0, 254);
                        log.warn("数据截取，{}:{}。", entry.getKey(), value);
                    }
                    preparedStatement.setString(entry.getKey() + 1, value);
                }
                preparedStatement.executeUpdate();
            }
        }
        return true;
    }

    @Override
    public boolean exportDatabaseStructure(String databaseUrl, String databaseName, String sqlFilePath) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {
            // 写入文件头。
            writeExportFileHead(writer);

            // 遍历数据表。
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(String.format("SHOW FULL TABLES FROM `%s` WHERE TABLE_TYPE = 'BASE TABLE'", databaseName))) {
                while (resultSet.next()) {
                    String tableName = resultSet.getString(1);
                    // 导出表结构。
                    exportTableStruct(connection, writer, tableName);
                }
            }

            // 写入文件尾。
            writeExportFileTail(writer);
        }
        return true;
    }

    @Override
    public boolean exportDatabaseStructureAndData(String databaseUrl, String databaseName, String sqlFilePath) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {
            // 写入文件头。
            writeExportFileHead(writer);

            // 遍历数据表。
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(String.format("SHOW FULL TABLES FROM `%s` WHERE TABLE_TYPE = 'BASE TABLE'", databaseName))) {
                while (resultSet.next()) {
                    String tableName = resultSet.getString(1);
                    // 导出表结构。
                    exportTableStruct(connection, writer, tableName);
                    // 导出表数据。
                    exportTableData(connection, writer, tableName);
                }
            }

            // 写入文件尾。
            writeExportFileTail(writer);
        }
        return true;
    }

    @Override
    public boolean exportTableStructure(String databaseUrl, String tableName, String sqlFilePath) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {
            // 写入文件头。
            writeExportFileHead(writer);
            // 导出表结构。
            exportTableStruct(connection, writer, tableName);
            // 写入文件尾。
            writeExportFileTail(writer);
        }
        return true;
    }

    @Override
    public boolean exportTableStructureAndData(String databaseUrl, String tableName, String sqlFilePath) throws SQLException, IOException {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {
            // 写入文件头。
            writeExportFileHead(writer);
            // 导出表结构。
            exportTableStruct(connection, writer, tableName);
            // 导出表数据。
            exportTableData(connection, writer, tableName);
            // 写入文件尾。
            writeExportFileTail(writer);
        }
        return true;
    }

    @Override
    public List<Map<String, String>> exportTableData(@NotNull String databaseUrl, String tableName, String sqlForSelect) throws Exception {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password);
             Statement statement = connection.createStatement()) {

            String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?"));
            String sql = String.format("SELECT `column_name` FROM `information_schema`.COLUMNS WHERE `table_schema` = '%s' AND `table_name` = '%s'",
                    databaseName, tableName);

            Map<Integer, String> column = new HashMap<>(8);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String columnName = resultSet.getString(1);
                column.put(column.size() + 1, columnName);
            }

            List<Map<String, String>> dataList = new ArrayList<>();
            resultSet = statement.executeQuery(sqlForSelect);
            while (resultSet.next()) {
                Map<String, String> dataMap = new HashMap<>(8);
                for (Map.Entry<Integer, String> entry : column.entrySet()) {
                    String value = resultSet.getString(entry.getValue());
                    dataMap.put(entry.getValue(), value);
                }
                dataList.add(dataMap);
            }
            return dataList;
        }
    }

    /**
     * 导出表结构
     * @param connection 连接
     * @param writer 缓存写入器
     * @param tableName 表名
     * @throws SQLException 异常
     * @throws IOException 异常
     */
    private void exportTableStruct(Connection connection, BufferedWriter writer, String tableName) throws SQLException, IOException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("SHOW CREATE TABLE `%s`", tableName))) {
            if (resultSet.next()) {
                writer.newLine();
                writer.newLine();
                writer.write("-- ----------------------------");
                writer.newLine();
                writer.write(String.format("-- Table structure for `%s`", tableName));
                writer.newLine();
                writer.write("-- ----------------------------");
                writer.newLine();
                writer.write(String.format("DROP TABLE IF EXISTS `%s`;", tableName));
                writer.newLine();
                writer.write(resultSet.getString(2) + ";");
                writer.newLine();
                writer.flush();
            }
        }
    }

    /**
     * 导出表数据
     * @param connection 连接
     * @param writer 缓存写入器
     * @param tableName 表名
     * @throws SQLException 异常
     * @throws IOException 异常
     */
    private void exportTableData(Connection connection, BufferedWriter writer, String tableName) throws SQLException, IOException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(1) FROM `%s`", tableName))) {
            int rowCount = resultSet.next() ? resultSet.getInt(1) : 0;
            if (0 >= rowCount) {
                return ;
            }

            writer.write("-- ----------------------------");
            writer.newLine();
            writer.write(String.format("-- Data for the table `%s`", tableName));
            writer.newLine();
            writer.write("-- ----------------------------");
            writer.newLine();
        }

        try (Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setFetchSize(Integer.MIN_VALUE);
            statement.setFetchDirection(ResultSet.FETCH_REVERSE);
            try (ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM `%s`", tableName))) {
                while (resultSet.next()) {
                    writer.write(String.format("INSERT INTO `%s` VALUES (", tableName));

                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int j = 0; j < columnCount; j ++) {
                        if (j > 0) {
                            writer.write(',');
                        }
                        Object columnValue = resultSet.getObject(j + 1);
                        writer.write(null != columnValue ? String.format("'%s'", escapeString(columnValue.toString())) : "NULL");
                    }
                    writer.write(");");
                    writer.newLine();
                    writer.flush();
                }
            }
        }
    }

    /**
     * 导出例行程序
     * @param connection 连接
     * @param writer 缓存写入器
     * @param databaseName 库名
     * @throws SQLException 异常
     * @throws IOException 异常
     */
    private void exportRoutines(Connection connection, BufferedWriter writer, String databaseName) throws SQLException, IOException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("SELECT `SPECIFIC_NAME` from `INFORMATION_SCHEMA`.`ROUTINES` " +
                     "WHERE `ROUTINE_SCHEMA` = '%s' AND ROUTINE_TYPE = 'PROCEDURE'; ", databaseName))) {
            while (resultSet.next()) {
                String procedureName = resultSet.getString(1);
                exportProcedure(connection, writer, procedureName);
            }
        }
    }

    /**
     * 导出存储过程
     * @param connection 连接
     * @param writer 缓存写入器
     * @param procedureName 存储过程名称
     * @throws SQLException 异常
     * @throws IOException 异常
     */
    private void exportProcedure(Connection connection, BufferedWriter writer, String procedureName) throws SQLException, IOException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("SHOW CREATE PROCEDURE `%s`", procedureName))) {
            if (resultSet.next()) {
                writer.newLine();
                writer.newLine();
                writer.write("-- ----------------------------");
                writer.newLine();
                writer.write(String.format("-- Procedure structure for procedure `%s`", procedureName));
                writer.newLine();
                writer.write("-- ----------------------------");
                writer.newLine();
                writer.write(String.format("/*!50003 DROP PROCEDURE IF EXISTS `%s`*/;", procedureName));
                writer.newLine();
                writer.write("DELIMITER $$");
                writer.newLine();
                writer.append("/*!50003 ").append(resultSet.getString(3)).append(" */$$");
                writer.newLine();
                writer.write("DELIMITER ;");
            }
        }
    }

    private void writeExportFileHead(@NotNull BufferedWriter writer) throws IOException {
        writer.write("/*");
        writer.newLine();
        writer.write("!40101 SET NAMES utf8;");
        writer.newLine();
        writer.write("!40101 SET SQL_MODE='';");
        writer.newLine();
        writer.write("!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;");
        writer.newLine();
        writer.write("!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;");
        writer.newLine();
        writer.write("!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';");
        writer.newLine();
        writer.write("!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0;");
        writer.newLine();
        writer.write("*/");
        writer.newLine();
        writer.flush();
    }

    private void writeExportFileTail(@NotNull BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.newLine();
        writer.write("/*");
        writer.newLine();
        writer.write("!40101 SET SQL_MODE=@OLD_SQL_MODE;");
        writer.newLine();
        writer.write("!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;");
        writer.newLine();
        writer.write("!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;");
        writer.newLine();
        writer.write("!40111 SET SQL_NOTES=@OLD_SQL_NOTES;");
        writer.newLine();
        writer.write("*/");
        writer.newLine();
        writer.flush();
    }

    /**
     * 字符转义
     * @param source 源字符串
     * @return 转义后字符串
     */
    private String escapeString(String source) {
        if (StringUtils.isEmpty(source)) {
            return source;
        }

        StringBuilder stringBuilder = new StringBuilder((int)((double) source.length() * 1.1000000000000001D));
        for (int i = 0; i < source.length(); i++) {
            char character = source.charAt(i);
            switch(character) {
                case 0:
                    // '\0'
                    stringBuilder.append('\\');
                    stringBuilder.append('0');
                    continue;
                case 10:
                    // '\n'
                    stringBuilder.append('\\');
                    stringBuilder.append('n');
                    continue;
                case 13:
                    // '\r'
                    stringBuilder.append('\\');
                    stringBuilder.append('r');
                    continue;
                case 92:
                    // '\\'
                    stringBuilder.append('\\');
                    stringBuilder.append('\\');
                    continue;
                case 39:
                    // '\''
                    stringBuilder.append('\\');
                    stringBuilder.append('\'');
                    continue;
                case 34:
                    // '"'
                    stringBuilder.append('\\');
                    stringBuilder.append('"');
                    continue;
                case 26:
                    // '\032'
                    stringBuilder.append('\\');
                    stringBuilder.append('Z');
                    continue;
                default:
                    break;
            }
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }
}
