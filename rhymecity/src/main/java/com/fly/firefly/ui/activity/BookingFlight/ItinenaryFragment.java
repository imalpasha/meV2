package com.fly.firefly.ui.activity.BookingFlight;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ItinenaryModule;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ItinenaryFragment extends BaseFragment implements BookingPresenter.ItinenaryView {

    @Inject
    BookingPresenter presenter;
    private boolean withSeat = false;

    @InjectView(R.id.btnItinerary)
    Button btnItinerary;

    @InjectView(R.id.returnFlight)
    LinearLayout returnFlight;

    @InjectView(R.id.txtGoingFlightType)
    TextView txtGoingFlightType;

    @InjectView(R.id.txtGoingFlightDate)
    TextView txtGoingFlightDate;

    @InjectView(R.id.txtGoingFlightStation)
    TextView txtGoingFlightStation;

    @InjectView(R.id.txtGoingFlightNumber)
    TextView txtGoingFlightNumber;

    @InjectView(R.id.txtGoingFlightTime)
    TextView txtGoingFlightTime;

    @InjectView(R.id.txtReturnFlightType)
    TextView txtReturnFlightType;

    @InjectView(R.id.txtReturnFlightDate)
    TextView txtReturnFlightDate;

    @InjectView(R.id.txtReturnFlightStation)
    TextView txtReturnFlightStation;

    @InjectView(R.id.txtReturnFlightNumber)
    TextView txtReturnFlightNumber;

    @InjectView(R.id.txtReturnFlightTime)
    TextView txtReturnFlightTime;

    @InjectView(R.id.returnFlightPrice)
    LinearLayout returnFlightPrice;

    @InjectView(R.id.txtGoingFlightPriceTotalGuest)
    TextView txtGoingFlightPriceTotalGuest;

    @InjectView(R.id.txtGoingFlightPriceGuest)
    TextView txtGoingFlightPriceGuest;

    @InjectView(R.id.txtGoingFlightPriceTitle)
    TextView txtGoingFlightPriceTitle;

    @InjectView(R.id.txtReturnFlightPriceTotalGuest)
    TextView txtReturnFlightPriceTotalGuest;

    @InjectView(R.id.txtReturnFlightPriceGuest)
    TextView txtReturnFlightPriceGuest;

    @InjectView(R.id.txtReturnFlightPriceTitle)
    TextView txtReturnFlightPriceTitle;

    @InjectView(R.id.txtGoingFlightPriceDetail)
    TextView txtGoingFlightPriceDetail;

    @InjectView(R.id.txtGoingFlightAdminFee)
    TextView txtGoingFlightAdminFee;

    @InjectView(R.id.txtGoingFlightAirportTax)
    TextView txtGoingFlightAirportTax;

    @InjectView(R.id.txtGoingFlightFuelSurcharge)
    TextView txtGoingFlightFuelSurcharge;

    @InjectView(R.id.txtGoingFlightGST)
    TextView txtGoingFlightGST;

    @InjectView(R.id.txtGoingFlightDetailTotal)
    TextView txtGoingFlightDetailTotal;

    @InjectView(R.id.txtGoingTotalFee)
    TextView txtGoingTotalFee;

    @InjectView(R.id.goingFlightPriceDetail)
    LinearLayout goingFlightPriceDetail;

    @InjectView(R.id.servicesList)
    LinearLayout servicesList;

    @InjectView(R.id.txtReturnFlightPriceDetail)
    TextView txtReturnFlightPriceDetail;

    @InjectView(R.id.txtReturnFlightAdminFee)
    TextView txtReturnFlightAdminFee;

    @InjectView(R.id.txtReturnFlightAirportTax)
    TextView txtReturnFlightAirportTax;

    @InjectView(R.id.txtReturnFlightFuelSurcharge)
    TextView txtReturnFlightFuelSurcharge;

    @InjectView(R.id.txtReturnFlightGST)
    TextView txtReturnFlightGST;

    @InjectView(R.id.txtReturnFlightDetailTotal)
    TextView txtReturnFlightDetailTotal;

    @InjectView(R.id.txtReturnTotalFee)
    TextView txtReturnTotalFee;

    @InjectView(R.id.returnFlightPriceDetail)
    LinearLayout returnFlightPriceDetail;

    @InjectView(R.id.txtTotalPriceAll)
    TextView txtTotalPriceAll;

    @InjectView(R.id.serviceAndFeesLayout)
    LinearLayout serviceAndFeesLayout;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private String flightType_return;
    private Boolean goingFlightDetailTxt = true;
    private Boolean returnFlightDetailTxt = true;

    boolean showingFirst = true;

    View view;

    public static ItinenaryFragment newInstance(Bundle bundle) {

        ItinenaryFragment fragment = new ItinenaryFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ItinenaryModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.itinenary, container, false);
        ButterKnife.inject(this, view);
        //pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

        String itinenary = bundle.getString("ITINENARY_INFORMATION");
        Log.e("itinenary", itinenary);

        Gson gson = new Gson();
        ContactInfoReceive obj = gson.fromJson(itinenary, ContactInfoReceive.class);
        setSummary(obj);

        txtGoingFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goingFlightDetailTxt){
                    goingFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtGoingFlightPriceDetail.setText("Hide");
                    txtGoingTotalFee.setVisibility(View.GONE);
                    goingFlightDetailTxt = false;
                }else{
                    goingFlightPriceDetail.setVisibility(View.GONE);
                    txtGoingFlightPriceDetail.setText("[Details]");
                    txtGoingTotalFee.setVisibility(View.VISIBLE);
                    goingFlightDetailTxt = true;
                }
            }
        });

        txtReturnFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnFlightDetailTxt) {
                    returnFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtReturnFlightPriceDetail.setText("Hide");
                    txtReturnTotalFee.setVisibility(View.GONE);
                    returnFlightDetailTxt = false;
                } else {
                    returnFlightPriceDetail.setVisibility(View.GONE);
                    txtReturnFlightPriceDetail.setText("[Details]");
                    txtReturnTotalFee.setVisibility(View.VISIBLE);
                    returnFlightDetailTxt = true;
                }
            }
        });

        btnItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seatSelection = new Intent(getActivity(), PaymentFlightActivity.class);
                seatSelection.putExtra("PAYMENT_FROM","NORMAL");
                getActivity().startActivity(seatSelection);
            }
        });

        return view;
    }

    public void setSummary(ContactInfoReceive obj){

        int flightLoop = obj.getObj().getFlight_details().size();

        //Going Flight Information
        String goingFlightType = obj.getObj().getFlight_details().get(0).getType();
        String goingFlightDate = obj.getObj().getFlight_details().get(0).getDate();
        String goingFlightStation = obj.getObj().getFlight_details().get(0).getStation();
        String goingFlightNumber = obj.getObj().getFlight_details().get(0).getFlight_number();
        String goingFlightTime = obj.getObj().getFlight_details().get(0).getTime();

        txtGoingFlightType.setText(goingFlightType);
        txtGoingFlightDate.setText(goingFlightDate);
        txtGoingFlightStation.setText(goingFlightStation);
        txtGoingFlightNumber.setText(goingFlightNumber);
        txtGoingFlightTime.setText(goingFlightTime);

        //Going Flight Price
        String goingFlightPriceTitle = obj.getObj().getPrice_details().get(0).getTitle();
        String goingFlightPriceGuest = obj.getObj().getPrice_details().get(0).getGuest();
        String goingFlightPriceGuestTotal = obj.getObj().getPrice_details().get(0).getTotal_guest();

        txtGoingFlightPriceTitle.setText(goingFlightPriceTitle);
        txtGoingFlightPriceGuest.setText(goingFlightPriceGuest);
        txtGoingFlightPriceTotalGuest.setText(goingFlightPriceGuestTotal);

        //Going Flight Price
        String goingFlightAdminFee = obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getAdmin_fee();
        String goingFlightAirportTax = obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getAirport_tax();
        String goingFlightFuelSurcharge = obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getFuel_surcharge();
        String goingFlightGST = obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getGoods_and_services_tax();
        String goingFlightDetailTotal= obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getTotal();

        txtGoingFlightAdminFee.setText(goingFlightAdminFee);
        txtGoingFlightAirportTax.setText(goingFlightAirportTax);
        txtGoingFlightFuelSurcharge.setText(goingFlightFuelSurcharge);
        txtGoingFlightGST.setText(goingFlightGST);
        txtGoingFlightDetailTotal.setText(goingFlightDetailTotal);
        txtGoingTotalFee.setText(goingFlightDetailTotal);

        //Services & Fee
        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.4f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int services = 0 ; services < obj.getObj().getPrice_details().size() ; services++){
            if(obj.getObj().getPrice_details().get(services).getStatus().equals("Services and Fees") && obj.getObj().getPrice_details().get(services).getServices().size() > 0){
                serviceAndFeesLayout.setVisibility(View.VISIBLE);
                for(int servicesLoop = 0 ; servicesLoop < obj.getObj().getPrice_details().get(services).getServices().size() ; servicesLoop++){

                    LinearLayout servicesRow = new LinearLayout(getActivity());
                    servicesRow.setOrientation(LinearLayout.HORIZONTAL);
                    servicesRow.setPadding(2, 2, 2, 2);
                    servicesRow.setWeightSum(1);
                    servicesRow.setLayoutParams(matchParent);

                    String servicesName = obj.getObj().getPrice_details().get(services).getServices().get(servicesLoop).getService_name();
                    String servicePrice = obj.getObj().getPrice_details().get(services).getServices().get(servicesLoop).getService_price();

                    TextView txtServicesName = new TextView(getActivity());
                    txtServicesName.setText(servicesName);
                    txtServicesName.setLayoutParams(half06);

                    TextView txtServicePrice = new TextView(getActivity());
                    txtServicePrice.setText(servicePrice);
                    txtServicePrice.setLayoutParams(half04);
                    txtServicePrice.setGravity(Gravity.RIGHT);
                    //txtServicesName.setLayoutParams(param);
                    servicesRow.addView(txtServicesName);
                    servicesRow.addView(txtServicePrice);

                    servicesList.addView(servicesRow);

                }

            }
        }
        txtTotalPriceAll.setText(obj.getObj().getTotal_price());

        //If more than 1 - mean booking with going & return flight
        if(flightLoop > 1){
            returnFlight.setVisibility(View.VISIBLE);
            returnFlightPrice.setVisibility(View.VISIBLE);

            //Going Flight Information
            String returnFlightType = obj.getObj().getFlight_details().get(1).getType();
            String returnFlightDate = obj.getObj().getFlight_details().get(1).getDate();
            String returnFlightStation = obj.getObj().getFlight_details().get(1).getStation();
            String returnFlightNumber = obj.getObj().getFlight_details().get(1).getFlight_number();
            String returnFlightTime = obj.getObj().getFlight_details().get(1).getTime();

            txtReturnFlightType.setText(returnFlightType);
            txtReturnFlightDate.setText(returnFlightDate);
            txtReturnFlightStation.setText(returnFlightStation);
            txtReturnFlightNumber.setText(returnFlightNumber);
            txtReturnFlightTime.setText(returnFlightTime);

            //Going Flight Price
            String returnFlightPriceTitle = obj.getObj().getPrice_details().get(1).getTitle();
            String returnFlightPriceGuest = obj.getObj().getPrice_details().get(1).getGuest();
            String returnFlightPriceGuestTotal = obj.getObj().getPrice_details().get(1).getTotal_guest();

            txtReturnFlightPriceTitle.setText(returnFlightPriceTitle);
            txtReturnFlightPriceGuest.setText(returnFlightPriceGuest);
            txtReturnFlightPriceTotalGuest.setText(returnFlightPriceGuestTotal);

            //Going Flight Price
            String returnFlightAdminFee = obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getAdmin_fee();
            String returnFlightAirportTax = obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getAirport_tax();
            String returnFlightFuelSurcharge = obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getFuel_surcharge();
            String returnFlightGST = obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getGoods_and_services_tax();
            String returnFlightDetailTotal= obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getTotal();

            txtReturnFlightAdminFee.setText(returnFlightAdminFee);
            txtReturnFlightAirportTax.setText(returnFlightAirportTax);
            txtReturnFlightFuelSurcharge.setText(returnFlightFuelSurcharge);
            txtReturnFlightGST.setText(returnFlightGST);
            txtReturnFlightDetailTotal.setText(returnFlightDetailTotal);
            txtReturnTotalFee.setText(returnFlightDetailTotal);
        }

    }

    @Override
    public void onContactInfo(ContactInfoReceive obj) {

        String status = obj.getObj().getStatus();
        Log.e("Receive", "success");
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}