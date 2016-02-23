package com.fly.firefly;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Dell on 1/13/2016.
 */
public class GeoFenceController{

    private final String TAG = GeoFenceController.class.getName();

    private Context context;
    private GoogleApiClient googleApiClient;
    private Gson gson;
    private SharedPreferences prefs;

  /*  private List<NamedGeofence> namedGeofences;
    public List<NamedGeofence> getNamedGeofences() {
        return namedGeofences;
    }
    private List<NamedGeofence> namedGeofencesToRemove;

    private GeofenceController geofenceToAdd;
    private NamedGeofence namedGeofenceToAdd;

    private List<NamedGeofence> namedGeofencesToRemove;

    private Geofence geofenceToAdd;
    private NamedGeofence namedGeofenceToAdd;

*/




    private static GeoFenceController INSTANCE;

    public static GeoFenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeoFenceController();
        }
        return INSTANCE;
    }

   /* public void init(Context context) {
        this.context = context.getApplicationContext();

        gson = new Gson();
        namedGeofences = new ArrayList<NamedGeofence>();
        namedGeofencesToRemove = new ArrayList<NamedGeofence>();
        prefs = this.context.getSharedPreferences(SyncStateContract.Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
    }

    private List<NamedGeofence> namedGeofences;
    public List<NamedGeofence> getNamedGeofences() {
        return namedGeofences;
    }*/

}
