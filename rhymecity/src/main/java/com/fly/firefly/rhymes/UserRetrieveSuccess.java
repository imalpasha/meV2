package com.fly.firefly.rhymes;

import com.fly.firefly.api.obj.tryObj;

public class UserRetrieveSuccess {

    private final tryObj userObj;

    public UserRetrieveSuccess(tryObj obj) {
        this.userObj = obj;
    }

    public tryObj getUserObj() {
        return userObj;
    }
}
