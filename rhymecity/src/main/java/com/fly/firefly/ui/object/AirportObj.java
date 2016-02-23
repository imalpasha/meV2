package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/9/2015.
 */
public class AirportObj {

    private String location;
    private String location_code;
    private String travel_location;
    private String travel_location_code;
    private String status;
    private String mobile_check_in;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getTravel_location() {
        return travel_location;
    }

    public void setTravel_location(String travel_location) {
        this.travel_location = travel_location;
    }

    public String getTravel_location_code() {
        return travel_location_code;
    }

    public void setTravel_location_code(String travel_location_code) {
        this.travel_location_code = travel_location_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile_check_in() {
        return mobile_check_in;
    }

    public void setMobile_check_in(String mobile_check_in) {
        this.mobile_check_in = mobile_check_in;
    }
}
