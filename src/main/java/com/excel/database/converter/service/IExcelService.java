package com.excel.database.converter.service;

import java.io.File;
import java.io.InputStream;

/**
 * <p>
 *     Excel服务接口类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
public interface IExcelService {

    /**
     * 读取Excel
     * @param excel Excel
     */
    void read(InputStream excel);

    /**
     * Excel转储到SQlite
     * @param excel Excel
     * @param database SQlite
     */
    void convertToSqlite(InputStream excel, File database);

    /**
     * Excel转储到MySQL
     * @param excel Excel
     * @param database MySQL
     * @throws Exception 异常
     */
    void convertToMysql(InputStream excel, File database) throws Exception;

    /**
     * Excel转储到MySQL
     * @param excel Excel
     * @param databaseName MySQL库名
     */
    void saveToMysql(InputStream excel, String databaseName);
}
