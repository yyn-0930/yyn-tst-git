package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 半角英数字チェック判定のクラス.<br>
 * 半角英数字チェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class HalfAlphanumericChkValidator implements ConstraintValidator<HalfAlphanumericChk, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ValidatorUtils.isHalfAlphanumeric(value);
	}

}
