package com.excel.database.converter.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Excel读取过程监听类
 * </p>
 * @author zhangbin
 * @date 2020-05-14
 */
@Slf4j
public class ExcelReadListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 批次处理量，达到此数据量先进行转储，方便内存回收。
     */
    private static final int BATCH_COUNT = 10;

    /**
     * 临时数据存储区。
     */
    private final List<Map<Integer, String>> list = new ArrayList<>();

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条表头数据：{}。", JSON.toJSONString(headMap));
    }

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
        log.info("解析到一条数据：{}。", JSON.toJSONString(integerStringMap));
        list.add(integerStringMap);
        if (list.size() >= BATCH_COUNT) {
            log.info("====================");
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
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
}
