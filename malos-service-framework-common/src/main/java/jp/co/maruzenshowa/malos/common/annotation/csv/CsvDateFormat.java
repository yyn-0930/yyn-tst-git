package jp.co.maruzenshowa.malos.common.annotation.csv;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CSVオブジェクトに日付データ型のフォーマットを定義する.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface CsvDateFormat {
	/**
	 * データの日付フォーマット
	 * 
	 * @return 日付フォーマット
	 */
	String pattern() default "";
}
