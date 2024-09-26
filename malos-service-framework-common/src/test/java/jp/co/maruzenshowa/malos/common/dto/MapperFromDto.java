package jp.co.maruzenshowa.malos.common.dto;

import lombok.Data;

@Data
public class MapperFromDto {
	
	// Typeの確認
	private boolean required ;
	
	// コピーの確認
	private String name;
	
	// nullスキップ確認
	private String none;
	
	// その他項目存在確認
	private String otherFrom;
	
	// 部分一致確認
	private String partName;
}
