package com.metech.firefly.ui.activity.SlidePage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.object.PagerBoardingPassObj;
import com.metech.firefly.ui.object.ProductImage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProductImagesFragment extends BaseFragment {

    @InjectView(R.id.txtPassengerTitle)
    TextView txtPassengerTitle;

    @InjectView(R.id.txtPassengerName)
    TextView txtPassengerName;

    @InjectView(R.id.txtPassengerDepart)
    TextView txtPassengerDepart;

    @InjectView(R.id.txtPassengerFlightDate)
    TextView txtPassengerFlightDate;

    @InjectView(R.id.txtPassengerArrive)
    TextView txtPassengerArrive;

    @InjectView(R.id.txtPassengerDepartureTime)
    TextView txtPassengerDepartureTime;

    @InjectView(R.id.txtPassengerBoardingTime)
    TextView txtPassengerBoardingTime;

    //@InjectView(R.id.txtPassengerGate)
    //TextView txtPassengerGate;

    @InjectView(R.id.txtPassengerFlightNo)
    TextView txtPassengerFlightNo;

    //@InjectView(R.id.txtPassengerSequence)
    //TextView txtPassengerSequence;

    @InjectView(R.id.txtPassengerSSR)
    TextView txtPassengerSSR;

    @InjectView(R.id.txtPassengerFare)
    TextView txtPassengerFare;

    @InjectView(R.id.txtRecordLocator)
    TextView txtRecordLocator;

    @InjectView(R.id.txtTotalBoardingPass)
    TextView txtTotalBoardingPass;

    private static final String KEY_PRODUCTIMAGE = ProductImagesFragment.class.getSimpleName() + ":KEY_PRODUCTIMAGE";

    public static ProductImagesFragment newInstance(ProductImage productImage) {
        ProductImagesFragment fragment = new ProductImagesFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_PRODUCTIMAGE, productImage);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductImagesFragment newInstance(PagerBoardingPassObj productImage, List<PagerBoardingPassObj> listProductImages) {
        ProductImagesFragment fragment = new ProductImagesFragment();
        ArrayList<String> imgUrls = new ArrayList<String>();
        Bundle args = new Bundle();
        args.putString(KEY_PRODUCTIMAGE, (new Gson()).toJson(productImage));
        //flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
        //for (PagerBoardingPassObj pI : listProductImages) {
        //    imgUrls.add(pI.getQRCodeURL());
       // }
       // args.putStringArrayList("imgUrls", imgUrls);
        fragment.setArguments(args);
        return fragment;
    }

    private PagerBoardingPassObj boardingObj = null;
    private String gsonBoardingPassObj;
    private ArrayList<String>listProductImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        gsonBoardingPassObj = args.getString(KEY_PRODUCTIMAGE);

        Gson gson = new Gson();
        boardingObj = gson.fromJson(gsonBoardingPassObj, PagerBoardingPassObj.class);

        //listProductImages = args.getStringArrayList("imgUrls");

        //if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_PRODUCTIMAGE)) {
        //    boardingObj = savedInstanceState.getParcelable(KEY_PRODUCTIMAGE);
        //}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.boarding_pass, container, false);
        aq.recycle(rootView);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        aq.recycle(view);

        txtTotalBoardingPass.setText(boardingObj.getBoardingPosition());
        txtPassengerName.setText(boardingObj.getName());
        txtPassengerDepart.setText(boardingObj.getDepartureStation() +" ("+ boardingObj.getDepartureStationCode()+")");
        txtPassengerFlightDate.setText(boardingObj.getDepartureDayDate());
        txtPassengerArrive.setText(boardingObj.getArrivalStation() +" ("+ boardingObj.getArrivalStationCode()+")");
        txtPassengerDepartureTime.setText(boardingObj.getDepartureTime());
        txtPassengerBoardingTime.setText(boardingObj.getBoardingTime());
        //txtPassengerGate.setText(boardingObj.getDepartureGate());
        txtPassengerFlightNo.setText(boardingObj.getFlightNumber());
        //txtPassengerSequence.setText(boardingObj.getBoardingSequence());
        txtPassengerSSR.setText(boardingObj.getSSR());
        txtPassengerFare.setText(boardingObj.getFare());
        txtRecordLocator.setText(boardingObj.getRecordLocator());

        try {
            byte[] decodedString = Base64.decode(boardingObj.getQRCode(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            aq.id(R.id.qrCodeImg).getImageView().setImageBitmap(decodedByte);
        }catch (Exception e){

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_PRODUCTIMAGE, (new Gson()).toJson(boardingObj));
    }
}
