package jp.co.maruzenshowa.malos.common.dto;

import java.math.BigDecimal;
import java.util.Date;

import jp.co.maruzenshowa.malos.common.annotation.csv.CsvCloumnIndex;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvDateFormat;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvFormat;
import jp.co.maruzenshowa.malos.common.annotation.csv.CsvHeaderName;
import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.QuoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvFormat(quoteType = QuoteType.NONE, delimiter = GlobalConstant.CSV_DELIMITER_COMMA, skipHeader = true,
    charset = GlobalConstant.CSV_CHARSET_UTF8)
public class NoneQuoteTypeNCsvDto {

//キーID
 @CsvCloumnIndex(1)
 @CsvHeaderName("id")
 private String id;

 // 名前
 @CsvCloumnIndex(2)
 @CsvHeaderName("name")
 private String name;

 // 数字
 @CsvCloumnIndex(4)
 @CsvHeaderName("number")
 private int number;

 // 小数
 @CsvCloumnIndex(3)
 @CsvHeaderName("decimal")
 private BigDecimal decimal;

 // 日付
 @CsvCloumnIndex(5)
 @CsvHeaderName("unFormattedDate")
 private Date unFormattedDate;
 
 // 日付
 @CsvCloumnIndex(5)
 @CsvHeaderName("abnormalDate")
 @CsvDateFormat(pattern = "yyyy/MM/dd")
 private String abnormalDate;

 // CSVファイル対象外項目
 private String ignore;
}
