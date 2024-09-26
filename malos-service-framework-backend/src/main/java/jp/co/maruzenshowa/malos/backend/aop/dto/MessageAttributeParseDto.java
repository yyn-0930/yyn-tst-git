package jp.co.maruzenshowa.malos.backend.aop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageAttributeParseDto {
	@JsonProperty("Value")
	private String stringValue;
}
