package com.fly.firefly.ui.object;

import java.util.ArrayList;

/**
 * Created by Dell on 12/14/2015.
 */
public class Passenger {



    private String booking_id;
    private String flight_type;
    private String signature;
    private String pnr;
    private ArrayList<PassengerInfo> passengers;
    private ArrayList<InfantInfo> infants;

    public Passenger(){

    }

    public Passenger(Passenger data){
        booking_id = data.getBooking_id();
        signature = data.getSignature();
        passengers = data.getPassengers();
        infants = data.getInfant();
        flight_type = data.getFlight_type();


    }

    public Passenger(Passenger data,String pnrData,String usernameData,String signatureData){
        booking_id = data.getBooking_id();
        signature = data.getSignature();
        passengers = data.getPassengers();
        infants = data.getInfant();
        pnr = pnrData;
        flight_type = data.getFlight_type();
    }


    public String getFlight_type() {
        return flight_type;
    }

    public void setFlight_type(String flight_type) {
        this.flight_type = flight_type;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public ArrayList<InfantInfo> getInfant() {
        return infants;
    }

    public void setInfant(ArrayList<InfantInfo> infant) {
        this.infants = infant;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ArrayList<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

}
