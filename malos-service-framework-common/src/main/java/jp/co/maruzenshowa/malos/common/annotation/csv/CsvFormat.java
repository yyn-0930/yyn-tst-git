package jp.co.maruzenshowa.malos.common.annotation.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.QuoteType;

/**
 * CSVオブジェクトに出力形式を定義する。<br>
 * CSV出力のみ
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.MODULE, ElementType.TYPE})
public @interface CsvFormat {
	/**
	 * 引用符タイプ <br>
	 * @return 引用符タイプ
	 */
	QuoteType quoteType() default QuoteType.DOUBLE_QUOTATION;

	/**
	 * 区切り文字 <br>
	 * @return 区切り文字
	 */
	String delimiter() default GlobalConstant.CSV_DELIMITER_COMMA;

	/**
	 * ヘッダー出力有無 <br>
	 * @return ヘッダー出力有無
	 */
	boolean skipHeader() default false;
	
	/**
	 * 文字コード <br>
	 * @return 文字コード
	 */
	String charset() default GlobalConstant.CSV_CHARSET_UTF8;
}
