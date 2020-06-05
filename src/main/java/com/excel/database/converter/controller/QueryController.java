package com.excel.database.converter.controller;

import com.alibaba.fastjson.JSON;
import com.excel.database.converter.service.IDatabaseExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     查询控制层
 * </p>
 * @author zhangbin
 * @date 2020-06-05
 */
@Slf4j
@RestController
@RequestMapping("query")
public class QueryController {

    @Resource
    @Qualifier("Mysql")
    private IDatabaseExportService mysqlDatabaseExportService;

    @Value("${datasource.mysql.jdbc-url}")
    private String jdbcUrl;

    @PostMapping("mysql")
    @ResponseBody
    public String queryFromMysql(@RequestParam("databaseName") String databaseName,
                                 @RequestParam("tableName") String tableName,
                                 @RequestParam("select") String sqlForSelect) throws Exception {
        log.info("Query from MySQL：`{}`.`{}`。", databaseName, tableName);
        log.info("SELECT: {}。", sqlForSelect);

        String databaseUrl = jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/") + 1) + databaseName + jdbcUrl.substring(jdbcUrl.indexOf("?"));
        List<Map<String, String>> data = mysqlDatabaseExportService.exportTableData(databaseUrl, tableName, sqlForSelect);
        log.info("DATA: {}。", JSON.toJSONString(data));
        return JSON.toJSONString(data);
    }
}
