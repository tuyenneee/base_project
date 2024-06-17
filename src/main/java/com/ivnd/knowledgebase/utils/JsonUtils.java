package com.ivnd.knowledgebase.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 06/05/2024
 */
@Log4j2
public final class JsonUtils {
    private JsonUtils() {
    }

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <O> String write(O o) {
        if (o == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public static <T> T read(String json, final Class<T> clazz) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T read(String json, final TypeReference<T> reference) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, reference);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T read(byte[] bytes, final Class<T> clazz) {
        if (ObjectUtils.isEmpty(bytes)) {
            return null;
        }
        var result = new String(bytes, StandardCharsets.UTF_8);
        return read(result, clazz);
    }

    public static <T> T read(InputStream is, final Class<T> clazz) {
        if (ObjectUtils.isEmpty(is)) {
            return null;
        }
        try {
            return MAPPER.readValue(is, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getText(JsonNode jsonNode,
                                 String property,
                                 String defaultValue) {
        try {
            return jsonNode.get(property).asText();
        } catch (Exception e) {
            log.debug("Not found property \"{}\"", property);
        }
        return defaultValue;
    }

    public static String getText(JsonNode jsonNode,
                                 String property) {
        return getText(jsonNode, property, "");
    }

    public static JsonNode getNodeWithPath(JsonNode jsonNode,
                                           String path,
                                           JsonNode defaultValue) {
        try {
            return jsonNode.at(path);
        } catch (Exception e) {
            log.debug("Not found path \"{}\"", path);
        }
        return defaultValue;
    }

    public static JsonNode getNodeWithPath(JsonNode jsonNode,
                                           String path) {
        return getNodeWithPath(jsonNode, path, null);
    }
}
