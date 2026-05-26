package org.framework.api.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.framework.api.config.ApiConfig;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class ApiClient {

    private ApiClient() {
        // utility class
    }

    public static RequestSpecification givenRequest() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ApiConfig.baseUrl())
                .setConfig(restAssuredConfig())
                .setAccept(ContentType.JSON)
                .setContentType(ApiConfig.defaultContentType());

        if (ApiConfig.loggingEnabled()) {
            builder.log(LogDetail.METHOD)
                    .log(LogDetail.URI)
                    .log(LogDetail.HEADERS)
                    .log(LogDetail.ALL)
                    .log(LogDetail.BODY);
        }

        applyDefaultAuth(builder);

        return given().spec(builder.build());
    }

    public static RequestSpecification givenRequest(Map<String, ?> headers) {
        return givenRequest().headers(headers);
    }

    private static RestAssuredConfig restAssuredConfig() {
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", ApiConfig.connectionTimeoutMillis())
                        .setParam("http.socket.timeout", ApiConfig.socketTimeoutMillis()));
    }

    private static void applyDefaultAuth(RequestSpecBuilder builder) {
        String bearerToken = ApiConfig.bearerToken();
        if (!bearerToken.isBlank()) {
            builder.addHeader("Authorization", "Bearer " + bearerToken);
        }

        String apiKeyHeader = ApiConfig.apiKeyHeader();
        String apiKeyValue = ApiConfig.apiKeyValue();
        if (!apiKeyHeader.isBlank() && !apiKeyValue.isBlank()) {
            builder.addHeader(apiKeyHeader, apiKeyValue);
        }
    }
}
