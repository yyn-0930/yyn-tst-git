package jp.co.maruzenshowa.malos.common.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * メールアドレスチェック判定のクラス.<br>
 * メールアドレスチェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class MailChkValidator implements ConstraintValidator<MailChk, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return ValidatorUtils.isValidMail(value);
	}

}
