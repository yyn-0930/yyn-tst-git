package jp.co.maruzenshowa.malos.batch.exceptions;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.stereotype.Component;

import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * バッチ異常ハンドルクラス.<br>
 * バッチ異常ハンドルクラスを提供する.<br>
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Slf4j
@Component
public class BatchExceptionHandler implements ExceptionHandler {

    private final Boolean isTerminate;

    /**
     * コンストラクタ.
     */
    public BatchExceptionHandler() {
        this.isTerminate = Boolean.FALSE;
    }

    /**
     * コンストラクタ.
     * 
     * @param isTerminate バッチ終了フラグ
     */
    public BatchExceptionHandler(Boolean isTerminate) {
        this.isTerminate = isTerminate;
    }

    /**
     * バッチ異常ハンドル.
     * 
     * @param context   リピートコンテキスト
     * @param throwable Throwable
     */
    @Override
    public void handleException(RepeatContext context, Throwable throwable) throws Throwable {

        if (Boolean.TRUE.equals(isTerminate)) {
            context.setTerminateOnly();
            if (throwable instanceof BatchException) {
              if (log.isErrorEnabled()) { 
                log.error(throwable.getMessage(), throwable);
              }
            } else {
              if (log.isErrorEnabled()) { 
                log.error(MessageUtils.getLogMessage(MessageCode.A9001E.name(), null), throwable);
              }
            }
        } else {
        
	        // 予想外異常の場合
	        if (!(throwable instanceof BatchException)) {
	        	throw new BatchException(MessageUtils.getLogMessage(MessageCode.A9001E.name(), null), throwable);
	        }
	        
	        throw throwable;
        }
    }
}
