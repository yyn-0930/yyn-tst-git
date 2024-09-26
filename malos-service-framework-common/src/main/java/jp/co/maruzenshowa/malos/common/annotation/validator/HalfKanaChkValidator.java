package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 半角カナチェックのインターフェース.<br>
 * 半角カナチェックになっているかを判定するインターフェースを定義する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class HalfKanaChkValidator implements ConstraintValidator<HalfKanaChk, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ValidatorUtils.isHalfKana(value);
	}

}
