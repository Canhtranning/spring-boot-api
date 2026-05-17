package com.example.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class LoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    // Bỏ qua các request không cần log
    private static final String[] SKIP_PATHS = {"/h2-console", "/favicon.ico", "/actuator"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Bỏ qua các path không cần log
        String uri = httpRequest.getRequestURI();
        for (String skip : SKIP_PATHS) {
            if (uri.startsWith(skip)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // Tạo request ID để trace
        String requestId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        chain.doFilter(requestWrapper, responseWrapper);

        long duration = System.currentTimeMillis() - startTime;

        // Lấy body
        String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);

        // Xác định emoji status
        int status = httpResponse.getStatus();
        String statusIcon = getStatusIcon(status);
        String methodIcon = getMethodIcon(httpRequest.getMethod());

        // Log đẹp
        log.info("");
        log.info("┌─────────────────────────────────────────────────");
        log.info("│ {} REQUEST  [{}]", methodIcon, requestId);
        log.info("│ {} {} {}", httpRequest.getMethod(), uri, getQueryString(httpRequest));
        log.info("│ 🌐 IP      : {}", getClientIp(httpRequest));
        if (!requestBody.isBlank()) {
            log.info("│ 📥 INPUT   : {}", requestBody.trim());
        }
        log.info("├─────────────────────────────────────────────────");
        log.info("│ {} RESPONSE [{}]", statusIcon, requestId);
        log.info("│ 📊 Status  : {} {}", status, getStatusText(status));
        log.info("│ ⏱️  Duration: {} ms {}", duration, getDurationIcon(duration));
        if (!responseBody.isBlank()) {
            log.info("│ 📤 OUTPUT  : {}", truncate(responseBody.trim(), 500));
        }
        log.info("└─────────────────────────────────────────────────");

        responseWrapper.copyBodyToResponse();
    }

    // Lấy query string nếu có
    private String getQueryString(HttpServletRequest request) {
        String query = request.getQueryString();
        return query != null ? "?" + query : "";
    }

    // Lấy IP thực của client (qua proxy/load balancer)
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        // Lấy IP đầu tiên nếu có nhiều
        if (ip != null && ip.contains(",")) ip = ip.split(",")[0].trim();
        return ip;
    }

    // Truncate response body nếu quá dài
    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "... [truncated]";
    }

    // Icon theo HTTP method
    private String getMethodIcon(String method) {
        return switch (method) {
            case "GET"    -> "🔍";
            case "POST"   -> "➕";
            case "PUT"    -> "✏️ ";
            case "DELETE" -> "🗑️ ";
            case "PATCH"  -> "🔧";
            default       -> "📡";
        };
    }

    // Icon theo status code
    private String getStatusIcon(int status) {
        if (status >= 200 && status < 300) return "✅";
        if (status >= 300 && status < 400) return "↪️ ";
        if (status >= 400 && status < 500) return "⚠️ ";
        if (status >= 500) return "❌";
        return "❓";
    }

    // Text mô tả status
    private String getStatusText(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 409 -> "Conflict";
            case 500 -> "Internal Server Error";
            default  -> "";
        };
    }

    // Icon theo thời gian response
    private String getDurationIcon(long duration) {
        if (duration < 100)  return "🚀 Fast";
        if (duration < 500)  return "✅ Good";
        if (duration < 1000) return "⚠️  Slow";
        return "🐢 Very Slow";
    }
}
