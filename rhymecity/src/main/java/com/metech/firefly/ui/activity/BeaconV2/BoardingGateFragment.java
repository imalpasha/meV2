package com.metech.firefly.ui.activity.BeaconV2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BoardingGateFragment extends BaseFragment {

    private int fragmentContainerId;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.txtMessage)
    TextView txtMessage;

    public static BoardingGateFragment newInstance(Bundle bundle) {

        BoardingGateFragment fragment = new BoardingGateFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.boarding_gate_message, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();

        if(bundle.containsKey("MESSAGE")){
            String flightSummary = bundle.getString("MESSAGE");
            txtMessage.setText(flightSummary);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
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
