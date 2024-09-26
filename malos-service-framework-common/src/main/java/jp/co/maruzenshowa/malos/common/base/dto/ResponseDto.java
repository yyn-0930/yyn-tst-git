package jp.co.maruzenshowa.malos.common.base.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返却結果基底クラス
 * @param <T> レスポンスボディータイプ
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(description = "結果データ", example = "")
    private T data;
    
    @Schema(description = "エラーデータ", example = "")
    private List<ErrorInfo> error;
    
    /**
     * コンストラクタ.
     * 
     * @param data 結果データ
     */
    public ResponseDto(T data) {
        this.data = data;
    }
    
    /**
     * コンストラクタ.
     * 
     * @param data 結果データ
     * @param etag etag
     */
    public ResponseDto(T data, String etag) {
    	this.data = data;
    	String decodeTag = new String(Base64.getDecoder().decode(etag), StandardCharsets.UTF_8);
    	DynamicContext.getContext().setEtag(decodeTag);
    }
    
    /**
     * コンストラクタ.
     * 
     * @param error エラーデータ
     */
    public ResponseDto(List<ErrorInfo> error) {
        this.error = error;
    }
    
    /**
     * 正常終了レスポンスを作成.
     * 
     * @param <T> レスポンスボディータイプ
     * @param data レスポンス
     * @return 標準レスポンス
     */
    public static <T extends Serializable> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data);
    }
    
    /**
     * 正常終了レスポンスを作成.
     * 
     * @param <T> レスポンスボディータイプ
     * @param data レスポンス 
     * @param etag レスポンス 
     * @return 標準レスポンス
     */
    public static <T extends Serializable> ResponseDto<T> success(T data, String etag) {
        return new ResponseDto<>(data, etag);
    }
    
    /**
     * 正常終了レスポンスを作成.
     * 
     * @param <T> レスポンスボディータイプ
     * @param errorInfo エラー情報
     * @return 標準レスポンス
     */
    public static <T extends Serializable> ResponseDto<T> failed(ErrorInfo errorInfo) {
    	List<ErrorInfo> errorList = new ArrayList<>();
    	errorList.add(errorInfo);
    	return new ResponseDto<>(errorList);
    }

}
