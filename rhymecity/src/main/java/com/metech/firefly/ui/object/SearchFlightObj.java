package com.metech.firefly.ui.object;

/**
 * Created by Dell on 11/23/2015.
 */
public class SearchFlightObj extends BaseObj {

    private String type;
    private String departure_station ;
    private String arrival_station ;
    private String departure_date ;
    private String return_date ;
    private String adult ;
    private String infant ;
    private String children ;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //private String registerAddressLine2;
    public SearchFlightObj(){

    }

    public SearchFlightObj(SearchFlightObj data){
        type = data.getType();
        departure_station = data.getDeparture_station();
        arrival_station = data.getArrival_station();
        departure_date = data.getDeparture_date();
        return_date = data.getReturn_date();
        adult = data.getAdult();
        infant = data.getInfant();
        children = data.getChildren();
        signature = data.getSignature();
        username = data.getUsername();
        password = data.getPassword();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeparture_station() {
        return departure_station;
    }

    public void setDeparture_station(String departure_station) {
        this.departure_station = departure_station;
    }

    public String getArrival_station() {
        return arrival_station;
    }

    public void setArrival_station(String arrival_station) {
        this.arrival_station = arrival_station;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getInfant() {
        return infant;
    }

    public void setInfant(String infant) {
        this.infant = infant;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }




}
