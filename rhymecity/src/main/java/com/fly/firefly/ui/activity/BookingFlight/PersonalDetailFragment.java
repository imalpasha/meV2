package com.fly.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.module.PersonalDetailModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.DefaultPassengerObj;
import com.fly.firefly.ui.object.InfantInfo;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
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
import io.realm.RealmResults;

public class PersonalDetailFragment extends BaseFragment implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener,BookingPresenter.PassengerInfoView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnDOB)
    LinearLayout btnDOB;

    @InjectView(R.id.btnTitle)
    LinearLayout btnTitle;

    @InjectView(R.id.txtTitle)
    TextView txtTitle;

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

    @InjectView(R.id.personalDetailScrollView)
    ScrollView personalDetailScrollView;


   /* @NotEmpty(sequence = 1)
    @InjectView(R.id.txtUserId)
    EditText txtUserId;

    @NotEmpty(sequence = 2)
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @InjectView(R.id.memberLoginBlock)
    LinearLayout memberLoginBlock;

    @InjectView(R.id.txtForgotPassword)
    Button txtForgotPassword;

    @InjectView(R.id.passengerBtnLogin)
    Button passengerBtnLogin;*/


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Personal Details(Passenger Details)";
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
    private AlertDialog dialog;
    private ArrayList<PassengerInfo> passengerObj = new ArrayList<PassengerInfo>();
    private ArrayList<InfantInfo> infantObj = new ArrayList<InfantInfo>();
    //private DefaultPassengerObj defaultObj = new DefaultPassengerObj();
    private ArrayList<DefaultPassengerObj> defaultObj = new ArrayList<DefaultPassengerObj>();

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int adultInc;
    private boolean lessThan12 = true;
    private ArrayList<Integer> ageOfTraveller = new ArrayList<Integer>();

    //different object for different field.
    private DatePickerDialog datePickerYear1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear2 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear3 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear4 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear5 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear6 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear7 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear8 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear9 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear10 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear11 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear12 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYear13 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    //different object for different field.
    private DatePickerDialog datePickerYearE1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE2 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE3 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE4 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE5 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE6 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE7 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE8 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE9 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE10 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE11 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE12 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private DatePickerDialog datePickerYearE13 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.personal_detail, container, false);
        ButterKnife.inject(this, view);

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        adult = bundle.getString(ADULT);
        infant = bundle.getString(INFANT);
        pref = new SharedPrefManager(getActivity());

        autoFill();

        setupPassengerBlock(adult, infant);

        /*DatePicker Setup - Failed to make it global*/

        final DatePickerDialog datePickerExpire = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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
        Log.e("signature",signature);

        /*Adult Passenger Data For Selection*/
        for (int i = 1; i < Integer.parseInt(adult)+1 ; i++)
        {
            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText("Adult" + " " + i);
            itemTitle.setCode(Integer.toString(i));
            adultPassengerList.add(itemTitle);
        }

        /*Display Title Data*/
        titleList = getStaticTitle(getActivity());
        genderList = getGender(getActivity());
        travelDocList = getTravelDoc(getActivity());
        countrys = getStaticCountry(getActivity());

        int totalPassenger = Integer.parseInt(adult)+Integer.parseInt(infant)+1;
        for (adultInc = 1; adultInc < totalPassenger; adultInc++) {

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
                        popupSelection(genderList, getActivity(), btnGender, false, view);
                    }
                });

                final TextView btnTravelDoc = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travel_doc");
                final LinearLayout txtExpireDateBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date_block");

                btnTravelDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupSelectionExtra(travelDocList, getActivity(), btnTravelDoc, false, txtExpireDateBlock,"P",null);
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

                        createDatePickerObject(selectedPassenger);
                        clickedPassenger = selectedPassenger;
                        boolDob = true;
                        boolExpireDate = false;

                    }
                });

                TextView txtExpireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date");
                txtExpireDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        creatExpiredDatePickerObject(selectedPassenger);
                        clickedPassenger = selectedPassenger;
                        boolDob = false;
                        boolExpireDate = true;
                    }
                });

        }

        //set adult passenger header
        for (int adultInc = 1; adultInc < Integer.parseInt(adult) + 1; adultInc++) {

            TextView txtPassengerType = (TextView) view.findViewWithTag("txtPassenger" + adultInc);
            txtPassengerType.setText("ADULT "+adultInc);

        }
        //auto set infant travelling with
        for (int infantInc = 1; infantInc < Integer.parseInt(infant) + 1; infantInc++) {

            TextView travellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(infantInc + Integer.parseInt(adult)) + "_travelling_with");
            travellingWith.setText(adultPassengerList.get(infantInc - 1).getText());

            TextView txtPassengerType = (TextView) view.findViewWithTag("txtPassenger" + Integer.toString(infantInc + Integer.parseInt(adult)));
            txtPassengerType.setText("INFANT " +infantInc);
        }


            btnPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intTotalAdult = 0;

                formContinue = true;
                passengerObj = new ArrayList<PassengerInfo>();
                infantObj = new ArrayList<InfantInfo>();
                defaultObj = new ArrayList<DefaultPassengerObj>();

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

                    title.setFocusable(true);

                    checkTextViewNull(title);
                    checkTextViewNull(dob);
                    checkTextViewNull(travelDoc);
                    checkEditTextNull(docNo);
                    checkTextViewNull(issuingCountry);
                    checkEditTextNull(firstName);
                    checkEditTextNull(lastname);
                    checkEditTextNull(docNo);
                    checkBonuslink(enrich);


                  //  if(!validateAdultAge()){
