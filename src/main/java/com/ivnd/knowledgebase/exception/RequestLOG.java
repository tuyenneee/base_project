package com.ivnd.knowledgebase.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ivnd.knowledgebase.model.error.ErrorResponse;
import com.ivnd.knowledgebase.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RequestLOG {
    private static final String HTTP_STATUS = "httpStatus";
    private static final String URL = "url";
    private static final String METHOD = "method";
    private static final String BODY = "body";
    private static final String RESPONSE = "response";
    private static final String USER = "user";
    private static final String MESSAGE_TEMPLATE = "Checkout request: {}";

    private static final Logger logger = LoggerFactory.getLogger(RequestLOG.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void error(
            HttpServletRequest request, HttpStatus httpStatus, ErrorResponse errResp, Throwable ex) {
        int status = httpStatus.value();
        JsonNode msg = createLogMsg(request, status, errResp);
        var message = write(msg);
        if (status < 400) {
            logger.info(MESSAGE_TEMPLATE, message, ex);
        } else if (status < 500) {
            logger.warn(MESSAGE_TEMPLATE, message, ex);
        } else {
            logger.error(MESSAGE_TEMPLATE, message, ex);
        }
    }

    public static void info(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        if (status < 300) {
            JsonNode msg = createLogMsg(request, status);
            var message = write(msg);
            logger.info(MESSAGE_TEMPLATE, message);
        }
    }

    public static void info(HttpServletRequest request) {
        JsonNode msg = createLogMsg(request, HttpStatus.PROCESSING.value());
        var message = write(msg);
        logger.info(MESSAGE_TEMPLATE, message);
    }

    private static String write(JsonNode msg) {
        return JsonUtils.write(msg);
    }

    private static ObjectNode createLogMsg(HttpServletRequest request, int httpStatus) {
        ObjectNode objectMsg = objectMapper.createObjectNode().put(HTTP_STATUS, httpStatus);
        String url = request.getRequestURL().toString();
        if (request.getQueryString() != null) {
            url = url + "?" + request.getQueryString();
        }
        objectMsg.put(URL, url).put(METHOD, request.getMethod());
        try {
            String body = getRequestBody(request);
            if (body != null
                    && request.getContentType() != null
                    && request.getContentType().toLowerCase().startsWith("application/json")) {
                objectMsg.set(BODY, objectMapper.readValue(body, JsonNode.class));
            } else {
                objectMsg.put(BODY, body);
            }
        } catch (IOException e1) { // never happen
            LoggerFactory.getLogger("RequestBody").error("can't get request body", e1);
        }
        return objectMsg;
    }

    private static JsonNode createLogMsg(
            HttpServletRequest request, int httpStatus, ErrorResponse errResp) {
        return createLogMsg(request, httpStatus)
                .set(RESPONSE, objectMapper.convertValue(errResp, JsonNode.class));
    }

    private static String getRequestBody(HttpServletRequest request)
            throws UnsupportedEncodingException {
        ContentCachingRequestWrapper wrapperReq = refineRequest(request);
        if (wrapperReq == null) {
            return null;
        }
        byte[] bodyContent = wrapperReq.getContentAsByteArray();
        if (bodyContent.length == 0) {
            return null;
        }
        return new String(bodyContent, request.getCharacterEncoding());
    }

    private static ContentCachingRequestWrapper refineRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        }

        if (request instanceof FirewalledRequest
                && ((FirewalledRequest) request).getRequest() instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) ((FirewalledRequest) request).getRequest();
        }

        return null;
    }
}
