package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.internal.util.Lists;
import org.springframework.core.annotation.Order;

@TestMethodOrder(OrderAnnotation.class)
class SequneceUtilsTest {

	@Test
	@Order(1)
	void getSequnece() {
		assertEquals("00000", SequneceUtils.sequneceToAlphanum(0, 5));
		assertEquals("000Z9", SequneceUtils.sequneceToAlphanum(359, 5));
		assertEquals("ZZZZ9", SequneceUtils.sequneceToAlphanum(16796159, 5));
		assertEquals(0, SequneceUtils.alphanumToSequnece("00000"));
		assertEquals(359, SequneceUtils.alphanumToSequnece("000Z9"));
		assertEquals(16796159, SequneceUtils.alphanumToSequnece("ZZZZ9"));
		Iterator<Integer> sequneceList1 = SequneceUtils.getSequneceList(0, 10, 1, 5);
		assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), Lists.from(sequneceList1));
		Iterator<Integer> sequneceList2 = SequneceUtils.getSequneceList(16796155, 10, 1, 5);
		assertEquals(Arrays.asList(16796156, 16796157, 16796158, 16796159, 1, 2, 3, 4, 5, 6), Lists.from(sequneceList2));
		assertEquals(1, SequneceUtils.getNextSequnece(0, 1, 5));
		assertEquals(16796159, SequneceUtils.getNextSequnece(16796158, 1, 5));
		assertEquals(1, SequneceUtils.getNextSequnece(16796159, 1, 5));
	}
}
