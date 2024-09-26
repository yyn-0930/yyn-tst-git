package jp.co.maruzenshowa.malos.backend.aop;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jp.co.maruzenshowa.malos.common.constant.GlobalConstant;
import jp.co.maruzenshowa.malos.common.constant.HttpHeader;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;

/**
 * DynamicContext初期化と終了処理のクラス.<br>
 * DynamicContextに対してリクエストヘッダーから初期化と終了処理時の削除機能を提供する.<br>
 * 
 * @author IBM Wei kai
 * @version 1.0
 */
@Component
public class ContextInterceptor implements HandlerInterceptor {
    /**
     * Controllerメソッドが呼び出される前に、DynamicContext初期化して処理を続ける。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // トレース情報設定
    	if (request.getHeader(HttpHeader.TRACEID) != null) {
    		MDC.put(GlobalConstant.MDC_AWS_TRACE_UUID, request.getHeader(HttpHeader.TRACEID)); //X-Trace-Id
    	}
        
        String etag = request.getHeader(HttpHeader.IF_MATCH);
        if (etag != null) {
            DynamicContext.getContext().setEtag(etag);
        }
        
        if (request.getHeader(HttpHeader.MALOS_COMPCODE) != null) {
        	DynamicContext.getContext().setCompCode(request.getHeader(HttpHeader.MALOS_COMPCODE));
        }
        
        if (request.getHeader(HttpHeader.MALOS_DEPTCODE) != null) {
        	DynamicContext.getContext().setDeptCode(request.getHeader(HttpHeader.MALOS_DEPTCODE));
        }
        
        if (request.getHeader(HttpHeader.MALOS_AUTH_USERID) != null) {
        	DynamicContext.getContext().setUserId(request.getHeader(HttpHeader.MALOS_AUTH_USERID));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 全体の要求処理が完了し、表示がレンダリングされた後、処理を行う。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 動的コンテキスト情報を削除する
        DynamicContext.remove();
    }
}
