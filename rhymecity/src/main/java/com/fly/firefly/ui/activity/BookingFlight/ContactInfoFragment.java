package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.ContactInfoModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import io.realm.RealmResults;

public class ContactInfoFragment extends BaseFragment implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener,BookingPresenter.ContactInfoView {

    @Inject
    BookingPresenter presenter;

    @Order(1) @NotEmpty
    @InjectView(R.id.txtPurpose)
    TextView txtPurpose;

    @NotEmpty
    @InjectView(R.id.txtCompanyName)
    EditText txtCompanyName;

    @NotEmpty
    @InjectView(R.id.txtCompanyAddress1)
    EditText txtCompanyAddress1;

    @Optional
    @InjectView(R.id.txtCompanyAddress2)
    EditText txtCompanyAddress2;

    @Optional
    @InjectView(R.id.txtCompanyAddress3)
    EditText txtCompanyAddress3;

    @Order(2) @NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @InjectView(R.id.btnSeatSelection)
    Button btnSeatSelection;

    @InjectView(R.id.btnWithoutSeatSelection)
    Button btnWithoutSeatSelection;

    @NotEmpty
    @Order(3)
    @InjectView(R.id.txtFirstName)
    TextView txtFirstName;

    @NotEmpty
    @Order(4)
    @InjectView(R.id.txtLastName)
    TextView txtLastName;

    @NotEmpty
    @Order(5)
    @Email(message = "Invalid Email")
    @InjectView(R.id.txtEmailAddress)
    TextView txtEmailAddress;

    @NotEmpty
    @Order(6)
    @InjectView(R.id.txtCountry)
    EditText txtCountry;

    @NotEmpty
    @Order(7)
    @InjectView(R.id.txtState)
    TextView txtState;

    @NotEmpty
    @Order(8)
    @InjectView(R.id.txtCity)
    TextView txtCity;

    @NotEmpty
    @Order(9)
    @Length(min = 4,max = 8, message = "Invalid postcode number")
    @InjectView(R.id.txtPostCode)
    TextView txtPostCode;

    @NotEmpty
    @Length(min = 6,max = 14, message = "Invalid phone number")
    @Order(10)
    @InjectView(R.id.txtPhone)
    TextView txtPhone;

    @NotEmpty
    @Length(min = 6,max = 14, message = "Invalid phone number")
    @Order(11)
    @InjectView(R.id.txtAlternatePhone)
    TextView txtAlternatePhone;

    @InjectView(R.id.companyBlock)
    LinearLayout companyBlock;

    @InjectView(R.id.txtInsurance1)
    TextView txtInsurance1;

    @InjectView(R.id.txtInsurance2)
    TextView txtInsurance2;

    @InjectView(R.id.txtInsurance3)
    TextView txtInsurance3;

    @InjectView(R.id.txtInsurance4)
    TextView txtInsurance4;

   /* @InjectView(R.id.txtInsuranceDetail)
    TextView txtInsuranceDetail;*/

    @InjectView(R.id.insuranceCheckBox)
    CheckBox insuranceCheckBox;

    @InjectView(R.id.insuranceBlock)
    LinearLayout insuranceBlock;

    @InjectView(R.id.changeContactInfoBtn)
    LinearLayout changeContactInfoBtn;


    private int fragmentContainerId;
    private String DATEPICKER_TAG = "DATEPICKER_TAG";

    private ArrayList<DropDownItem> travelDocList;
    private ArrayList<DropDownItem> adultPassengerList;

    private final String ADULT = "ADULT";
    private final String INFANT = "INFANT";
    private String adult,infant;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private int clickedPassenger;
    private Boolean boolDob = false;
    private Boolean boolExpireDate = false;

    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private String selectedState;
    private Validator mValidator;
    private String insuranceTxt1,insuranceTxt2,insuranceTxt3,insuranceTxt4;
    private boolean withSeat = false;
    private View view;
    private LoginReceive.UserInfo loginObj;

