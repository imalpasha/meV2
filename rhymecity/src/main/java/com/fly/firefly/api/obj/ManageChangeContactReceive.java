package com.fly.firefly.api.obj;

import com.fly.firefly.ui.object.SeatInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/16/2015.
 */
public class ManageChangeContactReceive {

    private String status;
    private ItinenaryInformation itinerary_information;
    private ContactInformation contact_information;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private String message;
    private Insurance insurance_details;

    private List<PaymentDetail> payment_details;
    private List<FlightDetails> flight_details;
    private List<PriceDetails> price_details;
    private List<PassengerList> passenger_information;

    private String total_price;
    private String total_paid;
    private String total_due;
    private String booking_id;
    private String pnr;
    private ManageChangeContactReceive obj;



    public void setObj(ManageChangeContactReceive obj) {
        this.obj = obj;


    }

    public String getStatus() {
        return status;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }


    public Insurance getInsurance_details() {
        return insurance_details;
    }

    public void setInsurance_details(Insurance insurance_details) {
        this.insurance_details = insurance_details;
    }

    public class Insurance{

        private String status;
        private String conf_number;
        private String rate;

        public String getConf_number() {
            return conf_number;
        }

        public void setConf_number(String conf_number) {
            this.conf_number = conf_number;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public List<PaymentDetail> getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(List<PaymentDetail> payment_details) {
        this.payment_details = payment_details;
    }

    public class PaymentDetail{

        private String payment_method;
        private String payment_status;
        private String payment_amount;


        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getPayment_amount() {
            return payment_amount;
        }

        public void setPayment_amount(String payment_amount) {
            this.payment_amount = payment_amount;
        }

    }


    public List<PassengerList> getPassenger_information() {
        return passenger_information;
    }

    public void setPassenger_information(List<PassengerList> passenger_information) {
        this.passenger_information = passenger_information;
    }

    public String getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(String total_paid) {
        this.total_paid = total_paid;
    }

    public String getTotal_due() {
        return total_due;
    }

    public void setTotal_due(String total_due) {
        this.total_due = total_due;
    }


    public class FlightDetails {

        private String date;
        private String station;
        private String flight_number;
        private String time;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getFlight_number() {
            return flight_number;
        }

        public void setFlight_number(String flight_number) {
            this.flight_number = flight_number;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }



    }

    public class PriceDetails{

        private String title;
        private String guest;
        private String total_guest;
        private String infant;
        private String total_infant;
        private TaxesOrFees taxes_or_fees;
        private String total_taxes_or_fees;
        private String status;
        private List<Services> services;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Services> getServices() {
            return services;
        }

        public void setServices(List<Services> services) {
            this.services = services;
        }

        public TaxesOrFees getTaxes_or_fees() {
            return taxes_or_fees;
        }

        public void setTaxes_or_fees(TaxesOrFees taxes_or_fees) {
            this.taxes_or_fees = taxes_or_fees;
        }

        public String getTotal_taxes_or_fees() {
            return total_taxes_or_fees;
        }

        public void setTotal_taxes_or_fees(String total_taxes_or_fees) {
            this.total_taxes_or_fees = total_taxes_or_fees;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGuest() {
            return guest;
        }

        public void setGuest(String guest) {
            this.guest = guest;
        }

        public String getTotal_guest() {
            return total_guest;
        }

        public void setTotal_guest(String total_guest) {
            this.total_guest = total_guest;
        }

        public String getInfant() {
            return infant;
        }

        public void setInfant(String infant) {
            this.infant = infant;
        }

        public String getTotal_infant() {
            return total_infant;
        }

        public void setTotal_infant(String total_infant) {
            this.total_infant = total_infant;
        }

        public class TaxesOrFees{
            private String admin_fee;
            private String airport_tax;
            private String fuel_surcharge;
            private String goods_and_services_tax;
            private String total;


            public String getAdmin_fee() {
                return admin_fee;
            }

            public void setAdmin_fee(String admin_fee) {
                this.admin_fee = admin_fee;
            }

            public String getAirport_tax() {
                return airport_tax;
            }

            public void setAirport_tax(String airport_tax) {
                this.airport_tax = airport_tax;
            }

            public String getFuel_surcharge() {
                return fuel_surcharge;
            }

            public void setFuel_surcharge(String fuel_surcharge) {
                this.fuel_surcharge = fuel_surcharge;
            }

            public String getGoods_and_services_tax() {
                return goods_and_services_tax;
            }

            public void setGoods_and_services_tax(String goods_and_services_tax) {
                this.goods_and_services_tax = goods_and_services_tax;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }

        public class Services{

            private String service_name;
            private String service_price;

            public String getService_name() {
                return service_name;
            }

            public void setService_name(String service_name) {
                this.service_name = service_name;
            }

            public String getService_price() {
                return service_price;
            }

            public void setService_price(String service_price) {
                this.service_price = service_price;
            }
        }


    }

    public class PassengerList{

        public String name;

        public String  getPassengerName() {
            return name;
        }

        public void setPassengerName(String passengerName) {
            this.name = passengerName;
        }

    }

    public ContactInformation getContact_information() {
        return contact_information;
    }

    public void setContact_information(ContactInformation contact_information) {
        this.contact_information = contact_information;
    }

    public ItinenaryInformation getItenerary_information() {
        return itinerary_information;
    }

    public void setItenerary_information(ItinenaryInformation itenerary_information) {
        this.itinerary_information = itenerary_information;
    }

    public class ContactInformation{

        private String title;
        private String first_name;
        private String last_name;
        private String country;
        private String mobile_phone;
        private String alternate_phone;
        private String email;
        private String travel_purpose;

        public String getPurpose() {
            return travel_purpose;
        }

        public void setPurpose(String purpose) {
            this.travel_purpose = purpose;
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getAlternate_phone() {
            return alternate_phone;
        }

        public void setAlternate_phone(String alternate_phone) {
            this.alternate_phone = alternate_phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    public class ItinenaryInformation{

        private String booking_status;
        private String pnr;
        private String booking_date;

        public String getBooking_status() {
            return booking_status;
        }

        public void setBooking_status(String booking_status) {
            this.booking_status = booking_status;
        }

        public String getPnr() {
            return pnr;
        }

        public void setPnr(String pnr) {
            this.pnr = pnr;
        }

        public String getBooking_date() {
            return booking_date;
        }

        public void setBooking_date(String booking_date) {
            this.booking_date = booking_date;
        }

    }

    public List<PassengerList> getPassenger_lists() {
        return passenger_information;
    }

    public void setPassenger_lists(List<PassengerList> passenger_lists) {
        this.passenger_information = passenger_lists;
    }

    public String getTotal_price() {return total_price;}

    public void setTotal_price(String total_price) {this.total_price = total_price;}

    public List<FlightDetails> getFlight_details() {
        return flight_details;
    }

    public void setFlight_details(List<FlightDetails> flight_details) {
        this.flight_details = flight_details;
    }

    public List<PriceDetails> getPrice_details() {
        return price_details;
    }

    public void setPrice_details(List<PriceDetails> price_details) {
        this.price_details = price_details;
    }


    public ManageChangeContactReceive(ManageChangeContactReceive param_obj){
        this.obj = param_obj;
        status = obj.getStatus();
        itinerary_information = obj.getItenerary_information();
        contact_information = obj.getContact_information();
        message = obj.getMessage();
        insurance_details = obj.getInsurance_details();
        payment_details = obj.getPayment_details();
        flight_details = obj.getFlight_details();
        price_details = obj.getPrice_details();
        passenger_information = obj.getPassenger_information();

        total_price = obj.getTotal_price();
        total_paid = obj.getTotal_paid();
        total_due = obj.getTotal_due();
        booking_id = obj.getBooking_id();
        pnr = obj.getPnr();
    }



    public void setStatus(String status) {
        this.status = status;
    }

    public ManageChangeContactReceive getObj() {
        return obj;
    }



}


