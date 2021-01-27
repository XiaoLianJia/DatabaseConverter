package com.excel.database.converter.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *     日期字符串相互转换工具
 * </p>
 * @author zhangbin
 * @date 2019-05-11
 */
public class DateUtil {

	public final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期对象转换成指定格式字符串
	 * @param date 日期
	 * @param format 格式
	 * @return 字符串
	 */
	@NotNull
	public static String format(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 日期对象转换成指定格式字符串
	 * @param longDate 日期
	 * @param format 格式
	 * @return 字符串
	 */
	@Nullable
	public static String format(Long longDate, String format) {
		try {
			Date date = new Date(longDate);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 日期格式字符串转换成日期对象
	 * @param dateString 字符串
	 * @param format 格式
	 * @return 日期
	 * @throws ParseException 异常
	 */
	public static Date parse(String dateString, String format) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(dateString);
	}
}
