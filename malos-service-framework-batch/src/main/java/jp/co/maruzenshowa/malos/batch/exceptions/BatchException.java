package jp.co.maruzenshowa.malos.batch.exceptions;

/**
 * バッチ異常クラス.<br>
 * 業務のバッチ異常クラスを提供する.<br>
 * 業務ロジックに例外が発生した場合、BatchExcetpionをスローする.<br>
 * throw new BatchException(メッセージ)
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class BatchException extends RuntimeException {

	private static final long serialVersionUID = -7034897190745766939L;

	private final String msg;
	
	/**
	 * コンストラクタ.
	 * 
	 * @param e Throwable
	 */
	public BatchException(Throwable e) {
		super(e);
		this.msg = null;
	}

	/**
	 * コンストラクタ.
	 * 
	 * @param msg メッセージ
	 */
	public BatchException(String msg) {
		super(msg);
		this.msg = msg;
	}

	/**
	 * コンストラクタ.
	 * 
	 * @param e 親例外
	 * @param msg メッセージ
	 */
	public BatchException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
