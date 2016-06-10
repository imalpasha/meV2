package com.metech.firefly.ui.object;

import java.util.List;

/**
 * Created by Dell on 2/3/2016.
 */
public class MobileConfirmCheckInPassenger {

    private String pnr;
    private String departure_station_code;
    private String arrival_station_code;
    private String signature;
    private List<PassengerInfo> passengers;

    public MobileConfirmCheckInPassenger(){
    }

    public MobileConfirmCheckInPassenger(MobileConfirmCheckInPassenger data){
        pnr = data.getPnr();
        departure_station_code = data.getDeparture_station_code();
        arrival_station_code = data.getArrival_station_code();
        signature = data.getSignature();
        passengers = data.getPassengers();
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

    public String getArrival_station_code() {
        return arrival_station_code;
    }

    public void setArrival_station_code(String arrival_station_code) {
        this.arrival_station_code = arrival_station_code;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

    public class Passenger{

        private String status;
        private String passenger_number;
        private String travel_document;
        private String issuing_country;
        private String document_number;
        private String expiration_date;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPassenger_number() {
            return passenger_number;
        }

        public void setPassenger_number(String passenger_number) {
            this.passenger_number = passenger_number;
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

}
