package com.fly.firefly.ui.object;

/**
 * Created by Dell on 12/30/2015.
 */
public class Signature {

    String username;
    String signature;
    public Signature(){

    }

    public Signature(Signature obj){
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
