package com.fly.firefly.api.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/23/2015.
 */
public class MobileCheckinReceive {

    private String status;
    private String message;
    private Flight flight_detail;
    private String signature;
    private String pnr;
    private String departure_station_code;
    private String arrival_station_code;
    private List<Passenger> passengers;
    private MobileCheckinReceive obj;

    public String getArrival_station_code() {
        return arrival_station_code;
    }

    public void setArrival_station_code(String arrival_station_code) {
        this.arrival_station_code = arrival_station_code;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getDeparture_station_code() {
        return departure_station_code;
    }

    public void setDeparture_station_code(String departure_station_code) {
        this.departure_station_code = departure_station_code;
    }

    public class Flight {

        private String flight_date;
        private String flight_number;
        private String station_code;
        private String departure_time;

        public String getDeparture_time() {
            return departure_time;
        }

        public void setDeparture_time(String departure_time) {
            this.departure_time = departure_time;
        }

        public String getFlight_date() {
            return flight_date;
        }

        public void setFlight_date(String flight_date) {
            this.flight_date = flight_date;
        }

        public String getFlight_number() {
            return flight_number;
        }

        public void setFlight_number(String flight_number) {
            this.flight_number = flight_number;
        }

        public String getStation_code() {
            return station_code;
        }

        public void setStation_code(String station_code) {
            this.station_code = station_code;
        }


    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Flight getFlight_detail() {
        return flight_detail;
    }

    public void setFlight_detail(Flight flight_detail) {
        this.flight_detail = flight_detail;
    }

    public class Passenger {

        private String passenger_number;
        private String title;
        private String first_name;
        private String last_name;
        private String seat;
        private String travel_document;
        private String issuing_country;
        private String document_number;
        private String expiration_date;
        private String checked = "N";
        private String status;
        private String bonuslink;

        public String getBonuslink() {
            return bonuslink;
        }

        public void setBonuslink(String bonuslink) {
            this.bonuslink = bonuslink;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getChecked() {
            return checked;
        }

        public void setChecked(String checked) {
            this.checked = checked;
        }

        public String getPassenger_number() {
            return passenger_number;
        }

        public void setPassenger_number(String passenger_number) {
            this.passenger_number = passenger_number;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }

        public String getTravel_document() {
            return travel_document;
        }

        public void setTravel_document(String travel_document) {
            this.travel_document = travel_document;
        }

        public String getIssuing_country() {
            return issuing_country;
        }

        public void setIssuing_country(String issuing_country) {
            this.issuing_country = issuing_country;
        }

        public String getDocument_number() {
            return document_number;
        }

        public void setDocument_number(String document_number) {
            this.document_number = document_number;
        }

        public String getExpiration_date() {
            return expiration_date;
        }

        public void setExpiration_date(String expiration_date) {
            this.expiration_date = expiration_date;
        }

    }

    public MobileCheckinReceive() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MobileCheckinReceive getObj() {
        return obj;
    }

    public void setObj(MobileCheckinReceive obj) {
        this.obj = obj;
    }

    public MobileCheckinReceive(MobileCheckinReceive data) {
        this.obj = data;
    }
}