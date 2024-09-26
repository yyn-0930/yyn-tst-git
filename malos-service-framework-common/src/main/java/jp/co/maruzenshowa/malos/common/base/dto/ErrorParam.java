package jp.co.maruzenshowa.malos.common.base.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * エラーパラメータクラス
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ErrorParam implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Schema(description = "キー", example = "")
    private String key;
    
    @Schema(description = "値", example = "")
    private String value;
    
}