    public static ContactInfoFragment newInstance(Bundle bundle) {

        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ContactInfoModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passenger_contact_info, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

        String insurance = bundle.getString("INSURANCE_STATUS");
        changeContactInfoBtn.setVisibility(View.GONE);
        Gson gson = new Gson();
        PassengerInfoReveice obj = gson.fromJson(insurance, PassengerInfoReveice.class);

        /* If Passenger Already Login - Auto display necessary data */
        HashMap<String, String> initLogin = pref.getLoginStatus();
        String loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        if(loginStatus != null && loginStatus.equals("Y")) {

            String userInfo = getUserInfoCached(getActivity());
            loginObj = gson.fromJson(userInfo, LoginReceive.UserInfo.class);
            txtTitle.setText(getTitleCode(getActivity(), loginObj.getContact_title(), "name"));
            txtTitle.setTag(loginObj.getContact_title());
            txtFirstName.setText(loginObj.getContact_first_name());
            txtLastName.setText(loginObj.getContact_last_name());
            txtEmailAddress.setText(loginObj.getContact_email());
            txtPhone.setText(loginObj.getContact_mobile_phone());
            txtAlternatePhone.setText(loginObj.getContact_alternate_phone());
            txtState.setText(getStateName(getActivity(), loginObj.getContact_state()));
            txtState.setTag(loginObj.getContact_state());
            txtCity.setText(loginObj.getContact_city());
            selectedCountryCode = loginObj.getContact_country();
            txtCountry.setTag(loginObj.getContact_country());
            txtCountry.setText(getCountryName(getActivity(),loginObj.getContact_country()));
            txtPostCode.setText(loginObj.getContact_postcode());

        }
        /* ---------------------------------------------------------- */


        String insuranceStatus = obj.getObj().getInsuranceObj().getStatus();
        if(insuranceStatus.equals("Y")){
            insuranceBlock.setVisibility(View.VISIBLE);

            insuranceTxt1 = obj.getObj().getInsuranceObj().getHtml().get(0).toString();
            insuranceTxt2 = obj.getObj().getInsuranceObj().getHtml().get(1).toString();
            insuranceTxt3 = obj.getObj().getInsuranceObj().getHtml().get(2).toString();
            insuranceTxt4 = obj.getObj().getInsuranceObj().getHtml().get(3).toString();


            txtInsurance1.setText(Html.fromHtml(insuranceTxt1));
            txtInsurance2.setText(Html.fromHtml(insuranceTxt2.replaceAll("</br>","<p>")));
            txtInsurance2.setMovementMethod(LinkMovementMethod.getInstance());

            txtInsurance3.setText(Html.fromHtml(insuranceTxt3));
            txtInsurance3.setMovementMethod(LinkMovementMethod.getInstance());

            txtInsurance4.setText(Html.fromHtml(insuranceTxt4));

           //txtInsuranceDetail.setText(Html.fromHtml("<html><b>Be sure to protect yourself with Firefly Travel Protection!</b></br><p></p></br>You got a good deal on our promo fares - but don't risk unexpected expenses!</br><p></p></br>>>Comprehensive coverage at phenomenal rates</br> <p></p>>>Added flexibility via the Trip Cancellation benefit if you are unable to proceed with your travels</br><p></p>>>Medical Coverage includes hospital admission and emergency medical evacuation*</br><p></p>>>24 Hour Worldwide Travel Assistance by our travel partner, AIG Travel</br><p></p><p></p></br>* For the full list of benefits, please refer to the <a href='https://www.aig.my/Chartis/internet/Malaysia/English/Firefly%20Travel%20Protection%20Product%20Disclosure%20Sheet_tcm4009-671123.pdf' target='_blank'>Terms and Conditions</a></br><p></p></br><b>The following passenger(s) are eligible for travel insurance:</b></br><p></p><li>Ggjji Gghjj</li><p></p></br><b>Firefly Travel Protection's Promo Plan is only 17.00 MYR MYR (inclusive of GST, when applicable)</b></br><p></p></br></html>"));
           // txtInsuranceDetail.setMovementMethod(LinkMovementMethod.getInstance());
        }

        /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);



         /*Get Data From BaseFragment*/
        purposeList = getPurpose(getActivity());
        titleList = getStaticTitle(getActivity());
        countrysList = getStaticCountry(getActivity());


