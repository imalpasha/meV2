package com.metech.firefly.ui.activity.ManageFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.FlightSummaryReceive;
import com.metech.firefly.api.obj.ManageChangeContactReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.metech.firefly.ui.module.ManageChangeContactModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.ContactInfo;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.metech.firefly.utils.Utils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
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
import io.realm.RealmResults;

public class MF_ChangeContactFragment extends BaseFragment implements Validator.ValidationListener,ManageFlightPrenter.ChangeContactView {

    @Inject
    ManageFlightPrenter presenter;


    @InjectView(R.id.btnContinue)
    Button btnContinue;

    @InjectView(R.id.contactInfoContinueBtn)
    LinearLayout contactInfoContinueBtn;

    @InjectView(R.id.changeContactInfoBtn)
    LinearLayout changeContactInfoBtn;

    @Order(1)
    @NotEmpty
    @InjectView(R.id.txtPurpose)
    TextView txtPurpose;

    @NotEmpty
    @InjectView(R.id.txtCompanyAddress1)
    TextView txtCompanyAddress1;

    @Optional
    @InjectView(R.id.txtCompanyAddress2)
    TextView txtCompanyAddress2;

    @Optional
    @InjectView(R.id.txtCompanyAddress3)
    TextView txtCompanyAddress3;

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

    @NotEmpty(sequence = 1)
    @Order(5)
    @Email(sequence = 2,message = "Invalid Email")
    @InjectView(R.id.txtEmailAddress)
    TextView txtEmailAddress;

    @NotEmpty
    @Order(6)
    @InjectView(R.id.txtCountry)
    TextView txtCountry;

    @NotEmpty
    @Order(7)
    @InjectView(R.id.txtState)
    TextView txtState;

    @NotEmpty
    @Order(8)
    @InjectView(R.id.txtCity)
    TextView txtCity;

    @NotEmpty(sequence = 1)
    @Order(9)
    @Length(sequence = 2,min = 4,max = 8, message = "Invalid postcode number")
    @InjectView(R.id.txtPostCode)
    TextView txtPostCode;

