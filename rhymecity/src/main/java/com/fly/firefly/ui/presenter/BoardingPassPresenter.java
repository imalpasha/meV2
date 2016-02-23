package com.fly.firefly.ui.presenter;

import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.PaymentInfoReceive;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SelectChangeFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.ui.object.BaseObj;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.FlightSummary;
import com.fly.firefly.ui.object.ManageFlightObjV2;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.Payment;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SelectChangeFlight;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.object.Signature;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BoardingPassPresenter {

    public interface RetrieveBoardingPassView {
        void onBoardingPassReceive(RetrieveBoardingPassReceive obj);
        void onUserPnrList(ListBookingReceive event);
    }

    private RetrieveBoardingPassView view;

    private final Bus bus;

    public BoardingPassPresenter(RetrieveBoardingPassView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    /*Retrieve Boarding Pass By PNR*/
    public void retrieveBoardingPass(RetrieveBoardingPassObj obj){
        bus.post(new RetrieveBoardingPassObj(obj));
    }


    /*Retrieve Boarding Pass By PNR*/
    public void retriveListOfBoardingPass(String username,String password,String module) {
        bus.post(new ManageFlightObjV2(username,password,module));
    }


    @Subscribe
    public void onItineraryReceive(RetrieveBoardingPassReceive event)
    {
        view.onBoardingPassReceive(event);
    }


    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
            view.onUserPnrList(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }
}
