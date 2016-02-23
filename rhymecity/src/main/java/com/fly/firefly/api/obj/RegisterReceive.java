package com.fly.firefly.api.obj;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class RegisterReceive {

    private final RegisterReceive userObj;
    private String status;
    private String message;
    private user_info user_info;

    public RegisterReceive(RegisterReceive param_userObj) {
        this.userObj = param_userObj;
    }

    public RegisterReceive getUserObj() {
        return userObj;
    }

    public class user_info{

        private String username;
        private String password;
        private String signature;

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
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

    }

    public user_info getUserInfo() {
        return user_info;
    }

    public void setUserInfo(user_info userInfo) {
        this.user_info = userInfo;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
