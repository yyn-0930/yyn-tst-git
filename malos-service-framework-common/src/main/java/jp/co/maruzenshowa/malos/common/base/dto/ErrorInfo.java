package jp.co.maruzenshowa.malos.common.base.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jp.co.maruzenshowa.malos.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *　エラー情報のクラス.<br>
 * エラー情報のデータ構造を提供する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "エラーコード", example = "")
    private String code;

	@Schema(description = "エラーメッセージ", example = "")
    private String message;
    
	@Schema(description = "エラーパラメータ", example = "")
    private List<ErrorParam> param;
    
    /**
     * コンストラクタ.
     * 
     * @param code エラーコード
     * @param args エラーパラメータ
     */
    public ErrorInfo(String code, Map<String, String> args) {
    	this.code = code;
    	this.message = MessageUtils.getMessage(code, args);
    	List<ErrorParam> paramList = new ArrayList<>();
    	if (args != null) {
	    	for (Map.Entry<String, String> entry : args.entrySet()) {
	    		paramList.add(new ErrorParam(entry.getKey(), entry.getValue()));
			}
    	}
	    this.param = paramList;
    }
}
