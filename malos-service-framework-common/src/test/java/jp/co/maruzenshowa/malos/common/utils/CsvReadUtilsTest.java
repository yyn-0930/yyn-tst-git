package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import jp.co.maruzenshowa.malos.common.dto.TestCsvDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.annotation.Order;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
class CsvReadUtilsTest {
	
	private static final String FILE_PATH_NORMAL = "/readcsv/testNormal.csv";
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	
	@BeforeAll
	void testInit() {
//		String path = this.getClass().getClassLoader().getResource("").getPath();
//		String filePath = path + FILE_PATH_NORMAL;
//		File testCsvFile = new File(filePath);
//		if (testCsvFile.exists()) {
//			testCsvFile.delete();
//		}
	}
	
	
	@Test
	@Order(2)
	void testReaderNormal() {
//		String path = this.getClass().getClassLoader().getResource("").getPath();
//		String filePath = path + FILE_PATH_NORMAL;
//		int skipLine = 1;
//		if (log.isDebugEnabled()) {
//		  log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//		}
//		List<TestCsvDto> result = CsvReaderUtils.readCsvFile(filePath, skipLine, TestCsvDto.class);
//		assertEquals(2, result.size());
//		TestCsvDto line1 = result.get(0);
//		assertEquals("ID0001", line1.getId());
//		assertEquals("佐藤\"テスト", line1.getName());
//		assertEquals(12345, line1.getNumber());
//		assertEquals(new BigDecimal("1.456"), line1.getDecimal());
//		assertEquals("2009/08/11", new SimpleDateFormat(DATE_FORMAT).format(line1.getDate()));
//		assertNull(line1.getIgnore());
//		TestCsvDto line2 = result.get(1);
//		assertEquals("ID0002", line2.getId());
//		assertEquals("高野", line2.getName());
//		assertEquals(67890, line2.getNumber());
//		assertEquals(new BigDecimal("9.781"), line2.getDecimal());
//		assertEquals("2010/09/12", new SimpleDateFormat(DATE_FORMAT).format(line2.getDate()));
//		assertNull(line2.getIgnore());
	}
}
