package com.metech.firefly.api.obj;

import com.metech.firefly.ui.object.AirportObj;
import com.metech.firefly.ui.object.Country;
import com.metech.firefly.ui.object.State;
import com.metech.firefly.ui.object.TitleObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 11/9/2015.
 */
public class DeviceInfoSuccess{

    private final DeviceInfoSuccess userObj;

    private AppVersion data_version_mobile;
    private String data_version;
    private String location;
    private String banner_default;
    private String banner_promo;
    private String banner_module;
    private String banner_url;
    private List<TitleObj> data_title = new ArrayList<TitleObj>();
    private List<Country> data_country = new ArrayList<Country>();
    private List<State> data_state = new ArrayList<State>();
    private List<AirportObj> data_market = new ArrayList<AirportObj>();
    private SocialMedia social_media_link;
    private String titleCode;
    private String titleName;
    private String status;
    private String signature;

    public class AppVersion{

        private String version;
        private String force_update;

        public String getForce_update() {
            return force_update;
        }
        public void setForce_update(String force_update) {
            this.force_update = force_update;
        }
        public String getVersion() {
            return version;
        }
        public void setVersion(String version) {
            this.version = version;
        }

    }

    public AppVersion getData_version_mobile() {
        return data_version_mobile;
    }

    public void setData_version_mobile(AppVersion data_version_mobile) {
        this.data_version_mobile = data_version_mobile;
    }

    public String getBanner_redirect_url() {
        return banner_url;
    }

    public void setBanner_redirect_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public SocialMedia getSocial_media() {
        return social_media_link;
    }

    public void setSocial_media(SocialMedia social_media) {
        this.social_media_link = social_media;
    }

    public String getBanner_module() {
        return banner_module;
    }

    public void setBanner_module(String banner_module) {
        this.banner_module = banner_module;
    }

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

    public class SocialMedia {

        private String facebook;
        private String twitter;
        private String instagram;

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

    }


}


