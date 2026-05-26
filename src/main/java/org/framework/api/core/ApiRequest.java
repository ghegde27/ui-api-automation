package org.framework.api.core;

import io.restassured.specification.RequestSpecification;

import java.util.Map;

public final class ApiRequest {

    private final RequestSpecification specification;

    private ApiRequest(RequestSpecification specification) {
        this.specification = specification;
    }

    public static ApiRequest create() {
        return new ApiRequest(ApiClient.givenRequest());
    }

    public ApiRequest header(String name, Object value) {
        specification.header(name, value);
        return this;
    }

    public ApiRequest headers(Map<String, ?> headers) {
        specification.headers(headers);
        return this;
    }

    public ApiRequest queryParam(String name, Object value) {
        specification.queryParam(name, value);
        return this;
    }

    public ApiRequest pathParam(String name, Object value) {
        specification.pathParam(name, value);
        return this;
    }

    public ApiRequest body(Object body) {
        specification.body(body);
        return this;
    }

    public ApiResponse get(String path) {
        return ApiResponse.from(specification.when().get(path));
    }

    public ApiResponse post(String path) {
        return ApiResponse.from(specification.when().post(path));
    }

    public ApiResponse put(String path) {
        return ApiResponse.from(specification.when().put(path));
    }

    public ApiResponse patch(String path) {
        return ApiResponse.from(specification.when().patch(path));
    }

    public ApiResponse delete(String path) {
        return ApiResponse.from(specification.when().delete(path));
    }
}
