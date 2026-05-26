package org.framework.api.service;

import org.framework.api.core.ApiRequest;

public abstract class BaseApiService {

    protected ApiRequest request() {
        return ApiRequest.create();
    }
}
