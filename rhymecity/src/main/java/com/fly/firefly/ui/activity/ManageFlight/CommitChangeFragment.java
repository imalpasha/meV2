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
import com.fly.firefly.api.obj.ConfirmUpdateReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.PaymentFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ManageFlightConfirmUpdate;
import com.fly.firefly.ui.object.ConfirmUpdateRequest;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommitChangeFragment extends BaseFragment implements ManageFlightPrenter.ConfirmUpdateView {

    @Inject
    ManageFlightPrenter presenter;

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

    @InjectView(R.id.cancelUpdate)
    Button cancelUpdate;

    @InjectView(R.id.confirmUpdate)
    Button confirmUpdate;

    @InjectView(R.id.txtReturnFlightFeeTotal)
    TextView txtReturnFlightFeeTotal;

    @InjectView(R.id.txtGoingFlightFeeTotal)
    TextView txtGoingFlightFeeTotal;

    //private ProgressBar progressIndicator;
    private int fragmentContainerId;
    private Boolean goingFlightDetailTxt = true;
    private Boolean returnFlightDetailTxt = true;
    private SharedPrefManager pref;
    private String flightSummary;
    private ManageChangeContactReceive obj = null;
    private String pnr,bookingId,signature;

    public static CommitChangeFragment newInstance(Bundle bundle) {

        CommitChangeFragment fragment = new CommitChangeFragment();
        fragment.setArguments(bundle);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightConfirmUpdate(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.commit_change, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        Bundle bundle = getArguments();
        if(bundle.containsKey("COMMIT_UPDATE")){
            flightSummary = bundle.getString("COMMIT_UPDATE");
            Gson gson = new Gson();
            obj = gson.fromJson(flightSummary, ManageChangeContactReceive.class);

            pnr = obj.getObj().getItenerary_information().getPnr();
            Log.e("pnr",pnr);

            bookingId = obj.getObj().getBooking_id();

            setSummary(obj);
        }

        txtGoingFlightPriceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goingFlightDetailTxt) {
                    goingFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtGoingFlightPriceDetail.setText("Hide");
                    goingFlightDetailTxt = false;
                    txtGoingFlightFeeTotal.setVisibility(View.GONE);
                } else {
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
                if (returnFlightDetailTxt) {
                    returnFlightPriceDetail.setVisibility(View.VISIBLE);
                    txtReturnFlightPriceDetail.setText("Hide");
                    returnFlightDetailTxt = false;
                    txtReturnFlightFeeTotal.setVisibility(View.GONE);

                } else {
                    returnFlightPriceDetail.setVisibility(View.GONE);
                    txtReturnFlightPriceDetail.setText("[Details]");
                    returnFlightDetailTxt = true;
                    txtReturnFlightFeeTotal.setVisibility(View.VISIBLE);

                }
            }
        });

        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeContact = new Intent(getActivity(), ManageFlightActionActivity.class);
                changeContact.putExtra("AlertDialog", "True");
                getActivity().startActivity(changeContact);
                getActivity().finish();
            }
        });

        confirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestChange();
            }
        });

        return view;
    }

    public void requestChange(){

      initiateLoading(getActivity());
        ConfirmUpdateRequest obj = new ConfirmUpdateRequest();
        obj.setPnr(pnr);
        obj.setBooking_id(bookingId);
        obj.setSignature(signature);

        presenter.requestChange(obj);

    }

    @Override
    public void changeConfirm(ConfirmUpdateReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(),obj.getObj().getMessage(), getActivity());
        if (status) {
           setSuccessDialog(getActivity(),"Information Updated!", ManageFlightActionActivity.class);
        }else if (obj.getObj().getStatus().equals("need_payment")){
            Intent intent = new Intent(getActivity(), PaymentFlightActivity.class);
            intent.putExtra("PAYMENT_FROM","CHANGE");
            getActivity().startActivity(intent);
        }

    }

    public void setSummary(ManageChangeContactReceive obj){

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
        txtGoingFlightFeeTotal.setText(goingFlightDetailTotal);

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
        Intent changeContact = new Intent(getActivity(), ManageFlight_ChangeContact.class);
        changeContact.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(changeContact);
    }

    public void goToEditPassenger()
    {
        Intent seatSelection = new Intent(getActivity(), MF_EditPassengerActivity.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToSeatSelection()
    {
        Intent seatSelection = new Intent(getActivity(), MF_SeatSelectionActivity.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToChangeFlight()
    {
        Intent seatSelection = new Intent(getActivity(), MF_ChangeFlightActivity.class);
        getActivity().startActivity(seatSelection);
    }

    public void goToSentItenary()
    {
        Intent seatSelection = new Intent(getActivity(), MF_SentItineraryActivity.class);
        getActivity().startActivity(seatSelection);
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