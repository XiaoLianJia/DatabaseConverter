package com.excel.database.converter.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import com.excel.database.converter.service.IDatabaseService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Excel到数据库转储过程监听类
 * </p>
 * @author zhangbin
 * @date 2020-05-15
 */
@Slf4j
public class ExcelToDatabaseListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 批次处理量，达到此数据量先进行转储，方便内存回收。
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 临时数据存储区。
     */
    private List<Map<Integer, String>> list = new ArrayList<>();

    /**
     * 数据库服务。
     */
    private IDatabaseService databaseService;

    /**
     * 转存数据库地址。
     */
    private String databaseUrl;

    /**
     * 数据库名或者数据库文件路径。
     */
    @Getter
    @Setter
    private String database;

    /**
     * 数据库名称。
     */
    @Getter
    @Setter
    private String databaseName;

    /**
     * 数据库表名称。
     */
    @Getter
    @Setter
    private String tableName;

    /**
     * 数据库表字段。
     */
    private Map<Integer, String> tableFields = new HashMap<>(4);

    /**
     * 如果使用了spring需要使用这个构造方法，
     * 在创建Listener的时候把spring管理的类传进来。
     * @param databaseService 数据库服务
     */
    public ExcelToDatabaseListener(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条表头数据：{}。", JSON.toJSONString(headMap));
        tableFields = headMap;
        try {
            databaseUrl = databaseService.createDatabase(database);
            if (null != databaseUrl
                    && databaseService.crateTable(databaseUrl, tableName, headMap)) {
                log.info("创建数据库、数据表，库：{}，表：{}。", databaseName, tableName);
            }
        } catch (Exception e) {
            log.error("创建数据库、数据表失败，库：{}，表：{}。", databaseName, tableName);
            log.error(e.getMessage());
        }
    }

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
        log.info("解析到一条数据：{}。", JSON.toJSONString(integerStringMap));
        list.add(integerStringMap);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        log.info("所有数据解析完成。");
    }

    @Override
    public void onException(@NotNull Exception exception, AnalysisContext context) {
        log.error("解析失败，继续解析下一行：{}。", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("第{}行，第{}列解析异常，数据为：{}。", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    private void saveData() {
        try {
            if (databaseService.insert(databaseUrl, tableName, tableFields, list)) {
                log.info("插入数据成功，库：{}，表：{}，数据量：{}。", databaseName, tableName, list.size());
            }
        } catch (Exception e) {
            log.error("插入数据失败，库：{}， 表：{}。", databaseName, tableName);
            log.error(e.getMessage());
        }
    }
}
