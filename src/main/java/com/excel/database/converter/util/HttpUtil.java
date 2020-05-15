package com.excel.database.converter.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     HTTP工具类
 * </p>
 * @author zhangbin
 * @date 2019-05-21
 */
public class HttpUtil {

	private static final int CONNECT_TIMEOUT = 60;

	private static final int SOCKET_TIMEOUT = 60;

	public static String get(String url) throws IOException {
		return get(url, new HashMap<>(0));
	}

	public static String get(String url, @NotNull Map<String, String> headers) throws IOException {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build();

		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet)) {
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		}
	}

	public static String post(String url, String json) throws IOException {
		return post(url, json, new HashMap<>(0));
	}

	public static String post(String url, String json, @NotNull Map<String, String> headers) throws IOException {
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build();

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		httpPost.setEntity(stringEntity);
		httpPost.setConfig(requestConfig);

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpPost.addHeader(entry.getKey(), entry.getValue());
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		}
	}

	public static String post(String url, List<NameValuePair> form) throws IOException {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(form, "UTF-8"));
		httpPost.setConfig(requestConfig);

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		}
	}
}
