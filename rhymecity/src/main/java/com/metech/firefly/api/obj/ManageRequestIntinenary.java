package com.metech.firefly.api.obj;

/**
 * Created by Dell on 1/20/2016.
 */
public class ManageRequestIntinenary{

    private String status;
    private String message;
    private ManageRequestIntinenary obj;

    public ManageRequestIntinenary (ManageRequestIntinenary data) {
        obj = data;
        status = data.getStatus();
        message = data.getMessage();
    }

    public ManageRequestIntinenary getObj() {
        return obj;
    }

    public void setObj(ManageRequestIntinenary obj) {
        this.obj = obj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
