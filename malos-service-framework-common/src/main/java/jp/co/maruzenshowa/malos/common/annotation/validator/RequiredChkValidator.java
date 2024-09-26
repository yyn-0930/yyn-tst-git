package jp.co.maruzenshowa.malos.common.annotation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jp.co.maruzenshowa.malos.common.utils.ValidatorUtils;

/**
 * 必須入力チェック判定のクラス.<br>
 * 必須入力チェックになっているかを判定する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class RequiredChkValidator implements ConstraintValidator<RequiredChk, Object> {

	/**
	 * 必須入力チェックになっているかを判定.
	 * 
	 * @param value 検証対象
	 * @param context バリデータのコンテキスト
	 * @return 有効かどうか
	 */
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return ValidatorUtils.isNotEmpty(value);
	}

}
