package com.metech.firefly.api.obj;

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
    private List<SSRMeal> meal;

    public class SSRMeal{

        private String destination_name;
        private List<SSRPassenger> passenger;
        private List<ListMeal> list_meal;

        public String getDestination_name() {
            return destination_name;
        }

        public void setType(String type) {
            this.destination_name = type;
        }

        public List<SSRPassenger> getPassenger() {
            return passenger;
        }

        public void setPassenger(List<SSRPassenger> passenger) {
            this.passenger = passenger;
        }

        public List<ListMeal> getList_meal() {
            return list_meal;
        }

        public void setList_meal(List<ListMeal> list_meal) {
            this.list_meal = list_meal;
        }

        public class SSRPassenger{

            private String pasenger_number;
            private String name;

            public String getPasenger_number() {
                return pasenger_number;
            }

            public void setPasenger_number(String pasenger_number) {
                this.pasenger_number = pasenger_number;
            }

            public String getPassenger_name() {
                return name;
            }

            public void setPassenger_name(String passenger_name) {
                this.name = passenger_name;
            }

        }

        public class ListMeal{

            private String meal_code;
            private String name;

            public String getMeal_code() {
                return meal_code;
            }

            public void setMeal_code(String meal_code) {
                this.meal_code = meal_code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }
    }

    public List<SSRMeal> getMeal() {
        return meal;
    }

    public void setMeal(List<SSRMeal> meal) {
        this.meal = meal;
    }

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
        meal = param_obj.getMeal();
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
