package jp.co.maruzenshowa.malos.backend.aop;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jp.co.maruzenshowa.malos.backend.aop.dto.PayloadParseDto;
import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.HttpHeader;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import jp.co.maruzenshowa.malos.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * AsyncContextAspect.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * フレームワークバックエンド <br>
 * DynamicContextに対して非同期メッセージのヘッダーから初期化と終了処理時の削除機能を提供する. <br>
 * <br>
 */
@Slf4j
@Aspect
@Component
public class AsyncContextAspect {

  /**
   * メソッド名 ： SQSリスナーアスペクト. <br>
   * メソッド名 ： SQSリスナーアスペクト. <br>
   * ソッド名 ： SQSリスナーアスペクト. <br>
   * メソッド説明 ： SqsListenerのアノテーションにアスペクトを行う <br>
   * <br>
   */
  @Pointcut("@annotation(io.awspring.cloud.sqs.annotation.SqsListener)")
  public void springSqsListenerPointcut() {
    // Do Nothing
  }

  /**
   * メソッド名 ： 非同期初期化処理. <br>
   * メソッド説明 ： DynamicContextに対して非同期メッセージのヘッダーから初期化と削除を行う <br>
   * <br>
   * 
   * @param joinPoint 横断ポイント
   * @throws Throwable throwさせる例外
   */
  @Around("springSqsListenerPointcut()")
  public void aroundListener(ProceedingJoinPoint joinPoint) throws Throwable {
    Signature signature = joinPoint.getSignature();
    String clazz = signature.getDeclaringTypeName();
    String method = signature.getName();
    Object message = joinPoint.getArgs()[0];
    log.debug("Class:{}, Method:{}, start with message:{}", clazz, method, message);
    if (message instanceof GenericMessage) {
      initContext(((GenericMessage<?>) message).getHeaders(), ((GenericMessage<?>) message).getPayload().toString());
    }
    joinPoint.proceed();
    log.debug("Class:{}, Method:{}, end", clazz, method);
    DynamicContext.remove();
  }


  /**
   * メソッド名 ： DynamicContext初期化処理. <br>
   * メソッド説明 ： DynamicContextに対して非同期メッセージのヘッダーから初期化を行う <br>
   * <br>
   * 
   * @param headerMap ヘッダーマップ
   * @param payload メッセージ内容
   */
  private void initContext(Map<String, Object> headerMap, String payload) {
    Map<String, Object> tempMap = new ConcurrentHashMap<>();
    tempMap.putAll(headerMap);
    if (payload.contains(GlobalConstant.MESSAGE_ATTRIBUTES)) {
      PayloadParseDto payloadDto = JsonUtils.parse(payload, PayloadParseDto.class);
      if (payloadDto.getMessageAttributes() != null) {
        payloadDto.getMessageAttributes().forEach((key, value) -> {
          tempMap.put(key, value.getStringValue());
        });
      }
    }
    setContext(headerMap);
  }

  /**
   * メソッド名 ： DynamicContext設定処理. <br>
   * メソッド説明 ： DynamicContextは非同期メッセージのヘッダーの内容を設定する <br>
   * <br>
   * 
   * @param headerMap ヘッダーマップ
   */
  private void setContext(Map<String, Object> headerMap) {
    if (headerMap.containsKey(HttpHeader.TRACEID)) {
      MDC.put(GlobalConstant.MDC_AWS_TRACE_UUID, headerMap.get(HttpHeader.TRACEID).toString());
    }
    if (headerMap.containsKey(HttpHeader.MALOS_COMPCODE)) {
      DynamicContext.getContext().setCompCode(headerMap.get(HttpHeader.MALOS_COMPCODE).toString());
    }
    if (headerMap.containsKey(HttpHeader.MALOS_DEPTCODE)) {
      DynamicContext.getContext().setDeptCode(headerMap.get(HttpHeader.MALOS_DEPTCODE).toString());
    }
    if (headerMap.containsKey(HttpHeader.MALOS_AUTH_USERID)) {
      DynamicContext.getContext().setUserId(headerMap.get(HttpHeader.MALOS_AUTH_USERID).toString());
    }
  }

}
