package jp.co.maruzenshowa.malos.common.dto;

import lombok.Data;

@Data
public class MapperToDto {
	// Typeの確認
	private String required ;
	
	// コピーの確認
	private String name;
	
	// nullスキップ確認
	private String none;
	
	// その他項目存在確認
	private String otherTo;
	
	// 部分一致確認
	private String part;
}
