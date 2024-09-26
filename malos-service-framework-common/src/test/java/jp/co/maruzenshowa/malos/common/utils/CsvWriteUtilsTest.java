package jp.co.maruzenshowa.malos.common.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jp.co.maruzenshowa.malos.common.dto.NoAnnotationCsvDto;
import jp.co.maruzenshowa.malos.common.dto.NoSuchMethodCsvDto;
import jp.co.maruzenshowa.malos.common.dto.NoneQuoteTypeNCsvDto;
import jp.co.maruzenshowa.malos.common.dto.TestCsvDto;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.core.annotation.Order;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
class CsvWriteUtilsTest {

  private static final String FILE_PATH_NORMAL = "";
  private static final String FOLDER_PATH_NORMAL = "";
  private static final String FILE_PATH_ABNORMAL = "";
  private static final String DATE_FORMAT = "yyyy/MM/dd";

  @AfterAll
  void testInit() {
//    String path = this.getClass().getClassLoader().getResource("").getPath();
//    String filePath = path + FILE_PATH_NORMAL;
//    String folderPath = path + FOLDER_PATH_NORMAL;
//    File testCsvFile = new File(filePath);
//    File testCsvFolder = new File(folderPath);
//    if (testCsvFolder.exists() && testCsvFile.exists()) {
//      testCsvFile.delete();
//      testCsvFolder.delete();
//    }

  }

  /**
   * テスト観点:(正常系)<br>
   * 1.親フォルダを作成する。<br>
   * 2.入力ファイルからCSVファイルを作成する。<br>
   * 3.新規作成CSVファイルの場合、CSVヘッダーを追加する。<br>
   * 
   * テスト条件:<br>
   * 1.親フォルダが存在しない。<br>
   * 2.ファイルが存在しない。<br>
   * 3.入力DTOには@CsvFormat、@CsvCloumnIndex、@CsvHeaderName注釈がある。<br>
   * 4.スキプヘッドがfalse。<br>
   * 
   * 予想結果:<br>
   * ファイルは正常に作成され、データは正しく書き込まれる。<br>
   *
   * @throws Exception 異常
   */
  @Test
  @Order(1)
  void testWriterCsv_001N() {

//    try {
//      String path = this.getClass().getClassLoader().getResource("").getPath();
//      String filePath = path + FILE_PATH_NORMAL;
//      if (log.isDebugEnabled()) {
//        log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//      }
//      List<TestCsvDto> csvList = new ArrayList<>();
//      TestCsvDto line1 = new TestCsvDto(null, "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//          DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//      TestCsvDto line2 = new TestCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//          DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//      csvList.add(line1);
//      csvList.add(line2);
//      CsvWriterUtils.writeCsv(filePath, csvList);
//    } catch (Exception e) {
//      fail();
//    }
  }

  /**
   * テスト観点:(正常系)<br>
   * 1.親フォルダを作成する。<br>
   * 2.入力DTOにはCsvFormatとヘッダーの注釈がないの場合。<br>
   * 
   * テスト条件:<br>
   * 1.親フォルダが存在しない。<br>
   * 2.入力DTOには@CsvFormat、@CsvCloumnIndex、@CsvHeaderName注釈がない。<br>
   * 
   * 予想結果:<br>
   * ファイルは正常に作成され、ヘッダとデータは設定されない。<br>
   *
   */
  @Test
  @Order(2)
  void testWriterCsv_002N() {
//    try {
//      String path = this.getClass().getClassLoader().getResource("").getPath();
//      String filePath = path + FILE_PATH_NORMAL;
//
//      if (log.isDebugEnabled()) {
//        log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//      }
//      List<NoAnnotationCsvDto> csvList = new ArrayList<>();
//      NoAnnotationCsvDto line1 = new NoAnnotationCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//          DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//      NoAnnotationCsvDto line2 = new NoAnnotationCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//          DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//      csvList.add(line1);
//      csvList.add(line2);
//      CsvWriterUtils.writeCsv(filePath, csvList);
//    } catch (Exception e) {
//      fail();
//    }
  }

  /**
   * テスト観点:(異常系)<br>
   * 1.入力データ読み込み中に異常が発生する。<br>
   * 
   * テスト条件:<br>
   * 1.DTO入力にget方法はない<br>
   * 
   * 予想結果:<br>
   * 異常が発生する。<br>
   *
   * @throws Exception 異常
   */
  @Test
  @Order(3)
  void testWriterCsv_003E() {
//    String path = this.getClass().getClassLoader().getResource("").getPath();
//    String filePath = path + FILE_PATH_NORMAL;
//
//    if (log.isDebugEnabled()) {
//      log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//    }
//    List<NoSuchMethodCsvDto> csvList = new ArrayList<>();
//    NoSuchMethodCsvDto line1 = new NoSuchMethodCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//        DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//    NoSuchMethodCsvDto line2 = new NoSuchMethodCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//        DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//    csvList.add(line1);
//    csvList.add(line2);
//    Exception exception = assertThrows(SystemException.class, () -> {
//      CsvWriterUtils.writeCsv(filePath, csvList);
//    });
//    assertTrue(exception instanceof SystemException);
//    assertTrue(exception.getMessage().equals("A9008E"));
  }

