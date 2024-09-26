package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 全角チェック判定のクラス.<br>
 * 全角チェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class FullWidthChkValidator implements ConstraintValidator<FullWidthChk, String> {
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ValidatorUtils.isFullWidth(value);
	}

}
