package com.fly.firefly.api.obj;


public class FlightInfo{

    private String arrival_time;
    private String departure_time;
    private String flight_number;
    //private String fare;
    private String journey_sell_key;
    private basic_class basic_class;
    private flex_class flex_class;

    public class basic_class{

        private String class_of_service;
        private String fare_sell_key;
        private String tax;
        private String fare_price;
        private String discount;
        private String total_fare;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getClass_of_service() {
            return class_of_service;
        }

        public void setClass_of_service(String class_of_service) {
            this.class_of_service = class_of_service;
        }

        public String getFare_sell_key() {
            return fare_sell_key;
        }

        public void setFare_sell_key(String fare_sell_key) {
            this.fare_sell_key = fare_sell_key;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getFare_price() {
            return fare_price;
        }

        public void setFare_price(String fare_price) {
            this.fare_price = fare_price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTotal_fare() {
            return total_fare;
        }

        public void setTotal_fare(String total_fare) {
            this.total_fare = total_fare;
        }

    }

    public class flex_class{

        private String class_of_service;
        private String fare_sell_key;
        private String tax;
        private String fare_price;
        private String discount;
        private String total_fare;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getClass_of_service() {
            return class_of_service;
        }

        public void setClass_of_service(String class_of_service) {
            this.class_of_service = class_of_service;
        }

        public String getFare_sell_key() {
            return fare_sell_key;
        }

        public void setFare_sell_key(String fare_sell_key) {
            this.fare_sell_key = fare_sell_key;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getFare_price() {
            return fare_price;
        }

        public void setFare_price(String fare_price) {
            this.fare_price = fare_price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTotal_fare() {
            return total_fare;
        }

        public void setTotal_fare(String total_fare) {
            this.total_fare = total_fare;
        }

    }

    public basic_class getBasicObj() {
        return basic_class;
    }

    public void setBasicObj(basic_class basicObj) {
        this.basic_class = basicObj;
    }

    public flex_class getFlexObj() {
        return flex_class;
    }

    public void setFlexObj(flex_class flexObj) {
        this.flex_class = flexObj;
    }


    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getJourney_sell_key() {
        return journey_sell_key;
    }

    public void setJourney_sell_key(String journey_sell_key) {
        this.journey_sell_key = journey_sell_key;
    }

}
