package com.excel.database.converter.service;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     MySQL数据库服务测试类
 * </p>
 * @author zhangbin
 * @date 2020-05-15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MysqlDatabaseServiceTest {

    @Resource
    @Qualifier("Mysql")
    private IDatabaseService databaseService;

    @Resource
    @Qualifier("Mysql")
    private IDatabaseExportService databaseExportService;

    private String sqlFilePath = System.getProperty("user.dir") + File.separator + "mysqlTest.sql";

    private String databaseName = "mysqlTest";

    private String tableName = "tableTest";

    private String tableName2 = "tableTest2";

    @Test
    public void createAndDropDatabase() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        boolean result = databaseService.dropDatabase(databaseUrl, databaseName);
        System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
    }

    @Test
    public void createAndDropTable() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>();
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        boolean result = databaseService.crateTable(databaseUrl, tableName, fields);
        System.out.println(String.format("创建数据表%s。", result ? "成功" : "失败"));

        result = databaseService.dropTable(databaseUrl, tableName);
        System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));
    }

    @Test
    public void insert() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }

    @Test
    public void exportDatabaseStructure() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)
                && databaseService.crateTable(databaseUrl, tableName2, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data)
                    && databaseService.insert(databaseUrl, tableName2, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            result = databaseExportService.exportDatabaseStructure(databaseUrl, databaseName, sqlFilePath);
            System.out.println(String.format("导出数据库结构%s。", result ? "成功" : "失败"));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName)
                        && databaseService.dropTable(databaseUrl, tableName2);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }

    @Test
    public void exportDatabaseStructureAndData() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)
                && databaseService.crateTable(databaseUrl, tableName2, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data)
                    && databaseService.insert(databaseUrl, tableName2, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            result = databaseExportService.exportDatabaseStructureAndData(databaseUrl, databaseName, sqlFilePath);
            System.out.println(String.format("导出数据库结构和数据%s。", result ? "成功" : "失败"));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName)
                        && databaseService.dropTable(databaseUrl, tableName2);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }

    @Test
    public void exportTableStructure() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)
                && databaseService.crateTable(databaseUrl, tableName2, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data)
                    && databaseService.insert(databaseUrl, tableName2, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            result = databaseExportService.exportTableStructure(databaseUrl, tableName, sqlFilePath);
            System.out.println(String.format("导出数据表结构%s。", result ? "成功" : "失败"));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName)
                        && databaseService.dropTable(databaseUrl, tableName2);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }

    @Test
    public void exportTableStructureAndData() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)
                && databaseService.crateTable(databaseUrl, tableName2, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data)
                    && databaseService.insert(databaseUrl, tableName2, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            result = databaseExportService.exportTableStructureAndData(databaseUrl, tableName, sqlFilePath);
            System.out.println(String.format("导出数据表结构和数据%s。", result ? "成功" : "失败"));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName)
                        && databaseService.dropTable(databaseUrl, tableName2);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }

    @Test
    public void exportData() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);

        Map<Integer, String> fields = new HashMap<>(4);
        fields.put(1, "字段1");
        fields.put(2, "字段2");
        fields.put(3, "字段3");

        if (databaseService.crateTable(databaseUrl, tableName, fields)) {
            Map<Integer, String> data = new HashMap<>(4);
            data.put(0, "数据1");
            data.put(1, "数据2");
            data.put(2, "数据3");

            boolean result = databaseService.insert(databaseUrl, tableName, fields, data);
            System.out.println(String.format("插入数据表%s。", result ? "成功" : "失败"));

            String sqlForSelect = String.format("SELECT * FROM `%s`", tableName);
            List<Map<String, String>> exportData = databaseExportService.exportTableData(databaseUrl, tableName, sqlForSelect);
            System.out.println(String.format("导出数据表数据: %s。", JSON.toJSONString(exportData)));

            if (result) {
                result = databaseService.dropTable(databaseUrl, tableName);
                System.out.println(String.format("删除数据表%s。", result ? "成功" : "失败"));

                result = databaseService.dropDatabase(databaseUrl, databaseName);
                System.out.println(String.format("删除数据库%s。", result ? "成功" : "失败"));
            }
        }
    }
}
