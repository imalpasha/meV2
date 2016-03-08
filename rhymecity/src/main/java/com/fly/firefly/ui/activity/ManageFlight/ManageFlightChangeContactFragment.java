package com.fly.firefly.ui.activity.ManageFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.ContactInfoActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.ManageChangeContactModule;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManageFlightChangeContactFragment extends BaseFragment implements Validator.ValidationListener,ManageFlightPrenter.ChangeContactView {

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
    EditText txtCountry;

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

    @Optional
    @InjectView(R.id.txtCompanyName)
    EditText txtCompanyName;

    private ArrayList<DropDownItem> countrysList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> purposeList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();
    private String selectedCountryCode;
    private Validator mValidator;
    private String selectedState;
    private int fragmentContainerId;
    private View view;
    private SharedPrefManager pref;
    private String pnr,username,bookingId,signature;
    private String travelPurpose;

    public static ManageFlightChangeContactFragment newInstance(Bundle bundle) {

        ManageFlightChangeContactFragment fragment = new ManageFlightChangeContactFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageChangeContactModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passenger_contact_info, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        contactInfoContinueBtn.setVisibility(View.GONE);
        changeContactInfoBtn.setVisibility(View.VISIBLE);
        travelPurpose = obj.getObj().getContact_information().getTravel_purpose();

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
                popupSelection(titleList, getActivity(), txtTitle, true,view);
            }
        });
        /* --------------------------- End Title ---------------------------------- */


        /*Assign value to field*/
        txtFirstName.setText(obj.getObj().getContact_information().getFirst_name());
        txtLastName.setText(obj.getObj().getContact_information().getLast_name());
        txtPurpose.setTag(obj.getObj().getContact_information().getPurpose());
        txtPurpose.setText(getFlightPurpose(obj.getObj().getContact_information().getPurpose()));
        txtEmailAddress.setText(obj.getObj().getContact_information().getEmail());
        txtTitle.setTag(obj.getObj().getContact_information().getTitle());
        txtTitle.setText(obj.getObj().getContact_information().getTitle());
        txtCountry.setTag(obj.getObj().getContact_information().getCountry());
        txtCountry.setText(getCountryName(getActivity(), obj.getObj().getContact_information().getCountry()));
        txtAlternatePhone.setText(obj.getObj().getContact_information().getAlternate_phone());
        txtPhone.setText(obj.getObj().getContact_information().getMobile_phone());



        pnr = obj.getObj().getItenerary_information().getPnr();
        bookingId = obj.getObj().getBooking_id();
        username = obj.getObj().getContact_information().getEmail();


        if(travelPurpose.equals("2")){

            companyBlock.setVisibility(View.VISIBLE);
            txtCity.setText(obj.getObj().getContact_information().getCity());
            txtPostCode.setText(obj.getObj().getContact_information().getPostcode());
            txtCompanyAddress1.setText(obj.getObj().getContact_information().getAddress1());
            txtCompanyAddress2.setText(obj.getObj().getContact_information().getAddress2());
            txtCompanyAddress3.setText(obj.getObj().getContact_information().getAddress3());
            txtCompanyName.setText(obj.getObj().getContact_information().getCompany_name());
            txtState.setTag(obj.getObj().getContact_information().getState());
            txtState.setText(getStateName(getActivity(), obj.getObj().getContact_information().getState()));

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

        return view;
    }

    @Override
    public void onGetChangeContact(ManageChangeContactReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
        }
    }


    @Override
    public void onValidationSucceeded() {

        requestContactInfoChange();

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
        //obj.setSignature(signature);
        //obj.setPnr(pnr);
        //obj.setUsername(username); //Email
        obj.setBooking_id(bookingId);
        obj.setContact_travel_purpose(txtPurpose.getTag().toString());
        obj.setContact_title(txtTitle.getTag().toString());
        obj.setContact_first_name(txtFirstName.getText().toString());
        obj.setContact_last_name(txtLastName.getText().toString());
        obj.setContact_country(txtCountry.getTag().toString());
        obj.setContact_email(txtEmailAddress.getText().toString());
        obj.setContact_mobile_phone(txtPhone.getText().toString());
        obj.setContact_alternate_phone(txtAlternatePhone.getText().toString());

        if(travelPurpose.equals("2")) {
           obj.setContact_company_name(txtCompanyName.getText().toString());
           obj.setContact_address1(txtCompanyAddress1.getText().toString());
           obj.setContact_address2(txtCompanyAddress2.getText().toString());
           obj.setContact_address3(txtCompanyAddress3.getText().toString());
           obj.setContact_state(txtState.getTag().toString());
           obj.setContact_city(txtCity.getText().toString());
           obj.setContact_postcode(txtPostCode.getText().toString());
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
                 croutonAlert(getActivity(), "Fill empty field");
            }

            Log.e("Validation Failed",splitErrorMsg[0]);

        }

    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(ManageFlightChangeContactFragment.this, 0);
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
                    selectedState = selectedCountry.getCode();
                }

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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
