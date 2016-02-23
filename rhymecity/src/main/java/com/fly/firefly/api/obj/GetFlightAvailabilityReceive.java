package com.fly.firefly.api.obj;

/**
 * Created by Dell on 1/21/2016.
 */
public class GetFlightAvailabilityReceive {

    private String status;
    private GetFlightAvailabilityReceive obj;


    public GetFlightAvailabilityReceive (GetFlightAvailabilityReceive data) {
        obj = data;
    }

    public GetFlightAvailabilityReceive getObj() {
        return obj;
    }

    public void setObj(GetFlightAvailabilityReceive obj) {
        this.obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
