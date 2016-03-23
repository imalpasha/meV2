package com.fly.firefly.api.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/15/2015.
 */
public class PassengerInfoReveice {

    private String status;
    private String message;
    private PassengerInfoReveice obj;
    private insurance insurance;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    private String booking_id;


    public insurance getInsuranceObj() {
        return insurance;
    }

    public void setInsuranceObj(insurance insuranceObj) {
        this.insurance = insuranceObj;
    }


    public class insurance{

        private String status;
        private String code;
        private String logo;
        private ArrayList<String> html = new ArrayList<String>();

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
        public ArrayList<String> getHtml() {
            return html;
        }

        public void setHtml(ArrayList<String> html) {
            this.html = html;
        }
    }
    public PassengerInfoReveice(PassengerInfoReveice param_obj){

        status  = param_obj.getStatus();
        message = param_obj.getMessage();
        insurance = param_obj.getInsuranceObj();
        this.obj = param_obj;
    }
    public PassengerInfoReveice getObj() {
        return obj;
    }

    public void setObj(PassengerInfoReveice obj) {
        obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
