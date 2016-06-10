/*package com.fly.firefly.ui.fragment.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.fragment.TouchImageView;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.indooratlas.android.CalibrationState;
import com.indooratlas.android.FloorPlan;
import com.indooratlas.android.FutureResult;
import com.indooratlas.android.IndoorAtlas;
import com.indooratlas.android.IndoorAtlasException;
import com.indooratlas.android.IndoorAtlasFactory;
import com.indooratlas.android.IndoorAtlasListener;
import com.indooratlas.android.ResultCallback;
import com.indooratlas.android.ServiceState;

import java.io.IOException;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class IndoorMapFragment extends BaseFragment implements View.OnTouchListener,HomePresenter.HomeView,IndoorAtlasListener,SensorEventListener {

    private int fragmentContainerId;
    private static final String TAG = "MainActivity";

    // private ListView mLogView;
    // private LogAdapter mLogAdapter;

    private IndoorAtlas mIndoorAtlas;
    private boolean mIsPositioning;
    private StringBuilder mSharedBuilder = new StringBuilder();

    /*
    *
    private String apiKey = "ff83ae2e-3428-44e5-844d-53b01454130a";
    private String apiSecret = "mB%vZ1cgT(16T1o6XA15yyl5zGZHVaYSDI6TiZXZl71mx2HidEP9tdz9Ck)cyefkf6FrNdmllg6gDyWoG0Yze4PqW17FvQEcHn1SNfAY!DuHn9nUJl6oMp97razw8oeI";
    private String floorPlanId = "b8f74e13-54e5-49cc-a29c-f20617e3343c";
    private String venueId = "95a7d4ea-3bf0-437c-a9e9-89a07102bcb4";
    private String floorId = "7fbad856-5e36-4b65-ab4b-3f296c9f2719";

    * */


