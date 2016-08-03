package com.metech.firefly.api.obj;

/**
 * Created by Dell on 6/16/2016.
 */
public class DeleteCCReceive {

    private String message;
    private String status;

    public DeleteCCReceive(DeleteCCReceive obj){
        message = obj.getMessage();
        status = obj.getStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
