package jp.co.maruzenshowa.malos.common.utils;

import java.util.Map;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * HTTPリクエスト共通ユーティリティクラス.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
public class ExternalWebClientUtils {

  /**
   * コンストラクター.
   */
  private ExternalWebClientUtils() {}

  // 外部API呼び出す方法
  /**
   * getリクエストを送信する.CA1A0210Handler.java
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T get(WebClient webClient, String url, Class<T> clazz, MultiValueMap<String, String> headers) {
    return webClient.get().uri(url).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz)
        .block();
  }

  /**
   * getリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param headers ヘッダー
   * @param uriVariables パラメータ
   * @return T リスポンス結果
   */
  public static <T> T get(WebClient webClient, String url, Class<T> clazz, MultiValueMap<String, String> headers,
      Map<String, Object> uriVariables) {
    return webClient.get().uri(uriBuilder -> {
      UriBuilder tmpBuilder = uriBuilder.path(url);
      uriVariables.entrySet().forEach(entry -> {
        tmpBuilder.queryParam(entry.getKey(), entry.getValue());
      });
      return tmpBuilder.build();
    }).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz).block();
  }

  /**
   * postリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param body リクエストボディ
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T post(WebClient webClient, String url, Class<T> clazz, Object body,
      MultiValueMap<String, String> headers) {
    return webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
        .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz).block();
  }

  /**
   * patchリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param body リクエストボディ
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T patch(WebClient webClient, String url, Class<T> clazz, Object body,
      MultiValueMap<String, String> headers) {
    return webClient.patch().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
        .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz).block();
  }

  /**
   * putリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param body リクエストボディ
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T put(WebClient webClient, String url, Class<T> clazz, Object body,
      MultiValueMap<String, String> headers) {
    return webClient.put().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
        .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz).block();
  }

  /**
   * putリクエストを送信する(NO_CONTENTS).
   * 
   * @param webClient ウェブクライアント
   * @param url パス
   * @param body リクエストボディ
   * @param headers ヘッダー
   */
  public static void put(WebClient webClient, String url, Object body, MultiValueMap<String, String> headers) {
    webClient.put().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
        .headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().toBodilessEntity().block();
  }

  /**
   * deleteリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T delete(WebClient webClient, String url, Class<T> clazz, MultiValueMap<String, String> headers) {
    return webClient.delete().uri(url).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().bodyToMono(clazz)
        .block();
  }

  /**
   * deleteリクエストを送信する.
   * 
   * @param <T> レスポンスボディータイプ
   * @param webClient ウェブクライアント
   * @param url パス
   * @param clazz リスポンス結果データクラス
   * @param uriVariables パラメータ
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T delete(WebClient webClient, String url, Class<T> clazz, Map<String, Object> uriVariables,
      MultiValueMap<String, String> headers) {
    return webClient.delete().uri(url, uriVariables).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve()
        .bodyToMono(clazz).block();
  }

  /**
   * deleteリクエストを送信する(NO_CONTENTS).
   * 
   * @param webClient ウェブクライアント
   * @param url パス
   * @param uriVariables パラメータ
   * @param headers ヘッダー
   */
  public static void delete(WebClient webClient, String url, Map<String, Object> uriVariables,
      MultiValueMap<String, String> headers) {
    webClient.delete().uri(uriBuilder -> {
      UriBuilder tmpBuilder = uriBuilder.path(url);
      uriVariables.entrySet().forEach(entry -> {
        tmpBuilder.queryParam(entry.getKey(), entry.getValue());
      });
      return tmpBuilder.build();
    }).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve();
  }

  /**
   * deleteリクエストを送信する(NO_CONTENTS).
   * 
   * @param webClient ウェブクライアント
   * @param url パス
   * @param headers ヘッダー
   */
  public static void delete(WebClient webClient, String url, MultiValueMap<String, String> headers) {
    webClient.delete().uri(url).headers(httpHeaders -> httpHeaders.addAll(headers)).retrieve().toBodilessEntity()
        .block();
  }

  // 外部API呼び出す方法(MultiPart)
  /**
   * getリクエストを送信する.
   * 
   * @param webClient ウェブクライアント
   * @param url パス
   * @param headers ヘッダー
   * @return byte[] リスポンス結果
   */

  @SuppressWarnings("deprecation")
  public static <T> T getMultiPart(WebClient webClient, String url, Class<T> clazz,
      MultiValueMap<String, String> headers) {
    return webClient.get().uri(url).headers(httpHeadersSpec -> httpHeadersSpec.addAll(headers))
        .accept(MediaType.APPLICATION_PDF).exchange().flatMap(response -> {
          if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(clazz);
          } else {
            return response.bodyToMono(String.class) // assuming error response is a String
                    .flatMap(errorBody -> Mono.error(new RuntimeException("Failed to retrieve data: " + errorBody)));
          }
        }).block();
  }

  /**
   * postリクエストを送信する.
   * 
   * @param webClient ウェブクライアント
   * @param url パス
   * @param body リクエストボディ
   * @param headers ヘッダー
   * @return T リスポンス結果
   */
  public static <T> T postMultiPart(WebClient webClient, String url, Class<T> clazz, byte[] body,
      MultiValueMap<String, String> headers) {
    DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(body);
    Flux<DataBuffer> dataBufferFlux = Flux.just(dataBuffer);
    return webClient.post().uri(url).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromPublisher(dataBufferFlux, DataBuffer.class))
        .headers(httpHeaders -> httpHeaders.addAll(headers)).exchange().flatMap(response -> {
          if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(clazz);
          } else {
            return response.bodyToMono(String.class) // assuming error response is a String
                    .flatMap(errorBody -> Mono.error(new RuntimeException("Failed to retrieve data: " + errorBody)));
          }
        }).block();
  }
}
