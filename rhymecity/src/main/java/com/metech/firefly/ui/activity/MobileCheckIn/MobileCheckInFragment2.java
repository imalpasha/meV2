package com.metech.firefly.ui.activity.MobileCheckIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.MobileCheckInPassengerReceive;
import com.metech.firefly.api.obj.MobileCheckinReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.metech.firefly.ui.adapter.CheckInAdapter;
//import com.fly.firefly.ui.adapter.CheckInPassengerListAdapter;
import com.metech.firefly.ui.module.MobileCheckInModule2;
import com.metech.firefly.ui.object.MobileCheckInPassenger;
import com.metech.firefly.ui.object.PassengerInfo;
import com.metech.firefly.ui.presenter.MobileCheckInPresenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.RealmObjectController;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;

public class MobileCheckInFragment2 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView2 {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext2)
    Button mobileCheckInNext2;

    //@InjectView(R.id.listview)ExpandAbleGridView listview;
    @InjectView(R.id.listview)
    ExpandableListView listview;

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Mobile Check In Detail";
    private MobileCheckinReceive obj;
    private String PNR;
    private String arrivalCode, departureCode;
    //private CheckInPassengerListAdapter adapter;
    private CheckInAdapter adapter;
    private Boolean allCheckIn = true;
    private View header;
    private Boolean formContinue = true;
    private Boolean bonuslinkError = false;
    private Boolean documentNumberError = false;

    public static MobileCheckInFragment2 newInstance(Bundle bundle) {

        MobileCheckInFragment2 fragment = new MobileCheckInFragment2();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule2(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_2, container, false);
        ButterKnife.inject(this, view);

        //add header to listview
        header = inflater.inflate(R.layout.mobile_checkin_2_header, listview, false);
        listview.addHeaderView(header, null, false);

        Bundle bundle = getArguments();
        if (bundle.containsKey("FLIGHT_OBJ")) {
            String flightSummary = bundle.getString("FLIGHT_OBJ");

            Gson gson = new Gson();
            obj = gson.fromJson(flightSummary, MobileCheckinReceive.class);

            PNR = obj.getPnr();
            departureCode = obj.getDeparture_station_code();
            arrivalCode = obj.getArrival_station_code();

            TextView flightdate = (TextView) header.findViewById(R.id.flightdate);
            TextView flightnumber = (TextView) header.findViewById(R.id.flightnumber);
            TextView stationcode = (TextView) header.findViewById(R.id.stationcode);
            TextView departuretime = (TextView) header.findViewById(R.id.departuretime);

            stationcode.setText(obj.getFlight_detail().getStation_code());
            flightdate.setText(obj.getFlight_detail().getFlight_date());
            flightnumber.setText(obj.getFlight_detail().getFlight_number());
            departuretime.setText(obj.getFlight_detail().getDeparture_time());

        }

        //adapter = new CheckInPassengerListAdapter(getActivity(),obj.getObj().getPassengers(),this,getActivity().getSupportFragmentManager());
        adapter = new CheckInAdapter(getActivity(), obj.getPassengers(), this, getActivity().getSupportFragmentManager());
        listview.setAdapter(adapter);
        listview.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listview.setGroupIndicator(null);

        mobileCheckInNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean goNext = false;

                mobileCheckInNext2.requestFocus();
                //validate atleast one passenger is selected
                for (int i = 0; i < obj.getPassengers().size(); i++) {
                    if (obj.getPassengers().get(i).getChecked() != null) {
                        goNext = true;
                    }
                }

                if (goNext) {
                    next();
                } else {
                    setAlertDialog(getActivity(), "Atleast one passenger selected", "Check - In.");
                }

            }
        });


        for (int y = 0; y < obj.getPassengers().size(); y++) {

            if (!obj.getPassengers().get(y).getStatus().equals("Checked In")) {
                allCheckIn = false;
            }
        }
        if (allCheckIn) {
            mobileCheckInNext2.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    /*Public-Inner Func*/
    public void next() {

        formContinue = true;
        bonuslinkError = false;
        documentNumberError = false;

        ArrayList<PassengerInfo> mainObj = new ArrayList<PassengerInfo>();

        for (int i = 0; i < obj.getPassengers().size(); i++) {

            PassengerInfo passengerObj = new PassengerInfo();
            passengerObj.setExpiration_date(obj.getPassengers().get(i).getExpiration_date());
            passengerObj.setDocument_number(obj.getPassengers().get(i).getDocument_number());
            passengerObj.setIssuing_country(obj.getPassengers().get(i).getIssuing_country());
            passengerObj.setTravel_document(obj.getPassengers().get(i).getTravel_document());
            passengerObj.setPassenger_number(obj.getPassengers().get(i).getPassenger_number());

            String bonuslink;
            if (obj.getPassengers().get(i).getBonuslink() == null) {
                bonuslink = "";
            } else {
                bonuslink = obj.getPassengers().get(i).getBonuslink();
            }
            passengerObj.setBonusLink(bonuslink);

            String enrich;
            if (obj.getPassengers().get(i).getEnrich() == null) {
                enrich = "";
            } else {
                enrich = obj.getPassengers().get(i).getEnrich();
            }

            String docNumber;
            if (obj.getPassengers().get(i).getDocument_number() == null) {
                docNumber = "";
            } else {
                docNumber = obj.getPassengers().get(i).getDocument_number();
            }

            passengerObj.setEnrich(enrich);

            if (obj.getPassengers().get(i).getChecked() == null) {
                passengerObj.setStatus("N");
            } else {
                passengerObj.setStatus(obj.getPassengers().get(i).getChecked());
            }

            if (obj.getPassengers().get(i).getChecked() != null && obj.getPassengers().get(i).getChecked().equals("Y")) {
                checkDocumentNumber(docNumber);
                checkEnrich(enrich);
                checkBonuslink(bonuslink);
            }

            mainObj.add(passengerObj);

        }

        /*MobileCheckInPassenger obj2 = new MobileCheckInPassenger();
        obj2.setPassengers(mainObj);
        obj2.setPnr(PNR);
        obj2.setDeparture_station_code(departureCode);
        obj2.setArrival_station_code(arrivalCode);
        obj2.setSignature(obj.getSignature());
        presenter.checkInPassenger(obj2);*/

        //validate enrich here
        if (formContinue) {

            initiateLoading(getActivity());

            MobileCheckInPassenger obj2 = new MobileCheckInPassenger();
            obj2.setPassengers(mainObj);
            obj2.setPnr(PNR);
            obj2.setDeparture_station_code(departureCode);
            obj2.setArrival_station_code(arrivalCode);
            obj2.setSignature(obj.getSignature());
            presenter.checkInPassenger(obj2);

        } else {
            if (documentNumberError) {
                croutonAlert(getActivity(), "Document no is required");
            } else if (bonuslinkError) {
                croutonAlert(getActivity(), "Invalid bonuslink number");
            } else {
                croutonAlert(getActivity(), "Invalid enrich number");
            }
        }

    }

    public void checkDocumentNumber(String no) {
        if (no.equals("")) {
            Log.e("Document No", "Doc" + no);
            documentNumberError = true;
            formContinue = false;
        } else {
            Log.e("Document No", "Doc" + no);
        }
    }

    public void checkEnrich(String enrich) {

        String enrich2 = enrich;
        if (!enrich2.toString().matches("")) {

            if (enrich2.length() == 11) {
                if (!Character.toString(enrich2.charAt(0)).equals("M")) {
                    formContinue = false;
                }

                if (!Character.toString(enrich2.charAt(1)).equals("H")) {
                    formContinue = false;
                }

                //check the rest - must be digit
                for (int f = 2; f < 11; f++) {
                    if (Character.isDigit(enrich2.charAt(f))) {
                        //ok
                    } else {
                        formContinue = false;
                    }
                }

                int c = 0;
                int j = 0;
                int k = 0;

                if (!enrich2.matches("")) {
                    try {
                        j = Integer.parseInt(enrich2.toString().substring(2, 10));
                    } catch (Exception e) {
                        formContinue = false;
                    }

                    try {
                        k = Integer.parseInt(enrich2.toString().substring(enrich2.toString().length() - 1));
                    } catch (Exception e) {
                        formContinue = false;
                    }

                    c = j % 7;

                    if (c != k) {
                        Log.e("Invalid", "Y");
                        formContinue = false;
                    }
                }
            } else {
                formContinue = false;
            }
        } else {
            Log.e("Null", "Y");
        }
    }

    public void checkBonuslink(String bonuslink) {

        if (!bonuslink.matches("")) {
            if (bonuslink.length() < 16 || bonuslink.length() > 16) {
                formContinue = false;
                bonuslinkError = true;
            } else {
                Log.e("Y", "y");
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
                adapter.setSelectedCountry(selectedCountry.getText(), selectedCountry.getCode());
            }
        }
    }

    @Override
    public void onCheckInPassenger(MobileCheckInPassengerReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            Intent next = new Intent(getActivity(), MobileCheckInActivity3.class);
            next.putExtra("MOBILE_CHECK_IN", (new Gson()).toJson(obj));
            getActivity().startActivity(next);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
