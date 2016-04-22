package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.ChangeSSRReceive;
import com.fly.firefly.api.obj.ChangeSearchFlightReceive;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.ConfirmUpdateReceive;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.GetFlightAvailabilityReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.ManageFlightReceive;
import com.fly.firefly.api.obj.ManageRequestIntinenary;
import com.fly.firefly.api.obj.SSRReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SplashFailedConnect;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.ui.object.ChangeSSR;
import com.fly.firefly.ui.object.ConfirmUpdateRequest;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.GetChangeFlight;
import com.fly.firefly.ui.object.GetFlightAvailability;
import com.fly.firefly.ui.object.GetSSR;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.ManageContactInfo;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.ManageFlightObjV2;
import com.fly.firefly.ui.object.ManageFlightObjV3;
import com.fly.firefly.ui.object.ManageFlightRetrieveObj;
import com.fly.firefly.ui.object.ManagePassengerInfo;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.object.SeatAvailabilityRequest;
import com.fly.firefly.ui.object.SendItinenaryObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ManageFlightPrenter {

    public interface ManageFlightView {
        void onGetFlightFromPNR(FlightSummaryReceive event);
        void onUserPnrList(ListBookingReceive event);
        void onCheckUserStatus(CheckInListReceive event);
    }

    public interface ChangeSpecialMealView{
        void onSSRReceive(SSRReceive obj);
        void onChangeSSRReceive(ManageChangeContactReceive obj);
    }

    public interface ChangeContactView {
        void onGetChangeContact(ManageChangeContactReceive obj);
    }

    public interface ConfirmUpdateView {
        void changeConfirm(ConfirmUpdateReceive obj);
    }

    public interface ChangePassengerInfoView {
        void onChangePassengerInfo(ManageChangeContactReceive obj);
    }

    public interface ChangeSeatView {
        void onRequestSeat(ContactInfoReceive obj);
        void onSeatChange(ManageChangeContactReceive obj);

    }

    public interface SendItinenary {
        void onSuccessRequest(ManageRequestIntinenary obj);
    }

    public interface GetFlightView {
        void onGetFlightSuccess(ChangeSearchFlightReceive obj);
        void onNewFlightReceive(SearchFlightReceive obj);
    }



    private ManageFlightView view;
    private ChangeContactView view2;
    private ConfirmUpdateView view3;
    private ChangePassengerInfoView view4;
    private ChangeSeatView view5;
    private SendItinenary view6;
    private GetFlightView view7;
    private ChangeSpecialMealView view8;

    private final Bus bus;

    public ManageFlightPrenter(ManageFlightView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeContactView view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ConfirmUpdateView view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangePassengerInfoView view, Bus bus) {
        this.view4 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeSeatView view, Bus bus) {
        this.view5 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(SendItinenary view, Bus bus) {
        this.view6 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(GetFlightView view, Bus bus) {
        this.view7 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeSpecialMealView view, Bus bus) {
        this.view8 = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void getSSRMeal(GetSSR data){
        bus.post(new GetSSR(data));
    }

    public void changeMeal(ChangeSSR data){
        bus.post(new ChangeSSR(data));
    }

    public void onSendPNRV1(ManageFlightObj data) {
        bus.post(new ManageFlightObj(data));
    }

    public void onSendPNRV2(String username,String password,String module) {
        bus.post(new ManageFlightObjV2(username,password,module));
    }

    public void onSendPNRV3(String username,String password,String module) {
        bus.post(new ManageFlightObjV3(username,password,module));
    }

    public void onSeatAvailability(SeatAvailabilityRequest data) {
        bus.post(new SeatAvailabilityRequest(data));
    }

    public void onChangeContact(ContactInfo data,String pnr , String username,String signature) {
        bus.post(new ManageContactInfo(data,pnr,username,signature));
    }

    public void requestChange(ConfirmUpdateRequest data) {
        bus.post(new ConfirmUpdateRequest(data));
    }

    public void onChangePassengerInfo(ManagePassengerInfo data,String pnr , String username,String signature) {
        bus.post(new ManagePassengerInfo(data, pnr, username, signature));
    }

    public void seatSelect(ManageSeatInfo data,String pnr , String username,String signature) {
        bus.post(new ManageSeatInfo(data, pnr, username, signature));
    }

    public void onSentItinenary(SendItinenaryObj obj) {
        bus.post(new SendItinenaryObj(obj));
    }

    public void onGetFlightAvailability(GetFlightAvailability obj) {
        bus.post(new GetFlightAvailability(obj));
    }

    public void onNewFlightDate(GetChangeFlight data) {
        bus.post(new GetChangeFlight(data));
    }

    @Subscribe
    public void onUserSuccessLogin(FlightSummaryReceive event) {
        try{
            view.onGetFlightFromPNR(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
        try{
            view.onUserPnrList(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onUserPnrList2(CheckInListReceive event) {
        try{
            view.onCheckUserStatus(event);
        }catch (Exception e){

        }
    }


    @Subscribe
    public void onManageChangeContact(ManageChangeContactReceive event) {
        try{
            view2.onGetChangeContact(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onChangeConfirmSuccess(ConfirmUpdateReceive event) {
        view3.changeConfirm(event);
    }

    @Subscribe
    public void onChangeConfirmSuccess(ManageChangeContactReceive event) {
        try{
            view4.onChangePassengerInfo(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onSeatRequestSuccess(ContactInfoReceive event) {
        view5.onRequestSeat(event);
    }

    @Subscribe
    public void onSeatChange(ManageChangeContactReceive event) {
        try{
            view5.onSeatChange(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onSuccessRequestItinenary(ManageRequestIntinenary event) {
        view6.onSuccessRequest(event);
    }

    @Subscribe
    public void onGetFlight(ChangeSearchFlightReceive event) {
        view7.onGetFlightSuccess(event);
    }

    @Subscribe
    public void onGetNewFlight(SearchFlightReceive event) {
        view7.onNewFlightReceive(event);
    }

    @Subscribe
    public void onSSRReceive(SSRReceive event) {
        view8.onSSRReceive(event);
    }

    @Subscribe
    public void onChangeSSRReceive(ManageChangeContactReceive event) {
        try{
            view8.onChangeSSRReceive(event);
        }catch (Exception e){

        }
    }



    //@Subscribe
    //public void onSuccessGetFlightList(SearchFlightReceive event) {
    //    view6.onSuccessRequest(event);
   // }

}
