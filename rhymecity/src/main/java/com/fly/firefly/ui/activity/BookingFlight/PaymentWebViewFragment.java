package com.fly.firefly.ui.activity.BookingFlight;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.ManageFlight.MF_ActionActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PaymentWebViewFragment extends BaseFragment  {

    //@Inject
    //BookingPresenter presenter;
    @InjectView(R.id.webView)WebView webview;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Payment Details(Payment Web)";
    private static final String SCREEN_LABEL_MANAGE = "Manage Flight: Payment Details(Payment Web)";
    private String paymentFrom;
    boolean loadingFinished = true;
    boolean redirect = false;

    public static PaymentWebViewFragment newInstance(Bundle bundle) {

        PaymentWebViewFragment fragment = new PaymentWebViewFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.payment_webview, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();

        String url = bundle.getString("PAYMENT_URL");
        paymentFrom = bundle.getString("PAYMENT_FROM");


        //Gson gson = new Gson();
        //PassengerInfoReveice obj = gson.fromJson(insurance, PassengerInfoReveice.class);
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

                Log.e("URL",url);
                String[] parts = url.split("//");
                String part1 = parts[0]; // 004
                if(part1.equals("https:")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    status = true;

                }else{

                    if(paymentFrom.equals("NORMAL")){
                        Intent intent = new Intent(getActivity(), FlightSummaryActivity2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                        System.gc();
                        getActivity().finish();
                    }else {
                        Intent intent = new Intent(getActivity(), MF_ActionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("AlertDialog", "Y");
                        getActivity().startActivity(intent);
                        System.gc();
                        getActivity().finish();
                    }
                        status = true;
                }
                return true ;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                loadingFinished = false;
                initiateLoading(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    dismissLoading();
                } else{
                    redirect = false;
                }
            }

        });

        return view;
    }

    @JavascriptInterface
    public void PaymentFinished(String success) {
        Log.e("Status From Webview", success);

        newIntent();


    }

    public void newIntent(){

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

        if (paymentFrom.equals("CHANGE") ) {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL_MANAGE);
            Log.e("Tracker", SCREEN_LABEL_MANAGE);

        }else {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL);
            Log.e("Tracker", SCREEN_LABEL);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
