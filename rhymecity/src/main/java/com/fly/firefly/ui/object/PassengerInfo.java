package com.fly.firefly.ui.object;

import java.util.ArrayList;

/**
 * Created by Dell on 12/14/2015.
 */
public class PassengerInfo {

   /* ArrayList<Passenger> array = new ArrayList<Passenger>();

    public ArrayList<Passenger> getArray() {
        return array;
    }
    public void setArray(ArrayList<Passenger> array) {
        this.array = array;
    }

    public class Passenger{*/
        private String passenger_number;


    private String status;

        private String title;
        private String gender;
        private String first_name;
        private String last_name;
        private String dob;
        private String travel_document;
        private String issuing_country;
        private String document_number;
        private String expiration_date;
        private String bonuslink;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
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

        public String getExpiration_date() {
            return expiration_date;
        }

        public void setExpiration_date(String expiration_date) {
            this.expiration_date = expiration_date;
        }

        public String getBonuslink() {
            return bonuslink;
        }

        public void setBonusLink(String enrich_loyalty_number) {
            this.bonuslink = enrich_loyalty_number;
        }

        public String getDocument_number() {
            return document_number;
        }

        public void setDocument_number(String document_number) {
            this.document_number = document_number;
        }


    // }

}
