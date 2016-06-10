package com.metech.firefly.api.model;

import com.metech.firefly.api.obj.tryObj;

public class tryResponse {

    private final tryObj rhymes;

    public tryResponse(tryObj userObj) {
        this.rhymes = userObj;
    }

    public tryObj getUserDetailObj2() {
        return rhymes;
    }
}
