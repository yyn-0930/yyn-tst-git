package jp.co.maruzenshowa.malos.common.constant;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * 定数定義クラス.<br>
 * 全体の定数を定義する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class GlobalConstant {

	/** ログレベルマック：FATAL */
	public static final Marker FATAL = MarkerFactory.getMarker("FATAL");

	/** CSV区切文字：コンマ */
	public static final String CSV_DELIMITER_COMMA = ",";

	/** CSV区切文字：コンマ */
	public static final String CSV_CHARSET_UTF8 = "UTF-8";
	
	/** MDC: トレースID */
	public static final String NOTIFICATION_SUBJECT_HEADER = "notification-subject";

	/** MDC: トレースID */
	public static final String MDC_AWS_TRACE_UUID = "AWS-TRACE-UUID";

	/** MDC: バッチジョブID */
	public static final String MDC_BATCH_JOB_ID = "BATCH-JOB-ID";
	
	/** SQS: メッセージアトリビュート */
	public static final String MESSAGE_ATTRIBUTES = "MessageAttributes";
	
    /**
     * コンストラクター
     */
    private GlobalConstant() { }
}
