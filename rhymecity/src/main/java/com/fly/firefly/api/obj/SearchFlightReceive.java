package com.fly.firefly.api.obj;

import com.fly.firefly.ui.object.BaseObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/23/2015.
 */
public class SearchFlightReceive {

    private String signature;
    private String status;
    private String message;
    private String type;
    private String flight_type;
    private GoingFlight going_flight;
    private GoingFlight return_flight;
    private String pnr;
    private String booking_id;
    private List<JourneyInfo> journeys = new ArrayList<JourneyInfo>();
    private SearchFlightReceive journeyObj;

    public String getFlight_type() {
        return flight_type;
    }

    public void setFlight_type(String flight_type) {
        this.flight_type = flight_type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public GoingFlight getGoing_flight() {
        return going_flight;
    }

    public void setGoing_flight(GoingFlight going_flight) {
        this.going_flight = going_flight;
    }

    public GoingFlight getReturn_flight() {
        return return_flight;
    }

    public void setReturn_flight(GoingFlight return_flight) {
        this.return_flight = return_flight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public class GoingFlight{

        private String arrival_station;
        private String departure_date;
        private String departure_station;
        private String status;

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

        public String getDeparture_station() {
            return departure_station;
        }

        public void setDeparture_station(String departure_station) {
            this.departure_station = departure_station;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }






















    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }



    public SearchFlightReceive(){
    }

    public SearchFlightReceive(SearchFlightReceive obj){

        status = obj.getStatus();
        message = obj.getMessage();
        type = obj.getType();
        going_flight = obj.getGoing_flight();
        return_flight = obj.getReturn_flight();
        pnr = obj.getPnr();
        booking_id = obj.getBooking_id();
        journeys = obj.getJourneys();
        signature = obj.getSignature();
        flight_type = obj.getFlight_type();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<JourneyInfo> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList journeys) {
        this.journeys = journeys;
    }

    public SearchFlightReceive getJourneyObj() {
        return journeyObj;
    }

    public void setJourneyObj(SearchFlightReceive journeyObj) {
        this.journeyObj = journeyObj;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

}
