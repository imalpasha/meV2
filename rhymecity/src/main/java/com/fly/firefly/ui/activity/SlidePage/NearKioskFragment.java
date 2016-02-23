package com.fly.firefly.ui.activity.SlidePage;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.util.BitmapCache;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.module.CheckCheckInModule;
import com.fly.firefly.ui.module.HomeModule;
import com.fly.firefly.ui.object.Animal;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.PagerBoardingPassObj;
import com.fly.firefly.ui.object.ProductImage;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class NearKioskFragment extends BaseFragment implements ManageFlightPrenter.ManageFlightView {

    @Inject
    ManageFlightPrenter presenter;

    private int fragmentContainerId;
    private BitmapCache mMemoryCache;
    SQLiteDatabase db;
    private ArrayList<Animal> anim;
    private SharedPrefManager pref;

    protected static final String[] CONTENT = new String[] {
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
            "https://www.google.com/imgres?imgurl=http://2.bp.blogspot.com/__9ZoM5OsU5o/TLh4y8ssgeI/AAAAAAAAAkQ/SWXTuuiLV74/s1600/Gogle,%252Bgooogle,%252Band%252Bgoogel.JPG&imgrefurl=http://bludreamer-tech.blogspot.com/2010/10/gogle-gooogle-and-googel-is-google.html&h=553&w=431&tbnid=nXA5GTUQQ2DTGM:&docid=lbXYITwfqcfZTM&hl=en&ei=1_6uVoTzKaLTmAXovaiQBw&tbm=isch",
    };

    public static NearKioskFragment newInstance() {

        NearKioskFragment fragment = new NearKioskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            FireFlyApplication.get(getActivity()).createScopedGraph(new CheckCheckInModule(this)).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gallery_screen, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);

        /*Bundle bundle = getArguments();
        if(bundle.containsKey("BOARDING_PASS_OBJ")){
            boardingPassList = bundle.getString("BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            obj = gson.fromJson(boardingPassList, RetrieveBoardingPassReceive.class);

            startPagination(obj);
        }
        startPagination();*/

        pref = new SharedPrefManager(getActivity());
        HashMap<String, String> initUsername = pref.getUserEmail();
        String storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);

        initiateLoading(getActivity());
        presenter.onSendPNRV3(storeUsername, storePassword, "beacon");


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

    @Override
    public void onCheckUserStatus(CheckInListReceive obj){

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

        }

    }

    @Override
    public void onUserPnrList(ListBookingReceive obj){

    }

    @Override
    public void onGetFlightFromPNR(FlightSummaryReceive obj){

    }

    public void startPagination(RetrieveBoardingPassReceive passObj){

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < 5; i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();
            boardingPass.setQRCodeURL(passObj.getObj().getBoarding_pass().get(i).getQRCodeURL());
            listProductImages.add(boardingPass);
        }

       ViewPager mPager = (ViewPager) aq.id(R.id.pager).getView();

       ProductImagesPagerAdapter mAdapter = new ProductImagesPagerAdapter(getFragmentManager());
       mAdapter.addAll(listProductImages);
       mPager.setAdapter(mAdapter);

       //CirclePageIndicator mIndicator = (CirclePageIndicator) aq.id(R.id.indicator).getView();
       //mIndicator.setViewPager(mPager);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    public void onResume()
    {
        super.onResume();
    }
}


