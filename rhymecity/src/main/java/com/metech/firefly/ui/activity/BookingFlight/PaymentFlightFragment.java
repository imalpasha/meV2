package com.metech.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.DeleteCCReceive;
import com.metech.firefly.api.obj.PaymentInfoReceive;
import com.metech.firefly.api.obj.PaymentReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.module.PaymentFlightModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.Payment;
import com.metech.firefly.ui.object.Signature;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.metech.firefly.utils.Utils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class PaymentFlightFragment extends BaseFragment implements BookingPresenter.PaymentFlightView, Validator.ValidationListener {

    @Inject
    BookingPresenter presenter;

    @Order(1)
    @NotEmpty
    @InjectView(R.id.txtCardType)
    TextView txtCardType;

    @Order(2)
    @NotEmpty
    @InjectView(R.id.txtCardNumber)
    EditText txtCardNumber;

    @Order(3)
    @NotEmpty
    @InjectView(R.id.txtPaymentMonth)
    TextView txtPaymentMonth;

    @Order(4)
    @NotEmpty
    @InjectView(R.id.txtPaymentYear)
    TextView txtPaymentYear;

    @Order(5)
    @NotEmpty
    @InjectView(R.id.txtCardHolderName)
    EditText txtCardHolderName;

    @Order(6)
    @NotEmpty
    @InjectView(R.id.txtCardCVV)
    EditText txtCardCVV;

    @InjectView(R.id.btnPay)
    Button btnPay;

    @InjectView(R.id.creditCardFormLayout)
    LinearLayout creditCardFormLayout;

    @NotEmpty
    @InjectView(R.id.txtIssuingBank)
    EditText txtIssuingBank;

    @InjectView(R.id.txtTotalDue)
    TextView txtTotalDue;

    @InjectView(R.id.checkToSaveCC)
    CheckBox checkToSaveCC;

    @InjectView(R.id.txtCheckBoxCC)
    TextView txtCheckBoxCC;

    @InjectView(R.id.saveCCLayout)
    LinearLayout saveCCLayout;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Payment Details(Add Payment)";
    private static final String SCREEN_LABEL_MANAGE = "Manage Flight: Payment Details(Add Payment)";
    private SharedPrefManager pref;
    private String signature;
    private View view;
    private String selectedCheckBoxTag;
    private String part1 = "1", part2;
    private final List<String> channelType = new ArrayList<String>();
    private final List<String> channelCode = new ArrayList<String>();

    private ArrayList<DropDownItem> cardType = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> monthList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> yearList = new ArrayList<DropDownItem>();
    private Validator mValidator;
    private String bookingId;
    private String paymentFrom;
    private String totalDue;
    private boolean getPaymentList;
    private boolean getPayment;
    private boolean paymentVendorGenerated = false;
    private String paymentCode;
    private String personID;
    private String accountNumberID;
    private boolean saveCreditCardInfo = false;

    public static PaymentFlightFragment newInstance(Bundle bundle) {

        PaymentFlightFragment fragment = new PaymentFlightFragment();
        fragment.setArguments(bundle);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new PaymentFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.payment_flight, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        Bundle bundle = getArguments();
        paymentFrom = bundle.getString("PAYMENT_FROM");
        //paymentFrom = "NORMAL";

        try {
            totalDue = bundle.getString("TOTAL_DUE");
            txtTotalDue.setText("TOTAL DUE: " + totalDue);
        } catch (Exception e) {
        }
        HashMap<String, String> initPersonID = pref.getPersonID();
        personID = initPersonID.get(SharedPrefManager.PERSON_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initLogin = pref.getLoginStatus();
        String loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);
        if (!loginStatus.equals("Y")) {
            saveCCLayout.setVisibility(View.GONE);
        }

        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingId = initBookingID.get(SharedPrefManager.BOOKING_ID);
        Signature baseObj = new Signature();
        baseObj.setSignature(signature);
        baseObj.setPersonID(personID);

        //baseObj.setSignature("V3BZc1EzcnU5MEU9fGcyK1dkbEtnR1J0c1V3d2k0M1d2eFFsVVlFdlcySjBNdW1xU0xuWlg2TXJsSzlFVkxmOEs3OUI1MklOanB0VWQzY0JFVFFvKytRcDBvbktoYnZGZ3dLSmNLYkdxeThRZU5KNVU2M3E1KzlsVkVhci9xOElteFNYajBkdUYxcFFsUC9XcFBLVXVBcEk9");
        getPaymentInfo(baseObj);

        //Gson gson = new Gson();
        //PaymentInfoReceive obj = gson.fromJson(paymentDummy, PaymentInfoReceive.class);
        //generatePaymentVendorList(obj);

        //Card Selection
        txtCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(cardType, getActivity(), txtCardType, true, view);
            }
        });

        //setMonthList
        yearList = getListOfYear(getActivity());
        //Card Selection
        txtPaymentYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(yearList, getActivity(), txtPaymentYear, true, view);
            }
        });

        //setMonthList
        monthList = getListOfMonth(getActivity());
        //Card Selection
        txtPaymentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(monthList, getActivity(), txtPaymentMonth, true, view);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (part1.equals("1")) {
                    mValidator.validate();
                } else {
                    debitCard();
                }
                Utils.hideKeyboard(getActivity(), v);
            }
        });


        checkToSaveCC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    saveCreditCardInfo = true;
                } else {
                    saveCreditCardInfo = false;
                }
            }
        });

        return view;
    }

    public void debitCard() {

        initiateLoading(getActivity());
        getPaymentList = false;

        Payment paymentObj = new Payment();
        paymentObj.setSignature(signature);
        paymentObj.setChannelCode(part2);
        paymentObj.setChannelType(part1);
        paymentObj.setBookingID(bookingId);
        paymentCode = part2;

        presenter.paymentRequest(paymentObj);

    }

    @Override
    public void onValidationSucceeded() {

        initiateLoading(getActivity());
        //paymentRequest();
        Payment paymentObj = new Payment();
        paymentObj.setSignature(signature);
        paymentObj.setCardHolderName(txtCardHolderName.getText().toString());
        paymentObj.setCardNumber(txtCardNumber.getText().toString());
        paymentObj.setChannelCode(txtCardType.getTag().toString());
        paymentObj.setChannelType(part1);
        paymentObj.setCvv(txtCardCVV.getText().toString());
        paymentObj.setExpirationDateMonth(txtPaymentMonth.getText().toString());
        paymentObj.setExpirationDateYear(txtPaymentYear.getText().toString());
        //paymentObj.setIssuingBank(txtIssuingBank.getText().toString());
        paymentObj.setIssuingBank(txtCardType.getText().toString());
        paymentObj.setBookingID(bookingId);

        if (accountNumberID != null) {
            paymentObj.setAccountNumberID(accountNumberID);
        }

        if (saveCreditCardInfo) {
            paymentObj.setPersonID(personID);
        }

        getPaymentList = false;
        paymentCode = txtCardType.getTag().toString();

        presenter.paymentRequest(paymentObj);

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
            } else if (view instanceof TextView) {
                ((TextView) view).setError(splitErrorMsg[0]);
            }

            if (firstView) {
                view.requestFocus();
            }
            firstView = false;
        }

    }

    @Override
    public void onPaymentReceive(PaymentReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //Open Secure Site At Browser
            String sanitizeUrl = obj.getPass().replaceAll("[/]", "");
            String url = obj.getLink() + "android/" + sanitizeUrl;
            String popupWindow = obj.getPopup_window();

            Intent intent = new Intent(getActivity(), PaymentWebViewActivity.class);
            intent.putExtra("PAYMENT_URL", url);
            intent.putExtra("PAYMENT_FROM", paymentFrom);
            intent.putExtra("PAYMENT_POPUP", popupWindow);
            intent.putExtra("PAYMENT_CODE", paymentCode);
            Log.e("url", url);
            getActivity().startActivity(intent);

        }

    }

    @Override
    public void onPaymentInfoReceive(PaymentInfoReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            generatePaymentVendorList(obj);
            if (obj.getFop() != null) {
                autoFillPayment(obj.getFop());
            }
        }
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public void onRemoveCC(DeleteCCReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            setNormalDialog(getActivity(), obj.getMessage(), "Success!");

            //remove all autofill info
            accountNumberID = null;
            txtCardType.setText("");
            //txtCardType.setFocusableInTouchMode(true);
            txtCardType.setEnabled(true);

            txtCardHolderName.setText("");
            //txtCardHolderName.setFocusableInTouchMode(true);
            txtCardHolderName.setEnabled(true);

            txtCardNumber.setText("");
            //txtCardNumber.setFocusableInTouchMode(true);
            txtCardNumber.setEnabled(true);

            txtPaymentMonth.setText("");
            //txtPaymentMonth.setFocusableInTouchMode(true);
            txtPaymentMonth.setEnabled(true);

            txtPaymentYear.setText("");
            //txtPaymentYear.setFocusableInTouchMode(true);
            txtPaymentYear.setEnabled(true);

            checkToSaveCC.setChecked(false);
            txtCheckBoxCC.setText("Add this credit card to your account for faster booking.");
        }
        RealmObjectController.clearCachedResult(getActivity());
    }

    public void autoFillPayment(PaymentInfoReceive.FOP fopObj) {

        //get card name from cardlist - need global function?
        String cardName = "";
        for (int i = 0; i < cardType.size(); i++) {
            if (cardType.get(i).getCode().equals(fopObj.getCard_type())) {
                cardName = cardType.get(i).getText();
            }
        }
        accountNumberID = fopObj.getAccount_number_id();
        txtCardType.setText(cardName);
        txtCardType.setTag(fopObj.getCard_type());
        //txtCardType.setFocusableInTouchMode(false);
        txtCardType.setEnabled(false);

        txtCardHolderName.setText(fopObj.getCard_holder_name());
        //txtCardHolderName.setFocusableInTouchMode(false);
        txtCardHolderName.setEnabled(false);

        txtCardNumber.setText(fopObj.getCard_number());
        //txtCardNumber.setFocusableInTouchMode(false);
        txtCardNumber.setEnabled(false);

        txtPaymentMonth.setText(fopObj.getExpiration_date_month());
        //txtPaymentMonth.setFocusableInTouchMode(false);
        txtPaymentMonth.setEnabled(false);

        txtPaymentYear.setText(fopObj.getExpiration_date_year());
        //txtPaymentYear.setFocusableInTouchMode(false);
        txtPaymentYear.setEnabled(false);

        checkToSaveCC.setChecked(true);

        String removeCCtext = "<html>Add this credit card to your account for faster booking.Click <b>[HERE]</b> to remove this card information from your account</html>";
        txtCheckBoxCC.setText(Html.fromHtml(removeCCtext), TextView.BufferType.SPANNABLE);

        String filterRemoveCC = txtCheckBoxCC.getText().toString();
        int i1 = filterRemoveCC.indexOf("[H");
        int i2 = filterRemoveCC.indexOf("E]");

        Spannable mySpannable = (Spannable) txtCheckBoxCC.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Signature signatureObj = new Signature();
                signatureObj.setPersonID(personID);
                signatureObj.setSignature(signature);

                initiateLoading(getActivity());
                presenter.onRemoveCC(signatureObj);
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtCheckBoxCC.setMovementMethod(LinkMovementMethod.getInstance());

    }


    public void generatePaymentVendorList(final PaymentInfoReceive obj) {

        if (!paymentVendorGenerated) {

            for (int a = 0; a < obj.getPayment_channel().size(); a++) {

                String paymentType = obj.getPayment_channel().get(a).getChannel_type();
                String paymentCode = obj.getPayment_channel().get(a).getChannel_code();

                if (channelType.contains(paymentType)) {
                /*SKIP*/
                    if (paymentType.equals("2")) {
                        channelType.add(paymentType);
                        channelCode.add(paymentCode);
                    }
                } else {
                    channelType.add(paymentType);
                    channelCode.add(paymentCode);
                }

                if (obj.getPayment_channel().get(a).getChannel_type().equals("1")) {
                    DropDownItem itemTitle = new DropDownItem();
                    itemTitle.setText(obj.getPayment_channel().get(a).getChannel_name());
                    itemTitle.setCode(obj.getPayment_channel().get(a).getChannel_code());
                    itemTitle.setTag("Title");
                    cardType.add(itemTitle);
                }


                //List<String> imageURLCreditCard = new ArrayList<String>();

/*            if(obj.getPayment_channel().get(a).getChannel_type().equals("1")){
                imageURL.add(obj.getPayment_channel().get(a).getChannel_logo());
            }
            else if(obj.getPayment_channel().get(a).getChannel_type().equals("2")){
                imageURL.add(obj.getPayment_channel().get(a).getChannel_logo());
            }


        }*/
            }

            LinearLayout paymentChannelList = (LinearLayout) getActivity().findViewById(R.id.paymentChannelList);
            paymentChannelList.removeAllViews();

            for (int totalPaymentChannel = 0; totalPaymentChannel < channelType.size(); totalPaymentChannel++) {

                LinearLayout seatRow = new LinearLayout(getActivity());
                seatRow.setOrientation(LinearLayout.HORIZONTAL);
                seatRow.setPadding(0, 5, 0, 5);
                // seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);

                final LinearLayout imageRow = new LinearLayout(getActivity());
                imageRow.setOrientation(LinearLayout.VERTICAL);
                imageRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);

                final RadioButton selectPaymentChannel = new RadioButton(getActivity());
                selectPaymentChannel.setId(totalPaymentChannel + 1);
                selectPaymentChannel.setTag(channelType.get(totalPaymentChannel).toString() + "/" + channelCode.get(totalPaymentChannel).toString());
                if (channelType.get(totalPaymentChannel).toString().equals("1")) {
                    selectPaymentChannel.setText("Credit Card ");
                }

                seatRow.addView(selectPaymentChannel);


                for (int totalImage = 0; totalImage < obj.getPayment_channel().size(); totalImage++) {

                    //if(channelType.get(totalPaymentChannel).toString().equals(obj.getPayment_channel().get(totalImage).getChannel_type())){
                    if (channelType.get(totalPaymentChannel).toString().equals("1") && obj.getPayment_channel().get(totalImage).getChannel_type().equals("1")) {

                        //Need to move this later
                        final AjaxCallback<Bitmap> cb = new AjaxCallback<Bitmap>() {
                            @Override
                            public void callback(String url, Bitmap bm, AjaxStatus status) {
                                // do whatever you want with bm (the image)
                                ImageView image = new ImageView(getActivity());
                                image.setImageBitmap(bm);
                                imageRow.addView(image);

                            }
                        };
                        final AQuery aq = new AQuery(getActivity());
                        aq.ajax(obj.getPayment_channel().get(totalImage).getChannel_logo(), Bitmap.class, 0, cb);

                        seatRow.addView(imageRow);
                        break;
                    } else if (channelType.get(totalPaymentChannel).toString().equals("2") && obj.getPayment_channel().get(totalImage).getChannel_type().equals("2")) {
                        //Need to move this later
                        if (channelCode.get(totalPaymentChannel).toString().equals(obj.getPayment_channel().get(totalImage).getChannel_code())) {

                            final LinearLayout oneImage = new LinearLayout(getActivity());
                            oneImage.setOrientation(LinearLayout.HORIZONTAL);
                            oneImage.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);

                            final AjaxCallback<Bitmap> cb = new AjaxCallback<Bitmap>() {
                                @Override
                                public void callback(String url, Bitmap bm, AjaxStatus status) {
                                    // do whatever you want with bm (the image)
                                    ImageView image = new ImageView(getActivity());
                                    image.setImageBitmap(bm);
                                    oneImage.addView(image);


                                }
                            };
                            final AQuery aq = new AQuery(getActivity());
                            aq.ajax(obj.getPayment_channel().get(totalImage).getChannel_logo(), Bitmap.class, 0, cb);

                            seatRow.addView(oneImage);
                            break;

                        }
                    }
                }


                paymentChannelList.addView(seatRow);

            }
            //set first checkbox checked
            final RadioButton checkBox = (RadioButton) view.findViewWithTag(channelType.get(0).toString() + "/" + channelCode.get(0).toString());
            checkBox.setChecked(true);

            for (int y = 0; y < channelType.size(); y++) {

                final RadioButton checkToRemove = (RadioButton) view.findViewWithTag(channelType.get(y).toString() + "/" + channelCode.get(y).toString());

                checkToRemove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                             @Override
                                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                                 if (isChecked) {
                                                                     selectedCheckBoxTag = checkToRemove.getTag().toString();
                                                                     String[] parts = selectedCheckBoxTag.split("/");
                                                                     part1 = parts[0]; // 004
                                                                     part2 = parts[1]; // 004

                                                                     if (part1.equals("1")) {
                                                                         creditCardFormLayout.setVisibility(View.VISIBLE);
                                                                     } else {
                                                                         creditCardFormLayout.setVisibility(View.GONE);
                                                                     }

                                                                     for (int b = 0; b < channelType.size(); b++) {
                                                                         if (selectedCheckBoxTag.equals(channelType.get(b).toString() + "/" + channelCode.get(b).toString())) {

                                                                         } else {
                                                                             RadioButton checkToRemove = (RadioButton) view.findViewWithTag(channelType.get(b).toString() + "/" + channelCode.get(b).toString());
                                                                             checkToRemove.setChecked(false);
                                                                         }
                                                                     }
                                                                 }
                                                             }
                                                         }
                );
            }

            paymentVendorGenerated = true;
            getPayment = true;
        }


    }

    public void getPaymentInfo(Signature baseObj) {
        initiateLoading(getActivity());
        getPaymentList = true;
        presenter.paymentInfo(baseObj);
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

        if (paymentFrom.equals("CHANGE")) {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL_MANAGE);

        } else {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        }

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if (getPaymentList) {
            if (result.size() > 0) {
                Gson gson = new Gson();
                PaymentInfoReceive obj = gson.fromJson(result.get(0).getCachedResult(), PaymentInfoReceive.class);
                onPaymentInfoReceive(obj);

            } else {

            }
        } else if (!getPaymentList) {
            if (result.size() > 0) {
                Gson gson = new Gson();
                PaymentReceive obj = gson.fromJson(result.get(0).getCachedResult(), PaymentReceive.class);
                onPaymentReceive(obj);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
