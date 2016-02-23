package com.fly.firefly.ui.object;

/**
 * Created by Dell on 12/16/2015.
 */
public class ContactInfo {

    private String booking_id;
    private String seat_selection_status;
    private String contact_travel_purpose;
    private String contact_title;
    private String contact_first_name;
    private String contact_last_name;
    private String contact_email;
    private String contact_company_name;
    private String contact_address1;
    private String contact_address2;
    private String contact_address3;
    private String contact_country;
    private String contact_city;
    private String contact_state;
    private String contact_postcode;
    private String contact_mobile_phone;
    private String contact_alternate_phone;
    private String insurance;
    private String signature;
    private String pnr;
    private String username;


    public ContactInfo(){

    }

    public ContactInfo(ContactInfo data){

        booking_id = data.getBooking_id();
        seat_selection_status = data.getSeat_selection_status();
        signature = data.getSignature();
        contact_travel_purpose = data.getContact_travel_purpose();
        contact_title = data.getContact_title();
        contact_first_name = data.getContact_first_name();
        contact_last_name = data.getContact_last_name();
        contact_email = data.getContact_email();
        contact_company_name = data.getContact_company_name();
        contact_address1 = data.getContact_address1();
        contact_address2 = data.getContact_address2();
        contact_address3 = data.getContact_address3();
        contact_country = data.getContact_country();
        contact_city = data.getContact_city();
        contact_state = data.getContact_state();
        contact_postcode = data.getContact_postcode();
        contact_mobile_phone = data.getContact_mobile_phone();
        contact_alternate_phone = data.getContact_alternate_phone();
        insurance = data.getInsurance();



    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeat_selection_status() {
        return seat_selection_status;
    }

    public void setSeat_selection_status(String seat_selection_status) {
        this.seat_selection_status = seat_selection_status;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getContact_travel_purpose() {
        return contact_travel_purpose;
    }

    public void setContact_travel_purpose(String contact_travel_purpose) {
        this.contact_travel_purpose = contact_travel_purpose;
    }

    public String getContact_title() {
        return contact_title;
    }

    public void setContact_title(String contact_title) {
        this.contact_title = contact_title;
    }

    public String getContact_first_name() {
        return contact_first_name;
    }

    public void setContact_first_name(String contact_first_name) {
        this.contact_first_name = contact_first_name;
    }

    public String getContact_last_name() {
        return contact_last_name;
    }

    public void setContact_last_name(String contact_last_name) {
        this.contact_last_name = contact_last_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_company_name() {
        return contact_company_name;
    }

    public void setContact_company_name(String contact_company_name) {
        this.contact_company_name = contact_company_name;
    }

    public String getContact_address1() {
        return contact_address1;
    }

    public void setContact_address1(String contact_address1) {
        this.contact_address1 = contact_address1;
    }

    public String getContact_address2() {
        return contact_address2;
    }

    public void setContact_address2(String contact_address2) {
        this.contact_address2 = contact_address2;
    }

    public String getContact_address3() {
        return contact_address3;
    }

    public void setContact_address3(String contact_address3) {
        this.contact_address3 = contact_address3;
    }

    public String getContact_country() {
        return contact_country;
    }

    public void setContact_country(String contact_country) {
        this.contact_country = contact_country;
    }

    public String getContact_city() {
        return contact_city;
    }

    public void setContact_city(String contact_city) {
        this.contact_city = contact_city;
    }

    public String getContact_state() {
        return contact_state;
    }

    public void setContact_state(String contact_state) {
        this.contact_state = contact_state;
    }

    public String getContact_postcode() {
        return contact_postcode;
    }

    public void setContact_postcode(String contact_postcode) {
        this.contact_postcode = contact_postcode;
    }

    public String getContact_mobile_phone() {
        return contact_mobile_phone;
    }

    public void setContact_mobile_phone(String ntact_mobile_phone) {
        this.contact_mobile_phone = ntact_mobile_phone;
    }

    public String getContact_alternate_phone() {
        return contact_alternate_phone;
    }

    public void setContact_alternate_phone(String contact_alternate_phone) {
        this.contact_alternate_phone = contact_alternate_phone;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


}
