package com.fly.firefly.api.obj;

/**
 * Created by Dell on 12/9/2015.
 */
public class SelectFlightReceive {

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    private String booking_id;
    private SelectFlightReceive flightObj;

    public String getBookingId() {
        return booking_id;
    }

    public void setBookingId(String booking_id) {
        this.booking_id = booking_id;
    }

    public SelectFlightReceive(SelectFlightReceive param_obj){
        this.flightObj = param_obj;
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
