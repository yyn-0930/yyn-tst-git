package jp.co.maruzenshowa.malos.backend.aop;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.vavr.control.Try;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import jp.co.maruzenshowa.malos.common.annotation.ApiTimeOut;
import jp.co.maruzenshowa.malos.common.constant.MessageCode;
import jp.co.maruzenshowa.malos.common.context.DynamicContext;
import jp.co.maruzenshowa.malos.common.exception.ApplicationException;
import jp.co.maruzenshowa.malos.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * プログラムアスペクトクラス
 * 共通ログを出力する
 * 荷主コード設定処理
 *
 * @author IBM Wei kai
 * @version 1.0
 */
@Aspect
@Component
@Slf4j
public class ProgramAspect {

	@Autowired
	private EntityManager entityManager;

	@Value("${app.api.timeout:29000}")
	private int timeout;

    /**
     * コントロールを切取して、ログを追加する。
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController))")
    public void springRestControllerPointcut() {
    	// Do Nothing
    }

    /**
     * サビースを切取して、ログを追加する。
     */
    @Pointcut("@within(org.springframework.stereotype.Service) && !@annotation(io.awspring.cloud.sqs.annotation.SqsListener)")
    public void springServicePointcut() {
    	// Do Nothing
    }

    /**
     * 異常処理を切取して、ログを追加する。
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
    public void springExceptionPointcut() {
    	// Do Nothing
    }

    /**
     * コントロールの開始と終了ログ、またHttpリクエストログは自動追加します。
     *
     * @param joinPoint ログ追加ポイント
     * @return result 返却結果
     * @throws Throwable throwさせる例外
     */
    @Around("springRestControllerPointcut()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
    	var requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String methodName = request.getMethod();
        if (log.isDebugEnabled()) {
          log.debug("Method:{}, URI:{}", methodName, request.getRequestURI());
        }
        @SuppressWarnings("unchecked")
        Annotation declaringType = ((MethodSignature) joinPoint.getSignature()).getDeclaringType().getAnnotation(ApiTimeOut.class);
        ApiTimeOut classTimeout = (ApiTimeOut) declaringType;
        if (classTimeout != null) {
        	timeout = classTimeout.value();
        }
        ApiTimeOut methodTimeout = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ApiTimeOut.class);
        if (methodTimeout != null) {
        	timeout = methodTimeout.value();
        }
        TimeLimiter ourTimeLimiter = TimeLimiter.of(TimeLimiterConfig.custom()
        		  .timeoutDuration(Duration.ofMillis(timeout)).build());

        DynamicContext context = DynamicContext.getContext();

        Try<Object> timeTry = Try.of(TimeLimiter.decorateFutureSupplier(ourTimeLimiter, () ->
        CompletableFuture.supplyAsync(() -> {
        	Object result;
        	DynamicContext.setContext(context);
			try {
				result = logAround(joinPoint, request);
			} catch (ApplicationException | SystemException | OptimisticLockException | ObjectOptimisticLockingFailureException | WebClientResponseException e) {
				throw e;
			} catch (Throwable e) {
				throw new SystemException(e);
			}
        	ResponseStatus annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ResponseStatus.class);
            int httpCode = HttpStatus.OK.value();
            if (annotation != null) {
            	httpCode = annotation.value().value();
            }
            if (log.isDebugEnabled()) {
              log.debug("Method:{}, URI:{}, HttpStatus:{}", request.getMethod(), request.getRequestURI(), httpCode);
            }
        	return result;
        }))::call);
        if (timeTry.isFailure()) {
            Throwable e = timeTry.getCause();
        	if (e instanceof TimeoutException) {
        		throw new SystemException(MessageCode.A9010E.name());
        	}
        	throw timeTry.getCause();
        } else {
        	return timeTry.get();
        }
    }

    /**
     * 異常処理の終了ログは自動追加します。
     *
     * @param joinPoint ログ追加ポイント
     * @return result 返却結果
     * @throws Throwable throwさせる例外
     */
    @Around("springServicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
    	// TODO: ShprCodeの設定は権限チェック時にDynamicContextへ設定する想定で、権限チェック時要対応
        if (DynamicContext.getContext().getShprCode() != null) {
        	Query query = entityManager.createNativeQuery("set local app.current_shipper_cd = '" + DynamicContext.getContext().getShprCode() + "'");
        	query.executeUpdate();
        }
        return logAround(joinPoint, null);
    }

    /**
     * サビースの開始と終了ログは自動追加します。
     *
     * @param joinPoint ログ追加ポイント
     * @return result 返却結果
     * @throws Throwable throwさせる例外
     */
    @Around("springExceptionPointcut()")
	public Object logAroundException(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		Object result = joinPoint.proceed();
		ResponseStatus annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ResponseStatus.class);
        int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (annotation != null) {
        	httpCode = annotation.value().value();
        }
        if (log.isDebugEnabled()) {
          log.debug("Method:{}, URI:{}, HttpStatus:{}", request.getMethod(), request.getRequestURI(), httpCode);
        }
		return result;
	}

    /**
     * 開始と終了ログは自動追加します。
     *
     * @param joinPoint ログ追加ポイント
     * @param request Httpリクエスト
     * @return result 返却結果
     * @throws Throwable throwさせる例外
     */
    public Object logAround(ProceedingJoinPoint joinPoint, HttpServletRequest request) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String clazz = signature.getDeclaringTypeName();
        String method = signature.getName();
        String args = Arrays.toString(joinPoint.getArgs());

        log.debug("Class:{}, Method:{}, start with paramters:{}", clazz, method, args);

		Object result = joinPoint.proceed();
		log.debug("Class:{}, Method:{}, end with response:{}", clazz, method, result);
		return result;

    }
}
