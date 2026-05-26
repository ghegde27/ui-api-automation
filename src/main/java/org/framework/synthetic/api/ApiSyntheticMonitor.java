package org.framework.synthetic.api;

import org.framework.api.assertions.ApiAssertions;
import org.framework.api.core.ApiRequest;

public class ApiSyntheticMonitor {

    public void healthCheck() {
        ApiAssertions.assertStatusCode(
                ApiRequest.create().get("/health"),
                200
        );
    }
}
