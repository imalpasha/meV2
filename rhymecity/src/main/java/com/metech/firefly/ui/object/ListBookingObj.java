package com.metech.firefly.ui.object;

import java.util.List;

/**
 * Created by Dell on 5/25/2016.
 */
public class ListBookingObj {

        private String pnr;
        private String departure_station_code;
        private String arrival_station_code;
        private String date;
        private String check_in;
        private String departure_datetime;

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


        public String getDeparture_datetime() {
            return departure_datetime;
        }

        public void setDeparture_datetime(String departure_datetime) {
            this.departure_datetime = departure_datetime;
        }

        public String getCheck_in() {
            return check_in;
        }

        public void setCheck_in(String check_in) {
            this.check_in = check_in;
        }


        public List<Passenger> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<Passenger> passengers) {
            this.passengers = passengers;
        }

        private List<Passenger> passengers;
    }

