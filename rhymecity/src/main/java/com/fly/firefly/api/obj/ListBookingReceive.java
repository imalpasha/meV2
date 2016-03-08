package com.fly.firefly.api.obj;

import java.util.List;

/**
 * Created by Dell on 1/29/2016.
 */
public class ListBookingReceive {

    private List<ListBooking> list_booking;
    private String status;
    private String signature;
    private String message;

    /* ------------------------------------ */

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    public ListBookingReceive(ListBookingReceive param_obj){
        this.obj = param_obj;
    }

    public ListBookingReceive getObj() {
        return obj;
    }

    public void setObj(ListBookingReceive obj) {
        this.obj = obj;
    }

    private ListBookingReceive obj;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ListBooking> getList_booking() {
        return list_booking;
    }

    public void setList_booking(List<ListBooking> list_booking) {
        this.list_booking = list_booking;
    }

    public class ListBooking{

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

        private String pnr;
        private String departure_station_code;
        private String arrival_station_code;
        private String date;

        public String getCheck_in() {
            return check_in;
        }

        public void setCheck_in(String check_in) {
            this.check_in = check_in;
        }

        private String check_in;

        public List<Passenger> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<Passenger> passengers) {
            this.passengers = passengers;
        }

        private List<Passenger> passengers;
    }

    public class Passenger{

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

        public String getCheck_in() {
            return check_in;
        }

        public void setCheck_in(String check_in) {
            this.check_in = check_in;
        }

        private String passenger_number;
        private String title;
        private String first_name;
        private String last_name;
        private String check_in;

    }

}
