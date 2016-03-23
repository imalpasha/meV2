package com.fly.firefly.ui.activity.ManageFlight;

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

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.PaymentFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.module.ManageFlightActionModule;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MF_ActionFragment extends BaseFragment implements ManageFlightPrenter.ManageFlightView {

    @Inject
    ManageFlightPrenter presenter;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.mfChangeContact)
    LinearLayout mfChangeContact;

    @InjectView(R.id.mfEditPassenger)
    LinearLayout mfEditPassenger;

    @InjectView(R.id.mfChangeSeat)
    LinearLayout mfChangeSeat;

    @InjectView(R.id.mfChangeFlight)
    LinearLayout mfChangeFlight;

    @InjectView(R.id.mfSendItinenary)
    LinearLayout mfSendItinenary;

    @InjectView(R.id.txtPNR)
    TextView txtPNR;

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

    @InjectView(R.id.passengerList)
    LinearLayout passengerList;

    @InjectView(R.id.paymentList)
    LinearLayout paymentList;

    @InjectView(R.id.paymentListMain)
    LinearLayout paymentListMain;

    @InjectView(R.id.insuranceBlock)
    LinearLayout insuranceBlock;

    @InjectView(R.id.txtTotalPriceAll)
    TextView txtTotalPriceAll;

    @InjectView(R.id.servicesList)
    LinearLayout servicesList;

    @InjectView(R.id.txtConfInsurance)
    TextView txtConfInsurance;

    @InjectView(R.id.txtInsuranceRate)
    TextView txtInsuranceRate;

    @InjectView(R.id.serviceAndFeesLayout)
    LinearLayout serviceAndFeesLayout;

    @InjectView(R.id.addPayment)
    LinearLayout addPayment;

    @InjectView(R.id.txtGoingFlightFeeDetail)
    TextView txtGoingFlightFeeDetail;

    @InjectView(R.id.txtReturnFlightFeeTotal)
    TextView txtReturnFlightFeeTotal;

    //private ProgressBar progressIndicator;
    private int fragmentContainerId;
    private Boolean goingFlightDetailTxt = true;
    private Boolean returnFlightDetailTxt = true;
    private SharedPrefManager pref;
    private String flightSummary;
    private FlightSummaryReceive obj = null;

    public static MF_ActionFragment newInstance(Bundle bundle) {

        MF_ActionFragment fragment = new MF_ActionFragment();
        fragment.setArguments(bundle);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightActionModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_flight_select_action, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());



        HashMap<String, String> intint = pref.getPNR();
        String pnrAndEmail2 = intint.get(SharedPrefManager.PNR);

        Log.e("pnrAndEmail",pnrAndEmail2);



        Bundle bundle = getArguments();
        if(bundle.containsKey("ITINENARY_INFORMATION")){
          flightSummary = bundle.getString("ITINENARY_INFORMATION");
          Gson gson = new Gson();
          obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);
          setSummary(obj);
        }else if(bundle.containsKey("AlertDialog")){

            HashMap<String, String> initPNR = pref.getPNR();
            String pnrAndEmail = initPNR.get(SharedPrefManager.PNR);

            HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
            String signature = initSignature.get(SharedPrefManager.SIGNATURE);

            HashMap<String, String> initUserID = pref.getUserID();
            String userID = initUserID.get(SharedPrefManager.USER_ID);

            String[] parts = pnrAndEmail.split(",");
            String part1 = parts[0];
            String part2 = parts[1];

            Log.e(part1,part2);
            initiateLoading(getActivity());
            ManageFlightObj manageFlightObj = new ManageFlightObj();
            manageFlightObj.setSignature(signature);
            manageFlightObj.setUsername(part2);
            manageFlightObj.setPnr(part1);
            manageFlightObj.setUser_id(userID);

            presenter.onSendPNRV1(manageFlightObj);
        }

        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentFlightActivity.class);
                intent.putExtra("PAYMENT_FROM","CHANGE");
                intent.putExtra("TOTAL_DUE",obj.getTotal_due());
                getActivity().startActivity(intent);
            }
        });

        mfChangeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeContactPage(obj);
            }
        });

        mfEditPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditPassenger(obj);
            }
        });

        mfChangeSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSeatSelection();
            }
        });

        mfChangeFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangeFlight();
            }
        });

        mfSendItinenary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSentItenary();
            }
        });

        txtGoingFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(goingFlightDetailTxt){
                    goingFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtGoingFlightPriceDetail.setText("[Hide]");
                    goingFlightDetailTxt = false;
                    txtGoingFlightFeeDetail.setVisibility(View.GONE);
                }else{
                    goingFlightPriceDetail.setVisibility(View.GONE);
                    txtGoingFlightPriceDetail.setText("[Details]");
                    goingFlightDetailTxt = true;
                    txtGoingFlightFeeDetail.setVisibility(View.VISIBLE);
                }
            }
        });

        txtReturnFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnFlightDetailTxt){
                    returnFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtReturnFlightPriceDetail.setText("[Hide]");
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


        return view;
    }

    @Override
    public void onGetFlightFromPNR(FlightSummaryReceive receiveObj){

        dismissLoading();
        Boolean status = Controller.getRequestStatus(receiveObj.getObj().getStatus(), receiveObj.getObj().getMessage(), getActivity());
        if (status) {

            //Gson gsonUserInfo = new Gson();
            //String userInfo = gsonUserInfo.toJson(obj);
            //pref.setSeat(userInfo);
            //pref.setPNR(pnr+","+email);
            obj = receiveObj;
            /*SaveAllInPref*/
            setSummary(obj);
        }

    }


    @Override
    public void onUserPnrList(ListBookingReceive obj){

        //DeadCode
    }

    @Override
    public void onCheckUserStatus(CheckInListReceive obj){

        //DeadCode
    }

    /*public void requestLatestFlightSummary(String pnr,String email , String signature){

        ManageFlightObj manageFlightObj = new ManageFlightObj();
        manageFlightObj.setSignature(email);
        manageFlightObj.setUsername(signature);
        manageFlightObj.setPnr(pnr);

        presenter.onSendPNRV2(manageFlightObj);

    }*/


    public void setSummary(FlightSummaryReceive obj){

        if(obj.getObj().getTotal_due().equals("0.00 MYR"))
        {
            addPayment.setVisibility(View.GONE);
        }else{
            addPayment.setVisibility(View.VISIBLE);
        }




        txtPNR.setText(obj.getObj().getItenerary_information().getPnr());
        txtBookingStatus.setText(obj.getObj().getItenerary_information().getBooking_status());
        txtBookingDate.setText(obj.getObj().getItenerary_information().getBooking_date());

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
        txtGoingFlightFeeDetail.setText(goingFlightDetailTotal);

        //Contact Information
        String title = obj.getObj().getContact_information().getTitle();
        String first_name = obj.getObj().getContact_information().getFirst_name();
        String last_name = obj.getObj().getContact_information().getLast_name();
        String contactName = title + " " + first_name + " " + last_name;

        String contactCountry = obj.getObj().getContact_information().getCountry();
        String contactMobilePhone = obj.getObj().getContact_information().getMobile_phone();
        String contactAlternatPhone = obj.getObj().getContact_information().getAlternate_phone();
        String contactEmail= obj.getObj().getContact_information().getEmail();

        txtContactName.setText(contactName);
        txtContactCountry.setText("Country : "+contactCountry);
        txtContactMobilePhone.setText("Mobile Phone : "+contactMobilePhone);
        txtContactAlternatePhone.setText("Alternate Phone : "+contactAlternatPhone);
        txtContactEmail.setText("Email : "+contactEmail);

        //Insurance
        //checkInsuranceStatus
        if(obj.getObj().getInsurance_details().getStatus().equals("Y")){
            insuranceBlock.setVisibility(View.VISIBLE);

            String insuranceConf = obj.getObj().getInsurance_details().getConf_number();
            String insuranceRate = obj.getObj().getInsurance_details().getRate();

            txtConfInsurance.setText(insuranceConf);
            txtInsuranceRate.setText(insuranceRate);
        }

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

        //Passsenger Information
        int totalPassenger = obj.getObj().getPassenger_lists().size();
        Log.e("a", Integer.toString(totalPassenger));

        for(int passenger = 0 ; passenger < totalPassenger ; passenger++){

            Log.e("b",obj.getObj().getPassenger_lists().get(passenger).getPassengerName());

            TextView txtPassenger = new TextView(getActivity());
            txtPassenger.setText(obj.getObj().getPassenger_lists().get(passenger).getPassengerName());
            txtPassenger.setPadding(3, 3, 3, 3);
            passengerList.addView(txtPassenger);
        }

        //Payment Information
        int totalPaymentCard = obj.getObj().getPayment_details().size();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0.33f);

        for(int payment = 0 ; payment < totalPaymentCard ; payment++){

            LinearLayout paymentRow = new LinearLayout(getActivity());
            paymentRow.setOrientation(LinearLayout.HORIZONTAL);
            paymentRow.setWeightSum(1);

            TextView txtMethod = new TextView(getActivity());
            txtMethod.setText(obj.getObj().getPayment_details().get(payment).getPayment_method());
            txtMethod.setPadding(2, 2, 2, 2);
            txtMethod.setLayoutParams(param);
            paymentRow.addView(txtMethod);


            TextView txtStatus = new TextView(getActivity());
            txtStatus.setText(obj.getObj().getPayment_details().get(payment).getPayment_status());
            txtStatus.setPadding(2, 2, 2, 2);
            txtStatus.setLayoutParams(param);
            paymentRow.addView(txtStatus);

            TextView txtAmount = new TextView(getActivity());
            txtAmount.setText(obj.getObj().getPayment_details().get(payment).getPayment_amount());
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
        String totalDue = obj.getObj().getTotal_due();
        String totalPaid = obj.getObj().getTotal_paid();
        String totalPrice = obj.getObj().getTotal_price();

        txtTotalDue.setText("Total Due: "+totalDue);
        txtTotalPaid.setText("Total Paid: "+totalPaid);
        txtTotalPrice.setText("Total Price: "+totalPrice);


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
            txtReturnFlightFeeTotal.setText(returnFlightDetailTotal);

        }

    }

    public void goToChangeContactPage(FlightSummaryReceive obj)
    {
        Intent changeContact = new Intent(getActivity(), MF_ChangeContactActivity.class);
        changeContact.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(changeContact);
    }

    public void goToEditPassenger(FlightSummaryReceive obj)
    {
        Intent passengerEdit = new Intent(getActivity(), MF_EditPassengerActivity.class);
        passengerEdit.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(passengerEdit);
    }

    public void goToSeatSelection()
    {
        Intent seatSelection = new Intent(getActivity(), MF_SeatSelectionActivity.class);
        seatSelection.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(seatSelection);
    }

    public void goToChangeFlight()
    {
        Intent seatSelection = new Intent(getActivity(), MF_ChangeFlightActivity.class);
        seatSelection.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(seatSelection);
    }

    public void goToSentItenary()
    {
        Intent itinenary = new Intent(getActivity(), MF_SentItineraryActivity.class);
        itinenary.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(itinenary);
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