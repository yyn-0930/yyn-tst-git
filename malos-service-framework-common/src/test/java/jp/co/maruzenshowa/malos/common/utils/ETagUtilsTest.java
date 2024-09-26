package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.annotation.Order;

import jp.co.maruzenshowa.malos.common.dto.EtagSumChildDto;
import jp.co.maruzenshowa.malos.common.dto.EtagSumParentDto;
import jp.co.maruzenshowa.malos.common.dto.EtagSumSubChildDto;
import jp.co.maruzenshowa.malos.common.dto.EtagSumTmstpDto;

@TestMethodOrder(OrderAnnotation.class)
class ETagUtilsTest {

	@Test
	@Order(1)
	void sumEtagVersion() {
		EtagSumParentDto parent = new EtagSumParentDto();
		parent.setVersion(1);
		List<EtagSumChildDto> childList = new ArrayList<>();
		EtagSumChildDto child1 = new EtagSumChildDto();
		child1.setVersion(2);
		childList.add(child1);
		EtagSumChildDto child2 = new EtagSumChildDto();
		child2.setVersion(3);
		EtagSumSubChildDto subChild = new EtagSumSubChildDto();
		child2.setCol4(subChild);
		subChild.setVersion(4);
		childList.add(child2);
		parent.setCol4List(childList);

		List<EtagSumChildDto> childList2 = new ArrayList<>();
		EtagSumChildDto child3 = new EtagSumChildDto();
		child3.setVersion(5);
		childList2.add(child3);
		EtagSumChildDto child4 = new EtagSumChildDto();
		child4.setVersion(6);
		EtagSumSubChildDto subChild2 = new EtagSumSubChildDto();
		subChild2.setVersion(7);
		child4.setCol4(subChild2);
		childList2.add(child4);

		EtagSumSubChildDto subChild3 = new EtagSumSubChildDto();
		subChild3.setVersion(8);
		try {
			assertEquals(ETagUtils.sumEtag(true, parent, childList2, subChild3, "9"), ETagUtils.getEtag("45"));
			assertEquals(ETagUtils.sumEtag(false, parent, childList2, subChild3, "9"), ETagUtils.getEtag("29"));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	@Order(2)
	void sumEtagTmstp() {
		LocalDateTime date1 = LocalDateTime.of(2023, 6, 24, 10, 1);
		LocalDateTime date2 = LocalDateTime.of(2022, 7, 30, 12, 5);
		Instant instant1 = date1.atZone(ZoneId.systemDefault()).toInstant();
		Instant instant2 = date2.atZone(ZoneId.systemDefault()).toInstant();
		Timestamp timestamp1 = new Timestamp(instant1.toEpochMilli());
		Timestamp timestamp2 = new Timestamp(instant2.toEpochMilli());
		EtagSumTmstpDto tmstpDto1 = new EtagSumTmstpDto();
		tmstpDto1.setTimestamp(timestamp1);
		EtagSumTmstpDto tmstpDto2 = new EtagSumTmstpDto();
		tmstpDto2.setTimestamp(timestamp2);
		try {
			assertEquals(ETagUtils.sumEtag(false, tmstpDto1, tmstpDto2),
					ETagUtils.getEtag(String.valueOf(instant1.toEpochMilli() + instant2.toEpochMilli())));
		} catch (Exception e) {
			fail();
		}

	}
}
