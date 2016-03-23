package com.fly.firefly.api.obj;

/**
 * Created by Dell on 12/31/2015.
 */
public class PaymentReceive {

    private String status;
    private String securesite;
    private String link;
    private String message;
    private PaymentReceive obj;
    private String pass;

    public PaymentReceive(PaymentReceive returnObj) {

        this.obj = returnObj;
        status = returnObj.getStatus();
        securesite = returnObj.getSecureSite();
        link = returnObj.getLink();
        message = returnObj.getMessage();
        pass = returnObj.getPass();
    }

    public String getSecureSite() {
        return securesite;
    }

    public void setSecureSite(String secureSite) {
        this.securesite = secureSite;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public PaymentReceive getObj() {
        return obj;
    }

    public void setObj(PaymentReceive obj) {
        this.obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