//
                  //      formContinue = false;
                  //  }

                    String infantTravelDocCode = getTravelDocCode(getActivity(), travelDoc.getText().toString());
                    if(infantTravelDocCode != null){
                        if(infantTravelDocCode.equals("P")){
                            checkTextViewNull(expireDate);
                        }
                    }
                    if(!dob.getText().toString().equals("")){
                        ageOfTraveller.add(travellerAge(dob.getText().toString()));
                    }

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

                    String infantTravelDocCode = getTravelDocCode(getActivity(), travelDoc.getText().toString());
                    if(infantTravelDocCode != null){
                        if(infantTravelDocCode.equals("P")){
                            checkTextViewNull(expireDate);
                        }
                    }

                    if(!dob.getText().toString().equals("")){
                        ageOfTraveller.add(travellerAge(dob.getText().toString()));
                    }

                    //checkEditTextNull(docNo);

                }

                //age validation
                if(travellerAgeValidation(ageOfTraveller)){
                        croutonAlert(getActivity(), "There must be at least one(1) passenger above 12 years old at the date(s) of travel");
                        formContinue = false;
                }

                    if(formContinue){

                        DefaultPassengerObj adultDefault = new DefaultPassengerObj();

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
                            if (travelDocCode.equals("P")) {
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


                            adultDefault = new DefaultPassengerObj();
                            adultDefault.setTitle(passengerObj.get(adultInc-1).getTitle());
                            adultDefault.setFirstname(passengerObj.get(adultInc-1).getFirst_name());
                            adultDefault.setLastname(passengerObj.get(adultInc-1).getLast_name());
                            adultDefault.setIssuingCountry(passengerObj.get(adultInc-1).getIssuing_country());
                            defaultObj.add(adultDefault);

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
                            if (travelDocCode.equals("P")) {

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

                        //if((Integer.parseInt(adult) + Integer.parseInt(infant)) == 1){


                        //}

                        runPassengerInfo(obj);


                    }
                    //else{
                    //    croutonAlert(getActivity(), "Please fill empty field");
                    //}

            }
        });


        //Login from passenger detail page

       /* passengerBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPassword();
            }
        });*/


        personalDetailScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                view.requestFocus();
            }
        });

        return view;
    }

    public void creatExpiredDatePickerObject(Integer currentPosition){

        if(currentPosition.equals(1)){
            datePickerYearE1.setYearRange(year, year+10);
            datePickerYearE1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(2)){
            datePickerYearE2.setYearRange(year , year+10);
            datePickerYearE2.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(3)){
            datePickerYearE3.setYearRange(year , year+10);
            datePickerYearE3.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(4)){
            datePickerYearE4.setYearRange(year , year+10);
            datePickerYearE4.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(5)){
            datePickerYearE5.setYearRange(year, year+10);
            datePickerYearE5.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(6)){
            datePickerYearE6.setYearRange(year , year+10);
            datePickerYearE6.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(7)){
            datePickerYearE7.setYearRange(year , year+10);
            datePickerYearE7.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(8)){
            datePickerYearE8.setYearRange(year, year+10);
            datePickerYearE8.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(9)){
            datePickerYearE9.setYearRange(year , year+10);
            datePickerYearE9.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(10)){
            datePickerYearE10.setYearRange(year, year+10);
            datePickerYearE10.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(11)){
            datePickerYearE11.setYearRange(year , year+10);
            datePickerYearE11.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(12)){
            datePickerYearE12.setYearRange(year, year+10);
            datePickerYearE12.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(13)){
            datePickerYearE13.setYearRange(year, year+10);
            datePickerYearE13.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }

    }

    public void createDatePickerObject(Integer currentPosition){

        if(currentPosition.equals(1)){
            datePickerYear1.setYearRange(year - 100, year);
            datePickerYear1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(2)){
            datePickerYear2.setYearRange(year - 100, year);
            datePickerYear2.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(3)){
            datePickerYear3.setYearRange(year - 100, year);
            datePickerYear3.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(4)){
            datePickerYear4.setYearRange(year - 100, year);
            datePickerYear4.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(5)){
            datePickerYear5.setYearRange(year - 100, year);
            datePickerYear5.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(6)){
            datePickerYear6.setYearRange(year - 100, year);
            datePickerYear6.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(7)){
            datePickerYear7.setYearRange(year - 100, year);
            datePickerYear7.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(8)){
            datePickerYear8.setYearRange(year - 100, year);
            datePickerYear8.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(9)){
            datePickerYear9.setYearRange(year - 100, year);
            datePickerYear9.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(10)){
            datePickerYear10.setYearRange(year - 100, year);
            datePickerYear10.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(11)){
            datePickerYear11.setYearRange(year - 100, year);
            datePickerYear11.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(12)){
            datePickerYear12.setYearRange(year - 100, year);
            datePickerYear12.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }else if(currentPosition.equals(13)){
            datePickerYear13.setYearRange(year - 100, year);
            datePickerYear13.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }

    }

    public void forgotPassword(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.forgot_password_screen, null);
        Button cont = (Button)myView.findViewById(R.id.btncontinue);

        final EditText editEmail = (EditText)myView.findViewById(R.id.editTextemail_login);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editEmail.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_LONG).show();

                }else if (!editEmail.getText().toString().matches(emailPattern)) {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                }else{
                    requestForgotPassword(editEmail.getText().toString(),"");
                    dialog.dismiss();
                }

            }

        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    public void requestForgotPassword(String username,String signature){
        initiateLoading(getActivity());
        PasswordRequest data = new PasswordRequest();
        data.setEmail(username);
        data.setSignature(signature);
        presenter.forgotPassword(data);
    }

    @Override
    public void onRequestPasswordSuccess(ForgotPasswordReceive obj) {
        dismissLoading();
        Log.e("Message", obj.getMessage());

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            setSuccessDialog(getActivity(), obj.getMessage(),null,"Success!");
        }

    }

    public void checkTextViewNull(TextView txtView){
        if(txtView.getText().toString() == "") {
            txtView.setError("Field Required");
            setShake(txtView);
            txtView.requestFocus();
            formContinue = false;
        }
    }

    public void checkEditTextNull(EditText editText){

        if (editText.getText().toString().matches("")) {
            editText.setError("Field Required");
            setShake(editText);
            editText.requestFocus();
            formContinue = false;
        }

    }

    public void checkBonuslink(EditText bonuslink){

        if (!bonuslink.getText().toString().matches("")) {
            if(bonuslink.length() < 16 || bonuslink.length() > 16){
                bonuslink.setError("Invalid bonuslink card number");
                setShake(bonuslink);
                bonuslink.requestFocus();
            }
            formContinue = false;
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
//
        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(),obj.getMessage() , getActivity());
        if (status) {
            Gson gsonFlight = new Gson();
            String seat = gsonFlight.toJson(obj);
            pref.setSeat(seat);

            Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
            intent.putExtra("INSURANCE_STATUS", (new Gson()).toJson(obj));
            //intent.putExtra("DEFAULT_PASSENGER_INFO", (new Gson()).toJson(defaultObj));
            intent.putParcelableArrayListExtra("DEFAULT_PASSENGER_INFO", defaultObj);
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
        //loginFragment(txtUserId.getText().toString(), AESCBC.encrypt(App.KEY, App.IV, txtPassword.getText().toString()));
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
            signature = obj.getUser_info().getSignature();

            Log.e(storeUsername,storePassword);
            pref.setUserEmail(storeUsername);
            pref.setUserPassword(storePassword);

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUser_info());
            pref.setUserInfo(userInfo);
            autoFill();
            //memberLoginBlock.setVisibility(View.GONE);
        }
    }

    public void autoFill(){

        /* If Passenger Already Login - Auto display necessary data */
        HashMap<String, String> initLogin = pref.getLoginStatus();
        String loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        if(loginStatus != null && loginStatus.equals("Y")) {
            //memberLoginBlock.setVisibility(View.GONE);
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
            //memberLoginBlock.setVisibility(View.VISIBLE);
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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(result.size() > 0){
            Log.e("x",result.get(0).getCachedResult().toString());
            Log.e("size",Integer.toString(result.size()));

            Gson gson = new Gson();
            PassengerInfoReveice obj = gson.fromJson(result.get(0).getCachedResult(), PassengerInfoReveice.class);
            onPassengerInfo(obj);
            try {
                Log.e("Info",obj.getObj().getStatus());
            }catch (Exception e){
                Log.e("y","y");
            }

            try {
                Log.e("Info", obj.getStatus());
            }catch (Exception e){
                Log.e("x","x");
            }

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
