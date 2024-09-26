package jp.co.maruzenshowa.malos.common.annotation.csv;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * CSVオブジェクトに日付データ型のヘッダー名を定義する.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CsvHeaderName {
	/**
	 * データのヘッダー名
	 * 
	 * @return ヘッダー名
	 */	
	String value();
}
