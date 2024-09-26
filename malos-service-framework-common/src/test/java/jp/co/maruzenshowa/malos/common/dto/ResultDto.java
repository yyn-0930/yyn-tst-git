package jp.co.maruzenshowa.malos.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private int age;
}
