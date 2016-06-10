package com.metech.firefly.ui.activity.PasswordExpired;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.ChangePasswordReceive;
import com.metech.firefly.api.obj.ForgotPasswordReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.activity.Login.LoginActivity;
import com.metech.firefly.ui.module.ChangePasswordModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.ChangePasswordRequest;
import com.metech.firefly.ui.presenter.ChangePasswordPresenter;
import com.metech.firefly.utils.AESCBC;
import com.metech.firefly.utils.App;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;

public class ChangePasswordFragment extends BaseFragment implements ChangePasswordPresenter.ChangePasswordView, Validator.ValidationListener {


    private Validator mValidator;
    private Tracker mTracker;
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login: Password Expired";
    private View view;

    @Inject
    ChangePasswordPresenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;
    @Order(1)
    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
    @InjectView(R.id.editTextemail)
    EditText editTextemail;

    @Order(2)
    @NotEmpty(sequence = 1)
    @InjectView(R.id.editTextforgotPasswordCurrent)
    EditText editTextPasswordCurrent;

    @Order(3)
    @NotEmpty(sequence = 1)
    @ConfirmPassword(message = "Password mismatch")
    @InjectView(R.id.editTextforgotPasswordConfirm)
    EditText editTextPasswordConfirm;

    @Order(4)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6, max = 16, message = "Must be at least 8 and maximum 16 characters")
    @Password(sequence = 3)// Password validator
    @InjectView(R.id.editTextforgotPasswordNew)
    EditText editTextPasswordNew;

    @InjectView(R.id.txtPasswordHint)
    TextView txtPasswordHint;

    @InjectView(R.id.btnchangepassword)
    Button changepasswordButton;

    public static ChangePasswordFragment newInstance() {

        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ChangePasswordModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_password_expired, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        //password
        editTextPasswordCurrent.setTransformationMethod(new PasswordTransformationMethod());
        editTextPasswordConfirm.setTransformationMethod(new PasswordTransformationMethod());
        editTextPasswordNew.setTransformationMethod(new PasswordTransformationMethod());

        HashMap<String, String> userinfo = pref.getUserEmail();
        String email = userinfo.get(SharedPrefManager.USER_EMAIL);

        editTextemail.setFocusable(false);
        editTextemail.setFocusableInTouchMode(false);
        editTextemail.setClickable(false);
        editTextemail.setText(email);

        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Action", "changepasswordButton");
                mValidator.validate();
            }
        });

           /*Display Password Hint*/
        txtPasswordHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNormalDialog(getActivity(), getActivity().getResources().getString(R.string.register_password_hint), getActivity().getResources().getString(R.string.register_password_policy));
            }
        });

        return view;
    }


    public void requestChangePassword(String username, String password, String new_password) {
        initiateLoading(getActivity());
        ChangePasswordRequest data = new ChangePasswordRequest();
        data.setEmail(username);
        data.setNewPassword(new_password);
        data.setCurrentPassword(password);
        presenter.changePassword(data);
    }


    public void goLoginPage() {

        Intent loginPage = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(loginPage);
        getActivity().finish();
    }


    @Override
    public void onUpdatePasswordSuccess(ChangePasswordReceive obj) {
        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            setSuccessDialog(getActivity(), obj.getMessage(), HomeActivity.class, "Password Changed!");
        }

    }


    //Validator Result//
    @Override
    public void onValidationSucceeded() {

        //check if new password same with confirm password
        if (!isValidatePassword(editTextPasswordNew.getText().toString())) {
            //Crouton.makeText(getActivity(), "Password must contain uppercase char,number and symbols", Style.ALERT).show();
            //EditText txtCurrentPassword = (EditText) view.findViewById(R.id.editTextforgotPasswordNew);
            //txtCurrentPassword.setError(null);
            editTextPasswordNew.setError("Password must contain uppercase char,number and symbols");
        } else {
            requestChangePassword(editTextemail.getText().toString(), AESCBC.encrypt(App.KEY, App.IV, editTextPasswordCurrent.getText().toString()), AESCBC.encrypt(App.KEY, App.IV, editTextPasswordNew.getText().toString()));
        }
    }


    private boolean isValidatePassword(String password) {

        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
        //Agent-17c
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
            }
        }

        //Crouton.makeText(getActivity(), message, Style.ALERT).show();

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
        if (result.size() > 0) {
            Gson gson = new Gson();
            ChangePasswordReceive obj = gson.fromJson(result.get(0).getCachedResult(), ChangePasswordReceive.class);
            onUpdatePasswordSuccess(obj);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
