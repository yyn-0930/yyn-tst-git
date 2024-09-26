package jp.co.maruzenshowa.malos.batch.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.launch.support.ExitCodeMapper;

import jp.co.maruzenshowa.malos.batch.constant.ExitCode;
import lombok.extern.slf4j.Slf4j;

/**
 * バッチ設定を定義するクラス.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Slf4j
public class CustomExitCodeMapper implements ExitCodeMapper {

	private final Map<String, Integer> mapping;

    /**
     * コンストラクタ.
     */
    public CustomExitCodeMapper() {
        mapping = new HashMap<>();
        mapping.put(ExitStatus.COMPLETED.getExitCode(), ExitCode.SUCCEEDED);
        mapping.put(ExitStatus.FAILED.getExitCode(), ExitCode.FAILED);
        mapping.put(ExitStatus.STOPPED.getExitCode(), ExitCode.STOPPED);
        mapping.put(JOB_NOT_PROVIDED, ExitCode.JOB_NOT_PROVIDED);
        mapping.put(NO_SUCH_JOB, ExitCode.NO_SUCH_JOB);
    }

    @Override
    public int intValue(String exitCode) {

        Integer statusCode = mapping.get(exitCode);
        
        if (statusCode != null && ExitCode.SUCCEEDED == statusCode) {
        	statusCode = SingletonExitCode.getSavedExitCode();
        }

        statusCode = (statusCode != null) ? statusCode : ExitCode.FAILED;
        log.info("System exit with code: {}", statusCode);
        return statusCode;
    }
}
