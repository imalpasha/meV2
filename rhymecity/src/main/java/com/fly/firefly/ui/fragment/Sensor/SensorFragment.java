package com.fly.firefly.ui.fragment.Sensor;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.presenter.LoginPresenter;
//import com.google.android.gms.location.LocationListener;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SensorFragment extends Fragment implements SensorEventListener {

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.idArrow)
    ImageView idArrow;

    @InjectView(R.id.txtHeading)
    TextView txtHeading;

    private int fragmentContainerId;
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    Location loc;
    GeomagneticField geoField;



    public static SensorFragment newInstance() {

        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sensor_screen, container, false);
        ButterKnife.inject(this, view);



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }


    /*@Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];

        Log.e("azimuth_angle",Float.toString(azimuth_angle));
        Log.e("pitch_angle",Float.toString(pitch_angle));
        Log.e("roll_angle", Float.toString(roll_angle));
        // Do something with these orientation angles.
    }*/

    public void onSensorChanged( SensorEvent event ) {

        // If we don't have a Location, we break out
        float azimuth = event.values[0];
        float baseAzimuth = azimuth;

        Location dest = new Location("Destination2");
        dest.setLatitude(2.9242003);
        dest.setLongitude(101.6562783);

       // if ( LocationObj == null ) return;

      /*  final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                loc = location;
                geoField = new GeomagneticField(
                    Double.valueOf(location.getLatitude()).floatValue(),
                    Double.valueOf(location.getLongitude()).floatValue(),
                    Double.valueOf(location.getAltitude()).floatValue(),
                    System.currentTimeMillis()
             );
            }
        };*/

        azimuth -= geoField.getDeclination(); // converts magnetic north into true north

        // Store the bearingTo in the bearTo variable
        float bearTo = loc.bearingTo( dest );

        // If the bearTo is smaller than 0, add 360 to get the rotation clockwise.
        if (bearTo < 0) {
            bearTo = bearTo + 360;
        }

        //This is where we choose to point it
        float direction = bearTo - azimuth;

        // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction = direction + 360;
        }

        rotateImageView(R.drawable.navigate_arrow, direction );

        //Set the field
        String bearingText = "N";

        if ( (360 >= baseAzimuth && baseAzimuth >= 337.5) || (0 <= baseAzimuth && baseAzimuth <= 22.5) ) bearingText = "N";
        else if (baseAzimuth > 22.5 && baseAzimuth < 67.5) bearingText = "NE";
        else if (baseAzimuth >= 67.5 && baseAzimuth <= 112.5) bearingText = "E";
        else if (baseAzimuth > 112.5 && baseAzimuth < 157.5) bearingText = "SE";
        else if (baseAzimuth >= 157.5 && baseAzimuth <= 202.5) bearingText = "S";
        else if (baseAzimuth > 202.5 && baseAzimuth < 247.5) bearingText = "SW";
        else if (baseAzimuth >= 247.5 && baseAzimuth <= 292.5) bearingText = "W";
        else if (baseAzimuth > 292.5 && baseAzimuth < 337.5) bearingText = "NW";
        else bearingText = "?";

        txtHeading.setText(bearingText);

    }

    private void rotateImageView( int drawable, float rotate ) {

        // Decode the drawable into a bitmap
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),drawable);

        // Get the width/height of the drawable
        DisplayMetrics dm = new DisplayMetrics(); getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = bitmapOrg.getWidth(), height = bitmapOrg.getHeight();

        // Initialize a new Matrix
        Matrix matrix = new Matrix();

        // Decide on how much to rotate
        rotate = rotate % 360;

        // Actually rotate the image
        matrix.postRotate( rotate, width, height );

        // recreate the new Bitmap via a couple conditions
        Bitmap rotatedBitmap = Bitmap.createBitmap( bitmapOrg, 0, 0, width, height, matrix, true );
        //BitmapDrawable bmd = new BitmapDrawable( rotatedBitmap );


        idArrow.setImageDrawable(new BitmapDrawable(getResources(), rotatedBitmap));
        idArrow.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onPause() {
        super.onPause();
        //presenter.onPause();
        mSensorManager.unregisterListener(this);

    }
}
