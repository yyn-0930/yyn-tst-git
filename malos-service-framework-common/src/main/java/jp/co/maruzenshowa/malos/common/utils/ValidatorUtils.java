package jp.co.maruzenshowa.malos.common.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 項目チェック共通ユーティリティクラス.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public class ValidatorUtils {

    /**
     * インスタンス化禁止.
     */
    private ValidatorUtils() {}
    
    /**
     * 空値チェック.
     * 
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isNotEmpty(Object value) {
        return  false;
    }
    
    /**
     * 半角数字チェック
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isHalfDigits(String value) {
        final String regex = "^\\d+$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }
    
    /**
     * 半角英数字チェック
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isHalfAlphanumeric(String value) {
        final String regex = "^[A-Za-z0-9]+$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }
    
    /**
     * 半角英数字記号チェック
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isHalfAlphanumericMark(String value) {
        final String regex = "^[a-zA-Z0-9!-/:-@\\[-`{-~]*$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }
    
    /**
     * メールアドレスチェック
     * ルール：英数字、アンダーバー、ハイフン、＠、ドット
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isValidMail(String value) {
        final String regex = "^[a-zA-Z0-9_\\-\\.]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+){1,4}$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }
    
    /**
     * 数字区間有効性チェック
     * @param value チェック対象
     * @param min 最小値 (設定なしの場合、-1)
     * @param max 最大値 (設定なしの場合、-1)
     * @return true/false
     */
    public static boolean isValidDigitRange(int value, int min, int max) {
    	boolean overMax = max != -1 && value > max;
    	boolean lessMin = min != -1 && value < min;
    	return !overMax && !lessMin;
    }
    
    /**
     * 全角文字列チェック
     * @param value チェック対象
     * @return true/false
     */
    public static boolean isFullWidth(String value) {
        final String regex = "^[^!-~｡-ﾟ]*$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }

    /**
     * 半角カナチェック
     * @param value チェック対象
     * @return true/false
     */
    public static  boolean isHalfKana(String value) {
        final String regex = "^[ｦ-ﾟ]+$";
        return StringUtils.isEmpty(value) || Pattern.matches(regex, value);
    }
    
    /**
     * 桁数チェック
     * 小数部分は固定長でチェックする
     * @param value チェック対象
     * @param length 桁数
     * @param decimal 小数桁数(固定)
     * @return true/false
     */
    public static boolean checkLength(Object value, int length, int decimal) {

        String checkValue = String.valueOf(value);
        if (StringUtils.isEmpty(checkValue)) {
            return true;
        }
        // 数字以外の場合
        if (decimal < 0) {
            return checkValue.length() <= length;
        }
        final String point = ".";
        // 数字の場合
        // 小数点がない
        if (StringUtils.indexOf(checkValue, point) == -1) {
            return decimal == 0 && checkValue.length() == length;
        }
        // 小数点が１桁目
        if (StringUtils.indexOf(checkValue, point) == 0) {
            return checkValue.length() <= length && checkValue.length() == (decimal + 1);
        }
        // 上記以外
        String[] numValue = StringUtils.split(checkValue, point);
        if (length < 0) {
            // 小数桁数チェックのみ
            return numValue[1].length() == decimal;
        } else {
            // 整数分、少数分それぞれチェック
            return numValue[0].length() <= length && numValue[1].length() == decimal;
        }
    }
}
