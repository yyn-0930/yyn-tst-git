package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.annotation.Order;

@TestMethodOrder(OrderAnnotation.class)
class ValidatorUtilsTest {

	@Test
	@Order(1)
	void getSequnece() {
		assertEquals(true, ValidatorUtils.isValidMail("a@a.b.c.d.e"));
		assertEquals(false, ValidatorUtils.isValidMail("a@a.b.c.d.e.f"));
		
	}
}
