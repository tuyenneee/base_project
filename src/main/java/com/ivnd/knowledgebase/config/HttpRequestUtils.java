package com.ivnd.knowledgebase.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("java:S1452")
public class HttpRequestUtils {

    private static final String X_REAL_IP_HEADER = "x-real-ip";
    private static final String X_FORWARDED_FOR_HEADER = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);
    public static final int MAX_LENGTH_SESSION_ID = 100;

    private static Pattern IP_ADDRESS_PATTERN;

    public static final String TOKEN_PARAM_NAME = "token-id";

    static {
        String builder = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        IP_ADDRESS_PATTERN = Pattern.compile(builder);
    }

    public static String getRequestIp(HttpServletRequest request) {
        if (request == null) return "127.0.0.1";
        StringBuilder builder = new StringBuilder();

        getClientIpFromHeader(builder, "X-Forwarded-For", request);
        if (builder.length() > 0) builder.append(';');
        builder.append(request.getRemoteAddr());
        return builder.toString();
    }

    private static void getClientIpFromHeader(StringBuilder ipBuilder, String headerName, HttpServletRequest request) {
        Enumeration<String> values = request.getHeaders(headerName);
        while (values.hasMoreElements()) {
            String ip = values.nextElement();
            Matcher matcher = IP_ADDRESS_PATTERN.matcher(ip);
            if (!matcher.matches()) continue;
            if (ipBuilder.length() > 0) ipBuilder.append(';');
            ipBuilder.append(ip);
        }
    }

    public static String getSessionId(HttpServletRequest request)  {
        HttpSession session = request.getSession();

        if(session != null) {
            String value = (String) session.getAttribute(TOKEN_PARAM_NAME);
            if(!StringUtils.isEmpty(value)) return value;
        }

        String value = request.getParameter("session");
        if (StringUtils.isEmpty(value)) value = request.getParameter(TOKEN_PARAM_NAME);

        if(!StringUtils.isEmpty(value)) {
            if(value.length() > MAX_LENGTH_SESSION_ID) { //jwt value
                String realIp  = HttpRequestUtils.getRealIp(request);
                String url = HttpRequestUtils.getRequestUrl(request);
                logger.error("SENSITIVE DATA from Server: " + realIp + " - Remote: " + request.getRemoteAddr()   + " - " + url);
            }
            return value;
        }

        value = request.getHeader("session");
        if (StringUtils.isEmpty(value)) value = request.getHeader(TOKEN_PARAM_NAME);
        if (StringUtils.isEmpty(value)) value = request.getHeader("access-token");

        if (StringUtils.isEmpty(value)) value = (String)request.getAttribute(TOKEN_PARAM_NAME);

        if (StringUtils.isEmpty(value)) {
            logger.warn("Not found session or token id from " + getRealIp(request) + " - " + getRequestUrl(request));
        }

        return value;
    }

    public static String getRequestUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String query = request.getQueryString();
        if(!StringUtils.isEmpty(query)) url += "?" + query;
        int idx = (url = url.toLowerCase()).indexOf("password");
        if(idx > 0) return url.substring(0, idx) + "[SENSITIVE DATA - PWD]";

        idx = (url = url.toLowerCase()).indexOf("token=");
        if(idx > 0) return url.substring(0, idx) + "[SENSITIVE DATA - TOKEN]";

        idx = (url = url.toLowerCase()).indexOf(TOKEN_PARAM_NAME + "=");
        if(idx > 0) return url.substring(0, idx) + "[SENSITIVE DATA - TOKEN-ID]";

        idx = (url = url.toLowerCase()).indexOf("session=");
        if(idx > 0) return url.substring(0, idx) + "[SENSITIVE DATA - SESSION]";

        return url;
    }


    public static String getRealIp(HttpServletRequest request) {
        Enumeration<String> _enum = request.getHeaderNames();

        String ipAddress = null;

        while(_enum.hasMoreElements()) {
            String header = _enum.nextElement();
            if(X_REAL_IP_HEADER.equalsIgnoreCase(header)) {
                return request.getHeader(header);
            } else if(X_FORWARDED_FOR_HEADER.equalsIgnoreCase(header)) {
                ipAddress = request.getHeader(header);
            } else if(PROXY_CLIENT_IP.equalsIgnoreCase(header)) {
                ipAddress = request.getHeader(header);
            } else if(WL_PROXY_CLIENT_IP.equalsIgnoreCase(header)) {
                ipAddress = request.getHeader(header);
            } else if("remote_address".equalsIgnoreCase(header)) {
                ipAddress = request.getHeader(header);
            }

        }
        return StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress) ? request.getRemoteAddr() : ipAddress;
    }

    public static HttpEntity<?> createGetEntity(String tokenId) {
        HttpHeaders headers = new HttpHeaders();
        if(!StringUtils.isEmpty(tokenId)) {
            headers.add(TOKEN_PARAM_NAME, tokenId);
        }
        return new HttpEntity<>(headers);
    }
}

