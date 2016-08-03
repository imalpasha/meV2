package com.metech.firefly.api.obj;

import com.metech.firefly.ui.object.DefaultPassengerObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/9/2015.
 */
public class DeleteFFReceive {

    private String status;
    private String message;

    private ArrayList<DefaultPassengerObj> family_and_friend;
    private DeleteFFReceive flightObj;

    public String getMessage() {
        return message;
    }


    public ArrayList<DefaultPassengerObj> getFamily_and_friend() {
        return family_and_friend;
    }

    public void setFamily_and_friend(ArrayList<DefaultPassengerObj> family_and_friend) {
        this.family_and_friend = family_and_friend;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public DeleteFFReceive(DeleteFFReceive param_obj){
        this.flightObj = param_obj;
        message = param_obj.getMessage();
        status = param_obj.getStatus();
        family_and_friend = param_obj.getFamily_and_friend();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DeleteFFReceive getFlightObj() {
        return flightObj;
    }

    public void setFlightObj(DeleteFFReceive flightObj) {
        this.flightObj = flightObj;
    }
}
