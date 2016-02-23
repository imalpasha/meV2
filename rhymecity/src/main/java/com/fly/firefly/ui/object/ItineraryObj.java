package com.fly.firefly.ui.object;

/**
 * Created by Dell on 11/23/2015.
 */

public class ItineraryObj{

    private String type;
    private String departure_station ;
    private String arrival_station ;
    private String pnr;
    private String signature;

    //private String registerAddressLine2;
    public ItineraryObj(){

    }

    public ItineraryObj(ItineraryObj data){
        type = data.getType();
        departure_station = data.getDeparture_station();
        arrival_station = data.getArrival_station();
       // signature = data.getSignature();
        pnr = data.getPnr();

    }

    public String getPnr() {return pnr;}

    public void setPnr(String pnr) {this.pnr = pnr;}

    public String getType() {return type;}

    public void setType(String type) {
        this.type = type;
    }

    public String getDeparture_station() {
        return departure_station;
    }

    public void setDeparture_station(String departure_station) {this.departure_station = departure_station;}

    public String getArrival_station() {
        return arrival_station;
    }

    public void setArrival_station(String arrival_station) {this.arrival_station = arrival_station;}






}