    @Order(10)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2,min = 6,max = 14, message = "Invalid phone number")
    @InjectView(R.id.txtPhone)
    TextView txtPhone;

    @Order(11)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2,min = 6,max = 14, message = "Invalid phone number")
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

    @NotEmpty
    @InjectView(R.id.txtCompanyName)
    EditText txtCompanyName;

    @InjectView(R.id.txtCountryBusiness)
    TextView txtCountryBusiness;

    @InjectView(R.id.countryBlock)
    LinearLayout countryBlock;

    @InjectView(R.id.checkAsPassengerLayout)
    LinearLayout checkAsPassengerLayout;

    @InjectView(R.id.contactInfoScrollView)
    ScrollView contactInfoScrollView;

    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private String dialingCode;
    private Validator mValidator;
    private String selectedState;
    private int fragmentContainerId;
    private View view;
    private SharedPrefManager pref;
    private String pnr,username,bookingId,signature,customerNumber;
    private String travelPurpose;
    private static final String SCREEN_LABEL = "Edit Contact Detail";

    public static MF_ChangeContactFragment newInstance(Bundle bundle) {

        MF_ChangeContactFragment fragment = new MF_ChangeContactFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
        RealmObjectController.clearCachedResult(getActivity());

        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageChangeContactModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passenger_contact_info, container, false);
        ButterKnife.inject(this, view);

        //hide
        checkAsPassengerLayout.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initCustomerNumber = pref.getCustomerNumber();
        customerNumber = initCustomerNumber.get(SharedPrefManager.CUSTOMER_NUMBER);

        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        contactInfoContinueBtn.setVisibility(View.GONE);
        changeContactInfoBtn.setVisibility(View.VISIBLE);

        //travelPurpose = obj.getObj().getContact_information().getTravel_purpose();
        txtPurpose.setTag(obj.getContact_information().getTravel_purpose());

        /*Travelling Purpose*/
        /*final String[] purpose = getResources().getStringArray(R.array.purpose);
        for(int i = 0;i<purpose.length; i++)
        {
            int purposeTag = i+1;
            DropDownItem itemPurpose = new DropDownItem();
            itemPurpose.setText(purpose[i]);
            itemPurpose.setCode(Integer.toString(purposeTag));
            purposeList.add(itemPurpose);
        }*/
        titleList = getStaticTitle(getActivity());
        countrysList = getStaticCountry(getActivity());
        purposeList = getPurpose(getActivity());
         /*Display Title Data*/
       /* JSONArray jsonTitle = getTitle(getActivity());
        for (int i = 0; i < jsonTitle.length(); i++)
        {
            JSONObject row = (JSONObject) jsonTitle.opt(i);

            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(row.optString("title_name"));
            itemTitle.setCode(row.optString("title_code"));
            itemTitle.setTag("Title");
            titleList.add(itemTitle);
        }*/

        /*Display Country Data*//*
        JSONArray jsonCountry = getCountry(getActivity());

        for (int i = 0; i < jsonCountry.length(); i++)
        {
            JSONObject row = (JSONObject) jsonCountry.opt(i);

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(row.optString("country_name"));
            itemCountry.setCode(row.optString("country_code"));
            itemCountry.setTag("Country");
            itemCountry.setId(i);
            countrysList.add(itemCountry);
        }*/


        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(), view);
                showCountrySelector(getActivity(),countrysList);
            }
        });

        txtCountryBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(), view);
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
                popupSelectionExtra(purposeList, getActivity(), txtPurpose, true, companyBlock, "2",countryBlock);

            }
        });
        /* --------------------------- End Purpose ----------------------------------- */

        /* --------------------------- Select Title ---------------------------------- */
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(titleList, getActivity(), txtTitle, true,view);
            }
        });
        /* --------------------------- End Title ---------------------------------- */


        /*Assign value to field*/
        txtFirstName.setText(obj.getContact_information().getFirst_name());
        txtLastName.setText(obj.getContact_information().getLast_name());
        txtPurpose.setTag(obj.getContact_information().getTravel_purpose());
        txtPurpose.setText(getFlightPurpose(obj.getContact_information().getTravel_purpose()));
        txtEmailAddress.setText(obj.getContact_information().getEmail());
        txtTitle.setTag(obj.getContact_information().getTitle());
        txtTitle.setText(getTitleCode(getActivity(), obj.getContact_information().getTitle(), "name"));
        txtCountry.setTag(obj.getContact_information().getCountry());
        txtCountry.setText(getCountryName(getActivity(), obj.getContact_information().getCountry()));
        txtAlternatePhone.setText(obj.getContact_information().getAlternate_phone());
        txtPhone.setText(obj.getContact_information().getMobile_phone());

        txtCountryBusiness.setText(getCountryName(getActivity(), obj.getContact_information().getCountry()));
        txtCountryBusiness.setTag(obj.getContact_information().getCountry());

        dialingCode = getDialingCode(obj.getContact_information().getCountry(),getActivity());

        pnr = obj.getItenerary_information().getPnr();
        bookingId = obj.getBooking_id();
        username = obj.getContact_information().getEmail();
        setState(obj.getContact_information().getCountry());

        if(txtPurpose.getTag().equals("2")){

            companyBlock.setVisibility(View.VISIBLE);
            txtCity.setText(obj.getContact_information().getCity());
            txtPostCode.setText(obj.getContact_information().getPostcode());
            txtCompanyAddress1.setText(obj.getContact_information().getAddress1());
            txtCompanyAddress2.setText(obj.getContact_information().getAddress2());
            txtCompanyAddress3.setText(obj.getContact_information().getAddress3());
            txtCompanyName.setText(obj.getContact_information().getCompany_name());
            txtState.setTag(obj.getContact_information().getState());
            txtState.setText(getStateName(getActivity(), obj.getContact_information().getState()));

            countryBlock.setVisibility(View.GONE);

            txtCountry.setText(getCountryName(getActivity(), obj.getContact_information().getCountry()));
            txtCountry.setTag(obj.getContact_information().getCountry());

            txtCountryBusiness.setText(getCountryName(getActivity(), obj.getContact_information().getCountry()));
            txtCountryBusiness.setTag(obj.getContact_information().getCountry());


        }


        /*
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
        */

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });

        contactInfoScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //view.requestFocus();
            }
        });

        return view;
    }

    @Override
    public void onGetChangeContact(ManageChangeContactReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
        }else{
        }
    }

    @Override
    public void onValidationSucceeded() {


        //checkDialingCode
        boolean cont = true;
        if(validateDialingCode(dialingCode, txtPhone.getText().toString())) {
            txtPhone.setError("Mobile phone must start with country code.");
            setShake(txtPhone);
            cont = false;
        }
        if(validateDialingCode(dialingCode,txtAlternatePhone.getText().toString())) {
            txtAlternatePhone.setError("Alternate phone must start with country code.");
            setShake(txtAlternatePhone);
            cont = false;
        }
        if(cont){
            requestContactInfoChange();
        }

        //CONFIRM UPDATE?
       /* new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to update?")
                .setConfirmText("Confirm!")
                .setCancelText("Cancel!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        requestContactInfoChange();
                        sDialog.dismiss();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
                */


    }

    public void requestContactInfoChange(){

        initiateLoading(getActivity());
        ContactInfo obj = new ContactInfo();
        obj.setBooking_id(bookingId);

        obj.setCustomer_number(customerNumber);
        obj.setContact_travel_purpose(txtPurpose.getTag().toString());
        obj.setContact_title(txtTitle.getTag().toString());
        obj.setContact_first_name(txtFirstName.getText().toString());
        obj.setContact_last_name(txtLastName.getText().toString());


        obj.setContact_email(txtEmailAddress.getText().toString());
        obj.setContact_mobile_phone(txtPhone.getText().toString());
        obj.setContact_alternate_phone(txtAlternatePhone.getText().toString());

        if(txtPurpose.getTag().equals("2")){
           obj.setContact_company_name(txtCompanyName.getText().toString());
           obj.setContact_address1(txtCompanyAddress1.getText().toString());
           obj.setContact_address2(txtCompanyAddress2.getText().toString());
           obj.setContact_address3(txtCompanyAddress3.getText().toString());
           obj.setContact_state(txtState.getTag().toString());
           obj.setContact_city(txtCity.getText().toString());
           obj.setContact_postcode(txtPostCode.getText().toString());
           obj.setContact_country(txtCountryBusiness.getTag().toString());
        }else{
            obj.setContact_country(txtCountry.getTag().toString());
        }

            //contact_title
        //contact_first_name
        //contact_last_name
        //contact_email
        //contact_country
        //contact_mobile_phone
        presenter.onChangeContact(obj, pnr, username,signature);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        boolean firstView = true;


        for (ValidationError error : errors) {
            View view = error.getView();
            view.setFocusable(true);

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

            }

            if(firstView){
                view.requestFocus();
            }
            firstView = false;

        }

    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(MF_ChangeContactFragment.this, 0);
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
                    txtCountryBusiness.setText(selectedCountry.getText());

                    //split country code with dialing code
                    String toCountryCode =  selectedCountry.getCode();
                    String[] splitCountryCode = toCountryCode.split("/");
                    selectedCountryCode = splitCountryCode[0];
                    dialingCode = splitCountryCode[1];
                    txtPhone.setText(dialingCode);
                    txtAlternatePhone.setText(dialingCode);

                    txtCountry.setTag(selectedCountryCode);
                    txtCountryBusiness.setTag(selectedCountryCode);

                    setState(selectedCountryCode);


                } else {
                    txtState.setText(selectedCountry.getText());
                    txtState.setTag(selectedCountry.getCode());
                    selectedState = selectedCountry.getCode();
                }

            }
        }
    }


    public void setState(String selectedCode){

        /*Each country click - reset state obj*/
        stateList = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
        JSONArray jsonState = getState(getActivity());
        for(int x = 0 ; x < jsonState.length() ; x++) {

            JSONObject row = (JSONObject) jsonState.opt(x);
            if(selectedCode.equals(row.optString("country_code"))) {
                DropDownItem itemCountry = new DropDownItem();
                itemCountry.setText(row.optString("state_name"));
                itemCountry.setCode(row.optString("state_code"));
                itemCountry.setTag("State");
                stateList.add(itemCountry);
            }
        }
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
            if(result.size() > 0){
                Gson gson = new Gson();
                ManageChangeContactReceive obj = gson.fromJson(result.get(0).getCachedResult(), ManageChangeContactReceive.class);
                onGetChangeContact(obj);
            }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
