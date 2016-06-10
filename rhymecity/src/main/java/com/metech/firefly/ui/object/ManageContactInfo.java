package com.metech.firefly.ui.object;

/**
 * Created by Dell on 1/13/2016.
 */
public class  ManageContactInfo {

    private ManageContactInfo obj;
    private ContactInfo contacInfo;
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
    private String customer_number;

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSeat_selection_status() {
        return seat_selection_status;
    }

    public void setSeat_selection_status(String seat_selection_status) {
        this.seat_selection_status = seat_selection_status;
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

    public void setContact_mobile_phone(String contact_mobile_phone) {
        this.contact_mobile_phone = contact_mobile_phone;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public ManageContactInfo getObj() {
        return obj;
    }

    public void setObj(ManageContactInfo obj) {
        this.obj = obj;
    }

    public ManageContactInfo(ContactInfo contactObj,String pnrData,String usernameData,String signatureData) {
        //contacInfo = contactObj;
        contact_first_name = contactObj.getContact_first_name();
        booking_id = contactObj.getBooking_id();
        seat_selection_status = contactObj.getSeat_selection_status();
        signature = contactObj.getSignature();
        contact_travel_purpose = contactObj.getContact_travel_purpose();
        contact_title = contactObj.getContact_title();
        contact_first_name = contactObj.getContact_first_name();
        contact_last_name = contactObj.getContact_last_name();
        contact_email = contactObj.getContact_email();
        contact_company_name = contactObj.getContact_company_name();
        contact_address1 = contactObj.getContact_address1();
        contact_address2 = contactObj.getContact_address2();
        contact_address3 = contactObj.getContact_address3();
        contact_country = contactObj.getContact_country();
        contact_city = contactObj.getContact_city();
        contact_state = contactObj.getContact_state();
        contact_postcode = contactObj.getContact_postcode();
        contact_mobile_phone = contactObj.getContact_mobile_phone();
        contact_alternate_phone = contactObj.getContact_alternate_phone();
        insurance = contactObj.getInsurance();
        pnr = pnrData;
        customer_number = contactObj.getCustomer_number();
        username = usernameData;
        signature = signatureData;
    }

    public ManageContactInfo(ManageContactInfo obj2) {
        obj = obj2;
    }

    public ManageContactInfo() {
    }

}
