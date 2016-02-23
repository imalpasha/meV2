package com.fly.firefly.utils;

import android.app.Activity;

import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.RealmFlightObj;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Dell on 2/11/2016.
 */
public class RealmObjectController extends BaseFragment{

    public static void saveBoardingPass(Activity act,MobileConfirmCheckInPassengerReceive obj,String username){

        Realm realm = Realm.getInstance(act);
        //Convert MobileConfirmCheckIn Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String mobileConfirmCheckIn = gsonFlight.toJson(obj);

        realm.beginTransaction();
        BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
        realmObject.setPnr(obj.getObj().getBoarding_pass().get(0).getRecordLocator());
        realmObject.setUsername(username);
        realmObject.setBoardingPassObj(mobileConfirmCheckIn);

        realm.commitTransaction();
   }

    /*public static void saveToRealmTestObj(Activity act,SearchFlightReceive obj){

        Realm realm = Realm.getInstance(act);
        realm.beginTransaction();

        BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
        realmObject.setRecordLocator(obj.getJourneyObj().getStatus());
        realm.commitTransaction();
    }*/

    public static void deleteRealmFile(Activity act){
        /*Remove Realm Data*/
        RealmConfiguration config = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm realm = Realm.getInstance(config);
        realm.close();
        realm.deleteRealm(config);

    }

    public static void saveFlight(Activity act, FlightSummaryReceive obj , String username){

        //Convert FlightSummary Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String flightSummaryReceive = gsonFlight.toJson(obj);

        Realm realm = Realm.getInstance(act);
        realm.beginTransaction();
        RealmFlightObj realmObject = realm.createObject(RealmFlightObj.class);
        realmObject.setPnr(obj.getObj().getPnr());
        realmObject.setUsername(username);
        realmObject.setFlightObj(flightSummaryReceive);
        realm.commitTransaction();

    }


}
