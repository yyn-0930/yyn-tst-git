package jp.co.maruzenshowa.malos.common.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jp.co.maruzenshowa.malos.common.annotation.validator.FullWidthChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.HalfAlphanumericChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.HalfAlphanumericMarkChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.HalfDigitsChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.HalfKanaChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.LengthChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.MailChk;
import jp.co.maruzenshowa.malos.common.annotation.validator.RequiredChk;
import lombok.Data;

@Data
public class RequestBody implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String CODE_D0008E = "D0008E"; 
  
  @Schema(description = "id", example = "")
  @RequiredChk(code = "D0001E")
  private String id;

  @Schema(description = "名前", example = "")
  @LengthChk(code = CODE_D0008E, length = 40)
  @FullWidthChk(code = "D0002E")
  private String name;

  @Schema(description = "名前カナ", example = "")
  @LengthChk(code = CODE_D0008E, length = 20)
  @HalfKanaChk(code = "D0003E")
  private String nameKana;

  @Schema(description = "従業員番号", example = "")
  @LengthChk(code = CODE_D0008E, length = 10)
  @HalfAlphanumericChk(code = "D0004E")
  private String employeeNo;

  @Schema(description = "デバイス", example = "")
  @LengthChk(code = CODE_D0008E, length = 10)
  @HalfAlphanumericMarkChk(code = "D0005E")
  private String device;

  @Schema(description = "年齢", example = "")
  @HalfDigitsChk(code = "D0006E", min = 1, max = 150)
  private String age;

  @Schema(description = "メールアドレス", example = "")
  @LengthChk(code = CODE_D0008E, length = 150)
  @MailChk(code = "D0007E")
  private String mail;
}
