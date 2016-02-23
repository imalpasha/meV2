package com.fly.firefly.api.obj;

import com.fly.firefly.ui.object.AirportObj;
import com.fly.firefly.ui.object.Country;
import com.fly.firefly.ui.object.State;
import com.fly.firefly.ui.object.TitleObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/9/2015.
 */
public class DeviceInfoSuccess{

    private final DeviceInfoSuccess userObj;
    private String data_version;
    private String location;
    private String banner_default;
    private String banner_promo;
    private List<TitleObj> data_title = new ArrayList<TitleObj>();
    private List<Country> data_country = new ArrayList<Country>();
    private List<State> data_state = new ArrayList<State>();
    private List<AirportObj> data_market = new ArrayList<AirportObj>();
    private String titleCode;
    private String titleName;
    private String status;
    private String signature;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public List<State> getData_state() {
        return data_state;
    }

    public void setData_state(List<State> data_state) {
        this.data_state = data_state;
    }

    public String getData_version() {
        return data_version;
    }

    public void setData_version(String data_version) {
        this.data_version = data_version;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBanner_default() {
        return banner_default;
    }

    public void setBanner_default(String banner_default) {
        this.banner_default = banner_default;
    }

    public String getBanner_promo() {
        return banner_promo;
    }

    public void setBanner_promo(String banner_promo) {
        this.banner_promo = banner_promo;
    }

    public List<TitleObj> getData_title() {
        return data_title;
    }

    public void setData_title(List<TitleObj> data_title) {
        this.data_title = data_title;
    }

    public List<Country> getData_country() {
        return data_country;
    }

    public void setData_country(List<Country> data_country) {
        this.data_country = data_country;
    }

    public List<AirportObj> getData_market() {
        return data_market;
    }

    public void setData_market(List<AirportObj> data_market) {
        this.data_market = data_market;
    }

    public DeviceInfoSuccess(DeviceInfoSuccess param_obj){
        this.userObj = param_obj;
    }

    public String getSignature() {
       return signature;
   }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DeviceInfoSuccess getObj() {
        return userObj;
    }


}


