package jp.co.maruzenshowa.malos.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.SystemException;


/**
 * 日付ユーティリティクラス.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public class DateUtils {

	public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * コンストラクター
	 */
	private DateUtils() {}
	
	/**
	 * 日付/時刻文字列を指定されたパターンの日付/時刻文字列にフォーマットします。
	 * @param source 元の日付/時刻文字列
	 * @param pattern パターン
	 * @return フォーマットされた日付/時刻文字列
	 */
	public static Date toDate(String source, String pattern) {
		if (source == null) {
			return null;
		}
		try {
			DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
			return format.parse(source);
		} catch (ParseException e) {
			throw new SystemException(MessageCode.A9009E.name(), e);
		}
	}


	/**
	 * 根据传入的字符串格式转换为日期.默认格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param date 日期
	 * @param pattern 格式
	 * @return
	 */
	public static String toString(Date date, String pattern) {
		if (date == null) {
			date = new Date();
		}
		if (pattern == null) {
			pattern = DEFAULT_DATE_TIME_FORMAT;
		}
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		} catch (Exception e) {
			throw new SystemException(MessageCode.A9009E.name(), e);
		}
	}
}
