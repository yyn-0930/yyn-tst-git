package jp.co.maruzenshowa.malos.common.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAnnotationCsvDto {

  // キーID
  private String id;

  // 名前
  private String name;

  // 数字
  private int number;

  // 小数
  private BigDecimal decimal;

  // 日付
  private Date date;

  // CSVファイル対象外項目
  private String ignore;
}
