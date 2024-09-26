package jp.co.maruzenshowa.malos.common.exception;

import java.util.Map;

import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
/**
 * システム例外
 * システム例外は、アプリケーション内で復旧不可な状態である場合に送出します。
 */
@Slf4j
public class SystemException extends RuntimeException {
	
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * この例外クラスを生成します。
	 */
	public SystemException() {
		super();
	}
    
	/**
	 * メッセージ付きて原因となる例外を作成（パラメータあり）
	 * @param messageId メッセージID
	 * @param args エラーメッセージのパラメータ
	 * @param cause 根本的な原因
	 */
    public SystemException(String messageId, Map<String, String> args, Throwable cause) {
    	super(messageId);
    	String message = MessageUtils.getLogMessage(messageId, args);
        log.error(message, cause);
    }
    
	/**
	 * メッセージ付きて原因となる例外を作成
	 * @param messageId メッセージID
	 * @param cause 根本的な原因
	 */
    public SystemException(String messageId, Throwable cause) {
    	super(messageId);
    	String message = MessageUtils.getLogMessage(messageId, null);
        log.error(message, cause);
    }
    
	/**
	 * メッセージ付きて原因を作成
	 * @param messageId メッセージID
	 */
    public SystemException(String messageId) {
    	super(messageId);
    	String message = MessageUtils.getLogMessage(messageId, null);
        log.error(message);
    }
    
	/**
	 * メッセージ付きて例外を作成
	 * @param cause 根本的な原因
	 */
    public SystemException(Throwable cause) {
    	super(MessageCode.A9001E.name());
    	String message = MessageUtils.getLogMessage(MessageCode.A9001E.name(), null);
        log.error(message, cause);
    }
}
