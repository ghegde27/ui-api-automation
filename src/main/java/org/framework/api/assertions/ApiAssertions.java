package org.framework.api.assertions;

import org.framework.api.core.ApiResponse;
import org.testng.Assert;

import java.util.Arrays;

public final class ApiAssertions {

    private ApiAssertions() {
        // utility class
    }

    public static ApiResponse assertStatusCode(ApiResponse response, int expectedStatusCode) {
        Assert.assertEquals(
                response.statusCode(),
                expectedStatusCode,
                "Unexpected API status code. Response body: " + response.bodyAsString()
        );
        return response;
    }
    

    public static ApiResponse assertStatusCodeIn(ApiResponse response, int... expectedStatusCodes) {
        boolean matched = Arrays.stream(expectedStatusCodes)
                .anyMatch(statusCode -> statusCode == response.statusCode());

        Assert.assertTrue(
                matched,
                "Unexpected API status code. Expected one of "
                        + Arrays.toString(expectedStatusCodes)
                        + " but got "
                        + response.statusCode()
                        + ". Response body: "
                        + response.bodyAsString()
        );
        return response;
    }

    public static ApiResponse assertJsonField(ApiResponse response, String path, Object expectedValue) {
        Object actualValue = response.raw().jsonPath().get(path);
        Assert.assertEquals(
                actualValue,
                expectedValue,
                "Unexpected JSON value at path: " + path
        );
        return response;
    }

    public static ApiResponse assertResponseTimeBelow(ApiResponse response, long maxMillis) {
        Assert.assertTrue(
                response.raw().time() <= maxMillis,
                "API response time exceeded " + maxMillis + " ms"
        );
        return response;
    }
}
