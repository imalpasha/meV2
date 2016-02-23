package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/4/2015.
 */
public class LoginRequest{

    /*Local Data Send To Server*/
    String username;
    String password;

    /*Initiate Class*/
    public LoginRequest(){
    }

    //public LoginRequest(String username123){
    //    this.username = username123;
    //}

    public LoginRequest(LoginRequest data){
        username = data.getUsername();
        password = data.getPassword();
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }



    /*Response Data From Server*/
    String status;


    public String getStatus() {
        return status;
    }


}
