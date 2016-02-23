package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/23/2015.
 */
public class MobileCheckinObj{

    private String departure_station_code ;
    private String arrival_station_code ;
    private String pnr;
    private String user_id;
    private String signature;

    public String getDpearture_station_code() {
        return departure_station_code;
    }

    public void setDpearture_station_code(String dpearture_station_code) {
        this.departure_station_code = dpearture_station_code;
    }

    public String getArrival_station_code() {
        return arrival_station_code;
    }

    public void setArrival_station_code(String arrival_station_code) {
        this.arrival_station_code = arrival_station_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    //private String registerAddressLine2;
    public MobileCheckinObj(){

    }

    public MobileCheckinObj(MobileCheckinObj data){
        departure_station_code = data.getDeparture_station();
        arrival_station_code = data.getArrival_station();
        signature = data.getSignature();
        pnr = data.getPnr();
        user_id = data.getUser_id();
    }

    public String getPnr() {return pnr;}

    public void setPnr(String pnr) {this.pnr = pnr;}

    public String getDeparture_station() {
        return departure_station_code;
    }

    public void setDeparture_station(String departure_station) {this.departure_station_code = departure_station;}

    public String getArrival_station() {
        return arrival_station_code;
    }

    public void setArrival_station(String arrival_station) {this.arrival_station_code = arrival_station;}

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }




}
