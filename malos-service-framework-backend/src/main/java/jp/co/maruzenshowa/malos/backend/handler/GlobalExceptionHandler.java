package jp.co.maruzenshowa.malos.backend.handler;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.persistence.OptimisticLockException;
import jp.co.maruzenshowa.malos.common.base.dto.ErrorInfo;
import jp.co.maruzenshowa.malos.common.base.dto.ResponseDto;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.ApplicationException;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import jp.co.maruzenshowa.malos.common.utils.ErrorInfoUtils;
import jp.co.maruzenshowa.malos.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
/**
 * グローバル例外ハンドラ
 * システム例外またサビース例外が送出した場合、該当ハンドラで編集する結果を返却する
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
    * 単項目チェック例外をキャッチして、編集する結果を返却する
    * 
    * @param <T> レスポンスボディータイプ
    * @param e 単項目チェック例外
    * @return 標準レスポンス
    */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public <T extends Serializable> ResponseDto<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ErrorInfo> errorList = ErrorInfoUtils.convertToErrorInfo(e.getBindingResult());
        log.debug("単項目チェックエラー:{}", errorList);
        return new ResponseDto<>(errorList);
    }
    
    /**
    * 楽観ロック例外(412).
    * 
    * @param <T> レスポンスボディータイプ
    * @param e 楽観ロック例外
    * @return 標準レスポンス
    */
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    @ResponseBody
    public <T extends Serializable> ResponseDto<T> optimisticLockExceptionHandler(Exception e) {
        String message = MessageUtils.getLogMessage(MessageCode.A9002D.name(), null);
        log.debug(message);
        return ResponseDto.failed(new ErrorInfo(MessageCode.A9002D.name(), null));
    }
    
    /**
    * APIリクエスト異常をキャッチして、結果を返却する
    * @param e APIリクエスト例外
    * @return エラー結果
    */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> applicationExceptionHandler(WebClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
    }
    
    /**
    * アプリケーション例外をキャッチして、編集する結果を返却する
    * 
    * @param <T> レスポンスボディータイプ
    * @param e アプリケーション例外
    * @return エラー結果
    */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    public <T extends Serializable> ResponseDto<T> applicationExceptionHandler(ApplicationException e) {
        return new ResponseDto<>(e.getErrorList());
    }
    
    /**
    * システム例外をキャッチして、編集する結果を返却する
    * 
    * @param <T> レスポンスボディータイプ
    * @param e システム例外
    * @return エラー結果
    */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SystemException.class)
    public <T extends Serializable> ResponseDto<T> systemExceptionHandler(SystemException e) {
    	String code = MessageCode.A9001E.name();
    	if (e.getMessage().equals(MessageCode.A9010E.name())) {
    		code = MessageCode.A9010E.name();
    	}
        return ResponseDto.failed(new ErrorInfo(code, null));
    }
    
    /**
    * すべて例外をキャッチして、編集する結果を返却する
    * 
    * @param <T> レスポンスボディータイプ
    * @param e システム例外
    * @return エラー結果
    */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public <T extends Serializable> ResponseDto<T> exceptionHandler(Exception e) {
        String message = MessageUtils.getLogMessage(MessageCode.A9001E.name(), null);
        log.error(message, e);
        return ResponseDto.failed(new ErrorInfo(MessageCode.A9001E.name(), null));
    }
}
