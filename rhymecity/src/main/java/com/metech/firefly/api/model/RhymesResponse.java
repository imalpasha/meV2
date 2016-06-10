package com.metech.firefly.api.model;

import com.metech.firefly.api.obj.RhymesObject;

public class RhymesResponse {

    private final RhymesObject rhymes;

    public RhymesResponse(RhymesObject rhymesObject) {
        this.rhymes = rhymesObject;
    }

    public RhymesObject getRhymesObject() {
        return rhymes;
    }
}
