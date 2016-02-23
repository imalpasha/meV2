package com.fly.firefly.api.obj;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class ForgotPasswordReceive {

    private final ForgotPasswordReceive userObj;
    private String status;
    private String message;
    private UserInfo userInfo;


    public ForgotPasswordReceive(ForgotPasswordReceive param_userObj) {
        this.userObj = param_userObj;
    }

    public ForgotPasswordReceive getUserObj() {
        return userObj;
    }

   public class UserInfo{

        private String username;
        private String password;

        public String getEmail() {
            return username;
        }
        public void setEmail(String email) {
            this.username = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
