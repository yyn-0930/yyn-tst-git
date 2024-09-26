package jp.co.maruzenshowa.malos.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.util.Strings;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Version;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import jp.co.maruzenshowa.malos.common.exception.SystemException;

/**
 * 楽観ロックのEtag計算ユーティリティクラス.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class ETagUtils {

  private static final String ETAG_PACKAGE_NAME = "jp.co.maruzenshowa.malos";

  /**
   * コンストラクター
   */
  private ETagUtils() {}

  /**
   * MD5のハッシュ結果を返却する.
   * 
   * @param target ハッシュ対象
   * @return ハッシュ結果
   */
  private static String getHashString(String target) {
    try {
      HexFormat hex = HexFormat.of().withLowerCase();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] md5Byte = md5.digest(target.getBytes());
      return hex.formatHex(md5Byte);
    } catch (NoSuchAlgorithmException e) {
      throw new SystemException(e);
    }
  }

  /**
   * 楽観ロック値検証 楽観ロックエラーの場合、OptimisticLockExceptionをスローする 計算対象に＠Versionの子オブジェクトがある場合、子オブジェクト存在フラグにtrueを設定する
   * 
   * @param childVersionFlg 子オブジェクト存在フラグ
   * @param objects 計算対象
   */
  public static void validateEtag(boolean childVersionFlg, Object... objects) {
    if (!sumEtag(childVersionFlg, objects).equals(DynamicContext.getContext().getEtag())) {
      throw new OptimisticLockException();
    }
  }

  /**
   * 楽観ロックのEtag値を返却する ＠Versionの項目のハッシュ結果を返却する
   * 
   * @param target 計算対象
   * @return Etagの合計値
   */
  public static String getEtag(String target) {
    return getHashString(target);
  }

  /**
   * 楽観ロックのEtag値を合計して返却する ＠Versionの項目の値を合計して、ハッシュ結果を返却する 計算対象に＠Versionの子オブジェクトがある場合、子オブジェクト存在フラグにtrueを設定する
   * 
   * @param childVersionFlg 子オブジェクト存在フラグ
   * @param objects 計算対象
   * @return Etagの合計値
   */
  public static String sumEtag(boolean childVersionFlg, Object... objects) {
    long eTag = 0;
    try {
      for (Object obj : objects) {
        if (obj instanceof List) {
          List<?> objList = (List<?>) obj;
          if (objList != null && !objList.isEmpty()) {
            if (!objList.get(0).getClass().getPackageName().startsWith(ETAG_PACKAGE_NAME)) {
              break;
            }
            for (Object listItem : objList) {
              eTag += getVersionValue(childVersionFlg, listItem);
            }
          }
        } else if (obj instanceof String) {
          eTag += Integer.valueOf(obj.toString());
        } else {
          eTag += getVersionValue(childVersionFlg, obj);
        }
      }

      return getHashString(String.valueOf(eTag));
    } catch (Exception e) {
      throw new SystemException(e);
    }
  }

  private static long processObject(boolean childVersionFlg, Object obj) throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    long eTag = 0;
    if (obj instanceof List) {
      eTag = processList(childVersionFlg, (List<?>) obj);
    } else if (obj instanceof String) {
      eTag = Integer.valueOf((String) obj);
    } else {
      eTag = getVersionValue(childVersionFlg, obj);
    }
    return eTag;
  }

  private static long processList(boolean childVersionFlg, List<?> objList) throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    long eTag = 0;
    if (objList != null && !objList.isEmpty()) {
      if (getPackageName(objList)) {
        return 0;
      }
      for (Object listItem : objList) {
        eTag += getVersionValue(childVersionFlg, listItem);
      }
    }
    return eTag;
  }

  private static boolean getPackageName(List<?> objList) {
    return !objList.get(0).getClass().getPackageName().startsWith(ETAG_PACKAGE_NAME);
  }

  /**
   * ＠Versionの項目の値を合計して返却する 計算対象に＠Versionの子オブジェクトがある場合、子オブジェクト存在フラグにtrueを設定する
   * 
   * @param childVersionFlg 子オブジェクト存在フラグ
   * @param obj 計算対象
   * @return 合計値
   */
  private static long getVersionValue(boolean childVersionFlg, Object obj) throws NoSuchMethodException,
      SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    long temp = 0;
    if (obj == null) {
      return temp;
    }
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      String methodName = field.getName().substring(0, 1).toUpperCase(Locale.ROOT) + field.getName().substring(1);
      if (field.isAnnotationPresent(OneToMany.class)) {
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (Strings.isEmpty(oneToMany.mappedBy())) {
          continue;
        }
      } else if (field.isAnnotationPresent(OneToOne.class)) {
        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        if (Strings.isEmpty(oneToOne.mappedBy())) {
          continue;
        }
      } else if (field.isAnnotationPresent(ManyToOne.class)) {
        continue;
      } else if (field.isAnnotationPresent(ManyToMany.class)) {
        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (Strings.isEmpty(manyToMany.mappedBy())) {
          continue;
        }
      }
      if (field.isAnnotationPresent(Version.class)) {
        Object value = getFieldValue(obj, methodName);
        if (value instanceof Integer) {
          temp += (Integer) value;
        } else if (value instanceof Timestamp) {
          temp += ((Timestamp) value).getTime();
        } else {
          temp += Integer.valueOf((String) value);
        }
        if (!childVersionFlg) {
          break;
        }
      } else if (childVersionFlg) {
        if (field.getType().equals(List.class)) {
          List<?> objList = (List<?>) getFieldValue(obj, methodName);
          if (objList != null && !objList.isEmpty()) {
            if (!objList.get(0).getClass().getPackageName().startsWith(ETAG_PACKAGE_NAME)) {
              break;
            }
            for (Object listItem : objList) {
              temp += getVersionValue(childVersionFlg, listItem);
            }
          }
        } else if (field.getType().getPackageName().startsWith(ETAG_PACKAGE_NAME)) {
          Object objItem = getFieldValue(obj, methodName);
          temp += getVersionValue(childVersionFlg, objItem);
        }
      }
    }
    return temp;
  }

  /**
   * 項目の値を取得する
   * 
   * @param obj 計算対象
   * @param methodName メッソド名
   * @return 項目の値
   */
  private static Object getFieldValue(Object obj, String methodName) throws NoSuchMethodException, SecurityException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method getMethod = obj.getClass().getMethod("get" + methodName);
    return getMethod.invoke(obj);
  }
}
