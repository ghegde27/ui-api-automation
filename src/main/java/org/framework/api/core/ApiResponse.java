package org.framework.api.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.framework.utils.JacksonProvider;

import java.util.List;

public final class ApiResponse {

    private final Response response;

    private ApiResponse(Response response) {
        this.response = response;
    }

    public static ApiResponse from(Response response) {
        return new ApiResponse(response);
    }

    public Response raw() {
        return response;
    }

    public int statusCode() {
        return response.statusCode();
    }

    public String bodyAsString() {
        return response.asString();
    }

    public <T> T as(Class<T> type) {
        try {
            return JacksonProvider.mapper().readValue(bodyAsString(), type);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize API response to " + type.getSimpleName(), e);
        }
    }

    public <T> List<T> asList(Class<T> type) {
        try {
            return JacksonProvider.mapper()
                    .readerForListOf(type)
                    .readValue(bodyAsString());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize API response list to " + type.getSimpleName(), e);
        }
    }

    public String jsonPath(String path) {
        return response.jsonPath().getString(path);
    }
}
