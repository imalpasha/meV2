package com.metech.firefly.api.obj;

import com.metech.firefly.ui.object.PassengerInfo;

import java.util.List;

/**
 * Created by Dell on 2/3/2016.
 */
public class MobileCheckInPassengerReceive {

    private String status;
    private String message;
    private MobileCheckInPassengerReceive obj;
    private List<PassengerInfo> passengers;
    private String departure_station_code;
    private String arrival_station_code;
    private String pnr;
    private String signature;
    private List<Rule> rules;

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public class Rule{

        private Rule1 rule_1;
        private Rule2 rule_2;
        private Rule3 rule_3;

        public Rule1 getRule_1() {
            return rule_1;
        }

        public void setRule_1(Rule1 rule_1) {
            this.rule_1 = rule_1;
        }

        public Rule2 getRule_2() {
            return rule_2;
        }

        public void setRule_2(Rule2 rule_2) {
            this.rule_2 = rule_2;
        }

        public Rule3 getRule_3() {
            return rule_3;
        }

        public void setRule_3(Rule3 rule_3) {
            this.rule_3 = rule_3;
        }

        public class Rule1{

            private String image;
            private String text;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

        }

        public class Rule2{

            private String text;
            private String title_1;
            private String html_1;
            private String title_2;
            private String html_2;
            private String title_3;
            private String html_3;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTitle_1() {
                return title_1;
            }

            public void setTitle_1(String title_1) {
                this.title_1 = title_1;
            }

            public String getHtml_1() {
                return html_1;
            }

            public void setHtml_1(String html_1) {
                this.html_1 = html_1;
            }

            public String getTitle_2() {
                return title_2;
            }

            public void setTitle_2(String title_2) {
                this.title_2 = title_2;
            }

            public String getHtml_2() {
                return html_2;
            }

            public void setHtml_2(String html_2) {
                this.html_2 = html_2;
            }

            public String getTitle_3() {
                return title_3;
            }

            public void setTitle_3(String title_3) {
                this.title_3 = title_3;
            }

            public String getHtml_3() {
                return html_3;
            }

            public void setHtml_3(String html_3) {
                this.html_3 = html_3;
            }
        }

        public class Rule3{

            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
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

    public void setSignature(String data) {
        signature = data;
    }

    public List<PassengerInfo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerInfo> passengers) {
        this.passengers = passengers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public MobileCheckInPassengerReceive getObj() {
        return obj;
    }

    public void setObj(MobileCheckInPassengerReceive obj) {
        this.obj = obj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MobileCheckInPassengerReceive(MobileCheckInPassengerReceive dataObj) {
        obj = dataObj;
    }

}
