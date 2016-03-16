package com.fly.firefly.ui.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Dell on 3/11/2016.
 */
public class DefaultPassengerObj implements Parcelable {

    private String title;
    private String firstname;
    private String lastname;
    private String issuingCountry;

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

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(firstname);
        out.writeString(lastname);
        out.writeString(issuingCountry);
    }

    public static final Parcelable.Creator<DefaultPassengerObj> CREATOR = new Parcelable.Creator<DefaultPassengerObj>() {
        public DefaultPassengerObj createFromParcel(Parcel in) {
            return new DefaultPassengerObj(in);
        }

        public DefaultPassengerObj[] newArray(int size) {
            return new DefaultPassengerObj[size];
        }
    };
}
