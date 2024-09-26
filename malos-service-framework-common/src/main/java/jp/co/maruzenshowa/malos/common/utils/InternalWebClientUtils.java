package jp.co.maruzenshowa.malos.common.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jp.co.maruzenshowa.malos.common.base.dto.ResponseDto;
import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.HttpHeader;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.util.UriBuilder;
import software.amazon.awssdk.utils.StringUtils;

/**
 * HTTPリクエスト共通ユーティリティクラス.
 * 
 * @author IBM Wei　kai
 * @version 1.0
 */
public class InternalWebClientUtils {
  
    /**
     * コンストラクター
     */
    private InternalWebClientUtils() {}
  
    /**
     * 転送するヘッダーを作成する.
     * 
     * @return HttpHeaders 
     */
	public static MultiValueMap<String, String> getForwardHeaders() {
	  MultiValueMap<String, String> headers = new HttpHeaders();
		if (DynamicContext.getContext().getCompCode() != null) {
			headers.add(HttpHeader.MALOS_COMPCODE, DynamicContext.getContext().getCompCode());
		}
		if (DynamicContext.getContext().getDeptCode() != null) {
			headers.add(HttpHeader.MALOS_DEPTCODE, DynamicContext.getContext().getDeptCode());
		}
		if (DynamicContext.getContext().getUserId() != null) {
			headers.add(HttpHeader.MALOS_AUTH_USERID, DynamicContext.getContext().getUserId());
		}
		if (MDC.get(GlobalConstant.MDC_AWS_TRACE_UUID) != null) {
			headers.add(HttpHeader.TRACEID, MDC.get(GlobalConstant.MDC_AWS_TRACE_UUID));
		}
		return headers;
	}
	
    /**
     * 転送するヘッダーを作成する.
     * 
     * @return HttpHeaders 
     */
	public static Map<String, Object> getAsyncForwardHeaders() {
		return getAsyncForwardHeaders("");
	}
	
    /**
     * 転送するヘッダーを作成する.
     * 
     * @param subject SNSのサブジェクト
     * @return HttpHeaders 
     */
	public static Map<String, Object> getAsyncForwardHeaders(String subject) {
		Map<String, Object> headers = new ConcurrentHashMap<>();
		if (!StringUtils.isBlank(subject)) {
			headers.put(GlobalConstant.NOTIFICATION_SUBJECT_HEADER, subject);
		}
		if (DynamicContext.getContext().getCompCode() != null) {
			headers.put(HttpHeader.MALOS_COMPCODE, DynamicContext.getContext().getCompCode());
		}
		if (DynamicContext.getContext().getDeptCode() != null) {
			headers.put(HttpHeader.MALOS_DEPTCODE, DynamicContext.getContext().getDeptCode());
		}
		if (DynamicContext.getContext().getUserId() != null) {
			headers.put(HttpHeader.MALOS_AUTH_USERID, DynamicContext.getContext().getUserId());
		}
		if (MDC.get(GlobalConstant.MDC_AWS_TRACE_UUID) != null) {
			headers.put(HttpHeader.TRACEID, MDC.get(GlobalConstant.MDC_AWS_TRACE_UUID));
		}
		return headers;
	}
    
    /**
     * WebClientをビルドする.
     * 
     * @param baseUrl リクエスト対象のベースURL
     * @param addHeader ヘッダー追加フラグ
     * @return WebClient 
     */
	public static WebClient getWebClient(String baseUrl, boolean addHeader) {
		Builder builder = WebClient.builder().baseUrl(baseUrl);
		if (addHeader) {
			builder = builder.defaultHeaders(httpHeaders -> {
				httpHeaders.addAll(getForwardHeaders());
	        });
		}
		return builder.build();
	}
	
	// 内部API呼び出す方法
    /**
     * getリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> get(WebClient webClient, String url, Class<T> clazz) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.get().uri(url).retrieve().bodyToMono(ptr).block();
	}
	
    /**
     * getリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @param uriVariables パラメータ
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> get(WebClient webClient, String url, Class<T> clazz, Map<String, Object> uriVariables) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.get().uri(uriBuilder -> {
			UriBuilder tmpBuilder = uriBuilder.path(url);
			uriVariables.entrySet().forEach(entry -> { 
				tmpBuilder.queryParam(entry.getKey(), entry.getValue());
			});
			return tmpBuilder.build();
			}).retrieve().bodyToMono(ptr).block();
	}
	
    /**
     * postリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @param body リクエストボディ
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> post(WebClient webClient, String url, Class<T> clazz, Object body) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().bodyToMono(ptr).block();
	}
	
	/**
	 * patchリクエストを送信する.
	 * 
	 * @param <T> レスポンスボディータイプ
	 * @param webClient ウェブクライアント
	 * @param url パス
	 * @param clazz リスポンス結果データクラス
	 * @param body リクエストボディ
	 * @return ResponseDto リスポンス結果
	 */
	public static <T extends Serializable> ResponseDto<T> patch(WebClient webClient, String url, Class<T> clazz,
			Object body) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference
				.forType(TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.patch().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
				.bodyToMono(ptr).block();
	}
	
    /**
     * putリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @param body リクエストボディ
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> put(WebClient webClient, String url, Class<T> clazz, Object body) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.put().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().bodyToMono(ptr).block();
	}
	
    /**
     * putリクエストを送信する(NO_CONTENTS).
     * 
     * @param webClient ウェブクライアント
     * @param url パス
     * @param body リクエストボディ
     */
	public static void put(WebClient webClient, String url, Object body) {
		webClient.put().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().toBodilessEntity().block();
	}
	
    /**
     * deleteリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> delete(WebClient webClient, String url, Class<T> clazz) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.delete().uri(url).retrieve().bodyToMono(ptr).block();
	}
	
    /**
     * deleteリクエストを送信する.
     * 
     * @param <T> レスポンスボディータイプ
     * @param webClient ウェブクライアント
     * @param url パス
     * @param clazz リスポンス結果データクラス
     * @param uriVariables パラメータ
     * @return ResponseDto リスポンス結果
     */
	public static <T extends Serializable> ResponseDto<T> delete(WebClient webClient, String url, Class<T> clazz, Map<String, Object> uriVariables) {
		ParameterizedTypeReference<ResponseDto<T>> ptr = ParameterizedTypeReference.forType(
                TypeUtils.parameterize(ResponseDto.class, clazz));
		return webClient.delete().uri(url, uriVariables).retrieve().bodyToMono(ptr).block();
	}
	
    /**
     * deleteリクエストを送信する(NO_CONTENTS).
     * 
     * @param webClient ウェブクライアント
     * @param url パス
     * @param uriVariables パラメータ
     */
	public static void delete(WebClient webClient, String url, Map<String, Object> uriVariables) {
		webClient.delete().uri(uriBuilder -> {
			UriBuilder tmpBuilder = uriBuilder.path(url);
			uriVariables.entrySet().forEach(entry -> { 
				tmpBuilder.queryParam(entry.getKey(), entry.getValue());
			});
			return tmpBuilder.build();
			}).retrieve().toBodilessEntity().block();
	}
	
    /**
     * deleteリクエストを送信する(NO_CONTENTS).
     * 
     * @param webClient ウェブクライアント
     * @param url パス
     */
	public static void delete(WebClient webClient, String url) {
		webClient.delete().uri(url).retrieve().toBodilessEntity().block();
	}
	
}