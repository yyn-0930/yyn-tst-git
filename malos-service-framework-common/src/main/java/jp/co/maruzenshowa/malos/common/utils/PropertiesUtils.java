package jp.co.maruzenshowa.malos.common.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.SystemException;

/**
 * プロパティ情報取得ユーティリティ.
 * 
 * @author  IBM Wei Kai
 * @version 1.0
 */
public final class PropertiesUtils {

    /**
     * コンストラクター
     */
    private PropertiesUtils() {}

    /**
     * 指定したプロパティーファイルをロードする.
     * 
     * @param filename  プロパティーファイル名
     * @return properties
     */
    public static Properties loadProps(String filename) {
        Properties properties = new Properties();
        try (var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            properties.load(is);
            return properties;
        } catch (IOException e) {
        	Map<String, String> param = new ConcurrentHashMap<>();
        	param.put("filePath", filename);
            throw new SystemException(MessageCode.A9011E.name(), param, e);
        }
    }

}
