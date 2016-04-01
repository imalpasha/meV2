package com.fly.firefly.api.obj;

/**
 * Created by Dell on 1/18/2016.
 */
public class ConfirmUpdateReceive {

    private String status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConfirmUpdateReceive getObj() {
        return obj;
    }

    public void setObj(ConfirmUpdateReceive obj) {
        this.obj = obj;
    }

    private ConfirmUpdateReceive obj;

    public ConfirmUpdateReceive(ConfirmUpdateReceive param_obj){
        this.obj = param_obj;
        status = param_obj.getStatus();
        message = param_obj.getMessage();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
