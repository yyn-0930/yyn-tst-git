package jp.co.maruzenshowa.malos.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * APIタイムアウトのインデックスを定義する.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiTimeOut {
	/**
	 * タイムアウトの値
	 * 
	 * @return インデックス
	 */
	int value();
}