        /* -------------------------- Select Country --------------------------------*/
        txtCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Utils.hideKeyboard(getActivity(),view);
                    showCountrySelector(getActivity(), countrysList);
                }
            }
        });

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(),view);
                showCountrySelector(getActivity(),countrysList);
            }
        });
        /* ---------------------------- End Country ----------------------------------*/

        /* ---------------------------- Select State -------------------------------- */
        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(),view);
                showCountrySelector(getActivity(), stateList);
            }
        });
        /* ---------------------------- End Select State -------------------------------- */

        /* --------------------------- Select Purpose ----------------------------------- */
        txtPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelectionExtra(purposeList, getActivity(), txtPurpose, true, companyBlock, "Leisure");
            }
        });
        /* --------------------------- End Purpose ----------------------------------- */

        /* --------------------------- Select Title ---------------------------------- */
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(titleList, getActivity(), txtTitle, true, view);
            }
        });
        /* --------------------------- End Title ---------------------------------- */

        /*Booking Id*/
        HashMap<String, String> initFlightType = pref.getBookingID();
        String flightType = initFlightType.get(SharedPrefManager.BOOKING_ID);

        if(flightType.equals("MH")){
            btnSeatSelection.setVisibility(View.GONE);
            btnWithoutSeatSelection.setText("Continue");
        }

         /*Onclick Continue*/
        btnSeatSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withSeat = true;
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });

         /*Onclick Continue*/
        btnWithoutSeatSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withSeat = false;
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });


        return view;
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(ContactInfoFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                DropDownItem selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);

                if (selectedCountry.getTag() == "Country") {
                    txtCountry.setText(selectedCountry.getText());
                    selectedCountryCode = selectedCountry.getCode();

                   /*Each country click - reset state obj*/
                    stateList = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
                    JSONArray jsonState = getState(getActivity());
                    for(int x = 0 ; x < jsonState.length() ; x++) {

                        JSONObject row = (JSONObject) jsonState.opt(x);
                        if(selectedCountryCode.equals(row.optString("country_code"))) {
                            DropDownItem itemCountry = new DropDownItem();
                            itemCountry.setText(row.optString("state_name"));
                            itemCountry.setCode(row.optString("state_code"));
                            itemCountry.setTag("State");
                            stateList.add(itemCountry);
                        }
                    }

                } else {
                    txtState.setText(selectedCountry.getText());
                    txtState.setTag(selectedCountry.getCode());
                    selectedState = selectedCountry.getCode();
                }

            }
        }
    }

    @Override
    public void onContactInfo(ContactInfoReceive obj){
        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            if(withSeat){
                Gson gsonFlight = new Gson();
                String seat = gsonFlight.toJson(obj);
                pref.setSeat(seat);

                Intent intent = new Intent(getActivity(), SeatSelectionActivity.class);
                intent.putExtra("SEAT_INFORMATION", (new Gson()).toJson(obj));
                getActivity().startActivity(intent);
            }else{

                Intent intent = new Intent(getActivity(), ItinenaryActivity.class);
                intent.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
                getActivity().startActivity(intent);
            }


        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10) {
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }

    }

    @Override
    public void onValidationSucceeded() {

        requestContacInfo();
        Log.e("Success", "True");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);

             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);

            }else if(view instanceof TextView){
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof CheckBox){
                ((CheckBox) view).setError(splitErrorMsg[0]);
               // croutonAlert(getActivity(), splitErrorMsg[0]);
            }

        }
        croutonAlert(getActivity(), "Fill empty field");


    }

    public void requestContacInfo(){

        String seatSelectionStatus;

        initiateLoading(getActivity());
        ContactInfo obj = new ContactInfo();

        if(withSeat){
            seatSelectionStatus = "Y";
        }else{
            seatSelectionStatus = "N";
        }
        Log.e("Seat Selection",seatSelectionStatus);
        obj.setBooking_id(bookingID);
        obj.setSeat_selection_status(seatSelectionStatus);
        obj.setSignature(signature);
        obj.setContact_travel_purpose(txtPurpose.getTag().toString());
        obj.setContact_title(txtTitle.getTag().toString());
        obj.setContact_first_name(txtFirstName.getText().toString());
        obj.setContact_last_name(txtLastName.getText().toString());
        obj.setContact_email(txtEmailAddress.getText().toString());

        /*Exception*/
        if(txtPurpose.getTag().toString().equals("2")){
            obj.setContact_state(txtState.getTag().toString());
            obj.setContact_city(txtCity.getText().toString());
            obj.setContact_postcode(txtPostCode.getText().toString());

            obj.setContact_company_name(txtCompanyName.getText().toString());
            obj.setContact_address1(txtCompanyAddress1.getText().toString());
            obj.setContact_address2(txtCompanyAddress2.getText().toString());
            obj.setContact_address3(txtCompanyAddress3.getText().toString());
        }

        if(insuranceCheckBox.isChecked()){
            obj.setInsurance("1");
        }else{
            obj.setInsurance("0");
        }
        obj.setContact_country(selectedCountryCode);
        obj.setContact_mobile_phone(txtPhone.getText().toString());
        obj.setContact_alternate_phone(txtAlternatePhone.getText().toString());

        presenter.contactInfo(obj);

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
            Log.e("x","1");
            Gson gson = new Gson();
            ContactInfoReceive obj = gson.fromJson(result.get(0).getCachedResult(), ContactInfoReceive.class);
            onContactInfo(obj);
        }else{
            Log.e("x","2");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
