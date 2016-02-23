package com.fly.firefly.ui.activity.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.Picker.DatePickerFragment;
import com.fly.firefly.ui.module.RegisterModule;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.presenter.RegisterPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,RegisterPresenter.RegisterView,Validator.ValidationListener {


    // Validator Attributes
    @Inject
    RegisterPresenter presenter;

    @Order(14) @NotEmpty
    @InjectView(R.id.editTextCountry)
    TextView editTextCountry;

    @InjectView(R.id.editTextState)
    TextView editTextState;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtUsername)
    EditText txtUsername;

    @Order(2)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6, message = "Must at least 6 character")
    @Password(sequence =3,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Must have uppercase char,number and symbols") // Password validator
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @Order(3)
    @NotEmpty(sequence = 1)
    @ConfirmPassword(sequence = 2)
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Order(4)
    @NotEmpty
    @InjectView(R.id.txtFirstName)
    EditText txtFirstName;

    @Order(5)
    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtRegisterDatePicker)
    TextView txtRegisterDatePicker;

    @Order(6) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtLastName)
    EditText txtLastName;

    @Order(7) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtAddressLine1)
    EditText txtAddressLine1;

    @Order(8) @Optional
    @InjectView(R.id.txtAddressLine2)
    EditText txtAddressLine2;

    @Order(9) @NotEmpty(sequence = 1) @Length(sequence = 2, min = 5,max = 7, message = "invalid postcode")
    @InjectView(R.id.editTextPostcode)
    EditText editTextPostcode;

    @Order(10) @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6,max = 14, message = "invalid phone number")
    @InjectView(R.id.editTextMobilePhone) EditText editTextMobilePhone;

    @InjectView(R.id.editTextAlternatePhone)
    EditText editTextAlternatePhone;

    @Order(12)@Optional
    @InjectView(R.id.editTextFax)
    EditText editTextFax;

    @Order(13)@NotEmpty
    @InjectView(R.id.txtCity)
    EditText txtCity;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    //@Order(16)
    //@Checked(message = "You must agree with tem & condition")
    @InjectView(R.id.chkTNC)
    CheckBox chkTNC;

    @Order(17)
    @InjectView(R.id.checkBox2)
    CheckBox checkBox2;

    @InjectView(R.id.registerContinueButton)
    Button registerContinueButton;

    @InjectView(R.id.txtBonusLink)
    EditText txtBonusLink;

    private Validator mValidator;
    private int currentPage;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> state;
    private int day;
    private int month;
    private int year;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private int month_number;
    private DatePickerObj date;
    private ArrayList<DropDownItem> titleList;
    private String selectedTitle;
    private String[] state_val;
    private String selectedState;
    private String selectedCountryCode;
    public static final String DATEPICKER_TAG = "datepicker";
    private String fullDate;
    private static final String SCREEN_LABEL = "Register";

    public static RegisterFragment newInstance() {

        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new RegisterModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register, container, false);
        ButterKnife.inject(this, view);


        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();

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

         /*Switch register info block*/
        editTextCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Country");
                showCountrySelector(getActivity(),countrys);
            }
        });

        editTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "State");
                showCountrySelector(getActivity(), state);
            }
        });

        txtRegisterDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Date");
                //datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(year - 80, year);
                //datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Title");
               Log.e("Clicked", "Ok");
               popupSelection(titleList, getActivity(),txtTitle,true,view);
            }
        });

        registerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "ContinueButton");
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });




        return view;
    }

    /*Date Picker -> need to move to main activity*/
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(RegisterFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }


    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(RegisterFragment.this, 0);
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
                   editTextCountry.setText(selectedCountry.getText());
                   selectedCountryCode = selectedCountry.getCode();

                   /*Each country click - reset state obj*/
                   state = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
                   JSONArray jsonState = getState(getActivity());
                   for(int x = 0 ; x < jsonState.length() ; x++) {

                       JSONObject row = (JSONObject) jsonState.opt(x);
                       if(selectedCountryCode.equals(row.optString("country_code"))) {
                           DropDownItem itemCountry = new DropDownItem();
                           itemCountry.setText(row.optString("state_name"));
                           itemCountry.setCode(row.optString("state_code"));
                           itemCountry.setTag("State");
                           state.add(itemCountry);
                       }
                   }

               } else {
                   editTextState.setText(selectedCountry.getText());
                   selectedState = selectedCountry.getCode();
               }

           }
       }
    }

    public void requestRegister(){

            initiateLoading(getActivity());

            HashMap<String, String> init = pref.getSignatureFromLocalStorage();
            String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

            HashMap<String, String> initi = pref.getNewsletterStatus();
            String newsletter = initi.get(SharedPrefManager.ISNEWSLETTER);

            RegisterObj regObj = new RegisterObj();

            regObj.setUsername(txtUsername.getText().toString());
            regObj.setFirst_name(txtFirstName.getText().toString());
            regObj.setLast_name(txtLastName.getText().toString());
            regObj.setPassword(AESCBC.encrypt(App.KEY, App.IV, txtPassword.getText().toString()));
            regObj.setTitle(txtTitle.getTag().toString());
            regObj.setDob(fullDate);
            regObj.setAddress_1(txtAddressLine1.getText().toString());
            regObj.setAddress_2(txtAddressLine2.getText().toString());
            regObj.setAddress_3(txtAddressLine2.getText().toString());
            regObj.setAlternate_phone(editTextAlternatePhone.getText().toString());
            regObj.setMobile_phone(editTextMobilePhone.getText().toString());
            regObj.setCountry(selectedCountryCode);
            regObj.setState(selectedState);
            regObj.setCity(txtCity.getText().toString());
            regObj.setPostcode(editTextPostcode.getText().toString());
            regObj.setFax(editTextFax.getText().toString());
            regObj.setBonuslink(txtBonusLink.getText().toString());
            regObj.setSignature("");

            //regObj.setNewsletter(newsletter);

            if (checkBox2.isChecked()) {
                pref.setNewsletterStatus("Y");
                regObj.setNewsletter("Y");
            }else{
                pref.setNewsletterStatus("N");
                regObj.setNewsletter("N");
            }

            presenter.onRequestRegister(regObj);
    }

    @Override
    public void onSuccessRegister(RegisterReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            setSuccessDialog(getActivity(), obj.getMessage(), LoginActivity.class);
        }

    }

    @Override
    public void onValidationSucceeded() {
        //check checkBox
        if(chkTNC.isChecked()){
            requestRegister();
        }else{
            croutonAlert(getActivity(), "You must agree with tem & condition");
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        boolean fieldError = false;
        String errorMessage = null;

        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);
             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);

            }
            fieldError = true;
            errorMessage = splitErrorMsg[0];
        }

        if(fieldError){
            croutonAlert(getActivity(), errorMessage);
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
        txtRegisterDatePicker.setText(day + " " + monthInAlphabet + " " + year);

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10){
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }

        fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        Log.e("fullDate", fullDate);
        /*fullDate = varDay+""+day+ "-" + varMonth+""+month + "-" + year;
        Log.e("fullDate", fullDate);*/
    }

}
