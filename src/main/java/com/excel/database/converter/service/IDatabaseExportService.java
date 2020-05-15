package com.excel.database.converter.service;

/**
 * <p>
 *     数据库导出服务接口类
 * </p>
 * @author zhangbin
 * @date 2020-05-15
 */
public interface IDatabaseExportService {

    /**
     * 导出数据库结构
     * @param databaseUrl 数据库URL
     * @param databaseName 库名
     * @param sqlFilePath 转存文件路径
     * @return 是否导出成功
     * @throws Exception 异常
     */
    boolean exportDatabaseStructure(String databaseUrl, String databaseName, String sqlFilePath) throws Exception;

    /**
     * 导出数据库结构和数据
     * @param databaseUrl 数据库URL
     * @param databaseName 库名
     * @param sqlFilePath 转存文件路径
     * @return 是否导出成功
     * @throws Exception 异常
     */
    boolean exportDatabaseStructureAndData(String databaseUrl, String databaseName, String sqlFilePath) throws Exception;

    /**
     * 导出数据表结构
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @param sqlFilePath 转存文件路径
     * @return 是否导出成功
     * @throws Exception 异常
     */
    boolean exportTableStructure(String databaseUrl, String tableName, String sqlFilePath) throws Exception;

    /**
     * 导出数据表结构和数据
     * @param databaseUrl 数据库URL
     * @param tableName 表名
     * @param sqlFilePath 转存文件路径
     * @return 是否导出成功
     * @throws Exception 异常
     */
    boolean exportTableStructureAndData(String databaseUrl, String tableName, String sqlFilePath) throws Exception;
}
