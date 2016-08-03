package com.metech.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.ForgotPasswordReceive;
import com.metech.firefly.api.obj.LoginReceive;
import com.metech.firefly.api.obj.PassengerInfoReveice;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.BookingFlight.ManageFamilyAndFriend.ManageFriendAndFamilyActivity;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.metech.firefly.ui.module.PersonalDetailModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.object.FamilyFriendList;
import com.metech.firefly.ui.object.FamilyFriendObj;
import com.metech.firefly.ui.object.InfantInfo;
import com.metech.firefly.ui.object.LoginRequest;
import com.metech.firefly.ui.object.Passenger;
import com.metech.firefly.ui.object.PassengerInfo;
import com.metech.firefly.ui.object.PasswordRequest;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.DropMenuAdapter;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

    @InjectView(R.id.friendAndFamilyLayout)
    LinearLayout friendAndFamilyLayout;

    @InjectView(R.id.btnFamilyFriend)
    Button btnFamilyFriend;

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
    private ArrayList<DefaultPassengerObj> friendAndFamilyObj = new ArrayList<DefaultPassengerObj>();

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int adultInc;
    private boolean lessThan12 = true;
    private ArrayList<Integer> ageOfTraveller = new ArrayList<Integer>();
    private String flightType;
    private boolean infantLoop;
    private String loginStatus;
    private int totalPassenger;

    //different object for different field.
    private DatePickerDialog datePickerYear1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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

        HashMap<String, String> initLoginStatus = pref.getLoginStatus();
        loginStatus = initLoginStatus.get(SharedPrefManager.ISLOGIN);

        autoFill();

        setupPassengerBlock(adult, infant);

        /*DatePicker Setup - Failed to make it global*/

        //final DatePickerDialog datePickerExpire = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //datePickerExpire.setYearRange(year, year+20);

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
            itemTitle.setText("Adult" + " " + i);
            itemTitle.setCode(Integer.toString(i));
            adultPassengerList.add(itemTitle);
        }

        totalPassenger = Integer.parseInt(adult)+Integer.parseInt(infant)+1;



        /*Display Title Data*/
        titleList = getStaticTitle(getActivity());
        genderList = getGender(getActivity());
        travelDocList = getTravelDoc(getActivity());
        countrys = getStaticCountry(getActivity());

        //if mh..disable bonuslink
        HashMap<String, String> initFlightType = pref.getFlightType();
        flightType = initFlightType.get(SharedPrefManager.FLIGHT_TYPE);

        if(flightType.equals("MH")){
            for (adultInc = 1; adultInc < totalPassenger; adultInc++) {
                LinearLayout enrichBlock = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc)+"_enrich_block");
                enrichBlock.setVisibility(View.GONE);
            }
        }


        for (adultInc = 1; adultInc < totalPassenger; adultInc++) {

            final int selectedPassenger = adultInc;
            final CheckBox checkFF = (CheckBox) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_selectFF");
            if(loginStatus.equals("Y")){
                checkFF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked)
                        {
                            Log.e("selectedPassenger",Integer.toString(selectedPassenger));

                            //check selected infant or adult//
                            TextView txtPassengerType = (TextView) view.findViewWithTag("txtPassenger" + selectedPassenger);
                            String[] splitPassengerType = txtPassengerType.getText().toString().split(" ");
                            Log.e("splitPassengerType[0]",splitPassengerType[0]);
                            if(splitPassengerType[0].equals("INFANT")){
                                infantLoop = true;
                            }else{
                                infantLoop = false;
                                Log.e("INFANT","FALSE");
                            }

                            RealmResults<FamilyFriendList> cachedListResult = RealmObjectController.getFamilyFriends(MainFragmentActivity.getContext());
                            if(cachedListResult.size() > 0){
                                Gson gson = new Gson();
                                FamilyFriendObj obj = gson.fromJson(cachedListResult.get(0).getCachedList(), FamilyFriendObj.class);
                                friendAndFamilyObj = obj.getFamily_and_friend();
                            }

                            if(friendAndFamilyObj.size() > 0){

                                ArrayList<DropDownItem> passengerList = new ArrayList<DropDownItem>();

                                for(int i = 0;i<friendAndFamilyObj.size(); i++)
                                {
                                    DropDownItem itemPurpose = new DropDownItem();
                                    if(infantLoop){
                                        if(friendAndFamilyObj.get(i).getPassenger_type().equals("Infant")){
                                            itemPurpose.setText(friendAndFamilyObj.get(i).getTitle()+" "+friendAndFamilyObj.get(i).getFirst_name());
                                            itemPurpose.setCode(Integer.toString(i));
                                            passengerList.add(itemPurpose);
                                        }
                                    }else{
                                        if(friendAndFamilyObj.get(i).getPassenger_type().equals("Adult")){
                                            itemPurpose.setText(friendAndFamilyObj.get(i).getTitle()+" "+friendAndFamilyObj.get(i).getFirst_name());
                                            itemPurpose.setCode(Integer.toString(i));
                                            passengerList.add(itemPurpose);
                                            Log.e("?","1");

                                        }
                                    }
                                }
                                //popupSelection(passengerList, getActivity());
                                Log.e("?","2");
                                customPopupForContactInfo(passengerList, getActivity(),selectedPassenger,splitPassengerType[0]);

                            }
                        }else{
                            clearSelectedFF(selectedPassenger);
                        }
                    }
                });
            }else{
                checkFF.setVisibility(View.GONE);
            }


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


            final TextView txtDob = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_dob");
            txtDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //createDatePickerObject(selectedPassenger);

                    String currentDOB = txtDob.getText().toString();
                    if(!currentDOB.equals("")){
                        String[] splitReturn = currentDOB.split("/");
                        createDateObj(Integer.parseInt(splitReturn[2]), Integer.parseInt(splitReturn[1])-1, Integer.parseInt(splitReturn[0]));
                    }else{
                        createDateObj(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }

                    clickedPassenger = selectedPassenger;
                    boolDob = true;
                    boolExpireDate = false;

                }
            });

            final TextView txtExpireDate = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_expire_date");
            txtExpireDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String currentExpired = txtExpireDate.getText().toString();
                    if(!currentExpired.equals("")){
                        String[] splitReturn = currentExpired.split("/");
                        createDateObj(Integer.parseInt(splitReturn[2]), Integer.parseInt(splitReturn[1])-1, Integer.parseInt(splitReturn[0]));
                    }else{
                        createDateObj(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    }

                    //creatExpiredDatePickerObject(selectedPassenger);
                    clickedPassenger = selectedPassenger;
                    boolDob = false;
                    boolExpireDate = true;
                }
            });

        }

        //set adult passenger header
        for (int txtAdult = 1; txtAdult < Integer.parseInt(adult) + 1; txtAdult++) {

            TextView txtPassengerType = (TextView) view.findViewWithTag("txtPassenger" + txtAdult);
            txtPassengerType.setText("ADULT "+txtAdult);
            Log.e("123","txtPassenger" + Integer.toString(txtAdult));
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

                    //checkTextViewNull(travelDoc);
                    //checkEditTextNull(docNo);

                    checkTextViewNull(issuingCountry);
                    checkEditTextNull(firstName);
                    checkEditTextNull(lastname);
                    //checkEditTextNull(docNo);
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
                    //checkTextViewNull(travelDoc);
                    //checkEditTextNull(docNo);
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
                        CheckBox checkAsFF = (CheckBox) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_checkAsFF");
                        TextView passengerTag = (TextView) view.findViewWithTag("txtPassenger" + Integer.toString(adultInc) + "Tag");

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
                        //String travelDocCode = getTravelDocCode(getActivity(), travelDoc.getText().toString());
                        //passengerInfo.setDocument_number(docNo.getText().toString());

                        String travelDocCode = "NRIC";

                        //auto assign travel doc - new req -  change later
                        passengerInfo.setTravel_document(travelDocCode);

                        if (travelDocCode.equals("P")) {
                            //ExpireDate
                            String fullExpireDate = expireDate.getText().toString();
                            String[] splitExpireDate = fullExpireDate.split("/");
                            passengerInfo.setExpiration_date(splitExpireDate[2] + "-" + splitExpireDate[1] + "-" +splitExpireDate[0]);
                        } else {
                            passengerInfo.setExpiration_date("");
                        }

                        passengerInfo.setDocument_number("");

                        //Issuing Country Code
                        String countryCode = getCountryCode(getActivity(), issuingCountry.getText().toString());
                        passengerInfo.setIssuing_country(countryCode);

                        passengerInfo.setBonusLink(enrich.getText().toString());
                        if(checkAsFF.isChecked()){
                            passengerInfo.setFriend_and_family("Y");
                            passengerInfo.setPassenger_type("Adult");

                            if(passengerTag.getText().toString() != ""){
                                passengerInfo.setFriend_and_family_id(passengerTag.getText().toString());
                            }//id is available
                        }

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
                        CheckBox checkAsFF = (CheckBox) view.findViewWithTag("passenger" + Integer.toString(infantInc + intTotalAdult2) + "_checkAsFF");
                        TextView passengerTag = (TextView) view.findViewWithTag("txtPassenger" + Integer.toString(infantInc + intTotalAdult2) + "Tag");

                        //Gender
                        infantInfo.setGender(gender.getText().toString());

                        //Travelling With
                        String travellingWithPassenger = travellingWith.getText().toString();
                        String[] splitTravelling = travellingWithPassenger.split(" ");
                        int travellingWithCode = Integer.parseInt(splitTravelling[1]) - 1;

                        Log.e("travellingWithCode",Integer.toString(travellingWithCode));

                        infantInfo.setTraveling_with(Integer.toString(travellingWithCode));
                        infantInfo.setFirst_name(firstName.getText().toString());
                        infantInfo.setLast_name(lastname.getText().toString());

                        //DOB
                        String fullDOB = dob.getText().toString();
                        String[] splitDOB = fullDOB.split("/");
                        infantInfo.setDob(splitDOB[2] + "-" +splitDOB[1]+ "-" +splitDOB[0]);

                        //Travel Doc
                        String travelDocCode = "NRIC";
                        //getTravelDocCode(getActivity(), travelDoc.getText().toString());
                        infantInfo.setTravel_document(travelDocCode);
                        infantInfo.setDocument_number("");

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
                        if(checkAsFF.isChecked()){
                            infantInfo.setFriend_and_family("Y");
                            infantInfo.setPassenger_type("Infant");

                            if(passengerTag.getText().toString() != ""){
                                infantInfo.setFriend_and_family_id(passengerTag.getText().toString());
                            }//id is available
                        }

                        infantObj.add(infantInfo);

                    }

                    HashMap<String, String> initFlightType = pref.getFlightType();
                    flightType = initFlightType.get(SharedPrefManager.FLIGHT_TYPE);

                    Passenger obj = new Passenger();
                    obj.setSignature(signature);
                    obj.setFlight_type(flightType);
                    obj.setBooking_id(bookingID);

                    //FF
                    if(loginStatus.equals("Y")){
                        HashMap<String, String> initEmail = pref.getUserEmail();
                        String userEmail = initEmail.get(SharedPrefManager.USER_EMAIL);
                        obj.setUser_email(userEmail);
                    }else{
                        obj.setUser_email("");
                    }

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
                // view.requestFocus();
            }
        });

        btnFamilyFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent manageFF = new Intent(getActivity(), ManageFriendAndFamilyActivity.class);
                manageFF.putParcelableArrayListExtra("FRIEND_AND_FAMILY", friendAndFamilyObj);
                getActivity().startActivity(manageFF);

            }
        });
        return view;
    }

    public void createDateObj(Integer year , Integer month , Integer day){

        datePickerYear1 = DatePickerDialog.newInstance(this,year,month,day);
        datePickerYear1.setYearRange(calendar.get(Calendar.YEAR) - 80, calendar.get(Calendar.YEAR));

        if(checkFragmentAdded()){
            datePickerYear1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }
    }


    /*Global PoPup*/
    public void customPopupForContactInfo(final ArrayList array,Activity act,final int currentPosition,final String type){


        final ArrayList<DropDownItem> a = array;
        DropMenuAdapter dropState = new DropMenuAdapter(act);
        dropState.setItems(a);

        AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

        alertStateCode.setSingleChoiceItems(dropState, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selectedCode = a.get(which).getCode();

                TextView title = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_title");
                TextView gender = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_gender");
                EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_first_name");
                EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_last_name");
                TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_dob");
                TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_issuing_country");
                EditText enrich = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_enrich");
                TextView passengerTag = (TextView) view.findViewWithTag("txtPassenger" + Integer.toString(currentPosition) + "Tag");

                title.setText(getTitleCode(getActivity(), friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getTitle(), "name"));
                firstName.setText(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getFirst_name());
                lastname.setText(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getLast_name());

                //split & rearrange
                dob.setText(reformatDOB3(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getDob()));
                issuingCountry.setText(getCountryName(getActivity(), friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getNationality()));
                passengerTag.setText(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getId());
                if(type.equals("INFANT")){
                    gender.setText(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getGender());
                }else{
                    enrich.setText(friendAndFamilyObj.get(Integer.parseInt(selectedCode)).getBonuslink_card());
                }
                dialog.dismiss();
            }
        });


        AlertDialog mDialog = alertStateCode.create();
        mDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = 600;
        mDialog.getWindow().setAttributes(lp);
    }

    public void clearSelectedFF(int currentPosition){

        TextView title = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_title");
        TextView gender = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_gender");
        EditText firstName = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_first_name");
        EditText lastname = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_last_name");
        TextView dob = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_dob");
        TextView issuingCountry = (TextView) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_issuing_country");
        EditText enrich = (EditText) view.findViewWithTag("passenger" + Integer.toString(currentPosition) + "_enrich");
        TextView passengerTag = (TextView) view.findViewWithTag("txtPassenger" + Integer.toString(currentPosition) + "Tag");

        title.setText("");
        firstName.setText("");
        lastname.setText("");
        dob.setText("");
        issuingCountry.setText("");
        enrich.setText("");
        passengerTag.setText("");
        gender.setText("");
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
            }
            else{
                croutonAlert(getActivity(), "One infant per adult only");
                dismissLoading();
            }
        }
    }

    /*Validate if many infant assign to one adult - return error*/
    public boolean manualValidation() {
        boolean manualValidationStatus = true;
        int totalPassenger = Integer.parseInt(adult) + Integer.parseInt(infant) + 1;
        ArrayList<String> passengerArray = new ArrayList<String>();
        for (int adultInc = totalPassenger-Integer.parseInt(adult); adultInc < totalPassenger; adultInc++) {
            TextView btnTravellingWith = (TextView) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_travelling_with");
            String passengerID = btnTravellingWith.getText().toString();
            passengerArray.add(passengerID);
            Log.e("passengerID1",passengerID);
        }

        //check duplicate
        List<String> usedNames = new ArrayList<String>();
        for(int x = 0 ; x < passengerArray.size() ;x++){

            if (usedNames.contains(passengerArray.get(x))){
                Log.e("passengerID2",passengerArray.get(x));
                manualValidationStatus = false;
            }else {
                usedNames.add(passengerArray.get(x));
                Log.e("passengerID3",passengerArray.get(x));
            }
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

            //Gson gsonFlight = new Gson();
            //String seat = gsonFlight.toJson(obj);
            //pref.setSeat(seat);

            //save into realm object
            FamilyFriendObj thisObj = new FamilyFriendObj();
            thisObj.setFamily_and_friend(obj.getFamily_and_friend());

            RealmObjectController.saveFamilyFriends(getActivity(),thisObj);

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

        if(month < 9) {
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
            friendAndFamilyLayout.setVisibility(View.VISIBLE);

            Gson gson = new Gson();
            String userInfo = getUserInfoCached(getActivity());
            loginObj = gson.fromJson(userInfo, LoginReceive.UserInfo.class);

            //modify date//
            String dob = reformatDOB(loginObj.getDOB());
            String[] splitDate = dob.split("/");

            datePickerYear1 = DatePickerDialog.newInstance(this, Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1])-1, Integer.parseInt(splitDate[0]));
            datePickerYear1.setYearRange(year - 100, year);

            txtTitle.setText(loginObj.getContact_title());
            TextView passenger1Title = (TextView) view.findViewWithTag("passenger1_title");
            TextView passenger1FirstName = (TextView) view.findViewWithTag("passenger1_first_name");
            TextView passenger1LastName = (TextView) view.findViewWithTag("passenger1_last_name");
            TextView passenger1Dob = (TextView) view.findViewWithTag("passenger1_dob");
            TextView passenger1IssuingCountry = (TextView) view.findViewWithTag("passenger1_issuing_country");

            passenger1IssuingCountry.setText(getCountryName(getActivity(),loginObj.getContact_country()));
            passenger1Title.setText(getTitleCode(getActivity(), loginObj.getContact_title(), "name"));
            passenger1FirstName.setText(loginObj.getContact_first_name());
            passenger1LastName.setText(loginObj.getContact_last_name());
            passenger1Dob.setText(reformatDOB(loginObj.getDOB()));

        }else{
            //memberLoginBlock.setVisibility(View.VISIBLE);
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(result.size() > 0){
            Gson gson = new Gson();
            PassengerInfoReveice obj = gson.fromJson(result.get(0).getCachedResult(), PassengerInfoReveice.class);
            onPassengerInfo(obj);
        }

        //check if ff available or not.. if not disable
        RealmResults<FamilyFriendList> cachedListResult = RealmObjectController.getFamilyFriends(getActivity());
        if(cachedListResult.size() > 0){
            Gson gson = new Gson();
            FamilyFriendObj obj = gson.fromJson(cachedListResult.get(0).getCachedList(), FamilyFriendObj.class);
            friendAndFamilyObj = obj.getFamily_and_friend();

            boolean infantAvailable = false;
            boolean adultAvailable = false;

            //check if adult/infant available
            for(int g = 0 ; g < friendAndFamilyObj.size() ; g++){
                if(friendAndFamilyObj.get(g).getPassenger_type().equals("Infant")){
                    infantAvailable = true;
                }else{
                    adultAvailable = true;
                }
            }
            for (adultInc = 1; adultInc < totalPassenger; adultInc++) {
                final LinearLayout linearCheckFF = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_asFF_block");
                final LinearLayout linearSaveAsFF = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_saveFF_block");

                if(friendAndFamilyObj.size() <= 0){
                    linearCheckFF.setVisibility(View.GONE);
                    linearSaveAsFF.setVisibility(View.GONE);
                    Log.e("HIDE","TRUE");
                }else{

                    //check what passenger type available
                    TextView txtPassengerType = (TextView) view.findViewWithTag("txtPassenger" + adultInc);
                    String[] splitPassengerType = txtPassengerType.getText().toString().split(" ");
                    if(splitPassengerType[0].equals("Infant")){
                        //check infant
                        if(infantAvailable){
                            linearCheckFF.setVisibility(View.VISIBLE);
                        }else{
                            linearCheckFF.setVisibility(View.GONE);
                        }
                    }else{
                        //check adult
                        if(adultAvailable){
                            linearCheckFF.setVisibility(View.VISIBLE);
                        }else{
                            linearCheckFF.setVisibility(View.GONE);
                        }
                    }

                    linearSaveAsFF.setVisibility(View.VISIBLE);

                }
            }

        }else{
            for (adultInc = 1; adultInc < totalPassenger; adultInc++) {
                final LinearLayout linearCheckFF = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_asFF_block");
                final LinearLayout linearSaveAsFF = (LinearLayout) view.findViewWithTag("passenger" + Integer.toString(adultInc) + "_saveFF_block");

                linearCheckFF.setVisibility(View.GONE);
                linearSaveAsFF.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}

