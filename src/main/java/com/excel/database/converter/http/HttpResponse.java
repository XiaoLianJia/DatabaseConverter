package com.excel.database.converter.http;

import com.excel.database.converter.enums.HttpResponseCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     响应
 * </p>
 * @author zhangbin
 * @date 2019-04-29
 * @param <T>
 */
@ApiModel("请求返回")
@Data
public final class HttpResponse<T> implements Serializable {

	private static final long serialVersionUID = 8460434669622955662L;

	@ApiModelProperty(value = "状态码")
	private String code;

	@ApiModelProperty(value = "状态信息")
	private String msg;

	@ApiModelProperty(value = "业务数据")
	private T result;

	@Contract(pure = true)
	public HttpResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Contract(pure = true)
	public HttpResponse(String code, String msg, T result) {
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	@NotNull
	@Contract(" -> new")
	public static <T> HttpResponse<T> success() {
		return new HttpResponse<>(HttpResponseCodeEnum.SUCCESS.getCode(), HttpResponseCodeEnum.SUCCESS.getMsg());
	}

	@NotNull
	@Contract("_ -> new")
	public static <T> HttpResponse<T> success(T result) {
		return new HttpResponse<>(HttpResponseCodeEnum.SUCCESS.getCode(), HttpResponseCodeEnum.SUCCESS.getMsg(), result);
	}


	@NotNull
	@Contract(value = "_, _ -> new", pure = true)
	public static <T> HttpResponse<T> error(String code, String msg) {
		return new HttpResponse<>(code, msg);
	}

	@Contract(value = "_, _, _ -> new", pure = true)
	@NotNull
	public static <T> HttpResponse<T> error(String code, String msg ,T result) {
		return new HttpResponse<>(code, msg ,result);
	}

	@NotNull
	@Contract("_ -> new")
	public static <T> HttpResponse<T> error(@NotNull HttpResponseCodeEnum error) {
		return new HttpResponse<>(error.getCode(), error.getMsg());
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <T> HttpResponse<T> pageSuccess(Page<T> page) {
		if (null == page) {
			return success();
		}
		HttpPageResponse<List<T>> pageResult = new HttpPageResponse<>();
		pageResult.setData(page.getContent());
		pageResult.setTotalPages(page.getTotalPages());
		pageResult.setTotalElements(page.getTotalElements());
		pageResult.setPageNumber(page.getNumber() + 1);
		pageResult.setPageSize(page.getSize());
		return (HttpResponse<T>) new HttpResponse<>(HttpResponseCodeEnum.SUCCESS.getCode(), HttpResponseCodeEnum.SUCCESS.getMsg(), pageResult);
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <T> HttpResponse<T> pageSuccess(PageInfo<T> pageInfo) {
		if (null == pageInfo) {
			return success();
		}
		HttpPageResponse<List<T>> pageResult = new HttpPageResponse<>();
		pageResult.setData(pageInfo.getList());
		pageResult.setTotalPages(pageInfo.getPages());
		pageResult.setTotalElements(pageInfo.getTotal());
		pageResult.setPageNumber(pageInfo.getPageNum());
		pageResult.setPageSize(pageInfo.getSize());
		return (HttpResponse<T>) new HttpResponse<>(HttpResponseCodeEnum.SUCCESS.getCode(), HttpResponseCodeEnum.SUCCESS.getMsg(), pageResult);
	}
}
