package com.metech.firefly.ui.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 3/11/2016.
 */
public class DefaultPassengerObj implements Parcelable {

    private String title;
    private String firstname;
    private String gender;
    private String lastname;
    private String first_name;
    private String last_name;
    private String issuingCountry;
    private String dob;
    private String bonuslink_card;
    private String nationality;
    private String id;
    private String type;
    private String status;
    private String message;
    private String enrich;

    public String getEnrich() {
        return enrich;
    }

    public void setEnrich(String enrich) {
        this.enrich = enrich;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassenger_type() {
        return type;
    }

    public void setPassenger_type(String passenger_type) {
        this.type = passenger_type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBonuslink_card() {
        return bonuslink_card;
    }

    public void setBonuslink_card(String bonuslink_card) {
        this.bonuslink_card = bonuslink_card;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public DefaultPassengerObj() {

    }

    public DefaultPassengerObj(Parcel in) {
        title = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        issuingCountry = in.readString();
        dob = in.readString();
        bonuslink_card = in.readString();
        nationality = in.readString();
        id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        type = in.readString();
        gender = in.readString();
        enrich = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(firstname);
        out.writeString(lastname);
        out.writeString(issuingCountry);
        out.writeString(dob);
        out.writeString(bonuslink_card);
        out.writeString(nationality);
        out.writeString(id);
        out.writeString(first_name);
        out.writeString(last_name);
        out.writeString(type);
        out.writeString(gender);
        out.writeString(enrich);
    }

    public static final Parcelable.Creator<DefaultPassengerObj> CREATOR = new Parcelable.Creator<DefaultPassengerObj>() {
        public DefaultPassengerObj createFromParcel(Parcel in) {
            return new DefaultPassengerObj(in);
        }

        public DefaultPassengerObj[] newArray(int size) {
            return new DefaultPassengerObj[size];
        }
    };

    public DefaultPassengerObj(DefaultPassengerObj param_obj) {

        title = param_obj.getTitle();
        first_name = param_obj.getFirst_name();
        gender = param_obj.getGender();
        last_name = param_obj.getLast_name();
        issuingCountry = param_obj.getIssuingCountry();
        dob = param_obj.getDob();
        nationality = param_obj.getNationality();
        id = param_obj.getId();
        type = param_obj.getPassenger_type();
        status = param_obj.getStatus();
        message = param_obj.getMessage();
        enrich = param_obj.getEnrich();

    }

}
