package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/4/2015.
 */
public class PasswordRequest extends BaseObj {


    /*Initiate Class*/
    public PasswordRequest(){
    }

    public PasswordRequest(PasswordRequest data){
        username = data.getEmail();
        signature = data.getSignature();
    }
    public String getEmail() {

        return username;
    }

    public void setEmail(String email) {

        this.username = email;
    }



    /*Response Data From Server*/
    String status;

    public String getStatus() {
        return status;
    }


}
