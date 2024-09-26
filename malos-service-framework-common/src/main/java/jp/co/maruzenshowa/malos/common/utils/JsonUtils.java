package jp.co.maruzenshowa.malos.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.SystemException;

/**
 * JSON操作用ユーティリティ.
 * 
 * @author  IBM Wei kai
 * @version 1.0
 */
public class JsonUtils {
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.ALWAYS);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		mapper.registerModule(new JavaTimeModule());
	}

	
	/**
	 * デフォルト　コンストラクタを提供.
	 */
	private JsonUtils() {}
	
    /**
     * オブジェクトをシリアライズ化する.
     * 
     * @param dto BEANオブジェクト
     * @return Jsonに変換した結果
     */
    public static String serialize(Object dto) {
        try {
			return mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
            throw new SystemException(MessageCode.A9012E.name(), e);
		}
	}

    /**
     * JSONメッセージを解析し、オブジェクトへ変換する.
     * 
     * @param <T> 変換先データタイプ
     * @param message JSONメッセージ
     * @param clazz   変換対象クラス
     * @return 変換先データ
     */
    public static <T> T parse(String message, Class<T> clazz) {
        try {
            return mapper.readValue(message, clazz);
        } catch (JsonProcessingException e) {
        	throw new SystemException(MessageCode.A9013E.name(), e);
        }
    }
    
    /**
     * JSONメッセージを解析し、型パラメータがあるオブジェクトへ変換する.
     * 
     * @param <T> 変換先データタイプ
     * @param message JSONメッセージ
     * @param valueTypeRef   型パラメータの定義
     * @return 変換先データ
     */
    public static <T> T parse(String message, TypeReference<T> valueTypeRef) {
        try {
            return mapper.readValue(message, valueTypeRef);
        } catch (JsonProcessingException e) {
        	throw new SystemException(MessageCode.A9013E.name(), e);
        }
    }
}
