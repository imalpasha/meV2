package com.fly.firefly.ui.activity.BookingFlight;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlightActionActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PaymentWebViewFragment extends BaseFragment  {

    //@Inject
    //BookingPresenter presenter;
    @InjectView(R.id.webView)WebView webview;


    private int fragmentContainerId;

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
        final String paymentFrom = bundle.getString("PAYMENT_FROM");


        //Gson gson = new Gson();
        //PassengerInfoReveice obj = gson.fromJson(insurance, PassengerInfoReveice.class);
        initiateLoading(getActivity());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new PaymentWebViewFragment(), "Android");
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(paymentFrom.equals("NORMAL")){
                    Intent intent = new Intent(getActivity(), FlightSummaryActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }else{
                    Intent intent = new Intent(getActivity(), ManageFlightActionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("AlertDialog", "Y");
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }

                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoading();
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
