package com.metech.firefly.api.obj;

/**
 * Created by Dell on 1/11/2016.
 */
public class ManageFlightReceive {

    private String status;
    private String message;
    private ManageFlightReceive obj;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public ManageFlightReceive(ManageFlightReceive param_obj){
        this.obj = param_obj;
    }
    public ManageFlightReceive getObj() {
        return obj;
    }

    public void setObj(ManageFlightReceive obj) {
        this.obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