/*
    private String apiKey = "ff83ae2e-3428-44e5-844d-53b01454130a";
    private String apiSecret = "mB%vZ1cgT(16T1o6XA15yyl5zGZHVaYSDI6TiZXZl71mx2HidEP9tdz9Ck)cyefkf6FrNdmllg6gDyWoG0Yze4PqW17FvQEcHn1SNfAY!DuHn9nUJl6oMp97razw8oeI";

    private String floorPlanId = "f504fb44-a841-46f3-8527-c84dbc553473";
    private String venueId = "95a7d4ea-3bf0-437c-a9e9-89a07102bcb4";
    private String floorId = "3dd4ff06-558e-42ce-8ece-3c5415a6ec9f";

    private FloorPlan mFloorPlan;
   // private ImageView floorPlan;
    private Bitmap bitmap;
    private SensorManager mSensorManager;
    private Bitmap copy;
    private float currentDegree = 0f;
    private double x;
    private double y;

    private TouchImageView image;
    private TextView scrollPositionTextView;
    private TextView zoomedRectTextView;
    private TextView currentZoomTextView;
    private DecimalFormat df;

    int pv1;
    int pv2;

    @InjectView(R.id.tvHeading)
    TextView tvHeading;

    @InjectView(R.id.floorPlan)
    ImageView floorPlan;

    //image = (TouchImageView) findViewById(R.id.img);

    //@InjectView(R.id.navigateFloor)
    //ImageView navigateFloor;
    private ImageView mImageView;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;

    public static IndoorMapFragment newInstance() {

        IndoorMapFragment fragment = new IndoorMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.indoor_map, container, false);
        ButterKnife.inject(this, view);

        mRrootLayout = (ViewGroup) view.findViewById(R.id.root);
        mImageView = (ImageView) view.findViewById(R.id.floorPlan);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(1250, 1250);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);

        initIndoorAtlas();

        return view;
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }

        mRrootLayout.invalidate();
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }



    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tearDown();
    }

    private void tearDown() {
        if (mIndoorAtlas != null) {
            mIndoorAtlas.tearDown();
        }
    }

    private void stopPositioning() {
        mIsPositioning = false;
        if (mIndoorAtlas != null) {
            //log("Stop positioning");
            mIndoorAtlas.stopPositioning();
        }
    }

    private void startPositioning() {
        if (mIndoorAtlas != null) {
            //log(String.format("startPositioning, venueId: %s, floorId: %s, floorPlanId: %s",venueId, floorId, floorPlanId));
            try {
                mIndoorAtlas.startPositioning(venueId, floorId, floorPlanId);
                mIsPositioning = true;
            } catch (IndoorAtlasException e) {
                //log("startPositioning failed: " + e);
            }
        } else {
            //log("calibration not ready, cannot start positioning");
        }
    }

    private void togglePositioning() {
        if (mIsPositioning) {
            stopPositioning();
        } else {
            startPositioning();
        }
    }

    private void initIndoorAtlas() {

        try {
           // log("Connecting with IndoorAtlas, apiKey: " + apiKey);
            // obtain instance to positioning service, note that calibrating
            // might begin instantly
            mIndoorAtlas = IndoorAtlasFactory.createIndoorAtlas(
                    getActivity().getApplicationContext(), this, // IndoorAtlasListener
                    apiKey, apiSecret);
            //log("IndoorAtlas instance created");
            FutureResult<FloorPlan> result = mIndoorAtlas
                    .fetchFloorPlan(floorPlanId);
            result.setCallback(new ResultCallback<FloorPlan>() {

                @Override
                public void onResult(final FloorPlan result) {
                    mFloorPlan = result;
                    loadFloorPlanImage(result);
                }

                @Override
                public void onApplicationError(IndoorAtlasException arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onSystemError(IOException arg0) {
                    // TODO Auto-generated method stub

                }
                // handle error conditions too
            });
            try {
                Log.d("abcd", "helo14");
                mIndoorAtlas.startPositioning(venueId, floorId, floorPlanId);
                Log.d("abcd", "helo1");
            } catch (IndoorAtlasException e) {
                // TODO Auto-generated catch block
                Log.d("abcd", "helo");
                e.printStackTrace();
            }
            togglePositioning();
        } catch (IndoorAtlasException ex) {
            Log.e("IndoorAtlas", "init failed", ex);
            ///log("init IndoorAtlas failed, " + ex.toString());
        }

    }

    private void updateImageViewInUiThread(final Bitmap result) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                bitmap = result;
                //floorPlan.setImageBitmap(R.drawable.navigate_arrow);
            }
        });
    }

    void loadFloorPlanImage(FloorPlan floorPlan) {
        BitmapFactory.Options options = createBitmapOptions(floorPlan);
        FutureResult<Bitmap> result = mIndoorAtlas.fetchFloorPlanImage(
                floorPlan, options);
        result.setCallback(new ResultCallback<Bitmap>() {
            @Override
            public void onResult(final Bitmap result) {
                // now you have floor plan bitmap, do something with it
                //Bitmap mapWithRoute = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.navigatemap);
                updateImageViewInUiThread(result);
            }

            @Override
            public void onApplicationError(IndoorAtlasException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSystemError(IOException arg0) {
                // TODO Auto-generated method stub

            }
            // handle error conditions too
        });
    }

    //private void log(final String msg) {
    //    Log.d(TAG, msg);
     //   getActivity().runOnUiThread(new Runnable() {
      //      @Override
      //      public void run() {
      //          // mLogAdapter.add(msg);
      //          // mLogAdapter.notifyDataSetChanged();
      //      }
     //   });
   // }

    /* IndoorAtlasListener interface */

    /**
     * This is where you will handle location updates.
     *//*
    @Override
    public void onServiceUpdate(ServiceState state) {
        Log.d("abcd", "testttt");
        final Bitmap icon = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(), R.drawable.marker_small);
        // TODO Auto-generated method stub
        final int i = state.getImagePoint().getI();
        final int j = state.getImagePoint().getJ();

        x = state.getMetricPoint().getX();
        y = state.getMetricPoint().getY();

        pv1 = i;
        pv2 = j;
        Log.d("abcd", "" + i + "  " + j);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    Bitmap copy2 = bitmap.copy(bitmap.getConfig(), true);
                    Canvas canvas = new Canvas(copy2);
                    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
                    //canvas.save(Canvas.MATRIX_SAVE_FLAG);
                    //canvas.rotate(50);
                    canvas.drawBitmap(icon, i, j, paint);
                    mImageView.setImageBitmap(copy2);
                    //floorPlan.setAlpha(9);

                }
            }
        });
        mSharedBuilder.setLength(0);
        mSharedBuilder.append("Location: ").append("\n\troundtrip : ")
                .append(state.getRoundtrip()).append("ms").append("\n\tlat : ")
                .append(state.getGeoPoint().getLatitude()).append("\n\tlon : ")
                .append(state.getGeoPoint().getLongitude())
                .append("\n\tX [meter] : ")
                .append(state.getMetricPoint().getX())
                .append("\n\tY [meter] : ")
                .append(state.getMetricPoint().getY())
                .append("\n\tI [pixel] : ")
                .append(state.getImagePoint().getI())
                .append("\n\tJ [pixel] : ")
                .append(state.getImagePoint().getJ()).append("\n\theading : ")
                .append(state.getHeadingDegrees()).append("\n\tuncertainty: ")
                .append(state.getUncertainty());
        //log(mSharedBuilder.toString());
    }

    private BitmapFactory.Options createBitmapOptions(FloorPlan floorPlan) {
        BitmapFactory.Options options = null;
        int reqWidth = 2048;
        int reqHeight = 2048;
        final int width = (int) floorPlan.dimensions[0];
        final int height = (int) floorPlan.dimensions[1];
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        if (options != null) {
            options.inSampleSize = inSampleSize;
        }
        return options;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Pivot: " + Integer.toString(pv1) +"-"+Integer.toString(pv2));

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);


        // Test Arrow Rotation
         //   Bitmap copy2 = bitmap.copy(bitmap.getConfig(), true);
        //    Canvas canvas = new Canvas(copy2);
        //    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        //   //canvas.save(Canvas.MATRIX_SAVE_FLAG);
         //   //canvas.rotate(50);
         //   canvas.drawBitmap(icon, i, j, paint);
        //    mImageView.setImageBitmap(copy2);


        // Start the animation
        floorPlan.startAnimation(ra);
        currentDegree = -degree - 85f;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(),
                bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }

    @Override
    public void onServiceFailure(int errorCode, String reason) {
        //log("onServiceFailure: reason : " + reason);
    }

    @Override
    public void onServiceInitializing() {
        //log("onServiceInitializing");
    }

    @Override
    public void onServiceInitialized() {
        ///log("onServiceInitialized");
    }

    @Override
    public void onInitializationFailed(final String reason) {
       // log("onInitializationFailed: " + reason);
    }

    @Override
    public void onServiceStopped() {
       // log("onServiceStopped");
    }

    @Override
    public void onCalibrationStatus(CalibrationState calibrationState) {
        //log("onCalibrationStatus, percentage: "+ calibrationState.getPercentage());
    }

    /**
     * Notification that calibration has reached level of quality that provides
     * best possible positioning accuracy.
     *//*
    @Override
    public void onCalibrationReady() {
        //log("onCalibrationReady");
    }

    @Override
    public void onNetworkChangeComplete(boolean success) {
    }

    /**
     * @deprecated this callback is deprecated as of version 1.4
     *//*
    @Override
    public void onCalibrationInvalid() {
    }

    /**
     * @deprecated this callback is deprecated as of version 1.4
     *//*
    @Override
    public void onCalibrationFailed(String reason) {
    }
}
*/