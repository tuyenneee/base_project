package com.ivnd.knowledgebase.config;

import com.ivnd.knowledgebase.exception.RequestLOG;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request.getAttribute("_wrappedRequest");
        RequestLOG.info(wrapper, response);
    }
}
