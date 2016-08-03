package com.metech.firefly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

public class SharedPrefManager {
    private static final String PREF_NAME = "AndroidHivePref";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String SELECTED = "SELECTED";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_INFO = "USER_INFO";
    public static final String CHECKIN_INFO = "CHECKIN_INFO";
    public static final String TERM_INFO = "TERM_INFO";
    public static final String TITLE = "TITLE";
    public static final String FLIGHT = "FLIGHT";
    public static final String COUNTRY = "COUNTRY";
    public static final String STATE = "STATE";

    public static final String ISLOGIN = "ISLOGIN";
    public static final String ISNEWSLETTER = "ISNEWSLETTER";
    public static final String PROMO_BANNER = "PM";
    public static final String DEFAULT_BANNER = "DB";
    public static final String USERNAME = "USERNAME";

    public static final String BOOKING_ID = "BOOKING_ID";
    // public static final String SELECTED = "SELECTED";

    public static final String SEAT = "SEAT";
    public static final String PAYMENT_DUMMY = "PAYMENT_DUMMY";
    public static final String PNR = "PNR";
    public static final String PASSWORD = "PASSWORD";
    public static final String SOCIAL_MEDIA ="SM";
    public static final String DATA_VERSION = "DV";
    public static final String TEMP_RESULT = "TR";
    public static final String BANNER_MODULE = "BM";

    public static final String FLIGHT_TYPE ="FT";
    public static final String USER_ID ="UI";

    public static final String OFFERSSR1 ="OFS1";
    public static final String OFFERSSR2 ="OFS2";

    public static final String CUSTOMER_NUMBER ="CN";

    public static final String PERSON_ID ="PI";
    public static final String BANNER_REDIRECT_URL ="BRU";

    public static final String APP_VERSION = "AV";

    public static final String FORCE_LOGOUT = "N";

    int PRIVATE_MODE = 0;
    Context _context;
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public SharedPrefManager(Context context) {
        this._context = context;
        _sharedPrefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        _prefsEditor = _sharedPrefs.edit();
    }

