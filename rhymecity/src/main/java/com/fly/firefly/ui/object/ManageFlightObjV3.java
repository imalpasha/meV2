package com.fly.firefly.ui.object;

/**
 * Created by Dell on 1/27/2016.
 */
public class ManageFlightObjV3 {

    private ManageFlightObj obj;
    private String username;
    private String password;
    private String module;

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
    public ManageFlightObjV3(){

    }

    public ManageFlightObjV3(String dataUsername,String dataPassword,String dataModule){
        username = dataUsername;
        password = dataPassword;
        module = dataModule;
    }

}

