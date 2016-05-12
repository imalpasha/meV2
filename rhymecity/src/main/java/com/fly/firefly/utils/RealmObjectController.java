package com.fly.firefly.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.BoardingPassPNRList;
import com.fly.firefly.ui.object.ManageFlightList;
import com.fly.firefly.ui.object.MobileCheckInList;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.NotificationMessage;
import com.fly.firefly.ui.object.RealmFlightObj;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Dell on 2/11/2016.
 */
public class RealmObjectController extends BaseFragment {


    public static Realm getRealmInstance(Activity act){

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }
    }

    public static Realm getRealmInstanceContext(Context act){

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }
    }


    public static void cachedResult(Activity act, String cachedResult) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        realm.beginTransaction();
        CachedResult realmObject = realm.createObject(CachedResult.class);
        //realmObject.setCachedAPI(cachedApi);
        realmObject.setCachedResult(cachedResult);
        realm.commitTransaction();

    }

    public static RealmResults<CachedResult> getCachedResult(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();

        return result;
    }

    public static RealmResults<NotificationMessage> getNotificationMessage(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();

        return result;
    }




    public static void clearCachedResult(Activity act) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }

    public static void clearNotificationMessage(Context act) {

        Realm realm = getRealmInstanceContext(act);

        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }

    /*Save List*/
    public static void saveMobileCheckInList(Activity act, ListBookingReceive obj) {

        Realm realm = getRealmInstance(act);

        final RealmResults<MobileCheckInList> result = realm.where(MobileCheckInList.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
        Log.e("Clear Result First", "Y");

        //Convert MobileConfirmCheckIn Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String stringfyObj = gsonFlight.toJson(obj);

        realm.beginTransaction();
        MobileCheckInList realmObject = realm.createObject(MobileCheckInList.class);
        realmObject.setCachedList(stringfyObj);
        realm.commitTransaction();
    }

    public static void saveManageFlightList(Activity act, ListBookingReceive obj) {

        Realm realm = getRealmInstance(act);

        final RealmResults<ManageFlightList> result = realm.where(ManageFlightList.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
        Log.e("Clear Result First", "Y");

        //Convert MobileConfirmCheckIn Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String stringfyObj = gsonFlight.toJson(obj);

        realm.beginTransaction();
        ManageFlightList realmObject = realm.createObject(ManageFlightList.class);
        realmObject.setCachedList(stringfyObj);
        realm.commitTransaction();
    }

    public static void saveNotificationMessage(Context act, String message,String title)
    {
        Realm realm = getRealmInstanceContext(act);
        realm.beginTransaction();
        NotificationMessage realmObject = realm.createObject(NotificationMessage.class);
        realmObject.setMessage(message);
        realmObject.setTitle(title);
        realm.commitTransaction();

    }


    public static void saveBoardingPassPNRList(Activity act, ListBookingReceive obj) {

        Realm realm = getRealmInstance(act);

        final RealmResults<BoardingPassPNRList> result = realm.where(BoardingPassPNRList.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
        Log.e("Clear Result First", "Y");

        //Convert MobileConfirmCheckIn Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String stringfyObj = gsonFlight.toJson(obj);

        realm.beginTransaction();
        BoardingPassPNRList realmObject = realm.createObject(BoardingPassPNRList.class);
        realmObject.setCachedList(stringfyObj);
        realm.commitTransaction();
    }

    public static RealmResults<MobileCheckInList> getMobileCheckInList(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<MobileCheckInList> result = realm.where(MobileCheckInList.class).findAll();

        return result;
    }

    public static RealmResults<ManageFlightList> getManageFlightList(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<ManageFlightList> result = realm.where(ManageFlightList.class).findAll();

        return result;
    }

    public static RealmResults<BoardingPassPNRList> getBoardingPassPNRList(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<BoardingPassPNRList> result = realm.where(BoardingPassPNRList.class).findAll();

        return result;
    }

    public static void saveBoardingPass(Activity act, MobileConfirmCheckInPassengerReceive obj, String username) {

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

    public static void deleteRealmFile(Activity act) {

        /*Try Remove Realm Data*/
        try {
            RealmConfiguration config = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
            Realm realm = Realm.getInstance(config);
            realm.deleteRealm(config);
            realm.close();

        }catch (Exception e){
            Log.e("Throw Exception","Realm.io");
        }
    }

    public static void saveFlight(Activity act, FlightSummaryReceive obj, String username) {

        //Convert FlightSummary Receive Obj to Gson
        Gson gsonFlight = new Gson();
        String flightSummaryReceive = gsonFlight.toJson(obj);

        Realm realm = getRealmInstance(act);
        realm.beginTransaction();
        RealmFlightObj realmObject = realm.createObject(RealmFlightObj.class);
        realmObject.setPnr(obj.getPnr());
        realmObject.setUsername(username);
        realmObject.setFlightObj(flightSummaryReceive);
        realm.commitTransaction();

    }

    public static boolean currentPNR(Activity act, final MobileConfirmCheckInPassengerReceive obj, final String username) {

        final Realm realm = getRealmInstance(act);
        BoardingPassObj boardingPas;
        Log.e("Record", obj.getObj().getBoarding_pass().get(0).getRecordLocator());

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
                RealmResults<BoardingPassObj> teenagers = realm.where(BoardingPassObj.class).equalTo("pnr", pnr).equalTo("departureDateTime", obj.getObj().getBoarding_pass().get(0).getDepartureDateTime()).findAll();
                //BoardingPassObj firstJohn = teenagers.where().equalTo("pnr", obj.getBoarding_pass().get(0).getRecordLocator()).findFirst();
                if (teenagers.size() == 0) {

                    Log.e("Do Realm Query", "OK");

                    //realm.beginTransaction();
                    BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
                    realmObject.setPnr(obj.getObj().getBoarding_pass().get(0).getRecordLocator());
                    realmObject.setUsername(username);
                    realmObject.setBoardingPassObj(mobileConfirmCheckIn);
                    //realm.commitTransaction();*/

                } else {

                    teenagers.clear();
                    Log.e("Quuery", "Update");

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

                Log.e("Query", "Success");
                //BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
                //Log.e("Size", realmObject.getPnr());
                // Original Queries and Realm objects are automatically updated.
                //puppies.size(); // => 0 because there are no more puppies (less than 2 years old)
                //dog.getAge();   // => 3 the dogs age is updated
            }

            @Override
            public void onError(Exception e) {
                // transaction is automatically rolled-back, do any cleanup here
                Log.e("Exception", e.getMessage());
            }

        });

        return true;
    }

    public static boolean cachedBoardingPass(Activity act, final RetrieveBoardingPassReceive obj) {


        Log.e("Record",obj.getBoarding_pass().get(0).getRecordLocator());

        final Realm realm = getRealmInstance(act);
        BoardingPassObj boardingPas;

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
                String pnr = obj.getBoarding_pass().get(0).getRecordLocator();
                RealmResults<BoardingPassObj> teenagers = realm.where(BoardingPassObj.class).equalTo("pnr",pnr).equalTo("departureDateTime", obj.getBoarding_pass().get(0).getDepartureDateTime()).findAll();
                //BoardingPassObj firstJohn = teenagers.where().equalTo("pnr", obj.getBoarding_pass().get(0).getRecordLocator()).findFirst();
                if(teenagers.size() == 0){

                    Log.e("Do Realm Query","OK");

                    //realm.beginTransaction();
                    BoardingPassObj realmObject = realm.createObject(BoardingPassObj.class);
                    realmObject.setPnr(obj.getBoarding_pass().get(0).getRecordLocator());
                    //realmObject.setUsername(username);
                    realmObject.setDepartureDateTime(obj.getBoarding_pass().get(0).getDepartureDateTime());
                    realmObject.setBoardingPassObj(mobileConfirmCheckIn);
                    //realm.commitTransaction();
                }else{
                    teenagers.get(0).removeFromRealm();
                    Log.e("Quuery","Update");

                    BoardingPassObj dog = realm.createObject(BoardingPassObj.class);
                    dog.setPnr(obj.getBoarding_pass().get(0).getRecordLocator());
                    //dog.setUsername(username);
                    dog.setDepartureDateTime(obj.getBoarding_pass().get(0).getDepartureDateTime());
                    dog.setBoardingPassObj(mobileConfirmCheckIn);

                    //realm.copyToRealm(dog);
                }
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {

                Realm realm = Realm.getInstance(MainFragmentActivity.getContext());
                final RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();
                Log.e("XXX",result2.toString());

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
