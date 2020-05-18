package com.excel.database.converter.controller;

import com.excel.database.converter.ApplicationTest;
import com.excel.database.converter.util.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *     转换控制层接口测试类
 * </p>
 * @author zhangbin
 * @date 2020-05-18
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConvertControllerTest {

    private String url = "http://localhost:2551/convert/";

    private String fileUpload = ApplicationTest.class.getResource("/").getPath() + "demo.xlsx";

    @Test
    public void excel() throws IOException {
        System.out.println(HttpUtil.post(url + "excel", new File(fileUpload)));
    }

    @Test
    public void excelToSqlite() throws IOException {
        File fileDownload = new File(ApplicationTest.class.getResource("/").getPath() + "demo.db");
        if (fileDownload.exists()
                || fileDownload.createNewFile()) {
            if (HttpUtil.post(url + "excel_to_sqlite", new File(fileUpload), fileDownload)) {
                System.out.println(fileDownload.getPath());
                System.out.println(fileDownload.getTotalSpace());
                fileDownload.deleteOnExit();
            }
        }
    }

    @Test
    public void excelToMysql() throws IOException {
        File fileDownload = new File(ApplicationTest.class.getResource("/").getPath() + "demo.sql");
        if (fileDownload.exists()
                || fileDownload.createNewFile()) {
            if (HttpUtil.post(url + "excel_to_mysql", new File(fileUpload), fileDownload)) {
                System.out.println(fileDownload.getPath());
                System.out.println(fileDownload.getTotalSpace());
                fileDownload.deleteOnExit();
            }
        }
    }
}
