package com.excel.database.converter.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.excel.database.converter.enums.LanguageCodeEnum;
import com.excel.database.converter.service.ITranslateService;
import com.excel.database.converter.util.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     谷歌翻译服务实现类
 * </p>
 * @author zhangbin
 * @date 2020-05-13
 */
@Service
public class GoogleTranslateServiceImpl implements ITranslateService {

    @Override
    public String translate(String text, @NotNull LanguageCodeEnum sourceLanguage, @NotNull LanguageCodeEnum targetLanguage) throws Exception {
        return translate(text, sourceLanguage.getCode(), targetLanguage.getCode());
    }

    @Override
    public String translate(String text, @NotNull String sourceLanguage, @NotNull String targetLanguage) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("client", "gtx"));
        nameValuePairs.add(new BasicNameValuePair("sl", sourceLanguage));
        nameValuePairs.add(new BasicNameValuePair("tl", targetLanguage));
        nameValuePairs.add(new BasicNameValuePair("dt", "t"));
        nameValuePairs.add(new BasicNameValuePair("q", text));

        String response = HttpUtil.post("https://translate.googleapis.com/translate_a/single", nameValuePairs);
        if (null == response) {
            throw new Exception("翻译失败。");
        }

        StringBuilder stringBuilder = new StringBuilder();
        JSONArray jsonObject = JSONArray.parseArray(response);
        for (Object o : jsonObject.getJSONArray(0)) {
            JSONArray jsonArray = (JSONArray) o;
            stringBuilder.append(jsonArray.getString(0));
        }
        return stringBuilder.toString();
    }
}
