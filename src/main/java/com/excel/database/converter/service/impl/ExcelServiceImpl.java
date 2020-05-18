package com.excel.database.converter.service.impl;

import com.alibaba.excel.EasyExcel;
import com.excel.database.converter.listener.ExcelReadListener;
import com.excel.database.converter.listener.ExcelToDatabaseListener;
import com.excel.database.converter.service.IDatabaseExportService;
import com.excel.database.converter.service.IDatabaseService;
import com.excel.database.converter.service.IExcelService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

/**
 * <p>
 *     Excel服务实现类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@Service
public class ExcelServiceImpl implements IExcelService {

    @Resource
    @Qualifier("Sqlite")
    private IDatabaseService sqliteDatabaseService;

    @Resource
    @Qualifier("Mysql")
    private IDatabaseService mysqlDatabaseService;

    @Resource
    @Qualifier("Mysql")
    private IDatabaseExportService mysqlDatabaseExportService;

    @Override
    public void read(@NotNull InputStream excel) {
        EasyExcel.read(excel, new ExcelReadListener()).sheet().doRead();
    }

    @Override
    public void convertToSqlite(@NotNull InputStream excel, @NotNull File database) {
        String searchFor = ".";
        String databaseName = database.getName().substring(0, database.getName().indexOf(searchFor));

        ExcelToDatabaseListener listener = new ExcelToDatabaseListener(sqliteDatabaseService);
        // 数据库文件路径赋值。
        listener.setDatabase(database.getPath());
        // 数据库名赋值。
        listener.setDatabaseName(databaseName);
        // 数据表名赋值。
        listener.setTableName(databaseName);
        EasyExcel.read(excel, listener).sheet().doRead();
    }

    @Override
    public void convertToMysql(InputStream excel, @NotNull File database) throws Exception {
        String searchFor = ".";
        String databaseName = database.getName().substring(0, database.getName().indexOf(searchFor));

        ExcelToDatabaseListener listener = new ExcelToDatabaseListener(mysqlDatabaseService);
        // 数据库名赋值。
        listener.setDatabase(databaseName);
        // 数据库名赋值。
        listener.setDatabaseName(databaseName);
        // 数据表名赋值。
        listener.setTableName(databaseName);
        EasyExcel.read(excel, listener).sheet().doRead();

        // 导出SQL文件。
        mysqlDatabaseExportService.exportDatabaseStructureAndData(listener.getDatabaseUrl(), databaseName, database.getPath());
        // 导出完成后删除数据库。
        mysqlDatabaseService.dropDatabase(listener.getDatabaseUrl(), databaseName);
    }
}
