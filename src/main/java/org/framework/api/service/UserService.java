package org.framework.api.service;

import org.framework.api.core.ApiResponse;
import org.framework.api.model.User;

public class UserService extends BaseApiService {

    public ApiResponse createUser(User user) {
        return request()
                .body(user)
                .post("/users");
    }
}

