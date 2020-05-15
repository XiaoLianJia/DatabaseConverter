package com.excel.database.converter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
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

    private String databaseName = "mysqlTest";

    private String tableName = "tableTest";

    @Test
    public void crateDatabase() throws Exception {
        String databaseUrl = databaseService.createDatabase(databaseName);
        System.out.println(databaseUrl);
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
            }
        }
    }
}
