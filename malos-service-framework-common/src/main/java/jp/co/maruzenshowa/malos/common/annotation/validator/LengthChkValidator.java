package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 桁数チェック判定のクラス.<br>
 * 桁数チェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class LengthChkValidator implements ConstraintValidator<LengthChk, Object> {
	private int length;
	private int decimal;

	@Override
	public void initialize(LengthChk constarintAnnotation) {
		length = constarintAnnotation.length();
		decimal = constarintAnnotation.decimal();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return ValidatorUtils.checkLength(value, length, decimal);
	}

}
