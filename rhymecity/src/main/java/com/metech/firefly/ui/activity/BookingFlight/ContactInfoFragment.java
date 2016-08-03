package com.metech.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
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
import com.metech.firefly.api.obj.ContactInfoReceive;
import com.metech.firefly.api.obj.LoginReceive;
import com.metech.firefly.api.obj.PassengerInfoReveice;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.metech.firefly.ui.activity.Picker.StateListDialogFragment;
import com.metech.firefly.ui.module.ContactInfoModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.ContactInfo;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.object.PassengerMeal;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.DropMenuAdapter;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.metech.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
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
    TextView txtCountry;

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
    @Length(min = 7,max = 14, message = "Invalid phone number")
    @Order(10)
    @InjectView(R.id.txtPhone)
    TextView txtPhone;

    @NotEmpty
    @Length(min = 7,max = 14, message = "Invalid phone number")
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

    @InjectView(R.id.checkAsPassenger)
    CheckBox checkAsPassenger;

    @InjectView(R.id.txtCountryBusiness)
    TextView txtCountryBusiness;

    @InjectView(R.id.countryBlock)
    LinearLayout countryBlock;

    @InjectView(R.id.insuranceCheckBoxLayout)
    LinearLayout insuranceCheckBoxLayout;

    @InjectView(R.id.wantToBeProtected)
    LinearLayout wantToBeProtected;

    @InjectView(R.id.wantToBeProtectedBtn)
    Button wantToBeProtectedBtn;

    @InjectView(R.id.contactInfoScrollView)
    ScrollView contactInfoScrollView;

    @InjectView(R.id.passengerSSRMealList)
    LinearLayout passengerSSRMealList;

    @InjectView(R.id.specialMealLayout)
    LinearLayout specialMealLayout;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Personal Details(Contact Details)";
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
    private String dialingCode;
    private String selectedState;
    private Validator mValidator;
    private String insuranceTxt1,insuranceTxt2,insuranceTxt3,insuranceTxt4;
    private boolean withSeat = false;
    private View view;
    private LoginReceive.UserInfo loginObj;
    private DefaultPassengerObj defaultPassengerObj;
    private ArrayList<DefaultPassengerObj> defaultObj = new ArrayList<DefaultPassengerObj>();
    private ArrayList<DropDownItem> passengerList = new ArrayList<DropDownItem>();
    private int index = -1;
    private AlertDialog dialog;
    private String insuranceStatus = "N";
    private PassengerInfoReveice obj;
    private ArrayList<DropDownItem> departMealList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> returnMealList = new ArrayList<DropDownItem>();
    private String flightType;

    private String ssrOffer1,ssrOffer2;

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

        Bundle xxx = getActivity().getIntent().getExtras();
        String insurance = bundle.getString("INSURANCE_STATUS");

        //HashMap<String, String> init = pref.getSeat();
        //String seatHash = init.get(SharedPrefManager.SEAT);

        //String defaultPassenger = "";
        try {
             defaultObj = getActivity().getIntent().getParcelableArrayListExtra("DEFAULT_PASSENGER_INFO");
        }catch (Exception e){
        }

        changeContactInfoBtn.setVisibility(View.GONE);
        Gson gson = new Gson();

        obj= gson.fromJson(insurance, PassengerInfoReveice.class);

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
            txtCountry.setText(getCountryName(getActivity(), loginObj.getContact_country()));

            //set state
            setState(selectedCountryCode);

            txtCountryBusiness.setTag(loginObj.getContact_country());
            txtCountryBusiness.setText(getCountryName(getActivity(), loginObj.getContact_country()));

            txtPostCode.setText(loginObj.getContact_postcode());
            dialingCode = getDialingCode(loginObj.getContact_country(),getActivity());
        }
        /* ---------------------------------------------------------- */

        checkAsPassenger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    if(defaultObj.size() > 1){
                        ArrayList<DropDownItem> passengerList = new ArrayList<DropDownItem>();

                        for(int i = 0;i<defaultObj.size(); i++)
                        {
                            DropDownItem itemPurpose = new DropDownItem();
                            itemPurpose.setText(defaultObj.get(i).getTitle()+" "+defaultObj.get(i).getFirstname());
                            itemPurpose.setCode(Integer.toString(i));
                            passengerList.add(itemPurpose);
                        }
                        //popupSelection(passengerList, getActivity());
                        customPopupForContactInfo(passengerList, getActivity());

                    }else{
                        txtTitle.setText(getTitleCode(getActivity(), defaultObj.get(0).getTitle(), "name"));
                        txtTitle.setTag(defaultObj.get(0).getTitle());
                        txtFirstName.setText(defaultObj.get(0).getFirstname());
                        txtLastName.setText(defaultObj.get(0).getLastname());
                        txtCountry.setTag(defaultObj.get(0).getIssuingCountry());
                        txtCountry.setText(getCountryName(getActivity(), defaultObj.get(0).getIssuingCountry()));
                        selectedCountryCode = defaultObj.get(0).getIssuingCountry();
                        txtCountryBusiness.setTag(defaultObj.get(0).getIssuingCountry());
                        txtCountryBusiness.setText(getCountryName(getActivity(), defaultObj.get(0).getIssuingCountry()));
                        dialingCode = getDialingCode(defaultObj.get(0).getIssuingCountry(),getActivity());
                        setState(selectedCountryCode);

                    }
                }else{
                    txtTitle.setText("");
                    txtTitle.setTag("");
                    txtFirstName.setText("");
                    txtLastName.setText("");
                    txtCountry.setTag("");
                    txtCountry.setText("");

                    txtCountryBusiness.setTag("");
                    txtCountryBusiness.setText("");
                }
            }
        });

        try {
            insuranceStatus = obj.getInsuranceObj().getStatus();
            if(insuranceStatus != null){
                if(insuranceStatus.equals("Y")) {
                    insuranceBlock.setVisibility(View.VISIBLE);

                    insuranceTxt1 = obj.getInsuranceObj().getHtml().get(0).toString();
                    insuranceTxt2 = obj.getInsuranceObj().getHtml().get(1).toString();
                    insuranceTxt3 = obj.getInsuranceObj().getHtml().get(2).toString();
                    insuranceTxt4 = obj.getInsuranceObj().getHtml().get(3).toString();

                    setInsuranceText();
                }
            }
        }catch(Exception e){}

        /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

         /*Get Data From BaseFragment*/
        purposeList = getPurpose(getActivity());
        titleList = getStaticTitle(getActivity());
        countrysList = getStaticCountry(getActivity());

        txtPurpose.setText(purposeList.get(0).getText());
        txtPurpose.setTag(purposeList.get(0).getCode());

        /* -------------------------- Select Country --------------------------------*/
        /*txtCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Utils.hideKeyboard(getActivity(),view);
                    showCountrySelector(getActivity(), countrysList);
                }
            }
        });*/

        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(),view);
                showCountrySelector(getActivity(), countrysList,"country");

            }
        });


        txtCountryBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(), view);
                showCountrySelector(getActivity(),countrysList,"country");

            }
        });
        /* ---------------------------- End Country ----------------------------------*/

        /* ---------------------------- Select State -------------------------------- */
        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity(),view);
                showCountrySelector(getActivity(), stateList,"state");

            }
        });
        /* ---------------------------- End Select State -------------------------------- */

        /* --------------------------- Select Purpose ----------------------------------- */
        txtPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.e(purposeList.get(0).getCode().toString(),purposeList.get(1).getCode().toString());
                popupSelectionExtra(purposeList, getActivity(), txtPurpose, true, companyBlock, "2", countryBlock);
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
        HashMap<String, String> initFlightType = pref.getFlightType();
        flightType = initFlightType.get(SharedPrefManager.FLIGHT_TYPE);

        if(flightType.equals("MH")) {
            btnSeatSelection.setVisibility(View.GONE);
            btnWithoutSeatSelection.setText("Continue");
        }

        if (obj.getSsr_status().equals("Y")) {
               displaySSRMeal();
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

         /*Onclick Continue*/
        wantToBeProtectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtInsurance1.setVisibility(View.VISIBLE);
                txtInsurance2.setVisibility(View.VISIBLE);
                txtInsurance3.setVisibility(View.VISIBLE);
                txtInsurance4.setVisibility(View.VISIBLE);
                insuranceCheckBoxLayout.setVisibility(View.VISIBLE);

                setInsuranceText();
                wantToBeProtected.setVisibility(View.GONE);
            }
        });


        contactInfoScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
               // view.requestFocus();
            }
        });

        return view;
    }

    public void mealSSR(){

		/*Depart Meal*/

        for (int i = 0; i < obj.getMeal().get(0).getList_meal().size(); i++)
        {
            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(obj.getMeal().get(0).getList_meal().get(i).getName());
            itemCountry.setCode(obj.getMeal().get(0).getList_meal().get(i).getMeal_code());
            itemCountry.setTag("Meal");
            itemCountry.setId(i);
            departMealList.add(itemCountry);
        }

        /*Return Meal*/
        if(obj.getMeal().size() > 1){
            for (int i = 0; i < obj.getMeal().get(1).getList_meal().size(); i++)
            {
                DropDownItem itemCountry = new DropDownItem();
                itemCountry.setText(obj.getMeal().get(1).getList_meal().get(i).getName());
                itemCountry.setCode(obj.getMeal().get(1).getList_meal().get(i).getMeal_code());
                itemCountry.setTag("Meal");
                itemCountry.setId(i);
                returnMealList.add(itemCountry);
            }
        }
    }

    public String getMealCode(String mealName,String type){
        String mealCode = "";

        if(type.equals("Depart")){
            for (int i = 0; i < obj.getMeal().get(0).getList_meal().size(); i++)
            {
                if(mealName.equals(obj.getMeal().get(0).getList_meal().get(i).getName())){
                    mealCode = obj.getMeal().get(0).getList_meal().get(i).getMeal_code();
                    break;
                }
            }
        }else{
            for (int i = 0; i < obj.getMeal().get(1).getList_meal().size(); i++)
            {
                if(mealName.equals(obj.getMeal().get(1).getList_meal().get(i).getName())){
                    mealCode = obj.getMeal().get(1).getList_meal().get(i).getMeal_code();
                    break;
                }
            }
        }

        return mealCode;
    }

    public void displaySSRMeal(){

           /*Booking Id*/
        HashMap<String, String> SS1 = pref.getSSR1();
        ssrOffer1 = SS1.get(SharedPrefManager.OFFERSSR1);

           /*Booking Id*/
        HashMap<String, String> SS2 = pref.getSSR2();
        ssrOffer2 = SS2.get(SharedPrefManager.OFFERSSR2);

        if(ssrOffer1.equals("Y") || ssrOffer2.equals("Y")){

            if(obj.getMeal().size() > 0){

                specialMealLayout.setVisibility(View.VISIBLE);
                mealSSR();

                //Services & Fee
                LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.4f);
                LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
                LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

                int departLoop = 1;
                int returnLoop = 1;

                for(int services = 0 ; services < obj.getMeal().size() ; services++){

                    boolean proceed = false;

                    if(ssrOffer1.equals("Y") && services == 0){
                        proceed = true;
                    }

                    if(ssrOffer2.equals("Y") && services == 1){
                        proceed = true;
                    }

                    if(proceed){

                        String ssrFlightDestination = obj.getMeal().get(services).getDestination_name();
                        TextView txtFlightType = new TextView(getActivity());
                        txtFlightType.setText(ssrFlightDestination);
                        txtFlightType.setTypeface(null, Typeface.BOLD);
                        txtFlightType.setGravity(Gravity.LEFT);
                        txtFlightType.setPadding(0, 10, 0, 10);

                        passengerSSRMealList.addView(txtFlightType);

                        for(int servicesLoop = 0 ; servicesLoop < obj.getMeal().get(services).getPassenger().size() ; servicesLoop++){

                            LinearLayout servicesRow = new LinearLayout(getActivity());
                            servicesRow.setOrientation(LinearLayout.VERTICAL);
                            servicesRow.setPadding(2, 2, 2, 2);
                            servicesRow.setWeightSum(1);
                            servicesRow.setLayoutParams(matchParent);
                            servicesRow.setBackgroundResource(R.drawable.drawable_login_bottom_border);

                            String passengerName = obj.getMeal().get(services).getPassenger().get(servicesLoop).getPassenger_name();

                            TextView txtName = new TextView(getActivity());
                            txtName.setTypeface(null, Typeface.BOLD);
                            txtName.setText(passengerName);
                            txtName.setPadding(0, 5, 0, 5);

                            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            llp.setMargins(0, 5, 0, 5); // llp.setMargins(left, top, right, bottom);

                            final TextView txtMealList = new TextView(getActivity());
                            txtMealList.setBackgroundResource(R.drawable.block_with_border);
                            txtMealList.setText("Default Meal");
                            txtMealList.setPadding(7, 7, 7, 7);
                            txtMealList.setLayoutParams(llp);
                            //onclick meal
                            if(services == 0){
                                txtMealList.setTag("passenger_depart"+Integer.toString(departLoop));
                                txtMealList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupSelection(departMealList, getActivity(), txtMealList, false, view);
                                    }
                                });

                                departLoop++;

                            }else{
                                txtMealList.setTag("passenger_return"+Integer.toString(returnLoop));

                                txtMealList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        popupSelection(returnMealList, getActivity(), txtMealList, false, view);
                                    }
                                });

                                returnLoop++;
                            }


                            servicesRow.addView(txtName);
                            servicesRow.addView(txtMealList);
                            servicesRow.setPadding(0, 10, 0, 20);

                /*if(servicesLoop == obj.getMeal().get(services).getPassenger().size() - 1){
                    //margin bottom
                    servicesRow.setPadding(0, 0, 0, 25);
                }else{
                    servicesRow.setPadding(0, 10, 0, 10);
                }*/

                            passengerSSRMealList.addView(servicesRow);
                        }
                    }

                }
            }
            }
    }

    public void setInsuranceText(){

        txtInsurance1.setText(Html.fromHtml(insuranceTxt1));
        txtInsurance2.setMovementMethod(LinkMovementMethod.getInstance());
        txtInsurance2.setText(Html.fromHtml(insuranceTxt2.replaceAll("</br>", "<p>")), TextView.BufferType.SPANNABLE);
        txtInsurance3.setText(Html.fromHtml(insuranceTxt3),TextView.BufferType.SPANNABLE);

        String insurance3 = txtInsurance3.getText().toString();
        int i1 = insurance3.indexOf("[R");
        int i2 = insurance3.indexOf("e]");

        Spannable mySpannable = (Spannable)txtInsurance3.getText();
        ClickableSpan myClickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(View widget) {
                    /* do something */
                removeInsurance();
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //

        txtInsurance3.setMovementMethod(LinkMovementMethod.getInstance());

        txtInsurance4.setText(Html.fromHtml(insuranceTxt4));

        //txtInsuranceDetail.setText(Html.fromHtml("<html><b>Be sure to protect yourself with Firefly Travel Protection!</b></br><p></p></br>You got a good deal on our promo fares - but don't risk unexpected expenses!</br><p></p></br>>>Comprehensive coverage at phenomenal rates</br> <p></p>>>Added flexibility via the Trip Cancellation benefit if you are unable to proceed with your travels</br><p></p>>>Medical Coverage includes hospital admission and emergency medical evacuation*</br><p></p>>>24 Hour Worldwide Travel Assistance by our travel partner, AIG Travel</br><p></p><p></p></br>* For the full list of benefits, please refer to the <a href='https://www.aig.my/Chartis/internet/Malaysia/English/Firefly%20Travel%20Protection%20Product%20Disclosure%20Sheet_tcm4009-671123.pdf' target='_blank'>Terms and Conditions</a></br><p></p></br><b>The following passenger(s) are eligible for travel insurance:</b></br><p></p><li>Ggjji Gghjj</li><p></p></br><b>Firefly Travel Protection's Promo Plan is only 17.00 MYR MYR (inclusive of GST, when applicable)</b></br><p></p></br></html>"));
        // txtInsuranceDetail.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /*PopupActivity Forgot Password*/
    public void removeInsurance(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.remove_insurance_opt, null);
        Button confirmRemove = (Button)myView.findViewById(R.id.confirmRemove);
        Button continueRemove = (Button)myView.findViewById(R.id.continueInsurance);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        confirmRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInsurance1.setText("Firefly Travel Protection protects you against unexpected events during your trip. From unfortunate accidents to lost travel documents, Firefly Travel Protection will take care of you (details).");
                txtInsurance2.setVisibility(View.GONE);
                txtInsurance3.setVisibility(View.GONE);
                txtInsurance4.setVisibility(View.GONE);
                wantToBeProtected.setVisibility(View.VISIBLE);
                insuranceStatus = "N";
                insuranceCheckBoxLayout.setVisibility(View.GONE);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        continueRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insuranceCheckBoxLayout.setVisibility(View.GONE);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

    }

     /*Global PoPup*/
     public void customPopupForContactInfo(final ArrayList array,Activity act){


            final ArrayList<DropDownItem> a = array;
            DropMenuAdapter dropState = new DropMenuAdapter(act);
            dropState.setItems(a);

            AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

            alertStateCode.setSingleChoiceItems(dropState, index, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String selectedCode = a.get(which).getCode();

                    txtTitle.setText(getTitleCode(getActivity(), defaultObj.get(Integer.parseInt(selectedCode)).getTitle(), "name"));
                    txtTitle.setTag(defaultObj.get(Integer.parseInt(selectedCode)).getTitle());
                    txtFirstName.setText(defaultObj.get(Integer.parseInt(selectedCode)).getFirstname());
                    txtLastName.setText(defaultObj.get(Integer.parseInt(selectedCode)).getLastname());
                    txtCountry.setTag(defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry());
                    txtCountry.setText(getCountryName(getActivity(), defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry()));
                    txtCountryBusiness.setTag(defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry());
                    txtCountryBusiness.setText(getCountryName(getActivity(), defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry()));
                    selectedCountryCode = defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry();
                    dialingCode = getDialingCode(defaultObj.get(Integer.parseInt(selectedCode)).getIssuingCountry(), getActivity());
                    setState(selectedCountryCode);

                    index = which;
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

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam,String data)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                if(data.equals("state")){
                    StateListDialogFragment countryListDialogFragment = StateListDialogFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(ContactInfoFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }else{
                    CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(ContactInfoFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }

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
                    //txtPhone.setText(dialingCode);
                    //txtAlternatePhone.setText(dialingCode);

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
    public void onContactInfo(ContactInfoReceive obj){
        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
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

        //checkDialingCode
        boolean cont = true;
        if(!txtPhone.getText().toString().equals("")){
            if(validateDialingCode(dialingCode, txtPhone.getText().toString())) {
                txtPhone.setError("Mobile phone must start with country code.");
                setShake(txtPhone);
                cont = false;
            }
        }
        if(!txtAlternatePhone.getText().toString().equals("")) {
            if (validateDialingCode(dialingCode, txtAlternatePhone.getText().toString())) {
                txtAlternatePhone.setError("Alternate phone must start with country code.");
                setShake(txtAlternatePhone);
                cont = false;
            }
        }

        if(cont){
            if(insuranceStatus.equals("Y") && !insuranceCheckBox.isChecked()){
                setAlertDialog(getActivity(),"To proceed, you need to agree with the Insurance Declaration.","Insurance Declaration");
                cont = false;
            }else{
                requestContacInfo();
            }
        }

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
               // croutonAlert(getActivity(), splitErrorMsg[0]);
            }

            if(firstView){
                view.requestFocus();
            }
            firstView = false;
        }

    }

    public void requestContacInfo(){

        String seatSelectionStatus;

        initiateLoading(getActivity());
        ContactInfo contactObj = new ContactInfo();

        if(withSeat){
            seatSelectionStatus = "Y";
        }else{
            seatSelectionStatus = "N";
        }
        HashMap<String, String> initFlightType = pref.getFlightType();
        String flightType = initFlightType.get(SharedPrefManager.FLIGHT_TYPE);

        HashMap<String, String> initCustomerNumber = pref.getCustomerNumber();
        String customerNumber = initCustomerNumber.get(SharedPrefManager.CUSTOMER_NUMBER);

        contactObj.setBooking_id(bookingID);
        if(customerNumber == null){
            customerNumber = "";
        }
        contactObj.setCustomer_number(customerNumber);
        contactObj.setFlight_type(flightType);
        contactObj.setSeat_selection_status(seatSelectionStatus);
        contactObj.setSignature(signature);
        contactObj.setContact_travel_purpose(txtPurpose.getTag().toString());
        contactObj.setContact_title(txtTitle.getTag().toString());
        contactObj.setContact_first_name(txtFirstName.getText().toString());
        contactObj.setContact_last_name(txtLastName.getText().toString());
        contactObj.setContact_email(txtEmailAddress.getText().toString());

        //getMealCode
        if(flightType.equals("MH") && obj.getSsr_status().equals("Y")){


            //if meal available

            ArrayList<PassengerMeal> departMeal = new ArrayList<PassengerMeal>();

            if(ssrOffer1.equals("Y")) {

                for (int y = 0; y < obj.getMeal().get(0).getPassenger().size(); y++) {

                    TextView mealCodePerPassenger = (TextView) view.findViewWithTag("passenger_depart" + Integer.toString(y + 1));
                    String departMealCode = getMealCode(mealCodePerPassenger.getText().toString(), "Depart");

                    PassengerMeal passengerMeal = new PassengerMeal();
                    passengerMeal.setPassenger_number(Integer.toString(y));
                    passengerMeal.setMeal_code(departMealCode);

                    departMeal.add(passengerMeal);

                }
            }

            contactObj.setGoing_flightMeal(departMeal);

            ArrayList<PassengerMeal> returnMeal = new ArrayList<PassengerMeal>();


                if(obj.getMeal().size() > 1){
                    if(ssrOffer2.equals("Y")) {
                    for(int x = 0; x < obj.getMeal().get(1).getPassenger().size(); x++){

                        //getMealCode
                        TextView mealCodePerPassenger2 = (TextView) view.findViewWithTag("passenger_return" + Integer.toString(x + 1));
                        String returnMealCode = getMealCode(mealCodePerPassenger2.getText().toString(),"Return");

                        PassengerMeal passengerMeal = new PassengerMeal();
                        passengerMeal.setPassenger_number(Integer.toString(x));
                        passengerMeal.setMeal_code(returnMealCode);

                        returnMeal.add(passengerMeal);

                    }

                }
            }

            contactObj.setReturn_flightMeal(returnMeal);

        }


        /*Exception*/
        if(txtPurpose.getTag().toString().equals("2")){
            contactObj.setContact_state(txtState.getTag().toString());
            contactObj.setContact_city(txtCity.getText().toString());
            contactObj.setContact_postcode(txtPostCode.getText().toString());

            contactObj.setContact_company_name(txtCompanyName.getText().toString());
            contactObj.setContact_address1(txtCompanyAddress1.getText().toString());
            contactObj.setContact_address2(txtCompanyAddress2.getText().toString());
            contactObj.setContact_address3(txtCompanyAddress3.getText().toString());
        }

        if(insuranceCheckBox.isChecked()){
            contactObj.setInsurance("1");
        }else{
            contactObj.setInsurance("0");
        }
        contactObj.setContact_country(selectedCountryCode);
        contactObj.setContact_mobile_phone(txtPhone.getText().toString());
        contactObj.setContact_alternate_phone(txtAlternatePhone.getText().toString());

        presenter.contactInfo(contactObj);

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
            ContactInfoReceive obj = gson.fromJson(result.get(0).getCachedResult(), ContactInfoReceive.class);
            onContactInfo(obj);
        }else{
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
