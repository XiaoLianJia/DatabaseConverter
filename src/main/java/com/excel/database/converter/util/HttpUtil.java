package com.excel.database.converter.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build());

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet)) {
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			String result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
			EntityUtils.consume(httpEntity);
			return result;
		}
	}

	public static String post(String url, String json) throws IOException {
		return post(url, json, new HashMap<>(0));
	}

	public static String post(String url, String json, @NotNull Map<String, String> headers) throws IOException {
		StringEntity stringEntity = new StringEntity(json);
		stringEntity.setContentEncoding("UTF-8");
		stringEntity.setContentType("application/json");

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		httpPost.setEntity(stringEntity);
		httpPost.setConfig(RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build());

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			httpPost.addHeader(entry.getKey(), entry.getValue());
		}

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
			return EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
		}
	}

	public static String post(String url, List<NameValuePair> form) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(form, StandardCharsets.UTF_8));
		httpPost.setConfig(RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build());

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
			return EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
		}
	}

	@NotNull
	public static String post(String url, @NotNull File file) throws IOException {
		return post(url, file, new ArrayList<>());
	}

	@NotNull
	public static String post(String url, @NotNull File file, @NotNull List<NameValuePair> form) throws IOException {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.setCharset(StandardCharsets.UTF_8)
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY));

		for (NameValuePair pair : form) {
			builder.addPart(pair.getName(), new StringBody(pair.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
		}

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(builder.build());
		httpPost.setConfig(RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build());

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
			return EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
		}
	}

	public static boolean post(String url, @NotNull File fileUpload, File fileDownload) throws IOException {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create()
				.setCharset(StandardCharsets.UTF_8)
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("file", new FileBody(fileUpload, ContentType.DEFAULT_BINARY));

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(builder.build());
		httpPost.setConfig(RequestConfig.custom()
				.setConnectTimeout(1000 * CONNECT_TIMEOUT)
				.setSocketTimeout(1000 * SOCKET_TIMEOUT)
				.build());

		try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
			 CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
			 InputStream inputStream = closeableHttpResponse.getEntity().getContent();
			 OutputStream outputStream = new FileOutputStream(fileDownload)) {
			byte[] buffer = new byte[4096];
			int read;
			while ((read = inputStream.read(buffer)) > 0) {
				byte[] bytes = new byte[read];
				System.arraycopy(buffer, 0, bytes, 0, read);
				outputStream.write(bytes);
			}
			outputStream.flush();
			return true;
		}
	}

}