  /**
   * テスト観点:(異常系)<br>
   * 1.ファイル初期化時に異常が発生する。<br>
   * 
   * テスト条件:<br>
   * 1.不正なファイルパスを入力する。<br>
   * 
   * 予想結果:<br>
   * 異常が発生する。<br>
   *
   * @throws Exception 異常
   */
  @Test
  @Order(4)
  void testWriterCsv_004E() {
//    String path = this.getClass().getClassLoader().getResource("").getPath();
//    String filePath = path + FILE_PATH_ABNORMAL;
//
//    if (log.isDebugEnabled()) {
//      log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//    }
//    List<NoSuchMethodCsvDto> csvList = new ArrayList<>();
//    NoSuchMethodCsvDto line1 = new NoSuchMethodCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//        DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//    NoSuchMethodCsvDto line2 = new NoSuchMethodCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//        DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//    csvList.add(line1);
//    csvList.add(line2);
//    Exception exception = assertThrows(SystemException.class, () -> {
//      CsvWriterUtils.writeCsv(filePath, csvList);
//    });
//    assertTrue(exception instanceof SystemException);
//    assertTrue(exception.getMessage().equals("A9007E"));
  }

  /**
   * テスト観点:(正常系)<br>
   * 1.ヘッダーを出力しない。<br>
   * 2.QuoteTypeがNONEに設定する場合。<br>
   * 3.データの日付フォーマット = Nullの場合<br>
   * 
   * テスト条件:<br>
   * 1.ファイルが存在する。<br>
   * 2.入力DTOにQuoteTypeがNONEに設定する。<br>
   * 3.入力DTOで@CsvDateFormatを設定しない。<br>
   * 
   * 予想結果:<br>
   * ファイルは正常に作成され、データのみが設定される。<br>
   *
   * @throws Exception 異常
   */
  @Test
  @Order(5)
  void testWriterCsv_005N() {
//    try {
//      String path = this.getClass().getClassLoader().getResource("").getPath();
//      String filePath = path + FILE_PATH_NORMAL;
//      if (log.isDebugEnabled()) {
//        log.debug("start testing CsvReaderUtilsTest: \n" + filePath);
//      }
//      List<NoneQuoteTypeNCsvDto> csvList = new ArrayList<>();
//      NoneQuoteTypeNCsvDto line1 = new NoneQuoteTypeNCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//          DateUtils.toDate("2009/08/11", DATE_FORMAT), "2011/08/11", "line1");
//      NoneQuoteTypeNCsvDto line2 = new NoneQuoteTypeNCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//          DateUtils.toDate("2010/09/12", DATE_FORMAT), "2012/08/11", "line2");
//      csvList.add(line1);
//      csvList.add(line2);
//      CsvWriterUtils.writeCsv(filePath, csvList);
//    } catch (Exception e) {
//      fail();
//    }
  }

  /**
   * テスト観点:(正常系)<br>
   * 1.Streamを読み込んでcsvファイルを作成する。<br>
   * 
   * テスト条件:<br>
   * 1.入力データはStreamだ。<br>
   * 
   * 予想結果:<br>
   * ファイルは正常に作成され、データは正しく書き込まれる。<br>
   * 
   * @throws IOException
   *
   * @throws Exception 異常
   */
  @Test
  @Order(6)
  void testWriterCsv_006N() throws IOException {
//    try {
//      OutputStream os = new ByteArrayOutputStream();
//      List<TestCsvDto> csvList = new ArrayList<>();
//      TestCsvDto line1 = new TestCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//          DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//      TestCsvDto line2 = new TestCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//          DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//      csvList.add(line1);
//      csvList.add(line2);
//      CsvWriterUtils.writeStream(os, csvList);
//    } catch (Exception e) {
//      fail();
//    }
  }

  /**
   * テスト観点:(異常系)<br>
   * 1.出力ファイルへのデータ書き込み中にエラーが発生する。<br>
   * 
   * テスト条件:<br>
   * 1.データを書き込むのmock異常を設定する。<br>
   * 
   * 予想結果:<br>
   * 異常が発生する。<br>
   * 
   * @throws IOException
   *
   * @throws Exception 異常
   */
  @Test
  @Order(7)
  void testWriterCsv_007E() throws IOException {
//    OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
//    Exception exception = null;
//    try {
//      doThrow(new IOException("Simulated IOException")).when(mockOutputStream).write(any(byte[].class), any(int.class),
//          any(int.class));
//      List<TestCsvDto> csvList = new ArrayList<>();
//      TestCsvDto line1 = new TestCsvDto("ID0001", "佐藤\"テスト", 12345, new BigDecimal("1.456"),
//          DateUtils.toDate("2009/08/11", DATE_FORMAT), "line1");
//      TestCsvDto line2 = new TestCsvDto("ID0002", "高野", 67890, new BigDecimal("9.781"),
//          DateUtils.toDate("2010/09/12", DATE_FORMAT), "line2");
//      csvList.add(line1);
//      csvList.add(line2);
//      exception = assertThrows(SystemException.class, () -> {
//        CsvWriterUtils.writeStream(mockOutputStream, csvList);
//      });
//    } catch (SystemException e) {
//      assertTrue(exception instanceof SystemException);
//      assertTrue(exception.getMessage().equals("A9007E"));
//    }
//
//
  }
}
