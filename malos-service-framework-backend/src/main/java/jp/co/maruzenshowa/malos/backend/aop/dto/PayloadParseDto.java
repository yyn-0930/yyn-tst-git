package jp.co.maruzenshowa.malos.backend.aop.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadParseDto {
	@JsonProperty("MessageAttributes")
	private Map<String, MessageAttributeParseDto> messageAttributes;
}
