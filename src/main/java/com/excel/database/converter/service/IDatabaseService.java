package com.excel.database.converter.service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *     数据库服务接口类
 * </p>
 * @author zhangbin
 * @date 2020-05-14
 */
public interface IDatabaseService {

    /**
     * 创建库
     * @param database 库名或库文件路径
     * @return 数据库URL
     * @throws Exception 异常
     */
    String createDatabase(String database) throws Exception;

    /**
     * 删除库
     * @param databaseUrl 数据库URL
     * @param databaseName 库名
     * @return 是否删除成功
     * @throws Exception 异常
     */
    boolean dropDatabase(String databaseUrl, String databaseName) throws Exception;

    /**
     * 创建表
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @param fields 字段集合
     * @return 是否创建成功
     * @throws Exception 异常
     */
    boolean crateTable(String databaseUrl, String tableName, Map<Integer, String> fields) throws Exception;

    /**
     * 删除表
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @return 是否删除成功
     * @throws Exception 异常
     */
    boolean dropTable(String databaseUrl, String tableName) throws Exception;

    /**
     * 插入数据
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @param fields 字段
     * @param data 数据
     * @return 是否插入成功
     * @throws Exception 异常
     */
    boolean insert(String databaseUrl, String tableName, Map<Integer, String> fields, Map<Integer, String> data) throws Exception;

    /**
     * 插入数据（批量）
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @param fields 字段
     * @param list 数据集合
     * @return 是否插入成功
     * @throws Exception 异常
     */
    boolean insert(String databaseUrl, String tableName, Map<Integer, String> fields, List<Map<Integer, String>> list) throws Exception;
}
