package jp.co.maruzenshowa.malos.batch.base;

import java.util.Arrays;
import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.utils.MessageUtils;
import jp.co.maruzenshowa.malos.common.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;

/**
 * AsyncContextAspect.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * バッチアプリケーションを起動するためのメインクラス. <br>
 * DynamicContextに対して非同期メッセージのヘッダーから初期化と終了処理時の削除機能を提供する. <br>
 * <br>
 */
@Slf4j
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "PMD.UseUtilityClass"})
public class BatchBaseApplication {
  public static final int MIN_LENGTH = 2; 
  /**
   * メソッド名 ： ジョブメイン起動処理. <br>
   * メソッド説明 ： CommandLineJobRunnerでバッチを起動する <br>
   * <br>
   * 
   * @param args パラメータ
   */
  protected static void run(String... args) {
    String[] newArgs = null;
    if (args.length > 0) {
      String[] param = args[0].split("_");
      if (param.length >= MIN_LENGTH) {
        newArgs = new String[args.length + 2];
        String sessionId = param[0];
        String jobId = param[1];
        MDC.put(GlobalConstant.MDC_AWS_TRACE_UUID, sessionId);
        MDC.put(GlobalConstant.MDC_BATCH_JOB_ID, jobId);
        String configFile = PropertiesUtils.loadProps("job-config.properties").get(jobId).toString();
        newArgs[0] = configFile;
        newArgs[1] = jobId;
        newArgs[2] = "sessionId=" + sessionId;
        System.arraycopy(args, 1, newArgs, 3, args.length - 1);  
      }
    }
    if (newArgs == null) {
      newArgs = args;
    }
    if (log.isInfoEnabled()) {
      log.info("args:{}", Arrays.toString(newArgs));
    }
    try {
      CommandLineJobRunner.main(newArgs);
    } catch (Exception e) {
      if (log.isErrorEnabled()) {
        log.error(MessageUtils.getLogMessage(MessageCode.A9001E.name(), null), e);
      }
    }
  }
}
