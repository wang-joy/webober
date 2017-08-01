package com.ober.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化工具类
 * 
 * @author Administrator
 *
 */
public class DateUtil {

	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

	private static DateFormat getDateFormat(String pattern) {
		DateFormat dateFormat = threadLocal.get();
		if (dateFormat == null) {
			dateFormat = new SimpleDateFormat(pattern);
			threadLocal.set(dateFormat);
		}
		return dateFormat;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 * @throws ParseException
	 */
	public static String formatDate(Date date, String pattern) {
		String ymd = getDateFormat(pattern).format(date);
		return ymd;
	}

	/**
	 * 解析日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String pattern) {
		Date parseDate = null;
		try {
			parseDate = getDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parseDate;
	}
}
