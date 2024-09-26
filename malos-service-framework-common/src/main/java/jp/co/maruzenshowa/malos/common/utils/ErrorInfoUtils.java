package jp.co.maruzenshowa.malos.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jp.co.maruzenshowa.malos.common.base.dto.ErrorInfo;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * AsyncContextAspect.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * エラー情報作成ユーティリティクラス <br>
 * 単項目チェックのBindingResultを編集して、エラー情報を返却する. <br>
 * <br>
 */
public class ErrorInfoUtils {

  private static final String VALIDATOR_PACKAGE_NAME = "jp.co.maruzenshowa.malos.common.annotation.validator";

  /**
   * メソッド名 ： コンストラクター. <br>
   * メソッド説明 ： ユーティリティのprivateコンストラクター <br>
   * <br>
   */
  private ErrorInfoUtils() {}

  /**
   * メソッド名 ： エラー情報変換処理. <br>
   * メソッド説明 ： 単項目チェック結果からエラー情報を変換する. <br>
   * <br>
   * 
   * @param bindingResult 単項目チェック結果
   * @return エラー情報リスト
   */
  public static List<ErrorInfo> convertToErrorInfo(BindingResult bindingResult) {
    return convertToErrorInfo(bindingResult, null);
  }

  /**
   * メソッド名 ： エラー情報変換処理(バッチ). <br>
   * メソッド説明 ： 行番号を指定できる単項目チェック結果からエラー情報を変換する. <br>
   * <br>
   * 
   * @param bindingResult 単項目チェック結果
   * @param lineNumber 行番号
   * @return エラー情報リスト
   */
  public static List<ErrorInfo> convertToErrorInfo(BindingResult bindingResult, Integer lineNumber) {
    List<ErrorInfo> errorInfoList = new ArrayList<>();
    List<FieldError> allErrors = bindingResult.getFieldErrors();

    if (!CollectionUtils.isEmpty(allErrors)) {
      for (FieldError fieldError : allErrors) {
        Annotation[] annotations = getAnnotations(bindingResult.getTarget(), fieldError);
        for (Annotation annotation : annotations) {
          String annotationName = fieldError.getCode();
          if (VALIDATOR_PACKAGE_NAME.equals(annotation.annotationType().getPackageName()) && annotationName != null
              && annotation.annotationType().getCanonicalName().endsWith(annotationName)) {
            errorInfoList.add(getErrorInfo(annotation, fieldError.getField(), lineNumber));
          }
        }
      }
    }

    return errorInfoList;
  }

  /**
   * メソッド名 ： アノテーション一覧取得処理. <br>
   * メソッド説明 ： アノテーション一覧を取得する. <br>
   * <br>
   * 
   * @param target アノテーション対象
   * @param fieldError エラーフィールド
   * @return アノテーション一覧
   */
  private static Annotation[] getAnnotations(Object target, FieldError fieldError) {
    if (target != null) {
      Field field = ReflectionUtils.findField(target.getClass(), fieldError.getField());
      if (field != null) {
        return field.getAnnotations();
      }
    }
    return new Annotation[0];
  }

  /**
   * メソッド名 ： エラー情報取得処理. <br>
   * メソッド説明 ： エラー情報を取得する. <br>
   * <br>
   * 
   * @param annotation アノテーション
   * @param lineNumber 行番号
   * @return エラー情報
   */
  private static ErrorInfo getErrorInfo(Annotation annotation, String fildName, Integer lineNumber) {
    String code = (String) getMethodValue(annotation, "code");
    Integer min = (Integer) getMethodValue(annotation, "min");
    Integer max = (Integer) getMethodValue(annotation, "max");
    Integer length = (Integer) getMethodValue(annotation, "length");
    Integer decimal = (Integer) getMethodValue(annotation, "decimal");
    String[] key = (String[]) getMethodValue(annotation, "key");
    String[] value = (String[]) getMethodValue(annotation, "value");
    Map<String, String> param = new ConcurrentHashMap<>();
    if (key == null || value == null || key.length != value.length) {
      throw new SystemException(MessageCode.A9001E.name());
    } else {
      param.put("name", fildName);
      putParam(param, "min", min);
      putParam(param, "max", max);
      putParam(param, "length", length);
      putParam(param, "decimal", decimal);
      putParam(param, "lineNumber", lineNumber);
      for (int i = 0; i < key.length; i++) {
        param.put(key[i], value[i]);
      }
    }

    return new ErrorInfo(code, param);
  }


  /**
   * メソッド名 ： パラメータ追加処理. <br>
   * メソッド説明 ： パラメータを追加する. <br>
   * <br>
   * 
   * @param param パラメータ
   * @param key キー
   * @param value 対象値
   */
  private static void putParam(Map<String, String> param, String key, Integer value) {
    if (value != null && value != -1) {
      param.put(key, String.valueOf(value));
    }
  }

  /**
   * メソッド名 ： アノテーションの情報取得処理. <br>
   * メソッド説明 ： アノテーションの情報を取得する. <br>
   * <br>
   * 
   * @param annotation アノテーション
   * @param key キー
   * @return 対象値
   */
  private static Object getMethodValue(Annotation annotation, String key) {
    Method valueMethod = ReflectionUtils.findMethod(annotation.getClass(), key);
    if (valueMethod != null) {
      return ReflectionUtils.invokeMethod(valueMethod, annotation);
    }
    return null;
  }
}
