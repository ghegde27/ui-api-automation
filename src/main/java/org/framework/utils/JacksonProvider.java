package org.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JacksonProvider {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
