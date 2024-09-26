package jp.co.maruzenshowa.malos.batch.common;

import jp.co.maruzenshowa.malos.batch.constant.ExitCode;
import lombok.Getter;
import lombok.Setter;

/**
 * バッチ終了コードのシングルトンクラス.<br>
 * 終了コードの取得方法:SingletonExitCode.getInstance().getExitCode()
 * 
 * @author  IBM Han Feng
 * @version 1.0
 */
public class SingletonExitCode {
    // 終了コード
    @Getter
    @Setter
    private int exitCode = ExitCode.SUCCEEDED;
    private static SingletonExitCode instance = new SingletonExitCode();

    /**
     * コンストラクタ.
     */
    private SingletonExitCode() {
    }

    /**
     * バッチ終了コードの設定.
     * 
     * @param exitCode バッチ終了コード
     */
    public static void setSavedExitCode(int exitCode) {
        instance.setExitCode(exitCode);
    }

    /**
     * バッチ終了コードの取得.
     * 
     * @return バッチ終了コード
     */
    public static int getSavedExitCode() {
        return instance.getExitCode();
    }
}
