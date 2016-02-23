package com.fly.firefly.api;

import com.fly.firefly.api.obj.ChangePasswordReceive;
import com.fly.firefly.api.obj.ChangeSearchFlightReceive;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.ConfirmUpdateReceive;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.GetFlightAvailabilityReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.ManageFlightReceive;
import com.fly.firefly.api.obj.ManageRequestIntinenary;
import com.fly.firefly.api.obj.MobileCheckInPassengerReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.PaymentInfoReceive;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.api.obj.PushNotificationReceive;
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SelectChangeFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.api.obj.ItineraryInfoReceive;
import com.fly.firefly.api.obj.TermsReceive;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.api.obj.tryObj;
import com.fly.firefly.ui.object.BaseObj;
import com.fly.firefly.ui.object.ChangePasswordRequest;
import com.fly.firefly.ui.object.ConfirmUpdateRequest;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.object.FlightSummary;
import com.fly.firefly.ui.object.GetChangeFlight;
import com.fly.firefly.ui.object.GetFlightAvailability;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.ManageContactInfo;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.ManageFlightObjV2;
import com.fly.firefly.ui.object.ManageFlightObjV3;
import com.fly.firefly.ui.object.ManagePassengerInfo;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.MobileCheckInPassenger;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.object.Payment;
import com.fly.firefly.ui.object.PushNotificationObj;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SeatAvailabilityRequest;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SelectChangeFlight;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.object.SendItinenaryObj;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.ui.object.TermsRequest;
import com.fly.firefly.ui.object.UpdateProfileRequest;

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








    //@POST("/retrieveBooking")
    //void onChangeContactInfo(@Body ManageContactInfo obj, Callback<ManageChangeContactReceive> callback);

    //onRetrieveFlightSummary

}


