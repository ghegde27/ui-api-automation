package org.framework.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public final class JacksonProvider {

    private static final ObjectMapper OBJECT_MAPPER = create();

    private JacksonProvider() {
        // utility class
    }

    private static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();


        // Safe defaults
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }

    public static ObjectMapper mapper() {
        return OBJECT_MAPPER;
    }
}