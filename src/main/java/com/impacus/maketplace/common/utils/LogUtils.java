package com.impacus.maketplace.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LogUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void writeInfoLog(String header, String message) {
        log.info(String.format("[%s] %s", header, message));
    }

    public static void writeErrorLog(String header, String message, Exception e) {
        log.error(String.format("[%s] %s", header, message), e);
    }

    public static void info(String context, String message, Object data) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("context", context);
        logData.put("message", message);
        logData.put("data", data);
        try {
            String logEntry = objectMapper.writeValueAsString(logData);
            log.info(logEntry);
        } catch (JsonProcessingException e) {
            log.error("Logging error: unable to serialize log data", e);
        }
    }

    public static void error(String context, String message, Exception exception) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("context", context);
        logData.put("message", message);
        logData.put("exception", exception.toString());
        logData.put("stacktrace", exception.getStackTrace());
        try {
            String logEntry = objectMapper.writeValueAsString(logData);
            log.error(logEntry);
        } catch (JsonProcessingException e) {
            log.error("Logging error: unable to serialize log data", e);
        }
    }
/*
    fun info(context: String, message: String, data: Any? = null) {
        logger.info { objectMapper.writeValueAsString(mapOf("context" to context, "message" to message, "data" to data)) }
    }

    fun error(context: String, message: String, throwable: Throwable? = null) {
        logger.error { objectMapper.writeValueAsString(mapOf("context" to context, "message" to message, "exception" to throwable, "stacktrace" to throwable?.stackTrace)) }
    }
    */
}
