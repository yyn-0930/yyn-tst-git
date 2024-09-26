package jp.co.maruzenshowa.malos.common.exception;

import java.util.ArrayList;
import java.util.List;

import jp.co.maruzenshowa.malos.common.base.dto.ErrorInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * アプリケーション例外
 * アプリケーション例外は、アプリケーション内で復旧可能な状態である場合に送出します。
 */
@Slf4j
public class ApplicationException extends RuntimeException {
	
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter
    private final List<ErrorInfo> errorList;

	/**
	 * エラー情報付きて例外を作成
	 * チェックエラーの場合、エラー情報を設定して、Httpステータス400を返却する。
	 * @param errorInfo エラー情報
	 */
    public ApplicationException(ErrorInfo errorInfo) {
        super();
    	List<ErrorInfo> errorListTmp = new ArrayList<>();
    	errorListTmp.add(errorInfo);
    	this.errorList = errorListTmp;
        log.debug("アプリケーションエラー:{}", errorList);
    }
    
	/**
	 * エラー情報付きて例外を作成
	 * チェックエラーの場合、エラー情報を設定して、Httpステータス400を返却する。
	 * @param errorInfoList エラー情報リスト
	 */
    public ApplicationException(List<ErrorInfo> errorInfoList) {
        super();
    	this.errorList = errorInfoList;
        log.debug("アプリケーションエラー:{}", errorList);
    }

}
