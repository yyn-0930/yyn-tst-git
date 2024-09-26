package jp.co.maruzenshowa.malos.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 半角カナチェックのインターフェース.<br>
 * 半角カナチェックになっているかを判定するインターフェースを定義する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HalfKanaChkValidator.class)
public @interface HalfKanaChk {
	
	/**
	 * message id
	 * @return String
	 */
	String message() default "";
	
	/**
	 * message code
	 * @return String
	 */
	String code() default "";
	
	/**
	 * message value
	 * 
	 * @return String[]
	 */
	String[] value() default {};
	
	/**
	 * message key
	 * 
	 * @return String[]
	 */
	String[] key() default {};
	
	/**
	 * groups
	 *  
	 * @return Class[]
	 */
	Class<?>[] groups() default {};
	
	/**
	 * payload
	 * @return Class
	 */
	Class<? extends Payload>[] payload() default { };
}
