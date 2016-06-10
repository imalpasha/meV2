package com.metech.firefly.api;

import com.metech.firefly.api.obj.AboutUsReceive;
import com.metech.firefly.api.obj.ChangePasswordReceive;
import com.metech.firefly.api.obj.ChangeSearchFlightReceive;
import com.metech.firefly.api.obj.CheckInListReceive;
import com.metech.firefly.api.obj.ConfirmUpdateReceive;
import com.metech.firefly.api.obj.ContactInfoReceive;
import com.metech.firefly.api.obj.DeviceInfoSuccess;
import com.metech.firefly.api.obj.FlightSummaryReceive;
import com.metech.firefly.api.obj.ForgotPasswordReceive;
import com.metech.firefly.api.obj.ListBookingReceive;
import com.metech.firefly.api.obj.LoginReceive;
import com.metech.firefly.api.obj.ManageChangeContactReceive;
import com.metech.firefly.api.obj.ManageRequestIntinenary;
import com.metech.firefly.api.obj.MobileCheckInPassengerReceive;
import com.metech.firefly.api.obj.MobileCheckinReceive;
import com.metech.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.metech.firefly.api.obj.PassengerInfoReveice;
import com.metech.firefly.api.obj.PaymentInfoReceive;
import com.metech.firefly.api.obj.PaymentReceive;
import com.metech.firefly.api.obj.PushNotificationReceive;
import com.metech.firefly.api.obj.RegisterReceive;
import com.metech.firefly.api.obj.RetrieveBoardingPassReceive;
import com.metech.firefly.api.obj.SSRReceive;
import com.metech.firefly.api.obj.SearchFlightReceive;
import com.metech.firefly.api.obj.SeatSelectionReveice;
import com.metech.firefly.api.obj.SelectFlightReceive;
import com.metech.firefly.api.obj.ItineraryInfoReceive;
import com.metech.firefly.api.obj.TermsReceive;
import com.metech.firefly.api.obj.UpdateProfileReceive;
import com.metech.firefly.api.obj.tryObj;
import com.metech.firefly.ui.object.AboutUs;
import com.metech.firefly.ui.object.ChangePasswordRequest;
import com.metech.firefly.ui.object.ChangeSSR;
import com.metech.firefly.ui.object.ConfirmUpdateRequest;
import com.metech.firefly.ui.object.ContactInfo;
import com.metech.firefly.ui.object.DeviceInformation;
import com.metech.firefly.ui.object.FlightSummary;
import com.metech.firefly.ui.object.GetChangeFlight;
import com.metech.firefly.ui.object.GetFlightAvailability;
import com.metech.firefly.ui.object.GetSSR;
import com.metech.firefly.ui.object.LoginRequest;
import com.metech.firefly.ui.object.ManageContactInfo;
import com.metech.firefly.ui.object.ManageFlightObj;
import com.metech.firefly.ui.object.ManageFlightObjV2;
import com.metech.firefly.ui.object.ManageFlightObjV3;
import com.metech.firefly.ui.object.ManagePassengerInfo;
import com.metech.firefly.ui.object.ManageSeatInfo;
import com.metech.firefly.ui.object.MobileCheckInPassenger;
import com.metech.firefly.ui.object.MobileCheckinObj;
import com.metech.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.metech.firefly.ui.object.Passenger;
import com.metech.firefly.ui.object.PasswordRequest;
import com.metech.firefly.ui.object.Payment;
import com.metech.firefly.ui.object.RegisterObj;
import com.metech.firefly.ui.object.RetrieveBoardingPassObj;
import com.metech.firefly.ui.object.SearchFlightObj;
import com.metech.firefly.ui.object.SeatAvailabilityRequest;
import com.metech.firefly.ui.object.SeatSelection;
import com.metech.firefly.ui.object.SelectChangeFlight;
import com.metech.firefly.ui.object.SelectFlight;
import com.metech.firefly.ui.object.SendItinenaryObj;
import com.metech.firefly.ui.object.Signature;
import com.metech.firefly.ui.object.UpdateProfileRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ApiService {


    //@POST("/api.php")
    //void onRegisterNotification(@Body PushNotificationObj task, Callback<PushNotificationReceive> callback);

    @FormUrlEncoded
    @POST("/api.php")
    void onRegisterNotification(@Field("cmd")String cmd,@Field("user_id")String user_id,@Field("token")String token,@Field("name")String name,@Field("code")String code, Callback<PushNotificationReceive> callback);

    @GET("/users/{user}")
    void getFeed2(@Path("user") String user, Callback<LoginRequest> callback);

    @GET("/apis/8891c88e")
        //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    void getGoogleSpreedSheetData(Callback<tryObj> callback);

    //@FormUrlEncoded
    @POST("/login")
    //void getGoogleSpreedSheetData(@Path("user") String user, Callback<tryObj> callback);
    //void onRequestToLogin(@Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    //void onRequestToLogin(@Field("number") int number, @Field("username") String username, @Field("password") String password,Callback<LoginRequest> callback);
    void onRequestToLogin(@Body LoginRequest task, Callback<LoginReceive> callback);

    // @Body JSONObject searchstring

    @POST("/loading")
    void onSendDeviceInfo(@Body DeviceInformation task, Callback<DeviceInfoSuccess> callback);

    @POST("/register")
    void onRegisterRequest(@Body RegisterObj obj, Callback<RegisterReceive> callback);

    @POST("/searchFlight")
    void onSearchFlightRequest(@Body SearchFlightObj obj, Callback<SearchFlightReceive> callback);

    @POST("/forgotPassword")
    void onRequestPassword(@Body PasswordRequest task, Callback<ForgotPasswordReceive> callback);

    @POST("/changePassword")
    void onRequestChangePassword(@Body ChangePasswordRequest task, Callback<ChangePasswordReceive> callback);

    @POST("/updateProfile")
    void onUpdateProfileRequest(@Body UpdateProfileRequest task, Callback<UpdateProfileReceive> callback);

    @POST("/selectFlight")
    void onSelectFlight(@Body SelectFlight task, Callback<SelectFlightReceive> callback);

    @POST("/passengerDetails")
    void onPassengerInfo(@Body Passenger task, Callback<PassengerInfoReveice> callback);

    @POST("/contactDetails")
    void onContactInfo(@Body ContactInfo task, Callback<ContactInfoReceive> callback);

    @POST("/checkIn")
    void onMobileCheckinRequest(@Body MobileCheckinObj obj, Callback<MobileCheckinReceive> callback);

    @POST("/checkInPassengerList")
    void onPassengerCheckIn(@Body MobileCheckInPassenger obj, Callback<MobileCheckInPassengerReceive> callback);

    @POST("/checkInConfirmation")
    void onConfirmPassengerCheckIn(@Body MobileConfirmCheckInPassenger obj, Callback<MobileConfirmCheckInPassengerReceive> callback);

    @POST("/flightSummary")
    void onItineraryRequest( Callback<ItineraryInfoReceive> callback);

    @POST("/seatMap")
    void onSeatSelection(@Body SeatSelection obj, Callback<SeatSelectionReveice> callback);

    @POST("/terms")
    //void onTermsRequest(@Body TermsRequest obj, Callback<TermsReceive> callback);
    void onTermsRequest(Callback<TermsReceive> callback);

    @POST("/selectionPayment")
    void onPaymentInfo(@Body Signature obj, Callback<PaymentInfoReceive> callback);

    @POST("/paymentProcess")
    void onPaymentProcess(@Body Payment obj, Callback<PaymentReceive> callback);

    @POST("/flightSummary")
    void onFlightSummary(@Body FlightSummary obj, Callback<FlightSummaryReceive> callback);

    @POST("/retrieveBooking")
    void onManageFlight(@Body ManageFlightObj obj, Callback<FlightSummaryReceive> callback);

    @POST("/retrieveBookingList")
    void onManageFlightV2(@Body ManageFlightObjV2 obj, Callback<ListBookingReceive> callback);

    @POST("/retrieveBookingList")
    void onManageFlightV3(@Body ManageFlightObjV3 obj, Callback<CheckInListReceive> callback);

    @POST("/changeContact")
    void onChangeContactInfo(@Body ManageContactInfo obj, Callback<ManageChangeContactReceive> callback);

    @POST("/changeConfirmation")
    void onChangeRequestConfirm(@Body ConfirmUpdateRequest obj, Callback<ConfirmUpdateReceive> callback);

    @POST("/editPassengers")
    void onChangePassenger(@Body ManagePassengerInfo obj, Callback<ManageChangeContactReceive> callback);

    @POST("/getSeatAvailability")
    void onSeatRequest(@Body SeatAvailabilityRequest obj, Callback<ContactInfoReceive> callback);

    @POST("/changeSeat")
    void onChangeSeat(@Body ManageSeatInfo obj, Callback<ManageChangeContactReceive> callback);


    @POST("/sendItinerary")
    void onRequestItinenary(@Body SendItinenaryObj obj, Callback<ManageRequestIntinenary> callback);

    @POST("/getFlightAvailability")
    void onGetFlightRequest(@Body GetFlightAvailability obj, Callback<ChangeSearchFlightReceive> callback);

    @POST("/searchChangeFlight")
    void onGetChangeFlight(@Body GetChangeFlight obj, Callback<SearchFlightReceive> callback);

    @POST("/selectChangeFlight")
    void onSelectChangeFlight(@Body SelectChangeFlight obj, Callback<ManageChangeContactReceive> callback);

    @POST("/getBoardingPass")
    void onRetrieveBoardingPass(@Body RetrieveBoardingPassObj obj, Callback<RetrieveBoardingPassReceive> callback);

    @POST("/getAboutUS")
    void onRetrieveAboutUs(@Body AboutUs obj, Callback<AboutUsReceive> callback);

    @POST("/getMealSSR")
    void onRetrieveSSR(@Body GetSSR obj, Callback<SSRReceive> callback);


    @POST("/changeSSR")
    void onChangeSSR(@Body ChangeSSR obj, Callback<ManageChangeContactReceive> callback);



    //@POST("/retrieveBooking")
    //void onChangeContactInfo(@Body ManageContactInfo obj, Callback<ManageChangeContactReceive> callback);

    //onRetrieveFlightSummary

}


