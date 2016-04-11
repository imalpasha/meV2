package com.fly.firefly.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.firefly.R;

public class AutoCamera extends Activity {

    private Camera camera; // camera object
    private TextView textTimeLeft; // time left field
    private Button btnStartTimer; // time left field
    private ImageView img;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        textTimeLeft = (TextView) findViewById(R.id.textTimeLeft); // make time left object
        btnStartTimer = (Button) findViewById(R.id.btnStartTimer); // make time left object
        img = (ImageView) findViewById(R.id.bannerImg); // make time left object

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AnalyticsApplication.sendEvent("Click", "mobileCheckIn");
                startTimer(v);
            }
        });

        camera = Camera.open(getFrontCameraId());
        SurfaceView view = new SurfaceView(this);

        try {
            camera.setPreviewDisplay(view.getHolder()); // feed dummy surface to surface
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        camera.startPreview();
    }

    Camera.PictureCallback jpegCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // set file destination and file name
            File destination = new File(Environment.getExternalStorageDirectory(), "myPicture.jpg");
            try {
                Bitmap userImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                // set file out stream
                FileOutputStream out = new FileOutputStream(destination);
                // set compress format quality and stream
                userImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                img.setImageBitmap(userImage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

    public void startTimer(View v) {
        Log.e("Start Time","1");

        // 5000ms=5s at intervals of 1000ms=1s so that means it lasts 5 seconds
        new CountDownTimer(5000, 1000) {

            @Override
            public void onFinish() {
                // count finished
                textTimeLeft.setText("Picture Taken");
                Log.e("Start Time","3");
                camera.takePicture(null, null, null, jpegCallBack);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // every time 1 second passes
                Log.e("Start Time","2");

                textTimeLeft.setText("Seconds Left: " + millisUntilFinished / 1000);
            }

        }.start();
    }

    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

}