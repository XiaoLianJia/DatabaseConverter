package com.excel.database.converter.service;

import com.excel.database.converter.enums.LanguageCodeEnum;

/**
 * <p>
 *     翻译服务接口类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
public interface ITranslateService {

    /**
     * 翻译
     * @param text 待译内容
     * @param sourceLanguage 原语言
     * @param targetLanguage 目标语言
     * @return 译后内容
     * @throws Exception 异常
     */
    String translate(String text, LanguageCodeEnum sourceLanguage, LanguageCodeEnum targetLanguage) throws Exception;

    /**
     * 翻译
     * @param text 待译内容
     * @param sourceLanguage 原语言
     * @param targetLanguage 目标语言
     * @return 译后内容
     * @throws Exception 异常
     */
    String translate(String text, String sourceLanguage, String targetLanguage) throws Exception;
}
