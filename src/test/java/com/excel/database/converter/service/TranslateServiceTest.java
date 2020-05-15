package com.excel.database.converter.service;

import com.excel.database.converter.enums.LanguageCodeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * <p>
 *     翻译服务测试类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TranslateServiceTest {

    @Resource
    private ITranslateService translateService;

    @Test
    public void translate() throws Exception {
        String text = "China";
        System.out.println("原文：" + text);
        System.out.println("简体：" + translateService.translate(text, LanguageCodeEnum.ENGLISH, LanguageCodeEnum.CHINESE_SIMPLIFIED));
        System.out.println("繁体：" + translateService.translate(text, LanguageCodeEnum.ENGLISH.getCode(), LanguageCodeEnum.CHINESE_TRADITIONAL.getCode()));
    }
}
