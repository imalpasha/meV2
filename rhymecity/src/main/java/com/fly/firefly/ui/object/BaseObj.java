package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/9/2015.
 */
public class BaseObj {

    String username;
    String signature;
    public BaseObj(){

    }

    public BaseObj(BaseObj obj){
        this.username = obj.getUsername();
        this.signature = obj.getSignature();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
