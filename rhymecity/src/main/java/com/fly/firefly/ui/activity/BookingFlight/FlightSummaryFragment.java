package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.ContactInfoModule;
import com.fly.firefly.ui.module.FlightSummaryModule;
import com.fly.firefly.ui.module.ItinenaryModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.SeatSelect;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

public class FlightSummaryFragment extends BaseFragment implements BookingPresenter.FlightSummaryView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.txtPNR)
    TextView txtPNR;

    @InjectView(R.id.txtItineraryNote)
    TextView txtItineraryNote;

    @InjectView(R.id.txtGoingFlightStatus)
    TextView txtGoingFlightStatus;

    @InjectView(R.id.txtReturnFlightStatus)
    TextView txtReturnFlightStatus;

    @InjectView(R.id.txtBookingStatus)
    TextView txtBookingStatus;

    @InjectView(R.id.txtBookingDate)
    TextView txtBookingDate;

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

    @InjectView(R.id.goingFlightPriceDetail)
    LinearLayout goingFlightPriceDetail;


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

    @InjectView(R.id.returnFlightPriceDetail)
    LinearLayout returnFlightPriceDetail;

    @InjectView(R.id.txtContactName)
    TextView txtContactName;

    @InjectView(R.id.txtContactCountry)
    TextView txtContactCountry;

    @InjectView(R.id.txtContactMobilePhone)
    TextView txtContactMobilePhone;

    @InjectView(R.id.txtContactAlternatePhone)
    TextView txtContactAlternatePhone;

    @InjectView(R.id.txtContactEmail)
    TextView txtContactEmail;

    @InjectView(R.id.txtTotalPrice)
    TextView txtTotalPrice;

    @InjectView(R.id.txtTotalPaid)
    TextView txtTotalPaid;

    @InjectView(R.id.txtTotalDue)
    TextView txtTotalDue;

    @InjectView(R.id.paymentList)
    LinearLayout paymentList;

    @InjectView(R.id.insuranceBlock)
    LinearLayout insuranceBlock;

    @InjectView(R.id.txtTotalPriceAll)
    TextView txtTotalPriceAll;

    @InjectView(R.id.txtConfInsurance)
    TextView txtConfInsurance;

    @InjectView(R.id.txtInsuranceRate)
    TextView txtInsuranceRate;

    @InjectView(R.id.serviceAndFeesLayout)
    LinearLayout serviceAndFeesLayout;

    @InjectView(R.id.txtGoingFlightFeeTotal)
    TextView txtGoingFlightFeeTotal;

    @InjectView(R.id.txtReturnFlightFeeTotal)
    TextView txtReturnFlightFeeTotal;

    @InjectView(R.id.infantLayout)
    LinearLayout infantLayout;

    @InjectView(R.id.txtInfant)
    TextView txtInfant;

    @InjectView(R.id.txtInfantTotal)
    TextView txtInfantTotal;

    @InjectView(R.id.infantLayoutReturn)
    LinearLayout infantLayoutReturn;

    @InjectView(R.id.txtInfantReturn)
    TextView txtInfantReturn;

    @InjectView(R.id.txtInfantTotalReturn)
    TextView txtInfantTotalReturn;

    @InjectView(R.id.txtOperatedBy)
    TextView txtOperatedBy;

    @InjectView(R.id.txtReturnOperatedBy)
    TextView txtReturnOperatedBy;

    @InjectView(R.id.userSSR)
    LinearLayout userSSR;

    private SharedPrefManager pref;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Flight Summary";
    private Boolean goingFlightDetailTxt = true;
    private Boolean returnFlightDetailTxt = true;
    private String storeUsername;
    private boolean recreateSummary = true;

    public static FlightSummaryFragment newInstance() {

        FlightSummaryFragment fragment = new FlightSummaryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new FlightSummaryModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_summary, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        String signature = initSignature.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initUsername = pref.getUserEmail();
        storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        //Check Flight Type (FY/MH)
        HashMap<String, String> initLogin = pref.getFlightType();
        String type = initLogin.get(SharedPrefManager.FLIGHT_TYPE);



        txtGoingFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goingFlightDetailTxt){
                    goingFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtGoingFlightPriceDetail.setText("Hide");
                    goingFlightDetailTxt = false;
                    txtGoingFlightFeeTotal.setVisibility(View.GONE);
                }else{
                    goingFlightPriceDetail.setVisibility(View.GONE);
                    txtGoingFlightPriceDetail.setText("[Details]");
                    goingFlightDetailTxt = true;
                    txtGoingFlightFeeTotal.setVisibility(View.VISIBLE);
                }
            }
        });

        txtReturnFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnFlightDetailTxt){
                    returnFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtReturnFlightPriceDetail.setText("Hide");
                    returnFlightDetailTxt = false;
                    txtReturnFlightFeeTotal.setVisibility(View.GONE);

                }else{
                    returnFlightPriceDetail.setVisibility(View.GONE);
                    txtReturnFlightPriceDetail.setText("[Details]");
                    returnFlightDetailTxt = true;
                    txtReturnFlightFeeTotal.setVisibility(View.VISIBLE);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        Signature signatureObj = new Signature();
        signatureObj.setSignature(signature);
        requestFlightSummary(signatureObj);
        return view;
    }

    public void displaySSRList(FlightSummaryReceive obj){

        //Services & Fee
        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.4f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int services = 0 ; services < obj.getSpecial_services_request().size() ; services++){

            String ssrFlightType = obj.getSpecial_services_request().get(services).getType();
            TextView txtFlightType = new TextView(getActivity());
            txtFlightType.setText(ssrFlightType);
            txtFlightType.setTypeface(null, Typeface.BOLD);

            //txtServicePrice.setLayoutParams(half04);
            txtFlightType.setGravity(Gravity.LEFT);

            userSSR.addView(txtFlightType);

            for(int servicesLoop = 0 ; servicesLoop < obj.getSpecial_services_request().get(services).getPassenger().size() ; servicesLoop++){

                LinearLayout servicesRow = new LinearLayout(getActivity());
                servicesRow.setOrientation(LinearLayout.VERTICAL);
                servicesRow.setPadding(2, 2, 2, 2);
                servicesRow.setWeightSum(1);
                servicesRow.setLayoutParams(matchParent);

                String passengerName = obj.getSpecial_services_request().get(services).getPassenger().get(servicesLoop).getName();

                TextView txtName = new TextView(getActivity());
                txtName.setText(passengerName);

                TextView txtServicesName = new TextView(getActivity());
                txtServicesName.setText("Name: "+txtName.getText().toString());
                txtServicesName.setTypeface(null, Typeface.BOLD);

                servicesRow.addView(txtServicesName);

                if(obj.getSpecial_services_request().get(services).getPassenger().get(servicesLoop).getList_ssr() != null){
                    for(int ssrLoop = 0 ; ssrLoop < obj.getSpecial_services_request().get(services).getPassenger().get(servicesLoop).getList_ssr().size() ; ssrLoop++){

                        String passengerSSR = obj.getSpecial_services_request().get(services).getPassenger().get(servicesLoop).getList_ssr().get(ssrLoop).getSsr_name();

                        TextView txtServicePrice = new TextView(getActivity());
                        txtServicePrice.setText(passengerSSR);
                        //txtServicePrice.setLayoutParams(half04);
                        txtServicePrice.setGravity(Gravity.LEFT);

                        if(ssrLoop == obj.getSpecial_services_request().get(services).getPassenger().get(servicesLoop).getList_ssr().size() - 1){
                            //margin bottom
                            servicesRow.setPadding(0, 0, 0, 15);
                        }

                        servicesRow.addView(txtServicePrice);
                    }
                }


                if(servicesLoop == obj.getSpecial_services_request().get(services).getPassenger().size() - 1){
                    //margin bottom
                    servicesRow.setPadding(0, 0, 0, 25);
                }

                userSSR.addView(servicesRow);


            }

        }

    }

    public void requestFlightSummary(Signature signatureObj){
        initiateLoading(getActivity());
        HashMap<String, String> init = pref.getSeat();
        String seatHash = init.get(SharedPrefManager.SEAT);

        //Gson gson = new Gson();
        //FlightSummaryReceive obj = gson.fromJson(seatHash, FlightSummaryReceive.class);
        //setSummary(obj);

        presenter.requestFlightSummary(signatureObj);
    }

    @Override
    public void onFlightSummary(FlightSummaryReceive obj) {
        dismissLoading();
        //String status = obj.getObj().getStatus();
        //Gson gsonFlight = new Gson();
        //String seat = gsonFlight.toJson(obj);
        //pref.setSeat(seat);
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            setSummary(obj);
            //Save Object To Local DB(realm.io)
            RealmObjectController.saveFlight(getActivity(), obj, storeUsername);
        }
    }

    public void setSummary(FlightSummaryReceive obj){

        String $oneWayFlightStatus = null;
        String $twoWayFlightStatus = null;

        recreateSummary = false;

        displaySSRList(obj);

        if(obj.getFlight_type().equals("MH")){
                txtOperatedBy.setVisibility(View.VISIBLE);
                txtOperatedBy.setText("Operated By Malaysia Airlines");
                txtReturnOperatedBy.setVisibility(View.VISIBLE);
                txtReturnOperatedBy.setText("Operated By Malaysia Airlines");

                //set blink
                $oneWayFlightStatus = obj.getFlight_details().get(0).getFlight_segment_status();

                if($oneWayFlightStatus.equals("Unconfirmed")){
                    blinkText(txtGoingFlightStatus);
                    txtGoingFlightStatus.setVisibility(View.VISIBLE);
                    $oneWayFlightStatus = obj.getFlight_details().get(0).getFlight_segment_status();
                }

                txtBookingStatus.setText($oneWayFlightStatus);

        }else{
                txtItineraryNote.setVisibility(View.GONE);
                txtBookingStatus.setText(obj.getItenerary_information().getBooking_status());
        }



        txtPNR.setText(obj.getItenerary_information().getPnr());
        txtItineraryNote.setText(obj.getItenerary_information().getItinerary_note());

        //
        txtBookingDate.setText(obj.getItenerary_information().getBooking_date());

        int flightLoop = obj.getFlight_details().size();

        //Going Flight Information
        String goingFlightType = obj.getFlight_details().get(0).getType();
        String goingFlightDate = obj.getFlight_details().get(0).getDate();
        String goingFlightStation = obj.getFlight_details().get(0).getStation();
        String goingFlightNumber = obj.getFlight_details().get(0).getFlight_number();
        String goingFlightTime = obj.getFlight_details().get(0).getTime();

        txtGoingFlightType.setText(goingFlightType);
        txtGoingFlightDate.setText(goingFlightDate);
        txtGoingFlightStation.setText(goingFlightStation);
        txtGoingFlightNumber.setText(goingFlightNumber);
        txtGoingFlightTime.setText(goingFlightTime);

        //Going Flight Price
        String goingFlightPriceTitle = obj.getPrice_details().get(0).getTitle();
        String goingFlightPriceGuest = obj.getPrice_details().get(0).getGuest();
        String goingFlightPriceGuestTotal = obj.getPrice_details().get(0).getTotal_guest();

        String goingFlightInfant = obj.getPrice_details().get(0).getInfant();
        String goingFlightInfantTotal = obj.getPrice_details().get(0).getTotal_infant();

        txtGoingFlightPriceTitle.setText(goingFlightPriceTitle);
        txtGoingFlightPriceGuest.setText(goingFlightPriceGuest);
        txtGoingFlightPriceTotalGuest.setText(goingFlightPriceGuestTotal);

        if(goingFlightInfant != null){
            txtInfant.setText(goingFlightInfant);
            txtInfantTotal.setText(goingFlightInfantTotal);
            infantLayout.setVisibility(View.VISIBLE);
        }

        //Going Flight Price
        String goingFlightAdminFee = obj.getPrice_details().get(0).getTaxes_or_fees().getAdmin_fee();
        String goingFlightAirportTax = obj.getPrice_details().get(0).getTaxes_or_fees().getAirport_tax();
        String goingFlightFuelSurcharge = obj.getPrice_details().get(0).getTaxes_or_fees().getFuel_surcharge();
        String goingFlightGST = obj.getPrice_details().get(0).getTaxes_or_fees().getGoods_and_services_tax();
        String goingFlightDetailTotal= obj.getPrice_details().get(0).getTaxes_or_fees().getTotal();

        txtGoingFlightAdminFee.setText(goingFlightAdminFee);
        txtGoingFlightAirportTax.setText(goingFlightAirportTax);
        txtGoingFlightFuelSurcharge.setText(goingFlightFuelSurcharge);
        txtGoingFlightGST.setText(goingFlightGST);
        txtGoingFlightDetailTotal.setText(goingFlightDetailTotal);
        txtGoingFlightFeeTotal.setText(goingFlightDetailTotal);

        //Contact Information
        String title = obj.getContact_information().getTitle();
        String first_name = obj.getContact_information().getFirst_name();
        String last_name = obj.getContact_information().getLast_name();
        String contactName = getTitleCode(getActivity(),title,"name") + " " + first_name + " " + last_name;

        String contactCountry = obj.getContact_information().getCountry();
        String contactMobilePhone = obj.getContact_information().getMobile_phone();
        String contactAlternatPhone = obj.getContact_information().getAlternate_phone();
        String contactEmail= obj.getContact_information().getEmail();

        txtContactName.setText(contactName);
        txtContactCountry.setText("Country : "+contactCountry);
        txtContactMobilePhone.setText("Mobile Phone : "+contactMobilePhone);
        txtContactAlternatePhone.setText("Alternate Phone : "+contactAlternatPhone);
        txtContactEmail.setText("Email : "+contactEmail);

        //Insurance
        //checkInsuranceStatus
        if(obj.getInsurance_details().getStatus().equals("Y")){
            insuranceBlock.setVisibility(View.VISIBLE);

            String insuranceConf = obj.getInsurance_details().getConf_number();
            String insuranceRate = obj.getInsurance_details().getRate();

            txtConfInsurance.setText(insuranceConf);
            txtInsuranceRate.setText(insuranceRate);
        }

        //Services & Fee
        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.4f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        LinearLayout servicesList = (LinearLayout) getActivity().findViewById(R.id.servicesList);
        servicesList.removeAllViews();

        for(int services = 0 ; services < obj.getPrice_details().size() ; services++){
            if(obj.getPrice_details().get(services).getStatus().equals("Services and Fees") && obj.getPrice_details().get(services).getServices().size() > 0){
                serviceAndFeesLayout.setVisibility(View.VISIBLE);

                for(int servicesLoop = 0 ; servicesLoop < obj.getPrice_details().get(services).getServices().size() ; servicesLoop++){

                    LinearLayout servicesRow = new LinearLayout(getActivity());
                    servicesRow.setOrientation(LinearLayout.HORIZONTAL);
                    servicesRow.setPadding(2, 2, 2, 2);
                    servicesRow.setWeightSum(1);
                    servicesRow.setLayoutParams(matchParent);

                    String servicesName = obj.getPrice_details().get(services).getServices().get(servicesLoop).getService_name();
                    String servicePrice = obj.getPrice_details().get(services).getServices().get(servicesLoop).getService_price();

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
        txtTotalPriceAll.setText(obj.getTotal_price());

        //Passsenger Information
        int totalPassenger = obj.getPassenger_lists().size();

        LinearLayout passengerList = (LinearLayout) getActivity().findViewById(R.id.passengerList);
        passengerList.removeAllViews();

        for(int passenger = 0 ; passenger < totalPassenger ; passenger++){

            //Split - api data problem
            //String passengerName = obj.getObj().getPassenger_lists().get(passenger).getPassengerName();
            //String splitPassengerName[] = passengerName.split(" ");
            //String titleTxt = getTitleCode(getActivity(),splitPassengerName[0],"name");

            TextView txtPassenger = new TextView(getActivity());
            txtPassenger.setText(obj.getPassenger_lists().get(passenger).getPassengerName());
            txtPassenger.setPadding(3, 3, 3, 3);
            passengerList.addView(txtPassenger);
        }

        //Payment Information
        int totalPaymentCard = obj.getPayment_details().size();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.33f);

        LinearLayout paymentListMain = (LinearLayout) getActivity().findViewById(R.id.paymentListMain);
        paymentListMain.removeAllViews();

        for(int payment = 0 ; payment < totalPaymentCard ; payment++){

            LinearLayout paymentRow = new LinearLayout(getActivity());
            paymentRow.setOrientation(LinearLayout.HORIZONTAL);
            paymentRow.setWeightSum(1);

            TextView txtMethod = new TextView(getActivity());
            txtMethod.setText(obj.getPayment_details().get(payment).getPayment_method());
            txtMethod.setPadding(2, 2, 2, 2);
            txtMethod.setLayoutParams(param);
            paymentRow.addView(txtMethod);


            TextView txtStatus = new TextView(getActivity());
            txtStatus.setText(obj.getPayment_details().get(payment).getPayment_status());
            txtStatus.setPadding(2, 2, 2, 2);
            txtStatus.setLayoutParams(param);
            paymentRow.addView(txtStatus);

            TextView txtAmount = new TextView(getActivity());
            txtAmount.setText(obj.getPayment_details().get(payment).getPayment_amount());
            txtAmount.setPadding(2, 2, 2, 2);
            txtAmount.setLayoutParams(param);
            paymentRow.addView(txtAmount);

            paymentListMain.addView(paymentRow);
        }
        //Card Information
       /// String cardPaymentAmount = obj.getObj().getPayment_details().getPayment_amount();
       // String cardPaymentStatus = obj.getObj().getPayment_details().getPayment_status();
       // String cardPaymentType = obj.getObj().getPayment_details().getPayment_method();


       // txtPaymentStatus.setText(cardPaymentStatus);
       // txtTotalDue.setText(cardPaymentAmount);
       // txtPaymentFromCard.setText(cardPaymentType);

        //Total Price
        String totalDue = obj.getTotal_due();
        String totalPaid = obj.getTotal_paid();
        String totalPrice = obj.getTotal_price();

        txtTotalDue.setText("Total Due: "+totalDue);
        txtTotalPaid.setText("Total Paid: "+totalPaid);
        txtTotalPrice.setText("Total Price: "+totalPrice);


        //If more than 1 - mean booking with going & return flight
        if(flightLoop > 1){
            returnFlight.setVisibility(View.VISIBLE);
            returnFlightPrice.setVisibility(View.VISIBLE);

            //set blink
            if(obj.getFlight_type().equals("MH")) {

                $twoWayFlightStatus = obj.getFlight_details().get(1).getFlight_segment_status();

                if($twoWayFlightStatus.equals("Unconfirmed")){
                    blinkText(txtReturnFlightStatus);
                    txtReturnFlightStatus.setVisibility(View.VISIBLE);
                }

                //check both condition
                if($twoWayFlightStatus.equals("Unconfirmed") || $oneWayFlightStatus.equals("Unconfirmed")){
                    txtBookingStatus.setText("Unconfirmed");
                }else if($twoWayFlightStatus.equals("Payment Received") || $oneWayFlightStatus.equals("Payment Received")){
                    txtBookingStatus.setText("Payment Received");
                }else if($twoWayFlightStatus.equals("Confirmed") && $oneWayFlightStatus.equals("Confirmed")){
                    txtBookingStatus.setText("Confirmed");
                }

            }else{
                txtBookingStatus.setText(obj.getItenerary_information().getBooking_status());
            }

            //Going Flight Information
            String returnFlightType = obj.getFlight_details().get(1).getType();
            String returnFlightDate = obj.getFlight_details().get(1).getDate();
            String returnFlightStation = obj.getFlight_details().get(1).getStation();
            String returnFlightNumber = obj.getFlight_details().get(1).getFlight_number();
            String returnFlightTime = obj.getFlight_details().get(1).getTime();

            txtReturnFlightType.setText(returnFlightType);
            txtReturnFlightDate.setText(returnFlightDate);
            txtReturnFlightStation.setText(returnFlightStation);
            txtReturnFlightNumber.setText(returnFlightNumber);
            txtReturnFlightTime.setText(returnFlightTime);

            //Going Flight Price
            String returnFlightPriceTitle = obj.getPrice_details().get(1).getTitle();
            String returnFlightPriceGuest = obj.getPrice_details().get(1).getGuest();
            String returnFlightPriceGuestTotal = obj.getPrice_details().get(1).getTotal_guest();

            String returnFlightInfant = obj.getPrice_details().get(0).getInfant();
            String returnFlightInfantTotal = obj.getPrice_details().get(0).getTotal_infant();

            if(returnFlightInfant != null){
                txtInfantReturn.setText(goingFlightInfant);
                txtInfantTotalReturn.setText(returnFlightInfantTotal);
                infantLayoutReturn.setVisibility(View.VISIBLE);
            }

            txtReturnFlightPriceTitle.setText(returnFlightPriceTitle);
            txtReturnFlightPriceGuest.setText(returnFlightPriceGuest);
            txtReturnFlightPriceTotalGuest.setText(returnFlightPriceGuestTotal);

            //Going Flight Price
            String returnFlightAdminFee = obj.getPrice_details().get(1).getTaxes_or_fees().getAdmin_fee();
            String returnFlightAirportTax = obj.getPrice_details().get(1).getTaxes_or_fees().getAirport_tax();
            String returnFlightFuelSurcharge = obj.getPrice_details().get(1).getTaxes_or_fees().getFuel_surcharge();
            String returnFlightGST = obj.getPrice_details().get(1).getTaxes_or_fees().getGoods_and_services_tax();
            String returnFlightDetailTotal= obj.getPrice_details().get(1).getTaxes_or_fees().getTotal();

            txtReturnFlightAdminFee.setText(returnFlightAdminFee);
            txtReturnFlightAirportTax.setText(returnFlightAirportTax);
            txtReturnFlightFuelSurcharge.setText(returnFlightFuelSurcharge);
            txtReturnFlightGST.setText(returnFlightGST);
            txtReturnFlightDetailTotal.setText(returnFlightDetailTotal);
            txtReturnFlightFeeTotal.setText(returnFlightDetailTotal);

        }


        //ONEWAY DETAILS
            /*txtOneway.setText(obj.getObj().getFlight_details().get(0).getStation());
            oneway_guest.setText(obj.getObj().getPrice_details().get(0).getGuest());
            oneway_guest_price.setText((obj.getObj().getPrice_details().get(0).getTotal_guest()));
            oneway_tax_price.setText(obj.getObj().getPrice_details().get(0).getTaxes_or_fees().getTotal());

            //RETURN DETAILS
            if(obj.getObj().getFlight_details().toArray().length >1){
                txt_return.setText(obj.getObj().getFlight_details().get(1).getStation());
                return_guest.setText(obj.getObj().getPrice_details().get(1).getGuest());
                return_guest_price.setText((obj.getObj().getPrice_details().get(1).getTotal_guest()));
                return_tax_price.setText(obj.getObj().getPrice_details().get(1).getTaxes_or_fees().getTotal());
            }

            //Total Price
            sumtotalPrice.setText(obj.getObj().getTotal_price());

            //Block Hidden If null
            if(obj.getObj().getFlight_details().toArray().length <2) {
                oneWayBlock.setVisibility(View.VISIBLE);
                returnblock.setVisibility(View.GONE);

            } else if(obj.getObj().getFlight_details().toArray().length >1) {
                oneWayBlock.setVisibility(View.VISIBLE);
                returnblock.setVisibility(View.VISIBLE);
            }
            */
            //if(obj.getStatus()!= null && obj.getStatus().equals("Services and Fees")) {
            //    ServiceFeesblock.setVisibility(View.VISIBLE);
             //   insurance_details.setText(obj.getObj().getServices().get(0).getService_name());
             //   insurance_price.setText(obj.getObj().getServices().get(0).getService_price());
            //}else{
             //   ServiceFeesblock.setVisibility(View.GONE);
            //
        //    }

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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(recreateSummary){
            if(result.size() > 0){
            Log.e("x","1");
            Gson gson = new Gson();
            FlightSummaryReceive obj = gson.fromJson(result.get(0).getCachedResult(), FlightSummaryReceive.class);
            onFlightSummary(obj);
            recreateSummary = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    public void backButton() {

        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);

    }
}
