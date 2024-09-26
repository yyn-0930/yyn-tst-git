package jp.co.maruzenshowa.malos.common.dto;

import java.util.List;

import jakarta.persistence.Version;
import lombok.Data;

@Data
public class EtagSumSubChildDto {
	private String col1;
	
	private int col2;
	
	private List<String> col3;
	
	@Version
	private int version;
}
