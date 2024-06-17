package com.ivnd.knowledgebase.anotation;

import com.ivnd.knowledgebase.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class LoggingHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggingHandler.class);


    @Pointcut("@annotation(com.ivnd.knowledgebase.anotation.Loggable)")
    public void loggingController() {
    }

    @Around(value = "loggingController()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var signature = (MethodSignature) joinPoint.getSignature();
        var paramValues = joinPoint.getArgs();
        var paramNames = signature.getParameterNames();
        var requests = IntStream.range(0, paramNames.length)
                .mapToObj(i -> paramNames[i] + " = " + JsonUtils.write(paramValues[i]))
                .collect(Collectors.joining(",", "[", "]"));

        logRequestInfo(request, requests);

        var result = joinPoint.proceed();
        logResponseInfo(request, JsonUtils.write(result));

        return result;
    }

    @AfterThrowing(pointcut = "loggingController()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var signature = (MethodSignature) joinPoint.getSignature();
        var loggable = signature.getMethod().getAnnotation(Loggable.class);

        log.error("{} - METHOD: {} - URL: {} - ERROR: {}",
                loggable.value(),
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage());
    }

    private static void logRequestInfo(HttpServletRequest request, String body) {
        log.info("[LOG REQUEST] - METHOD: {} - URL: {} - {}",
                request.getMethod(),
                request.getRequestURL(),
                body);
    }

    private static void logResponseInfo(HttpServletRequest request, String body) {
        log.info("[LOG RESPONSE] - METHOD: {} - URL: {} - {}",
                request.getMethod(),
                request.getRequestURL(),
                body);

    }

}
