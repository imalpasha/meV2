package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.PersonalDetailModule;
import com.fly.firefly.ui.object.InfantInfo;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class PersonalDetailFragment extends BaseFragment implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener,BookingPresenter.PassengerInfoView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnDOB)
    LinearLayout btnDOB;

    @InjectView(R.id.btnTitle)
    LinearLayout btnTitle;

    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @InjectView(R.id.passengerBtnLogin)
    Button passengerBtnLogin;

    @InjectView(R.id.passengerBlock1)
    LinearLayout passengerBlock1;
    @InjectView(R.id.passengerBlock2)
    LinearLayout passengerBlock2;
    @InjectView(R.id.passengerBlock3)
    LinearLayout passengerBlock3;
    @InjectView(R.id.passengerBlock4)
    LinearLayout passengerBlock4;
    @InjectView(R.id.passengerBlock5)
    LinearLayout passengerBlock5;
    @InjectView(R.id.passengerBlock6)
    LinearLayout passengerBlock6;
    @InjectView(R.id.passengerBlock7)
    LinearLayout passengerBlock7;
    @InjectView(R.id.passengerBlock8)
    LinearLayout passengerBlock8;
    @InjectView(R.id.passengerBlock9)
    LinearLayout passengerBlock9;
    @InjectView(R.id.btnPersonalInfo)
    Button btnPersonalInfo;

    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtUserId)
    EditText txtUserId;

    @NotEmpty(sequence = 2)
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @InjectView(R.id.memberLoginBlock)
    LinearLayout memberLoginBlock;



    private int fragmentContainerId;
    private String DATEPICKER_TAG = "DATEPICKER_TAG";
    private ArrayList<DropDownItem> titleList;
    private ArrayList<DropDownItem> genderList;
    private ArrayList<DropDownItem> travelDocList;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> adultPassengerList;

    private final String ADULT = "ADULT";
    private final String INFANT = "INFANT";
    private String adult,infant;
    private SharedPrefManager pref;
    private String bookingID,signature;
    private int clickedPassenger;
    private Boolean boolDob = false;
    private Boolean boolExpireDate = false;
    private Boolean formContinue = true;
    private LoginReceive.UserInfo loginObj;
    private Validator mValidator;
    private View view;
    private String storeUsername;
    private String storePassword;

    public static PersonalDetailFragment newInstance(Bundle bundle) {

        PersonalDetailFragment fragment = new PersonalDetailFragment();
        fragment.setArguments(bundle);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new PersonalDetailModule(this)).inject(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.personal_detail, container, false);
        ButterKnife.inject(this, view);
        final ArrayList<PassengerInfo> passengerObj = new ArrayList<PassengerInfo>();
        final ArrayList<InfantInfo> infantObj = new ArrayList<InfantInfo>();

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        adult = bundle.getString(ADULT);
        infant = bundle.getString(INFANT);
        pref = new SharedPrefManager(getActivity());

        autoFill();



        setupPassengerBlock(adult, infant);

        /*DatePicker Setup - Failed to make it global*/
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final DatePickerDialog datePickerYear = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final DatePickerDialog datePickerExpire = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerYear.setYearRange(year - 100, year);
        datePickerExpire.setYearRange(year, year+20);

        titleList = new ArrayList<DropDownItem>();
        genderList = new ArrayList<DropDownItem>();
        travelDocList = new ArrayList<DropDownItem>();
        countrys = new ArrayList<DropDownItem>();
        adultPassengerList = new ArrayList<DropDownItem>();

        /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        /*Adult Passenger Data For Selection*/
        for (int i = 1; i < Integer.parseInt(adult)+1 ; i++)
        {
            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText("Passenger" + " " + i);
            itemTitle.setCode(Integer.toString(i));
            adultPassengerList.add(itemTitle);
        }

        /*Display Title Data*/
        JSONArray jsonTitle = getTitle(getActivity());
        for (int i = 0; i < jsonTitle.length(); i++)
        {
            JSONObject row = (JSONObject) jsonTitle.opt(i);

            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(row.optString("title_name"));
            itemTitle.setCode(row.optString("title_code"));
            itemTitle.setTag("Title");
            titleList.add(itemTitle);
        }

        /*Gender*/
        final String[] gender = getResources().getStringArray(R.array.gender);
        for(int i = 0;i<gender.length; i++)
        {
            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(gender[i]);
            genderList.add(itemTitle);
        }

        /*Travel Doc*/
        final String[] doc = getResources().getStringArray(R.array.travel_doc);
        for(int i = 0;i<doc.length; i++)
        {
            String travelDoc = doc[i];
            String[] splitDoc = travelDoc.split("-");

            DropDownItem itemDoc = new DropDownItem();
            itemDoc.setText(splitDoc[0]);
            travelDocList.add(itemDoc);
        }

          /*Display Country Data*/
        JSONArray jsonCountry = getCountry(getActivity());

        for (int i = 0; i < jsonCountry.length(); i++)
        {
            JSONObject row = (JSONObject) jsonCountry.opt(i);

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(row.optString("country_name"));
            itemCountry.setCode(row.optString("country_code"));
            itemCountry.setTag("Country");
            itemCountry.setId(i);
            countrys.add(itemCountry);
        }

        int totalPassenger = Integer.parseInt(adult)+Integer.parseInt(infant)+1;
        for (int adultInc = 1; adultInc < totalPassenger; adultInc++) {

            final int selectedPassenger = adultInc;
            try {
                final TextView btnTravellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travelling_with");
                btnTravellingWith.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupSelection(adultPassengerList, getActivity(), btnTravellingWith, false,view);
                    }
                });
            }
            catch(Exception e){

            }

            try {
                final TextView btnTitle = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_title");
                btnTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupSelection(titleList, getActivity(), btnTitle, false,view);
                    }
                });
            }catch (Exception e) {

            }

               final TextView btnGender = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_gender");
                btnGender.setOnClickListener(new View.OnClickListener() {
                   @Override
                    public void onClick(View v) {
                        popupSelection(genderList, getActivity(), btnGender, false,view);
                    }
                });

                final TextView btnTravelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travel_doc");
                final LinearLayout txtExpireDateBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date_block");

                btnTravelDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupSelectionExtra(travelDocList, getActivity(), btnTravelDoc, false, txtExpireDateBlock,"Malaysia IC");
                    }
                });

                final TextView btnCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_issuing_country");
                btnCountry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCountrySelector(getActivity(),countrys);
                        clickedPassenger = selectedPassenger;
                    }
                });


                TextView txtDob = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_dob");
                txtDob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerYear.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
                        clickedPassenger = selectedPassenger;
                        boolDob = true;
                    }
                });

                TextView txtExpireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date");
                txtExpireDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerExpire.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
                        clickedPassenger = selectedPassenger;
                        boolExpireDate = true;
                    }
                });
            Log.e("adultInc--", Integer.toString(adultInc));

        }


        btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intTotalAdult = 0;

                /*Manual Validation*/
                for (int adultInc = 1; adultInc < Integer.parseInt(adult) + 1; adultInc++) {

                    PassengerInfo passengerInfo = new PassengerInfo();

                    intTotalAdult++;
                    TextView title = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_title");
                    EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_first_name");
                    EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_last_name");
                    TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_dob");
                    TextView travelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travel_doc");
                    TextView expireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date");

                    TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_issuing_country");
                    EditText docNo = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_doc_no");
                    EditText enrich = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_enrich");

                    checkTextViewNull(title);
                    checkTextViewNull(dob);
                    checkTextViewNull(travelDoc);
                    checkEditTextNull(docNo);
                    checkTextViewNull(issuingCountry);
                    checkEditTextNull(firstName);
                    checkEditTextNull(lastname);
                    checkEditTextNull(docNo);

                }


                for (int infantInc = 1; infantInc < Integer.parseInt(infant) + 1; infantInc++) {

                    InfantInfo infantInfo = new InfantInfo();

                    TextView travellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_travelling_with");
                    TextView gender = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_gender");
                    EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_first_name");
                    EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_last_name");
                    TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_dob");
                    TextView travelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_travel_doc");
                    TextView expireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_expire_date");

                    TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_issuing_country");
                    EditText docNo = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult) + "_doc_no");

                    checkTextViewNull(travellingWith);
                    checkTextViewNull(gender);
                    checkTextViewNull(dob);
                    checkTextViewNull(travelDoc);
                    checkEditTextNull(docNo);
                    checkTextViewNull(issuingCountry);
                    checkEditTextNull(firstName);
                    checkEditTextNull(lastname);
                    checkEditTextNull(docNo);

                }

                    if(formContinue){

                        int intTotalAdult2 = 0;
                        //GET ADULT PASSENGER INFO
                        for (int adultInc = 1; adultInc < Integer.parseInt(adult) + 1; adultInc++) {

                            PassengerInfo passengerInfo = new PassengerInfo();

                            intTotalAdult2++;
                            TextView title = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_title");
                            TextView gender = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_gender");
                            EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_first_name");
                            EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_last_name");
                            TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_dob");
                            TextView travelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travel_doc");
                            TextView expireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date");

                            TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_issuing_country");
                            EditText docNo = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_doc_no");
                            EditText enrich = (EditText) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_enrich");

                            //TITLE
                            String titleCode = getTitleCode(getActivity(), title.getText().toString(),"code");
                            passengerInfo.setTitle(titleCode);

                            gender.setVisibility(View.GONE);
                            //Gender
                            //String genderCode = (getActivity(),);
                            //passengerInfo.setGender(gender.getText().toString());

                            passengerInfo.setFirst_name(firstName.getText().toString());
                            passengerInfo.setLast_name(lastname.getText().toString());

                            //DOB
                            String fullDOB = dob.getText().toString();
                            String[] splitDOB = fullDOB.split("/");
                            passengerInfo.setDob(splitDOB[2]+"-"+splitDOB[1]+"-"+splitDOB[0]);

                            //Travel Doc
                            String travelDocCode = getTravelDocCode(getActivity(), travelDoc.getText().toString());
                            passengerInfo.setTravel_document(travelDocCode);
                            if (!travelDocCode.equals("NRIC")) {
                                //ExpireDate
                                String fullExpireDate = expireDate.getText().toString();
                                String[] splitExpireDate = fullExpireDate.split("/");
                                passengerInfo.setExpiration_date(splitExpireDate[2] + "-" + splitExpireDate[1] + "-" +splitExpireDate[0]);
                            } else {
                                passengerInfo.setExpiration_date("");
                            }

                            //Issuing Country Code
                            String countryCode = getCountryCode(getActivity(), issuingCountry.getText().toString());
                            passengerInfo.setIssuing_country(countryCode);

                            passengerInfo.setDocument_number(docNo.getText().toString());
                            passengerInfo.setBonusLink(enrich.getText().toString());

                            passengerObj.add(passengerInfo);
                        }

                        for (int infantInc = 1; infantInc < Integer.parseInt(infant) + 1; infantInc++) {

                            InfantInfo infantInfo = new InfantInfo();

                            TextView travellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_travelling_with");
                            TextView gender = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_gender");
                            EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_first_name");
                            EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_last_name");
                            TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_dob");
                            TextView travelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_travel_doc");
                            TextView expireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_expire_date");

                            TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_issuing_country");
                            EditText docNo = (EditText) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_doc_no");

                            //Gender
                            infantInfo.setGender(gender.getText().toString());

                            //Travelling With
                            String travellingWithPassenger = travellingWith.getText().toString();
                            String[] splitTravelling = travellingWithPassenger.split(" ");
                            int travellingWithCode = Integer.parseInt(splitTravelling[1]) - 1;

                            Log.e(travellingWithPassenger,Integer.toString(travellingWithCode));
                            Log.e("firstName",firstName.getText().toString());
                            Log.e(travellingWithPassenger,splitTravelling[1]);

                            infantInfo.setTraveling_with(Integer.toString(travellingWithCode));
                            infantInfo.setFirst_name(firstName.getText().toString());
                            infantInfo.setLast_name(lastname.getText().toString());

                            //DOB
                            String fullDOB = dob.getText().toString();
                            String[] splitDOB = fullDOB.split("/");
                            infantInfo.setDob(splitDOB[2] + "-" +splitDOB[1]+ "-" +splitDOB[0]);

                            //Travel Doc
                            String travelDocCode = getTravelDocCode(getActivity(), travelDoc.getText().toString());
                            infantInfo.setTravel_document(travelDocCode);
                            if (!travelDocCode.equals("NRIC")) {

                                String fullExpireDate = expireDate.getText().toString();
                                String[] splitExpireDate = fullExpireDate.split("/");
                                infantInfo.setExpiration_date(splitExpireDate[2] + "-" +splitExpireDate[1]+ "-"+splitExpireDate[0]);
                            } else {
                                infantInfo.setExpiration_date("");
                            }

                            //Issuing Country Code
                            String countryCode = getCountryCode(getActivity(), issuingCountry.getText().toString());
                            infantInfo.setIssuing_country(countryCode);
                            infantInfo.setDocument_number(docNo.getText().toString());

                            infantObj.add(infantInfo);

                        }

                        Passenger obj = new Passenger();
                        obj.setSignature(signature);
                        obj.setBooking_id(bookingID);
                        obj.setPassengers(passengerObj);
                        obj.setInfant(infantObj);

                        runPassengerInfo(obj);

                    }else{
                        croutonAlert(getActivity(), "Please fill empty field");
                    }

            }
        });


        //Login from passenger detail page

        passengerBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });



        return view;
    }

    public void checkTextViewNull(TextView txtView){
        if(txtView.getText().toString() == "") {
            txtView.setError("Field Required");
            formContinue = false;
        }else{
            formContinue = true;
        }
    }

    public void checkEditTextNull(EditText editText){

        if (editText.getText().toString().matches("")) {
            editText.setError("Field Required");
            formContinue = false;
        }else{
            formContinue = true;
        }
    }

    public void runPassengerInfo(Passenger obj){

        if(infant.equals("0")){
            initiateLoading(getActivity());
            presenter.passengerInfo(obj);
        }else{
            if(manualValidation()){
                initiateLoading(getActivity());
                presenter.passengerInfo(obj);
            }else{
                croutonAlert(getActivity(), "One infant per adult only");
                dismissLoading();
            }
        }
    }

    /*Validate if many infant assign to one adult - return error*/
    public boolean manualValidation() {
        boolean manualValidationStatus = true;
        int totalPassenger = Integer.parseInt(adult) + Integer.parseInt(infant) + 1;
        Log.e("totalPassenger",Integer.toString(totalPassenger));
        ArrayList<String> passengerArray = new ArrayList<String>();
        for (int adultInc = totalPassenger-Integer.parseInt(adult); adultInc < totalPassenger; adultInc++) {
                TextView btnTravellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travelling_with");
                String passengerID = btnTravellingWith.getText().toString();
                Log.e("passengerID", passengerID);
                passengerArray.add(passengerID);
        }

        //check duplicate
        List<String> usedNames = new ArrayList<String>();
        for(int x = 0 ; x < passengerArray.size() ;x++){

            Log.e("Passenger"+Integer.toString(x),passengerArray.get(x));
            if (usedNames.contains(passengerArray.get(x))){
                 manualValidationStatus = false;
                    Log.e("FALSE","FALSE");
              }else {
                    Log.e("TRUE","TRUE");
                usedNames.add(passengerArray.get(x));
              }
              Log.e("Names",usedNames.toString());
        }


        return manualValidationStatus;
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(PersonalDetailFragment.this, 0);
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

                TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(clickedPassenger) + "_issuing_country");
                issuingCountry.setText(selectedCountry.getText());
            }
        }
    }

    public void goSeatSelectionPage()
    {
        Intent seatSelection = new Intent(getActivity(), SeatSelectionActivity.class);
        getActivity().startActivity(seatSelection);
    }

    @Override
    public void onPassengerInfo(PassengerInfoReveice obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
            intent.putExtra("INSURANCE_STATUS", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);

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

        if(boolExpireDate){
            TextView txtExpireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(clickedPassenger) + "_expire_date");
            txtExpireDate.setText(varDay+""+day + "/"+varMonth + "" + (month+1)+ "/" + year);

        }else if(boolDob){
            TextView txtDOB = (TextView) view.findViewWithTag("passenger" + Integer.toString(clickedPassenger) + "_dob");
            txtDOB.setText(varDay+""+day + "/"+varMonth + "" + (month+1)+ "/" + year);
        }

    }

    public void setupPassengerBlock(String totalAdult, String totalInfant){
        /*Setup Adult Passenger Box (not a proper way - just to suit with validator )*/

        int intTotalAdult = 0;
        for(int adultInc = 1 ; adultInc < Integer.parseInt(totalAdult) + 1 ; adultInc++) {
            intTotalAdult++;
            LinearLayout passengerBlock = (LinearLayout) view.findViewWithTag("passengerBlock" + Integer.toString(adultInc));
            passengerBlock.setVisibility(View.VISIBLE);

            LinearLayout travellingBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travelling_with_block");
            travellingBlock.setVisibility(View.GONE);

            LinearLayout genderBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_gender_block");
            genderBlock.setVisibility(View.GONE);

        }

        for(int infantInc = 1 ; infantInc < Integer.parseInt(totalInfant) + 1 ; infantInc++){

            LinearLayout passengerBlock = (LinearLayout) view.findViewWithTag("passengerBlock" + Integer.toString(infantInc+intTotalAdult));
            passengerBlock.setVisibility(View.VISIBLE);

            LinearLayout titleBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(infantInc+intTotalAdult)+"_title_with_block");
            titleBlock.setVisibility(View.GONE);

            LinearLayout enrichBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(infantInc+intTotalAdult)+"_enrich_block");
            enrichBlock.setVisibility(View.GONE);

            Log.e("-->","passenger" + Integer.toString(infantInc+intTotalAdult)+"_title_with_block");
        }
    }

    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {
        loginFragment(txtUserId.getText().toString(), AESCBC.encrypt(App.KEY, App.IV, txtPassword.getText().toString()));
    }

    /* SENT REQUEST TO LOGIN API*/
    public void loginFragment(String username,String password){
        /*Start Loading*/
        initiateLoading(getActivity());
        LoginRequest data = new LoginRequest();
        data.setUsername(username);
        data.setPassword(password);

        storeUsername = username;
        storePassword = password;

        presenter.requestLogin(data);
    }

    /* LOGIN API RETURN SUCCESS */
    @Override
    public void onLoginSuccess(LoginReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            pref.setLoginStatus("Y");
            pref.setNewsletterStatus(obj.getUser_info().getNewsletter());
            pref.setSignatureToLocalStorage(obj.getUser_info().getSignature());
            pref.setUsername(obj.getUser_info().getFirst_name());

            Log.e(storeUsername,storePassword);
            pref.setUserEmail(storeUsername);
            pref.setUserPassword(storePassword);

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUser_info());
            pref.setUserInfo(userInfo);
            autoFill();
            memberLoginBlock.setVisibility(View.GONE);
        }
    }

    public void autoFill(){

        /* If Passenger Already Login - Auto display necessary data */
        HashMap<String, String> initLogin = pref.getLoginStatus();
        String loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        if(loginStatus != null && loginStatus.equals("Y")) {
            memberLoginBlock.setVisibility(View.GONE);
            Log.e("GONE","TRUE");
            Gson gson = new Gson();
            String userInfo = getUserInfoCached(getActivity());
            loginObj = gson.fromJson(userInfo, LoginReceive.UserInfo.class);
            txtTitle.setText(loginObj.getContact_title());

            TextView passenger1Title = (TextView) view.findViewWithTag("passenger1_title");
            TextView passenger1FirstName = (TextView) view.findViewWithTag("passenger1_first_name");
            TextView passenger1LastName = (TextView) view.findViewWithTag("passenger1_last_name");
            TextView passenger1Dob = (TextView) view.findViewWithTag("passenger1_dob");

            passenger1Title.setText(getTitleCode(getActivity(), loginObj.getContact_title(), "name"));
            passenger1FirstName.setText(loginObj.getContact_first_name());
            passenger1LastName.setText(loginObj.getContact_last_name());
            passenger1Dob.setText(reformatDOB(loginObj.getDOB()));
        }else{
            memberLoginBlock.setVisibility(View.VISIBLE);
            Log.e("VISIBLE", "TRUE");
        }

    }

    /* Validation Failed - Toast Error */
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
                croutonAlert(getActivity(), splitErrorMsg[0]);
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
