package com.fly.firefly.ui.activity.ManageFlight;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ManageRequestIntinenary;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ManageFlightItinenary;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.SeatSelect;
import com.fly.firefly.ui.object.SendItinenaryObj;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MF_SentItineraryFragment extends BaseFragment implements ManageFlightPrenter.SendItinenary {

    @Inject
    ManageFlightPrenter presenter;
    private SharedPrefManager pref;

    @InjectView(R.id.txtEmail)
    TextView txtEmail;

    @InjectView(R.id.messageLayout)
    LinearLayout messageLayout;

    @InjectView(R.id.btnClose)
    Button btnClose;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;
    private String pnr,username,bookingId,signature;

    public static MF_SentItineraryFragment newInstance(Bundle bundle) {

        MF_SentItineraryFragment fragment = new MF_SentItineraryFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightItinenary(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_flight_sent_itenenary, container, false);
        ButterKnife.inject(this, view);

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        pnr = obj.getObj().getItenerary_information().getPnr();
        bookingId = obj.getObj().getBooking_id();
        username = obj.getObj().getContact_information().getEmail();

        SendItinenaryObj requestObj = new SendItinenaryObj();
        requestObj.setPnr(pnr);
        requestObj.setSignature(signature);
        requestObj.setBookind_Id(bookingId);

        initiateLoading(getActivity());
        presenter.onSentItinenary(requestObj);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageFlightActionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("AlertDialog", "Y");
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onSuccessRequest(ManageRequestIntinenary obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            txtEmail.setText(username);
            messageLayout.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
