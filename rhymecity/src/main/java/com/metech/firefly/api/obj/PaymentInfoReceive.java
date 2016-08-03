package com.metech.firefly.api.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 12/30/2015.
 */
public class PaymentInfoReceive {

    private String status;
    private String message;
    private PaymentInfoReceive obj;
    private String amount_due;
    private FOP fop;

    public class FOP{

        private String card_number;
        private String expiration_date_month;
        private String expiration_date_year;
        private String card_holder_name;
        private String account_number_id;

        private String card_type;

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getExpiration_date_month() {
            return expiration_date_month;
        }

        public void setExpiration_date_month(String expiration_date_month) {
            this.expiration_date_month = expiration_date_month;
        }

        public String getExpiration_date_year() {
            return expiration_date_year;
        }

        public void setExpiration_date_year(String expiration_date_year) {
            this.expiration_date_year = expiration_date_year;
        }

        public String getCard_holder_name() {
            return card_holder_name;
        }

        public void setCard_holder_name(String card_holder_name) {
            this.card_holder_name = card_holder_name;
        }

        public String getAccount_number_id() {
            return account_number_id;
        }

        public void setAccount_number_id(String account_number_id) {
            this.account_number_id = account_number_id;
        }

    }

    public FOP getFop() {
        return fop;
    }

    public void setFop(FOP fop) {
        this.fop = fop;
    }

    private List<PaymentChannel> payment_channel = new ArrayList<PaymentChannel>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public List<PaymentChannel> getPayment_channel() {
        return payment_channel;
    }

    public void setPayment_channel(List<PaymentChannel> payment_channel) {
        this.payment_channel = payment_channel;
    }


    public PaymentInfoReceive(PaymentInfoReceive returnObj) {
        this.obj = returnObj;
        status = returnObj.getStatus();
        message = returnObj.getMessage();
        amount_due = returnObj.getAmount_due();
        payment_channel = returnObj.getPayment_channel();
        fop = returnObj.getFop();
    }

    public PaymentInfoReceive getObj() {
        return obj;
    }

    public void setObj(PaymentInfoReceive obj) {
        this.obj = obj;
    }

    public class PaymentChannel{
       /* "channel_name": "Mastercard",
                "channel_code": "MC",
                "channel_type": 1,
                "channel_live": 1,
                "channel_status": 1,
                "channel_logo": "http://fyapidev.me-tech.com.my/paymentlogo/visa-mastercard-amex.png"*/

        private String channel_name;
        private String channel_code;
        private String channel_type;
        private String channel_live;
        private String channel_status;
        private String channel_logo;

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public String getChannel_code() {
            return channel_code;
        }

        public void setChannel_code(String channel_code) {
            this.channel_code = channel_code;
        }

        public String getChannel_type() {
            return channel_type;
        }

        public void setChannel_type(String channel_type) {
            this.channel_type = channel_type;
        }

        public String getChannel_live() {
            return channel_live;
        }

        public void setChannel_live(String channel_live) {
            this.channel_live = channel_live;
        }

        public String getChannel_status() {
            return channel_status;
        }

        public void setChannel_status(String channel_status) {
            this.channel_status = channel_status;
        }

        public String getChannel_logo() {
            return channel_logo;
        }

        public void setChannel_logo(String channel_logo) {
            this.channel_logo = channel_logo;
        }

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
