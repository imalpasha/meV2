package com.fly.firefly.api.obj;

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
