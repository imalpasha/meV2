package com.fly.firefly.ui.activity.UpdateProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.module.UpdateProfileModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.fly.firefly.ui.presenter.UpdateProfilePresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;

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
import de.keyboardsurfer.android.widget.crouton.Style;

public class UpdateProfileFragment extends BaseFragment implements
        DatePickerDialog.OnDateSetListener,UpdateProfilePresenter.UpdateProfileView,Validator.ValidationListener  {

    // Validator Attributes
    private Validator mValidator;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> state;
    private DatePickerObj date;
    private ArrayList<DropDownItem> titleList;
    private String selectedState;
    private String selectedCountryCode;
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private int fragmentContainerId;
    public static final String DATEPICKER_TAG = "datepicker";
    private String fullDate;
    private static final String SCREEN_LABEL = "Update Profile";
    DropDownItem selectedCountry,selectTitle_code;
    private View view;
    private LoginReceive.UserInfo loginObj;
    private Boolean validateStatus;

    @Inject UpdateProfilePresenter presenter;

    @InjectView(R.id.txtUsername)
    TextView txtUsername;

    @Order(1)@Optional
    @InjectView(R.id.txtCurrentPassword)
    EditText txtCurrentPassword;

    @Order(2)@Optional
    @InjectView(R.id.txtNewPassword)
    EditText txtNewPassword;

    @Order(3)@Optional
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Order(4)@NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @Order(5)@NotEmpty
    @InjectView(R.id.txtFirstName)
    EditText txtFirstName;

    @Order(6)
    @InjectView(R.id.txtLastName)
    EditText txtLastName;

    @Order(7)@NotEmpty
    @InjectView(R.id.txtAddressLine1)
    EditText txtAddressLine1;

    @Order(16)@Optional
    @InjectView(R.id.txtAddressLine2)
    EditText txtAddressLine2;

    @Order(17)@Optional
    @InjectView(R.id.txtAddressLine3)
    EditText txtAddressLine3;

    @Order(18) @NotEmpty
    @InjectView(R.id.txtCountry)
    TextView txtCountry;

    @Order(9)@NotEmpty
    @InjectView(R.id.txtCity)
    EditText txtCity;

    @Order(10) @NotEmpty
    @InjectView(R.id.txtState)
    TextView txtState;

    @Order(11)@NotEmpty
    @Length(sequence = 1, min = 5,max = 7, message = "Invalid postcode")
    @InjectView(R.id.txtPostCode)
    EditText txtPostCode;

    @Order(12)@NotEmpty
    @Length(sequence = 1, min = 7,max = 14, message = "Invalid phone number")
    @InjectView(R.id.txtMobilePhone)
    EditText txtMobilePhone;

    @InjectView(R.id.txtAltPhone)
    EditText txtAltPhone;

    @InjectView(R.id.txtFax)
    EditText txtFax;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtDOB)
    TextView txtDOB;

    @Order(16)
    @InjectView(R.id.checkBox2)
    CheckBox checkBox2;

    //@Order(17)
    //@Checked(message = "You must agree with term & condition")
    //@InjectView(R.id.chkTNC)
    //CheckBox chkTNC;

    @InjectView(R.id.txtBonusLink)
    EditText txtBonusLink;

    @InjectView(R.id.updateProfileBtn)
    Button updateProfileBtn;


    public static UpdateProfileFragment newInstance() {

        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new UpdateProfileModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.update_profile, container, false);
        ButterKnife.inject(this, view);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final int year = calendar.get(Calendar.YEAR);
        datePickerDialog.setYearRange(year-100,year);

        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        state = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();

        ///password
        txtCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());

        titleList = getStaticTitle(getActivity());
        countrys = getStaticCountry(getActivity());

        HashMap<String, String> init2 = pref.getNewsletterStatus();
        String newsletter = init2.get(SharedPrefManager.ISNEWSLETTER);
        if (newsletter.equals("Y")) {
            checkBox2.setChecked(true);
        }else{
            checkBox2.setChecked(false);
        }

        /*Display Existing User Data*/
        JSONObject jsonUserInfo = getUserInfo(getActivity());
        String userInfo = getUserInfoCached(getActivity());
        //Gson gson = new Gson();
        loginObj = new Gson().fromJson(userInfo, LoginReceive.UserInfo.class);

        String email = loginObj.getContact_email();
        String title = loginObj.getContact_title();
        String first_name = loginObj.getContact_first_name();
        String last_name = loginObj.getContact_last_name();
        String addressline1 = loginObj.getContact_address1();
        String addressline2 = loginObj.getContact_address2();
        String addressline3 = loginObj.getContact_address3();
        String countryCode = loginObj.getContact_country();
        String stateCode = loginObj.getContact_state();
        String city = loginObj.getContact_city();
        String postcode = loginObj.getContact_postcode();
        String dob = loginObj.getDOB();
        String mobile_phone = loginObj.getContact_mobile_phone();
        String alternate_phone = loginObj.getContact_alternate_phone();
        String fax = loginObj.getContact_fax();
        String bonuslink = loginObj.getBonuslink();

        txtUsername.setText(email);
        txtFirstName.setText(first_name, TextView.BufferType.EDITABLE);
        txtLastName.setText(last_name, TextView.BufferType.EDITABLE);
        txtAddressLine1.setText(addressline1, TextView.BufferType.EDITABLE);
        txtAddressLine2.setText(addressline2, TextView.BufferType.EDITABLE);
        txtAddressLine3.setText(addressline3, TextView.BufferType.EDITABLE);
        txtMobilePhone.setText(mobile_phone, TextView.BufferType.EDITABLE);
        txtAltPhone.setText(alternate_phone, TextView.BufferType.EDITABLE);
        txtPostCode.setText(postcode, TextView.BufferType.EDITABLE);
        txtCity.setText(city, TextView.BufferType.EDITABLE);
        txtFax.setText(fax, TextView.BufferType.EDITABLE);
        txtCurrentPassword.setText("",TextView.BufferType.EDITABLE);
        txtNewPassword.setText("", TextView.BufferType.EDITABLE);
        txtBonusLink.setText(bonuslink);
        //Log.e("bonuslink",bonuslink);
        //Reformat datepicker
        String dobAlphabet = reformatDOB(dob);
        txtDOB.setText(dobAlphabet);
        fullDate = dob;

        txtCountry.setText(getCountryName(getActivity(),countryCode));
        txtCountry.setTag(countryCode);
        Log.e("Title", title);
        txtTitle.setText(getTitleCode(getActivity(), title, "name"));
        txtTitle.setTag(title);
        txtState.setText(getStateName(getActivity(), stateCode));
        txtState.setTag(stateCode);
        setState(countryCode);

        /*Select dropdown list*/
        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(),countrys);
            }
        });

        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), state);
            }
        });

        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Ok");
                popupSelection(titleList, getActivity(),txtTitle,true,view);
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnalyticsApplication.sendEvent("Click", "btnUpdateProfile");
                Log.e("Clicked", "Ok");

                //Multiple Manual Validation - Library Problem (failed to validate optional field)
                resetManualValidationStatus();
                manualValidation(txtBonusLink, "bonuslink");
                manualValidation(txtAltPhone, "phoneNumber");
                manualValidation(txtFax,"phoneNumber");
                validateStatus = getManualValidationStatus();

                mValidator.validate();

            }
        });

        return view;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(UpdateProfileFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(UpdateProfileFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);

                if (selectedCountry.getTag() == "Country") {
                    txtCountry.setText(selectedCountry.getText());

                    String toCountryCode =  selectedCountry.getCode();
                    String[] splitCountryCode = toCountryCode.split("/");
                    selectedCountryCode = splitCountryCode[0];
                    setState(selectedCountryCode);

                } else {
                    txtState.setText(selectedCountry.getText());
                    selectedState = selectedCountry.getCode();

                }
            }
        }
    }


    public void setState(String selectedCode){

        /*Each country click - reset state obj*/
        state = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
        JSONArray jsonState = getState(getActivity());
        for(int x = 0 ; x < jsonState.length() ; x++) {

            JSONObject row = (JSONObject) jsonState.opt(x);
            if(selectedCode.equals(row.optString("country_code"))) {
                DropDownItem itemCountry = new DropDownItem();
                itemCountry.setText(row.optString("state_name"));
                itemCountry.setCode(row.optString("state_code"));
                itemCountry.setTag("State");
                state.add(itemCountry);
            }
        }
    }



    public void requestUpdateProfile() {


        initiateLoading(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);
        String newsletter;

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        UpdateProfileRequest data = new UpdateProfileRequest();

        String currentPassword= AESCBC.encrypt(App.KEY, App.IV, txtCurrentPassword.getText().toString());
        String newPassword = AESCBC.encrypt(App.KEY, App.IV, txtNewPassword.getText().toString());
        data.setUsername(txtUsername.getText().toString());
        data.setFirst_name(txtFirstName.getText().toString());
        data.setLast_name(txtLastName.getText().toString());
        data.setAddress_1(txtAddressLine1.getText().toString());
        data.setAddress_2(txtAddressLine2.getText().toString());
        data.setAddress_3(txtAddressLine3.getText().toString());
        data.setMobile_phone(txtMobilePhone.getText().toString());
        data.setAlternate_phone(txtAltPhone.getText().toString());
        data.setCity(txtCity.getText().toString());
        data.setPostcode(txtPostCode.getText().toString());
        data.setFax(txtFax.getText().toString());
        data.setSignature(signatureFromLocal);
        data.setBonuslink(txtBonusLink.getText().toString());

        //Post newsletter
        if (checkBox2.isChecked()) {
            pref.setNewsletterStatus("Y");
            newsletter = "Y";
        }else{
            pref.setNewsletterStatus("N");
            newsletter = "N";
        }
        data.setNewsletter(newsletter);

        //currentpassword
        if (txtCurrentPassword.getText().toString().equals("")) {
            data.setPassword("");
        }else{
            data.setPassword(currentPassword);
        }

        //Post new password
        if (txtNewPassword.getText().toString().equals("")) {
            data.setNew_password("");
        }else{
            data.setNew_password(newPassword);
        }

        if(!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
            dismissLoading();
            croutonAlert(getActivity(), "Password don't match.");
        }

        //Post title
            if (txtTitle.getTag() == null ) {
                data.setTitle(jsonUserInfo.optString("title"));
            }else{
              data.setTitle(txtTitle.getTag().toString());
        }

        //Post country
        if (selectedCountryCode == null) {
            data.setCountry(jsonUserInfo.optString("contact_country"));
        }else {
            data.setCountry(selectedCountryCode);
        }

        //Post state
        if (selectedState ==null) {
            data.setState(jsonUserInfo.optString("contact_state"));
        }else {
            data.setState(selectedState);
        }

        data.setDob(fullDate);

        presenter.onUpdateProfile(data);


    }

    @Override
    public void onSuccessUpdate(UpdateProfileReceive obj) {

       dismissLoading();
       Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
       if (status){

         Gson gsonUserInfo = new Gson();
         String userInfo = gsonUserInfo.toJson(obj.getUserInfo());
         pref.setUserInfo(userInfo);
         pref.setUsername(obj.getUserInfo().getFirst_name());

         setSuccessDialog(getActivity(), "Information Successfully Updated", HomeActivity.class,"Update Information");

       }
    }

    @Override
    public void onValidationSucceeded() {
        if(validateStatus){
            requestUpdateProfile();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

       for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);

            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

           if (view instanceof EditText) {
               ((EditText) view).setError(splitErrorMsg[0]);
           }
           else if (view instanceof CheckBox){
               ((CheckBox) view).setError(splitErrorMsg[0]);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthInAlphabet = getMonthAlphabet(month);
        txtDOB.setText(day + " " + monthInAlphabet + " " + year);

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 11){
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }
        //fullDate = varDay+""+day+ "-" + varMonth+""+month + "-" + year;
        //fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        Log.e("fullDate", fullDate);
    }
}
