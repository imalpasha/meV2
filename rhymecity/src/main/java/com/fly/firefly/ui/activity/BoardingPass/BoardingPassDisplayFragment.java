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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.util.BitmapCache;
//import com.commit451.inkpageindicator.InkPageIndicator;
import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity2;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.activity.SlidePage.ProductImagesPagerAdapter;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.module.BoardingPassDisplayModule;
import com.fly.firefly.ui.module.BoardingPassModule;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.PagerBoardingPassObj;
import com.fly.firefly.ui.object.ProductImage;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.presenter.BoardingPassPresenter;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
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
import io.realm.Realm;
import io.realm.RealmResults;
import me.relex.circleindicator.CircleIndicator;

public class BoardingPassDisplayFragment extends BaseFragment implements BoardingPassPresenter.RetrieveBoardingPassView {

    @Inject
    BoardingPassPresenter presenter;

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.indicator)
    CircleIndicator indicator;

    @InjectView(R.id.horizontalProgressBar)
    ProgressBar horizontalProgressBar;

    private BitmapCache mMemoryCache;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String boardingPassList,selectedBoardingPassList;
    private RetrieveBoardingPassReceive obj;

    public static BoardingPassDisplayFragment newInstance(Bundle bundle) {

        BoardingPassDisplayFragment fragment = new BoardingPassDisplayFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());
        FireFlyApplication.get(getActivity()).createScopedGraph(new BoardingPassDisplayModule(this)).inject(this);

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
            Log.e("PNR",bundle.getString("PNR"));

            startPagination(obj);

            //put some condition here later
            if(bundle.getString("LOAD_BACKGROUND").equals("Y")){
                retrieveBoardingPass(bundle);
            }

        }else if(bundle.containsKey("OFFLINE_BOARDING_PASS_OBJ")){
            boardingPassList = bundle.getString("OFFLINE_BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            MobileConfirmCheckInPassengerReceive oXXbXXj = gson.fromJson(boardingPassList, MobileConfirmCheckInPassengerReceive.class);
            offLineStartPagination(oXXbXXj);
        }


        return view;
    }


    public void startPagination(RetrieveBoardingPassReceive passObj){

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < passObj.getBoarding_pass().size(); i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();

            boardingPass.setQRCodeURL(passObj.getBoarding_pass().get(i).getQRCodeURL());
            boardingPass.setQRCode(passObj.getBoarding_pass().get(i).getQRCode());
            boardingPass.setSSR(passObj.getBoarding_pass().get(i).getSSR());
            boardingPass.setName(passObj.getBoarding_pass().get(i).getName());
            boardingPass.setFlightNumber(passObj.getBoarding_pass().get(i).getFlightNumber());
            boardingPass.setSeat(passObj.getBoarding_pass().get(i).getSeat());
            boardingPass.setFare(passObj.getBoarding_pass().get(i).getFare());
            boardingPass.setArrivalDateTime(passObj.getBoarding_pass().get(i).getArrivalDateTime());
            boardingPass.setArrivalStation(passObj.getBoarding_pass().get(i).getArrivalStation());
            boardingPass.setArrivalStationCode(passObj.getBoarding_pass().get(i).getArrivalStationCode());
            boardingPass.setArrivalTime(passObj.getBoarding_pass().get(i).getArrivalTime());
            boardingPass.setBarCodeData(passObj.getBoarding_pass().get(i).getBarCodeData());
            boardingPass.setBarCodeURL(passObj.getBoarding_pass().get(i).getBarCodeURL());
            boardingPass.setBoardingSequence(passObj.getBoarding_pass().get(i).getBoardingSequence());
            boardingPass.setBoardingTime(passObj.getBoarding_pass().get(i).getBoardingTime());
            boardingPass.setDepartureDate(passObj.getBoarding_pass().get(i).getDepartureDate());
            boardingPass.setDepartureDateTime(passObj.getBoarding_pass().get(i).getDepartureDateTime());
            boardingPass.setDepartureGate(passObj.getBoarding_pass().get(i).getDepartureGate());
            boardingPass.setDepartureStation(passObj.getBoarding_pass().get(i).getDepartureStation());
            boardingPass.setDepartureStationCode(passObj.getBoarding_pass().get(i).getDepartureStationCode());
            boardingPass.setDepartureTime(passObj.getBoarding_pass().get(i).getDepartureTime());
            boardingPass.setRecordLocator(passObj.getBoarding_pass().get(i).getRecordLocator());
            boardingPass.setDepartureDayDate(passObj.getBoarding_pass().get(i).getDepartureDayDate());

            boardingPass.setBoardingPosition((i+1)+" / "+(passObj.getBoarding_pass().size()));
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
            boardingPass.setQRCode(passObj.getObj().getBoarding_pass().get(i).getQRCode());
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

    //update current boarding pass list if available (cached list)
    public void retrieveBoardingPass(Bundle bundle){

        RetrieveBoardingPassObj flightObj = new RetrieveBoardingPassObj();
        flightObj.setUser_id(bundle.getString("USER_ID"));
        flightObj.setPnr(bundle.getString("PNR"));
        flightObj.setDeparture_station(bundle.getString("DEPARTURE_STATION_CODE"));
        flightObj.setArrival_station(bundle.getString("ARRIVAL_STATION_CODE"));
        flightObj.setSignature(bundle.getString("SIGNATURE"));
        //retrieveBoardingPass = true;
        horizontalProgressBar.setVisibility(View.VISIBLE);

        presenter.retrieveBoardingPass(flightObj);

    }

    @Override
    public void onBoardingPassReceive(RetrieveBoardingPassReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            startPagination(obj);

            //test
            Realm realm = Realm.getInstance(MainFragmentActivity.getContext());
            final RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();
            Log.e("Current",result2.toString());

            horizontalProgressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onUserPnrList(final ListBookingReceive obj){

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(result.size() > 0){
            Gson gson = new Gson();
            RetrieveBoardingPassReceive obj = gson.fromJson(result.get(0).getCachedResult(), RetrieveBoardingPassReceive.class);
            onBoardingPassReceive(obj);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();

    }


}
