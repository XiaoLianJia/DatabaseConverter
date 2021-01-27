package com.excel.database.converter.enums;

import lombok.Getter;

/**
 * <p>
 *     响应码枚举
 * </p>
 * @author zhangbin
 * @date 2019-04-29
 */
@Getter
public enum HttpResponseCodeEnum {

	/**
	 * 无异常。
	 */
	SUCCESS("200", "OK"),

    /**
     * 执行失败。
     */
    BUSINESS_FAILURE("500", "执行失败"),

	/**
	 * 未登录。
	 */
	ERROR_NOT_LOGGED_IN("501", "未登录"),

	/**
	 * 参数不全。
	 */
	ERROR_PARAMETER_MISSING("1001", "缺少参数/参数为空"),

	/**
	 * 参数重复。
	 */
	ERROR_EXISTED("1002", "参数已存在"),

	/**
	 * 参数缺失。
	 */
	ERROR_NOT_EXISTS("1003", "参数不存在"),

	/**
	 * 参数无效。
	 */
	ERROR_INVALID("1004", "参数无效"),

	/**
	 * 校验异常。
	 */
	ERROR_NOT_MATCH("1005", "帐号/密码不正确"),

	/**
	 * 资源不足。
	 */
	ERROR_LACK_OF_RESOURCE("1006", "资源不足"),

	/**
	 * 异常操作。
	 */
	ERROR_NOT_ALLOWED("1007", "不允许的操作"),

	/**
	 * 系统错误。
	 */
	ERROR_SYSTEM("9999", "系统错误");

	private String code;

	private String msg;

	HttpResponseCodeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static HttpResponseCodeEnum getByCode(String code) {
		for (HttpResponseCodeEnum anEnum : HttpResponseCodeEnum.values()) {
			if (anEnum.code.equals(code)) {
				return anEnum;
			}
		}
		return ERROR_SYSTEM;
	}
}
