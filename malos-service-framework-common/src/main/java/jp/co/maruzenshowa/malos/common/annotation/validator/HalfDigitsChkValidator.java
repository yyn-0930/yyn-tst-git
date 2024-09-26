package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 半角数字チェック判定のクラス.<br>
 * 半角数字チェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class HalfDigitsChkValidator implements ConstraintValidator<HalfDigitsChk, Object> {
	
	private int max;
	private int min;

	@Override
	public void initialize(HalfDigitsChk constarintAnnotation) {
		max = constarintAnnotation.max();
		min = constarintAnnotation.min();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return value == null || ValidatorUtils.isHalfDigits(value.toString()) && ValidatorUtils.isValidDigitRange(Integer.parseInt(value.toString()), min, max);
	}
}
