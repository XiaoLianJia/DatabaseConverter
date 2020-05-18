package com.excel.database.converter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <p>
 *     Excel服务测试类
 * </p>
 * @author zhangbin
 * @date 2020-05-18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExcelServiceTest {

    @Resource
    private IExcelService excelService;

    private String excel = ExcelServiceTest.class.getResource("/").getPath() + "demo.xlsx";

    @Test
    public void read() throws FileNotFoundException {
        excelService.read(new FileInputStream(new File(excel)));
    }

    @Test
    public void convertToSqlite() throws FileNotFoundException {
        File database = new File(ExcelServiceTest.class.getResource("/").getPath() + "demo.db");
        if (! database.exists()
                || database.delete()) {
            excelService.convertToSqlite(new FileInputStream(new File(excel)), database);
        }
        System.out.println(database.getPath());
    }

    @Test
    public void convertToMysql() throws Exception {
        File database = new File(ExcelServiceTest.class.getResource("/").getPath() + "demo.sql");
        if (! database.exists()
                || database.delete()) {
            excelService.convertToMysql(new FileInputStream(new File(excel)), database);
        }
        System.out.println(database.getPath());
    }
}
