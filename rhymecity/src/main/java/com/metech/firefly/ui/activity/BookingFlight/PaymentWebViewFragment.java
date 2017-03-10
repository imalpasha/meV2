package com.metech.firefly.ui.activity.BookingFlight;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.activity.ManageFlight.MF_ActionActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentWebViewFragment extends BaseFragment {

    //@Inject
    //BookingPresenter presenter;
    @InjectView(R.id.webView)
    WebView webview;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Payment Details(Payment Web)";
    private static final String SCREEN_LABEL_MANAGE = "Manage Flight: Payment Details(Payment Web)";
    private String paymentFrom;
    private String paymentCode;
    boolean loadingFinished = true;
    boolean redirect = false;
    private String paymentPopup;
    private boolean override = true;
    private Activity act;
    private Dialog dialog;

    public static PaymentWebViewFragment newInstance(Bundle bundle) {

        PaymentWebViewFragment fragment = new PaymentWebViewFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.payment_webview, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        String url = bundle.getString("PAYMENT_URL");
        paymentFrom = bundle.getString("PAYMENT_FROM");
        paymentCode = bundle.getString("PAYMENT_CODE");


        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new PaymentWebViewFragment(), "Android");
        webview.loadUrl(url);

        try {
            paymentPopup = bundle.getString("PAYMENT_POPUP");
        } catch (Exception e) {

        }

        if (paymentPopup.equals("Y")) {

            Log.e("1", url);

            override = false;
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new PaymentWebViewFragment(), "Android");
            webview.loadUrl(url);

            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;

                    Boolean status;

                    //String[] parts = url.split("//");
                    //String part1 = parts[0]; // 004
                    if (url.contains("maybank2u")) {
                        //open url in new browser
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        //webview.setWebChromeClient(new WebChromeClient());
                        //view.loadUrl(url);
                    } else {
                        if (paymentFrom.equals("NORMAL")) {
                            Intent intent = new Intent(getActivity(), FlightSummaryActivity2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                            System.gc();
                            getActivity().finish();
                        } else {
                            Intent intent = new Intent(getActivity(), MF_ActionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("AlertDialog", "Y");
                            getActivity().startActivity(intent);
                            System.gc();
                            getActivity().finish();
                        }
                        status = true;
                        //}
//
                    }

              /* if(part1.equals("https:")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    status = true;

                }else{ */


                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                    loadingFinished = false;
                    initiateLoadingPayment(act);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                    }

                    if (loadingFinished && !redirect) {
                        dismissLoadingPayment();
                    } else {
                        redirect = false;
                    }
                }

            });

        } else {
            override = true;
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new PaymentWebViewFragment(), "Android");

            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;

                    /*if(url.contains("cimb") || paymentCode.equals("PX") || paymentCode.equals("MC")){
                        view.setWebChromeClient(new WebChromeClient());
                        view.loadUrl(url);
                    }else{*/

                    //https://m.fireflyz.com.my/api/paymentCCProcess

                    if (url.equals("https://m.fireflyz.com.my/api/success")) {
                        if (paymentFrom.equals("NORMAL")) {
                            Intent intent = new Intent(getActivity(), FlightSummaryActivity2.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                            System.gc();
                            getActivity().finish();
                        } else {
                            Intent intent = new Intent(getActivity(), MF_ActionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("AlertDialog", "Y");
                            getActivity().startActivity(intent);
                            System.gc();
                            getActivity().finish();

                        }
                    } else {
                        webview.setWebChromeClient(new WebChromeClient());
                        webview.loadUrl(url);
                    }

                    // }

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                    loadingFinished = false;
                    initiateLoadingPayment(act);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                    }

                    if (loadingFinished && !redirect) {
                        dismissLoadingPayment();
                    } else {
                        redirect = false;
                    }
                }

            });
        }

        return view;
    }

    @JavascriptInterface
    public void PaymentFinished(String success) {
        newIntent();
    }

    public void newIntent() {

        Intent intent = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(intent);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (paymentFrom.equals("CHANGE")) {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL_MANAGE);
        } else {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void paymentBackButton() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Cancel payment and redirect to homepage.")
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setConfirmText("Confirm")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        System.gc();
                        getActivity().finish();

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    public void initiateLoadingPayment(Activity act) {

        if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(act, R.style.DialogTheme);

        LayoutInflater li = LayoutInflater.from(act);
        final View myView = li.inflate(R.layout.loading_screen, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setView(myView);

        dialog.setContentView(myView);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CCFFFFFF")));
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    public void dismissLoadingPayment() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                Log.e("Dismiss", "Y");
            }
        }
    }
}
