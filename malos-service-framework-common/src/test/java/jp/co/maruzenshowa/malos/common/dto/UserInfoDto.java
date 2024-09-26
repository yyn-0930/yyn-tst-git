package jp.co.maruzenshowa.malos.common.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private int age;
}
