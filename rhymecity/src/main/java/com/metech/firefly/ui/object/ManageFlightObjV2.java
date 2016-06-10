package com.metech.firefly.ui.object;

/**
 * Created by Dell on 1/27/2016.
 */
public class ManageFlightObjV2 {

    private ManageFlightObj obj;
    private String username;
    private String password;
    private String module;
    private String customer_number;

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*Initiate Class*/
    public ManageFlightObjV2(){

    }

    public ManageFlightObjV2(String dataUsername,String dataPassword,String dataModule,String customerNumber){
        username = dataUsername;
        password = dataPassword;
        module = dataModule;
        customer_number = customerNumber;
    }

}