    /*ForceLogout*/
    public HashMap<String, String> getForceLogout() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(FORCE_LOGOUT, _sharedPrefs.getString(FORCE_LOGOUT, null));
        return init;
    }

    /*PAYMENT*/
    public HashMap<String, String> getBannerRedirectURL() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(BANNER_REDIRECT_URL, _sharedPrefs.getString(BANNER_REDIRECT_URL, null));
        return init;
    }

    /*SSR2*/
    public HashMap<String, String> getCustomerNumber() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(CUSTOMER_NUMBER, _sharedPrefs.getString(CUSTOMER_NUMBER, null));
        return init;
    }

    /*SSR2*/
    public HashMap<String, String> getPersonID() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PERSON_ID, _sharedPrefs.getString(PERSON_ID, null));
        return init;
    }

    /*SSR1*/
    public HashMap<String, String> getSSR1() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(OFFERSSR1, _sharedPrefs.getString(OFFERSSR1, null));
        return init;
    }

    /*SSR2*/
    public HashMap<String, String> getSSR2() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(OFFERSSR2, _sharedPrefs.getString(OFFERSSR2, null));
        return init;
    }



    /*user id*/
    public HashMap<String, String> getUserID() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USER_ID, _sharedPrefs.getString(USER_ID, null));
        return init;
    }

    /*SOCIAL MEDIA*/
    public HashMap<String, String> getAppVersion() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(APP_VERSION, _sharedPrefs.getString(APP_VERSION, null));
        return init;
    }

    /*SOCIAL MEDIA*/
    public HashMap<String, String> getFlightType() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(FLIGHT_TYPE, _sharedPrefs.getString(FLIGHT_TYPE, null));
        return init;
    }

    /*SOCIAL MEDIA*/
    public HashMap<String, String> getBannerModule() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(BANNER_MODULE, _sharedPrefs.getString(BANNER_MODULE, null));
        return init;
    }

    /*SOCIAL MEDIA*/
    public HashMap<String, String> getSocialMedia() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SOCIAL_MEDIA, _sharedPrefs.getString(SOCIAL_MEDIA, null));
        return init;
    }

    /*SOCIAL MEDIA*/
    public HashMap<String, String> getTempResult() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(TEMP_RESULT, _sharedPrefs.getString(TEMP_RESULT, null));
        return init;
    }

    /*DATA VERSION*/
    public HashMap<String, String> getDataVesion() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(DATA_VERSION, _sharedPrefs.getString(DATA_VERSION, null));
        return init;
    }

    /*PAYMENT*/
    public HashMap<String, String> getPNR() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PNR, _sharedPrefs.getString(PNR, null));
        return init;
    }

    /*PAYMENT*/
    public HashMap<String, String> getPaymentDummy() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PAYMENT_DUMMY, _sharedPrefs.getString(PAYMENT_DUMMY, null));
        return init;
    }

    /*SEAT*/
    public HashMap<String, String> getSeat() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SEAT, _sharedPrefs.getString(SEAT, null));
        return init;
    }

    /*Return Booking ID*/
    public HashMap<String, String> getBookingID() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(BOOKING_ID, _sharedPrefs.getString(BOOKING_ID, null));
        return init;
    }

    /*Return Login Status*/
    public HashMap<String, String> getPromoBanner() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PROMO_BANNER, _sharedPrefs.getString(PROMO_BANNER, null));
        return init;
    }

    /*Return State*/
    public HashMap<String, String> getState() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(STATE, _sharedPrefs.getString(STATE, null));
        return init;
    }

    /*Return Username*/
    public HashMap<String, String> getUsername() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USERNAME, _sharedPrefs.getString(USERNAME, null));
        return init;
    }

    /*Return UserInfo*/
    public HashMap<String, String> getUserInfo() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USER_INFO, _sharedPrefs.getString(USER_INFO, null));
        return init;
    }


    /*Return Login Status*/
    public HashMap<String, String> getDefaultBanner() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(DEFAULT_BANNER, _sharedPrefs.getString(DEFAULT_BANNER, null));
        return init;
    }


    /*Return Login Status*/
    public HashMap<String, String> getLoginStatus() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(ISLOGIN, _sharedPrefs.getString(ISLOGIN, null));
        return init;
    }

    /*Return newsletter Status*/
    public HashMap<String, String> getNewsletterStatus() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(ISNEWSLETTER, _sharedPrefs.getString(ISNEWSLETTER, null));
        return init;
    }


    /*Return Signature Value*/
    public HashMap<String, String> getSignatureFromLocalStorage() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SIGNATURE, _sharedPrefs.getString(SIGNATURE, null));
        return init;
    }

    /*Return Selected Value*/
    public HashMap<String, String> getSelectedPopupSelection() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(SELECTED, _sharedPrefs.getString(SELECTED, null));
        return init;
    }

    /*Return Selected Value*/
    public HashMap<String, String> getTitle() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(TITLE, _sharedPrefs.getString(TITLE, null));
        return init;
    }

    /*Return Country Value*/
    public HashMap<String, String> getCountry() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(COUNTRY, _sharedPrefs.getString(COUNTRY, null));
        return init;
    }

    /*Return Country Value*/
    public HashMap<String, String> getFlight() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(FLIGHT, _sharedPrefs.getString(FLIGHT, null));
        return init;
    }


    /*Return Checkin_info*/
    public HashMap<String, String> getCheckinInfo() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(CHECKIN_INFO, _sharedPrefs.getString(CHECKIN_INFO, null));
        return init;
    }


    /*Return Term_info*/
    public HashMap<String, String> getTermInfo() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(TERM_INFO, _sharedPrefs.getString(TERM_INFO, null));
        return init;
    }

    /*ForceLogout*/
    public void setForceLogout(String logout) {
        _prefsEditor.putString(FORCE_LOGOUT, logout);
        _prefsEditor.apply();
    }

    /*Set SEAT*/
    public void setCustomerNumber(String customerNumber) {
        _prefsEditor.putString(CUSTOMER_NUMBER, customerNumber);
        _prefsEditor.apply();
    }

    public void setSSR1(String type) {
        _prefsEditor.putString(OFFERSSR1, type);
        _prefsEditor.apply();
    }

    public void setSSR2(String type) {
        _prefsEditor.putString(OFFERSSR2, type);
        _prefsEditor.apply();
    }

    /*Return User_info*/
    public HashMap<String, String> getUserEmail() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(USER_EMAIL, _sharedPrefs.getString(USER_EMAIL, null));
        return init;
    }

    /*Return User_info*/
    public HashMap<String, String> getUserPassword() {
        HashMap<String, String> init = new HashMap<String, String>();
        init.put(PASSWORD, _sharedPrefs.getString(PASSWORD, null));
        return init;
    }

    /*Set Booking ID*/
    public void setPersonID(String personID) {
        _prefsEditor.putString(PERSON_ID, personID);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void setAppVersion(String version) {
        _prefsEditor.putString(APP_VERSION, version);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void setUserID(String id) {
        _prefsEditor.putString(USER_ID, id);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void setFlightType(String type) {
        _prefsEditor.putString(FLIGHT_TYPE, type);
        _prefsEditor.apply();
    }

    /*Set SEAT*/
    public void setTempResult(String tempResult) {
        _prefsEditor.putString(TEMP_RESULT, tempResult);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void setSocialMedia(String version) {
        _prefsEditor.putString(SOCIAL_MEDIA, version);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void setDataVersion(String version) {
        _prefsEditor.putString(DATA_VERSION, version);
        _prefsEditor.apply();
    }

    /*Set SEAT*/
    public void setPNR(String pnr) {
        _prefsEditor.putString(PNR, pnr);
        _prefsEditor.apply();
    }

    /*Set SEAT*/
    public void setPaymentDummy(String payment) {
        _prefsEditor.putString(PAYMENT_DUMMY, payment);
        _prefsEditor.apply();
    }

    /*Set SEAT*/
    public void setSeat(String seat) {
        _prefsEditor.putString(SEAT, seat);
        _prefsEditor.apply();
    }


    /*Set Booking ID*/
    public void setUserPassword(String password) {
        _prefsEditor.putString(PASSWORD, password);
        _prefsEditor.apply();
    }


    /*Set Booking ID*/
    public void setBookingID(String id) {
        _prefsEditor.putString(BOOKING_ID, id);
        _prefsEditor.apply();
    }


    /*Set Checkin Value*/
    public void setCheckinInfo(String url) {
        _prefsEditor.putString(CHECKIN_INFO, url);
        _prefsEditor.apply();
    }


    /*Set Checkin Value*/
    public void setTermInfo(String url) {
        _prefsEditor.putString(TERM_INFO, url);
        _prefsEditor.apply();
    }
    /*Set Username Value*/
    public void setUsername(String url) {
        _prefsEditor.putString(USERNAME, url);
        _prefsEditor.apply();
    }

    /*Set STATE value*/
    public void setState(String url) {
        _prefsEditor.putString(STATE, url);
        _prefsEditor.apply();
    }


    /*Set Username Value*/
    public void setUserInfo(String url) {
        _prefsEditor.putString(USER_INFO, url);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setBannerUrl(String url) {
        _prefsEditor.putString(DEFAULT_BANNER, url);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setPromoBannerUrl(String url) {
        _prefsEditor.putString(PROMO_BANNER, url);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setBannerModule(String module) {
        _prefsEditor.putString(BANNER_MODULE, module);
        _prefsEditor.apply();
    }

    /*Set Signature Value*/
    public void setBannerRedirectURL(String url) {
        _prefsEditor.putString(BANNER_REDIRECT_URL,url);
        _prefsEditor.apply();
    }



    /*Set Signature Value*/
    public void setLoginStatus(String status) {
        _prefsEditor.putString(ISLOGIN, status);
        _prefsEditor.apply();
    }


    /*Set Signature Value*/
    public void setNewsletterStatus(String status) {
        _prefsEditor.putString(ISNEWSLETTER, status);
        _prefsEditor.apply();
    }
    /*Set Signature Value*/
    public void setSignatureToLocalStorage(String signature) {
        _prefsEditor.putString(SIGNATURE, signature);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setSelectedPopupSelection(String signature) {
        _prefsEditor.putString(SELECTED, signature);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setUserTitle(String title) {
        _prefsEditor.putString(TITLE, title);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setFlight(String flight) {
        _prefsEditor.putString(FLIGHT, flight);
        _prefsEditor.apply();
    }

    /*Set Airport Value*/
    public void setCountry(String country) {
        _prefsEditor.putString(COUNTRY, country);
        _prefsEditor.apply();
    }


    /*Set Userinfo Value*/
    public void setUserEmail(String url) {
        _prefsEditor.putString(USER_EMAIL, url);
        _prefsEditor.apply();
    }

    /*Set Booking ID*/
    public void clearSocialMedia() {
        _sharedPrefs.edit().remove(SOCIAL_MEDIA).apply();
    }

    /*Clear TermValue*/
    public void clearPNR() {
        // Clearing All URL
        _sharedPrefs.edit().remove(PNR).apply();

    }

    /*Clear Checkin Value*/
    public void removeUserID() {
        // Clearing All URL
        _sharedPrefs.edit().remove(USER_ID).apply();

    }
    /*Clear Checkin Value*/
    public void removeSeat() {
        // Clearing All URL
        _sharedPrefs.edit().remove(SEAT).apply();

    }

    /*Clear Checkin Value*/
    public void checkinInfoURL() {
        // Clearing All URL
        _sharedPrefs.edit().remove(CHECKIN_INFO).apply();

    }

    /*Clear Checkin Value*/
    public void clearPassword() {
        // Clearing All URL
        _sharedPrefs.edit().remove(PASSWORD).apply();

    }

    /*Clear TermValue*/
    public void clearPayment() {
        // Clearing All URL
        _sharedPrefs.edit().remove(PAYMENT_DUMMY).apply();


    }

    /*Clear TermValue*/
    public void termInfoURL() {
        // Clearing All URL
        _sharedPrefs.edit().remove(TERM_INFO).apply();

    }

    /*Clear State Value*/
    public void clearFlightType() {
        // Clearing Selected
        _sharedPrefs.edit().remove(FLIGHT_TYPE).apply();
    }

    /*Clear Signature Value*/
    public void clearDataVersion() {
        // Clearing All URL
        _sharedPrefs.edit().remove(DATA_VERSION).apply();

    }


    /*Clear Signature Value*/
    public void clearBannerURL() {
        // Clearing All URL
        _sharedPrefs.edit().remove(PROMO_BANNER).apply();
        _sharedPrefs.edit().remove(DEFAULT_BANNER).apply();

    }

    /*Clear Login Status*/
    public void clearLoginStatus() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(ISLOGIN).apply();
    }

    /*Clear Login Status*/
    public void clearNewsletterStatus() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(ISNEWSLETTER).apply();
    }

    /*Clear Signature Value*/
    public void clearSignatureFromLocalStorage() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(SIGNATURE).apply();
    }

    /*Clear Signature Value*/
    public void setUsername() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(USERNAME).apply();
    }

    public void setUserInfo() {
        // Clearing Siganture
        _sharedPrefs.edit().remove(USER_INFO).apply();
    }

    /*Clear Selected Value*/
    public void clearBookingID() {
        // Clearing Selected
        _sharedPrefs.edit().remove(BOOKING_ID).apply();
    }

    /*Clear Selected Value*/
    public void clearSelectedPopupSelection() {
        // Clearing Selected
        _sharedPrefs.edit().remove(SELECTED).apply();
    }

    /*Clear Selected Value*/
    public void clearTitle() {
        // Clearing Selected
        _sharedPrefs.edit().remove(TITLE).apply();
    }

    /*Clear State Value*/
    public void clearState() {
        // Clearing Selected
        _sharedPrefs.edit().remove(STATE).apply();
    }

    /*Clear State Value*/
    public void clearAppVersion() {
        // Clearing Selected
        _sharedPrefs.edit().remove(APP_VERSION).apply();
    }

    /*Clear Flight Value*/
    public void clearFlight() {
        // Clearing Selected
        _sharedPrefs.edit().remove(FLIGHT).apply();
    }

    /*Clear Country Value*/
    public void clearCoutnry() {
        // Clearing Selected
        _sharedPrefs.edit().remove(COUNTRY).apply();
    }


    /*Clear UserInfo Value*/
    public void clearUserEmail() {
        // Clearing Selected
        _sharedPrefs.edit().remove(USER_EMAIL).apply();
    }

    /*Clear UserInfo Value*/
    public void clearTempResult() {
        // Clearing Selected
        _sharedPrefs.edit().remove(TEMP_RESULT).apply();
        Log.e("Clear", "True");
    }

    /*Clear UserInfo Value*/
    public void clearBannerModule() {
        // Clearing Selected
        _sharedPrefs.edit().remove(BANNER_MODULE).apply();
        Log.e("Clear", "True");
    }

    /*Clear UserInfo Value*/
    public void clearCustomerNumber() {
        // Clearing Selected
        _sharedPrefs.edit().remove(CUSTOMER_NUMBER).apply();
        Log.e("Clear", "True");
    }

    /*Clear UserInfo Value*/
    public void clearPersonID() {
        // Clearing Selected
        _sharedPrefs.edit().remove(PERSON_ID).apply();
        Log.e("Clear", "True");
    }
}