package com.excel.database.converter.controller;

import com.excel.database.converter.service.IExcelService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * <p>
 *     转换控制层
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@Slf4j
@Controller
@RequestMapping("convert")
public class ConvertController {

    @Resource
    private IExcelService excelService;

    @PostMapping("excel")
    @ResponseBody
    public void excel(@NotNull MultipartFile file) throws IOException {
        excelService.read(file.getInputStream());
    }

    @PostMapping("excel_to_sqlite")
    @ResponseBody
    public ResponseEntity<FileSystemResource> excelToSqlite(@NotNull MultipartFile file) throws IOException {
        log.info("Excel：{}。", file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (null == fileName) {
            log.error("文件名为空。");
            return null;
        }

        String searchFor = ".";
        if (-1 != fileName.lastIndexOf(searchFor)) {
            fileName = fileName.substring(0, fileName.lastIndexOf(searchFor));
        }
        fileName = String.format("SQlite%s.sqlite", fileName);
        File database = new File(System.getProperty("user.dir") + File.separator + fileName);
        if (database.exists()
                && ! database.delete()) {
            log.error("文件名冲突。");
            return null;
        }

        excelService.convertToSqlite(file.getInputStream(), database);
        log.info("SQlite：{}。", database.getPath());
        return export(database);
    }

    @PostMapping("excel_to_mysql")
    @ResponseBody
    public ResponseEntity<FileSystemResource> excelToMysql(@NotNull MultipartFile file) throws Exception {
        log.info("Excel：{}。", file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (null == fileName) {
            log.error("文件名为空。");
            return null;
        }

        String searchFor = ".";
        if (-1 != fileName.lastIndexOf(searchFor)) {
            fileName = fileName.substring(0, fileName.lastIndexOf(searchFor));
        }
        fileName = String.format("MySQL%s.sql", fileName);
        File database = new File(System.getProperty("user.dir") + File.separator + fileName);
        if (database.exists()
                && ! database.delete()) {
            log.error("文件名冲突。");
            return null;
        }

        excelService.convertToMysql(file.getInputStream(), database);
        log.info("MySQL：{}。", database.getPath());
        return export(database);
    }

    @PostMapping("excel_to_mysql/save")
    @ResponseBody
    public String saveExcelToMysql(@NotNull MultipartFile file) throws Exception {
        log.info("Excel：{}。", file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        if (null == fileName) {
            log.error("文件名为空。");
            return null;
        }

        String searchFor = ".";
        if (-1 != fileName.lastIndexOf(searchFor)) {
            fileName = fileName.substring(0, fileName.lastIndexOf(searchFor));
        }

        log.info("Save to MySQL：`{}`.`{}`。", fileName, fileName);
        excelService.saveToMysql(file.getInputStream(), fileName);
        return "Success";
    }

    @PostMapping("excel_to_mysql/save_with_db_param")
    @ResponseBody
    public String saveExcelToMysql(@NotNull MultipartFile file,
                                   @RequestParam("databaseName") String databaseName,
                                   @RequestParam("tableName") String tableName) throws Exception {
        log.info("Excel：{}。", file.getOriginalFilename());
        log.info("Save to MySQL：`{}`.`{}`。", databaseName, tableName);
        excelService.saveToMysql(file.getInputStream(), databaseName, tableName);
        return "Success";
    }

    @Contract("null -> null")
    private ResponseEntity<FileSystemResource> export(File file) throws UnsupportedEncodingException {
        if (null == file) {
            return null;
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        httpHeaders.add("Last-Modified", new Date().toString());
        httpHeaders.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }
}
