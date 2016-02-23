package com.fly.firefly.ui.activity.BoardingPass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.util.BitmapCache;
//import com.commit451.inkpageindicator.InkPageIndicator;
import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity2;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.activity.SlidePage.ProductImagesPagerAdapter;
import com.fly.firefly.ui.module.BoardingPassModule;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.PagerBoardingPassObj;
import com.fly.firefly.ui.object.ProductImage;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.presenter.BoardingPassPresenter;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;

public class BoardingPassDisplayFragment extends BaseFragment {

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.indicator)
    CircleIndicator indicator;

    private BitmapCache mMemoryCache;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String boardingPassList;
    private RetrieveBoardingPassReceive obj;

    public static BoardingPassDisplayFragment newInstance(Bundle bundle) {

        BoardingPassDisplayFragment fragment = new BoardingPassDisplayFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.boarding_pass_list, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        if(bundle.containsKey("BOARDING_PASS_OBJ")){
            boardingPassList = bundle.getString("BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            obj = gson.fromJson(boardingPassList, RetrieveBoardingPassReceive.class);
            startPagination(obj);
        }else if(bundle.containsKey("OFFLINE_BOARDING_PASS_OBJ")){
            boardingPassList = bundle.getString("OFFLINE_BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            MobileConfirmCheckInPassengerReceive oXXbXXj = gson.fromJson(boardingPassList, MobileConfirmCheckInPassengerReceive.class);
            offLineStartPagination(oXXbXXj);
        }



        return view;
    }

    /*private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
          if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }*/

    public void startPagination(RetrieveBoardingPassReceive passObj){

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < passObj.getObj().getBoarding_pass().size(); i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();

            boardingPass.setQRCodeURL(passObj.getObj().getBoarding_pass().get(i).getQRCodeURL());
            boardingPass.setSSR(passObj.getObj().getBoarding_pass().get(i).getSSR());
            boardingPass.setName(passObj.getObj().getBoarding_pass().get(i).getName());
            boardingPass.setFlightNumber(passObj.getObj().getBoarding_pass().get(i).getFlightNumber());
            boardingPass.setSeat(passObj.getObj().getBoarding_pass().get(i).getSeat());
            boardingPass.setFare(passObj.getObj().getBoarding_pass().get(i).getFare());
            boardingPass.setArrivalDateTime(passObj.getObj().getBoarding_pass().get(i).getArrivalDateTime());
            boardingPass.setArrivalStation(passObj.getObj().getBoarding_pass().get(i).getArrivalStation());
            boardingPass.setArrivalStationCode(passObj.getObj().getBoarding_pass().get(i).getArrivalStationCode());
            boardingPass.setArrivalTime(passObj.getObj().getBoarding_pass().get(i).getArrivalTime());
            boardingPass.setBarCodeData(passObj.getObj().getBoarding_pass().get(i).getBarCodeData());
            boardingPass.setBarCodeURL(passObj.getObj().getBoarding_pass().get(i).getBarCodeURL());
            boardingPass.setBoardingSequence(passObj.getObj().getBoarding_pass().get(i).getBoardingSequence());
            boardingPass.setBoardingTime(passObj.getObj().getBoarding_pass().get(i).getBoardingTime());
            boardingPass.setDepartureDate(passObj.getObj().getBoarding_pass().get(i).getDepartureDate());
            boardingPass.setDepartureDateTime(passObj.getObj().getBoarding_pass().get(i).getDepartureDateTime());
            boardingPass.setDepartureGate(passObj.getObj().getBoarding_pass().get(i).getDepartureGate());
            boardingPass.setDepartureStation(passObj.getObj().getBoarding_pass().get(i).getDepartureStation());
            boardingPass.setDepartureStationCode(passObj.getObj().getBoarding_pass().get(i).getDepartureStationCode());
            boardingPass.setDepartureTime(passObj.getObj().getBoarding_pass().get(i).getDepartureTime());
            boardingPass.setRecordLocator(passObj.getObj().getBoarding_pass().get(i).getRecordLocator());
            boardingPass.setDepartureDayDate(passObj.getObj().getBoarding_pass().get(i).getDepartureDayDate());

            boardingPass.setBoardingPosition((i+1)+" / "+(passObj.getObj().getBoarding_pass().size()));
            listProductImages.add(boardingPass);
        }

        ProductImagesPagerAdapter mAdapter = new ProductImagesPagerAdapter(getFragmentManager());
        mAdapter.addAll(listProductImages);
        pager.setAdapter(mAdapter);

        indicator.setViewPager(pager);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //CirclePageIndicator mIndicator = (CirclePageIndicator) aq.id(R.id.indicator).getView();
        //mIndicator.setViewPager(mPager);

    }

    public void offLineStartPagination(MobileConfirmCheckInPassengerReceive passObj){

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < passObj.getObj().getBoarding_pass().size(); i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();

            boardingPass.setQRCodeURL(passObj.getObj().getBoarding_pass().get(i).getQRCodeURL());
            boardingPass.setSSR(passObj.getObj().getBoarding_pass().get(i).getSSR());
            boardingPass.setName(passObj.getObj().getBoarding_pass().get(i).getName());
            boardingPass.setFlightNumber(passObj.getObj().getBoarding_pass().get(i).getFlightNumber());
            boardingPass.setSeat(passObj.getObj().getBoarding_pass().get(i).getSeat());
            boardingPass.setFare(passObj.getObj().getBoarding_pass().get(i).getFare());
            boardingPass.setArrivalDateTime(passObj.getObj().getBoarding_pass().get(i).getArrivalDateTime());
            boardingPass.setArrivalStation(passObj.getObj().getBoarding_pass().get(i).getArrivalStation());
            boardingPass.setArrivalStationCode(passObj.getObj().getBoarding_pass().get(i).getArrivalStationCode());
            boardingPass.setArrivalTime(passObj.getObj().getBoarding_pass().get(i).getArrivalTime());
            boardingPass.setBarCodeData(passObj.getObj().getBoarding_pass().get(i).getBarCodeData());
            boardingPass.setBarCodeURL(passObj.getObj().getBoarding_pass().get(i).getBarCodeURL());
            boardingPass.setBoardingSequence(passObj.getObj().getBoarding_pass().get(i).getBoardingSequence());
            boardingPass.setBoardingTime(passObj.getObj().getBoarding_pass().get(i).getBoardingTime());
            boardingPass.setDepartureDate(passObj.getObj().getBoarding_pass().get(i).getDepartureDate());
            boardingPass.setDepartureDateTime(passObj.getObj().getBoarding_pass().get(i).getDepartureDateTime());
            boardingPass.setDepartureGate(passObj.getObj().getBoarding_pass().get(i).getDepartureGate());
            boardingPass.setDepartureStation(passObj.getObj().getBoarding_pass().get(i).getDepartureStation());
            boardingPass.setDepartureStationCode(passObj.getObj().getBoarding_pass().get(i).getDepartureStationCode());
            boardingPass.setDepartureTime(passObj.getObj().getBoarding_pass().get(i).getDepartureTime());
            boardingPass.setRecordLocator(passObj.getObj().getBoarding_pass().get(i).getRecordLocator());

            boardingPass.setBoardingPosition((i+1)+" / "+(passObj.getObj().getBoarding_pass().size()));
            listProductImages.add(boardingPass);
        }

        ProductImagesPagerAdapter mAdapter = new ProductImagesPagerAdapter(getFragmentManager());
        mAdapter.addAll(listProductImages);
        pager.setAdapter(mAdapter);

        indicator.setViewPager(pager);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //CirclePageIndicator mIndicator = (CirclePageIndicator) aq.id(R.id.indicator).getView();
        //mIndicator.setViewPager(mPager);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
