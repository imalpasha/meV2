package com.fly.firefly.ui.object;

/**
 * Created by Dell on 1/11/2016.
 */
public class ManageFlightObj {


    private String pnr;
    private String username;
    private String signature;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    /*Initiate Class*/
    public ManageFlightObj(){
    }

    public ManageFlightObj(ManageFlightObj data){
        username = data.getUsername();
        signature = data.getSignature();
        pnr = data.getPnr();

    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
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
