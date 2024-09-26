package jp.co.maruzenshowa.malos.common.utils;

import jp.co.maruzenshowa.malos.common.dto.RequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootTest
@AutoConfigureMockMvc
class ErrorInfoUtilsTest {
	
	@Autowired
	private LocalValidatorFactoryBean localValidatorFactoryBean;
	
	@Test
	void testErrorInfo() {
		RequestBody requestBody = new RequestBody();
		requestBody.setName("BOB");
		requestBody.setNameKana("123456789012345678901");
		requestBody.setEmployeeNo("あああ");
		requestBody.setAge("ABC");
		requestBody.setDevice("あああ");
		requestBody.setMail("a.c.d");
		
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(requestBody, "requestBody");
		localValidatorFactoryBean.validate(requestBody, bindingResult);
		
	}
	
	@Test
	void testNormal() {
		RequestBody requestBody = new RequestBody();
		requestBody.setId("ID0001");
		requestBody.setName("佐藤");
		requestBody.setNameKana("ｻﾄｳ");
		requestBody.setEmployeeNo("EN0001");
		requestBody.setAge("18");
		requestBody.setDevice("SN0ANC-ODF");
		requestBody.setMail("test@cn.ibm.com");
		
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(requestBody, "requestBody");
		localValidatorFactoryBean.validate(requestBody, bindingResult);
		
	}
}
