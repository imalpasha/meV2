package com.metech.firefly.ui.object;

/**
 * Created by Dell on 1/11/2016.
 */
public class ManageFlightObj {


    private String pnr;
    private String username;
    private String signature;
    private String user_id;
    private String customer_number;

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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
        user_id = data.getUser_id();
        customer_number = data.getCustomer_number();
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
