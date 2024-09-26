package jp.co.maruzenshowa.malos.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import jp.co.maruzenshowa.malos.common.constant.Mark;

/**
 * メッセージユーティリティクラス.<br>
 * メッセージリソースからメッセージIDにより、メッセージを取得する機能を提供する.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public final class MessageUtils {
	
	private static final String REPLACE_L = "\\{";
	
	private static final String REPLACE_R = "\\}";

	/**
	 * メッセージリソース
	 */
	private static MessageSource messageSource;
	/**
	 * メッセージリソース初期化
	 */
	static {
		messageSource = initMessageSource();
	}

	/**
	 * コンストラクタ.
	 */
	private MessageUtils() {
		super();
	}

	/**
	 * メッセージIDによりメッセージを取得.
	 * 
	 * @param messageId メッセージID
	 * @return 該当するメッセージ
	 */
	public static String getMessage(String messageId) {
		return getMessage(messageId, null);
	}

	/**
	 * メッセージIDによりメッセージを取得.
	 * 
	 * @param messageId メッセージID
	 * @param args      引数
	 * @return 該当するメッセージ
	 */
	public static String getMessage(String messageId, Map<String, String> args) {
		if (StringUtils.isBlank(messageId)) {
			return StringUtils.EMPTY;
		}
		String message = messageSource.getMessage(messageId, null, Locale.US);
		if (args != null) {
			for (Map.Entry<String, String> entry : args.entrySet()) {
				message = message.replaceAll(REPLACE_L + entry.getKey() + REPLACE_R, entry.getValue());
			}
		}
		return message;
	}
	
	/**
	 * メッセージIDによりログメッセージを取得.
	 * 
	 * @param messageId メッセージID
	 * @param args      引数
	 * @return 該当するメッセージ
	 */
	public static String getLogMessage(String messageId, Map<String, String> args) {
		return messageId + Mark.COLON.getVal() + getMessage(messageId, args);
	}
	
	/**
	 * メッセージリソースを取得.
	 * 
	 * @return メッセージリソース
	 */
	public static MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * メッセージリソースを初期化.
	 * 
	 * @return メッセージリソース
	 */
	private static MessageSource initMessageSource() {
		ReloadableResourceBundleMessageSource msgSource = new ReloadableResourceBundleMessageSource();
		msgSource.addBasenames("i18n/sys-messages");
		msgSource.addBasenames("i18n/messages");
		msgSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		msgSource.setDefaultLocale(Locale.US);
		return msgSource;
	}
}
