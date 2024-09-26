package jp.co.maruzenshowa.malos.batch.constant;

/**
 * 終了コードを定義するクラス.<br>
 * バッチの終了状態をシステムに通知するために、以下の終了コードを基盤で指定する.<br>
 * 
 * @author IBM Wei Kai
 * @version 1.0
 */
public class ExitCode {

	public static final int SUCCEEDED = 0;
	public static final int WARN_1 = 1;
	public static final int WARN_2 = 2;
	public static final int WARN_3 = 3;
	public static final int WARN_4 = 4;
	public static final int WARN_5 = 5;
	public static final int WARN_6 = 6;
	public static final int WARN_7 = 7;
	public static final int WARN_8 = 8;
	public static final int WARN_9 = 9;
	public static final int WARN_10 = 10;
	public static final int STOPPED = 21;
	public static final int FAILED = -1;
	public static final int JOB_NOT_PROVIDED = -2;
	public static final int NO_SUCH_JOB = -3;
	
	/**
     * 構築方法.
     */
    private ExitCode() {}
}
