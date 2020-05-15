package com.excel.database.converter.enums;

import lombok.Getter;

/**
 * <p>
 *     语言代码枚举类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@Getter
public enum LanguageCodeEnum {

    /**
     * 简体中文。
     */
    CHINESE_SIMPLIFIED("zh_cn", "Chinese Simplified"),

    /**
     * 繁体中文。
     */
    CHINESE_TRADITIONAL("zh_tw", "Chinese Traditional"),

    /**
     * 英文。
     */
    ENGLISH("en", "English");

    private String code;

    private String language;

    LanguageCodeEnum(String code, String language) {
        this.code = code;
        this.language = language;
    }
}
