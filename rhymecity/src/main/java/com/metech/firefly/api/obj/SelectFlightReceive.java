package com.metech.firefly.api.obj;

import com.metech.firefly.ui.object.DefaultPassengerObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/9/2015.
 */
public class SelectFlightReceive {

    private String status;
    private String message;
    private String booking_id;


    private ArrayList<DefaultPassengerObj> family_and_friend;
    private SelectFlightReceive flightObj;

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

    public String getBookingId() {
        return booking_id;
    }

    public void setBookingId(String booking_id) {
        this.booking_id = booking_id;
    }

    public SelectFlightReceive(SelectFlightReceive param_obj){
        this.flightObj = param_obj;
        message = param_obj.getMessage();
        booking_id = param_obj.getBookingId();
        status = param_obj.getStatus();
        family_and_friend = param_obj.getFamily_and_friend();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SelectFlightReceive getFlightObj() {
        return flightObj;
    }

    public void setFlightObj(SelectFlightReceive flightObj) {
        this.flightObj = flightObj;
    }
}
