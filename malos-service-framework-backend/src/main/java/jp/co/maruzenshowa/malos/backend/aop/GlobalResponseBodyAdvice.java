package jp.co.maruzenshowa.malos.backend.aop;

import java.io.Serializable;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jp.co.maruzenshowa.malos.common.base.dto.ResponseDto;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;

/**
 * レスポンスをフォーマットするクラス.<br>
 * レスポンスを標準レスポンスフォーマットに変換する機能を提供する.
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@ControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * レスポンスを標準レスポンスフォーマットに変換する.
	 * 
	 * @param body レスポンスボディー
	 * @param returnType 戻り値タイプ
	 * @param selectedContentType コンテンツ・ネゴシエーションによって選択されたコンテンツ・タイプ
	 * @param selectedConverterType 応答に書き込むために選択されたコンバーターの種類
	 * @param request 現在のリクエスト
	 * @param response 現在のレスポンス
	 * @return 標準化したレスポンス
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		String etag = DynamicContext.getContext().getEtag();
		if (etag != null) {
			response.getHeaders().add("etag", etag);
		}

		if (body instanceof ResponseDto<?>) {
			return body;	
		} else if (body instanceof Serializable serializable) {
			return ResponseDto.success(serializable);
		} else {
			return body;
		}
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return converterType == MappingJackson2HttpMessageConverter.class;
	}
}
