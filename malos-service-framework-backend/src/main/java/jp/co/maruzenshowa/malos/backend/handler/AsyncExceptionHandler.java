package jp.co.maruzenshowa.malos.backend.handler;

import java.util.Collection;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

/**
 * 非同期グローバル例外ハンドラ
 * 想定外の例外(システム例外以外の場合)が送出した場合、該当ハンドラでログを出力する
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Slf4j
@Configuration
public class AsyncExceptionHandler {
	
	/**
	 * 想定外の例外(システム例外以外の場合)が送出した場合、該当ハンドラでログを出力する
	 * 
	 * @return 例外ハンドラ
	 */
	@Bean
	ErrorHandler<Object> errorHandler() {
	    return new ErrorHandler<>() {
	        @Override
	        public void handle(Message<Object> message, Throwable t) {
	        	Throwable rootCause = ExceptionUtils.getRootCause(t);
	        	if (rootCause instanceof SystemException) {
	        		throw (SystemException) rootCause;
	        	} else {
	              if (log.isErrorEnabled()) { 
	        		log.error(message.getPayload().toString(), t);
	              }
	        		throw new SystemException(MessageCode.A9001E.name(), rootCause);
	        	}
	        }
	        
	        @Override
	        public void handle(Collection<Message<Object>> messages, Throwable t) {
	        	Throwable rootCause = ExceptionUtils.getRootCause(t);
	        	if (rootCause instanceof SystemException) {
	        		throw (SystemException) rootCause;
	        	} else {
	        	  if (log.isErrorEnabled()) { 
	        		log.error(messages.toString(), t);
	        	  }
	        		throw new SystemException(MessageCode.A9001E.name(), rootCause);
	        	}
	        }
	    };
	}
}
