package com.fly.firefly.utils;

import android.app.Activity;
import android.util.Log;

import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.RealmFlightObj;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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

    public static boolean currentPNR(Activity act, final MobileConfirmCheckInPassengerReceive obj,final String username){

        final Realm realm = Realm.getInstance(act);
        BoardingPassObj boardingPas;
        Log.e("Record",obj.getObj().getBoarding_pass().get(0).getRecordLocator());

        Gson gsonFlight = new Gson();
       final String mobileConfirmCheckIn = gsonFlight.toJson(obj);

        // Query and update the result asynchronously in another thread
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // begin & end transcation calls are done for you
                //Log.e("Record",obj.getBoarding_pass().get(0).getRecordLocator());
                //BoardingPassObj boardingPas = realm.where(BoardingPassObj.class).equalTo("pnr", obj.getObj().getBoarding_pass().get(0).getRecordLocator()).findFirst();
                //boardingPas.setBoardingPassObj(3);
                String pnr = obj.getObj().getBoarding_pass().get(0).getRecordLocator();
                RealmResults<BoardingPassObj> teenagers = realm.where(BoardingPassObj.class).equalTo("pnr",pnr).equalTo("departureDateTime", obj.getObj().getBoarding_pass().get(0).getDepartureDateTime()).findAll();
                //BoardingPassObj firstJohn = teenagers.where().equalTo("pnr", obj.getBoarding_pass().get(0).getRecordLocator()).findFirst();
                if(teenagers.size() == 0){

                    Log.e("Do Realm Query","OK");

                    //realm.beginTransaction();
                    BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
                    realmObject.setPnr(obj.getObj().getBoarding_pass().get(0).getRecordLocator());
                    realmObject.setUsername(username);
                    realmObject.setBoardingPassObj(mobileConfirmCheckIn);
                    //realm.commitTransaction();*/

                }else{

                    teenagers.clear();
                    Log.e("Quuery","Update");

                    BoardingPassObj dog = new BoardingPassObj();
                    dog.setPnr(obj.getObj().getBoarding_pass().get(0).getRecordLocator());
                    dog.setUsername(username);
                    dog.setDepartureDateTime(obj.getObj().getBoarding_pass().get(0).getDepartureDateTime());
                    dog.setBoardingPassObj(mobileConfirmCheckIn);

                    //realm.copyToRealm(dog);
                }
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {

                Log.e("Query","Success");
                //BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
                //Log.e("Size", realmObject.getPnr());
                // Original Queries and Realm objects are automatically updated.
                //puppies.size(); // => 0 because there are no more puppies (less than 2 years old)
                //dog.getAge();   // => 3 the dogs age is updated
            }

            @Override
            public void onError(Exception e) {
                // transaction is automatically rolled-back, do any cleanup here
                Log.e("Exception",e.getMessage());
            }

        });

        return true;
    }




}
