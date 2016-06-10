package com.metech.firefly.ui.activity.ManageFlight;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.FlightSummaryReceive;
import com.metech.firefly.api.obj.ManageChangeContactReceive;
import com.metech.firefly.api.obj.SSRReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.module.ManageFlightSpecialMeal;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.ChangeSSR;
import com.metech.firefly.ui.object.GetSSR;
import com.metech.firefly.ui.object.PassengerMeal;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class MF_SpecialServiceRequestFragment extends BaseFragment implements ManageFlightPrenter.ChangeSpecialMealView {

    @Inject
    ManageFlightPrenter presenter;

    @InjectView(R.id.passengerSSRMealList)
    LinearLayout passengerSSRMealList;

    @InjectView(R.id.specialMealLayout)
    LinearLayout specialMealLayout;

    @InjectView(R.id.ssrContinue)
    Button ssrContinue;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;
    private String pnr,username,bookingId,signature;
    private View view;
    private ArrayList<DropDownItem> departMealList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> returnMealList = new ArrayList<DropDownItem>();
    private SharedPrefManager pref;
    private SSRReceive ssrObj;
    private FlightSummaryReceive obj;
    private boolean recreate = true;

    public static MF_SpecialServiceRequestFragment newInstance(Bundle bundle) {

        MF_SpecialServiceRequestFragment fragment = new MF_SpecialServiceRequestFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightSpecialMeal(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.manage_flight_special_request, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");

        Gson gson = new Gson();
        obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        GetSSR getSSR = new GetSSR();
        getSSR.setSignature(obj.getSignature());

        initiateLoading(getActivity());
        presenter.getSSRMeal(getSSR);

        ssrContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMeal();
            }
        });

        return view;
    }


    public void mealSSR(){

		/*Depart Meal*/

        for (int i = 0; i < ssrObj.getMeal().get(0).getList_meal().size(); i++)
        {
            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(ssrObj.getMeal().get(0).getList_meal().get(i).getName());
            itemCountry.setCode(ssrObj.getMeal().get(0).getList_meal().get(i).getMeal_code());
            itemCountry.setTag("Meal");
            itemCountry.setId(i);
            departMealList.add(itemCountry);
        }

        /*Return Meal*/
        if(ssrObj.getMeal().size() > 1){
            for (int i = 0; i < ssrObj.getMeal().get(1).getList_meal().size(); i++)
            {
                DropDownItem itemCountry = new DropDownItem();
                itemCountry.setText(ssrObj.getMeal().get(1).getList_meal().get(i).getName());
                itemCountry.setCode(ssrObj.getMeal().get(1).getList_meal().get(i).getMeal_code());
                itemCountry.setTag("Meal");
                itemCountry.setId(i);
                returnMealList.add(itemCountry);
            }
        }
    }

    public String getMealCode(String mealName,String type){
        String mealCode = "";

        if(type.equals("Depart")){
            for (int i = 0; i < ssrObj.getMeal().get(0).getList_meal().size(); i++)
            {
                if(mealName.equals(ssrObj.getMeal().get(0).getList_meal().get(i).getName())){
                    mealCode = ssrObj.getMeal().get(0).getList_meal().get(i).getMeal_code();
                    break;
                }
            }
        }else{
            for (int i = 0; i < ssrObj.getMeal().get(1).getList_meal().size(); i++)
            {
                if(mealName.equals(ssrObj.getMeal().get(1).getList_meal().get(i).getName())){
                    mealCode = ssrObj.getMeal().get(1).getList_meal().get(i).getMeal_code();
                    break;
                }
            }
        }

        return mealCode;
    }

    public void displaySSRMeal(){

        recreate = false;
        specialMealLayout.setVisibility(View.VISIBLE);
        mealSSR();

        //Services & Fee
        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.4f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.6f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        int departLoop = 1;
        int returnLoop = 1;

        for(int services = 0 ; services < ssrObj.getMeal().size() ; services++){

            String ssrFlightDestination = ssrObj.getMeal().get(services).getDestination_name();
            TextView txtFlightType = new TextView(getActivity());
            txtFlightType.setText(ssrFlightDestination);
            txtFlightType.setTypeface(null, Typeface.BOLD);
            txtFlightType.setGravity(Gravity.LEFT);
            txtFlightType.setPadding(0, 10, 0, 10);

            passengerSSRMealList.addView(txtFlightType);

            for(int servicesLoop = 0 ; servicesLoop < ssrObj.getMeal().get(services).getPassenger().size() ; servicesLoop++){

                LinearLayout servicesRow = new LinearLayout(getActivity());
                servicesRow.setOrientation(LinearLayout.VERTICAL);
                servicesRow.setPadding(2, 2, 2, 2);
                servicesRow.setWeightSum(1);
                servicesRow.setLayoutParams(matchParent);
                servicesRow.setBackgroundResource(R.drawable.drawable_login_bottom_border);

                String passengerName = ssrObj.getMeal().get(services).getPassenger().get(servicesLoop).getPassenger_name();

                TextView txtName = new TextView(getActivity());
                txtName.setTypeface(null, Typeface.BOLD);
                txtName.setText(passengerName);
                txtName.setPadding(0, 5, 0, 5);

                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                llp.setMargins(0, 5, 0, 5); // llp.setMargins(left, top, right, bottom);

                final TextView txtMealList = new TextView(getActivity());
                txtMealList.setBackgroundResource(R.drawable.block_with_border);
                txtMealList.setText(ssrObj.getMeal().get(services).getPassenger().get(servicesLoop).getMeal_name());
                txtMealList.setPadding(7, 7, 7, 7);
                txtMealList.setLayoutParams(llp);
                //onclick meal
                if(services == 0){
                    txtMealList.setTag("passenger_depart"+Integer.toString(departLoop));
                    txtMealList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupSelection(departMealList, getActivity(), txtMealList, false, view);
                        }
                    });

                    departLoop++;

                }else{
                    txtMealList.setTag("passenger_return"+Integer.toString(returnLoop));

                    txtMealList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupSelection(returnMealList, getActivity(), txtMealList, false, view);
                        }
                    });

                    returnLoop++;
                }


                servicesRow.addView(txtName);
                servicesRow.addView(txtMealList);
                servicesRow.setPadding(0, 10, 0, 20);

                /*if(servicesLoop == obj.getMeal().get(services).getPassenger().size() - 1){
                    //margin bottom
                    servicesRow.setPadding(0, 0, 0, 25);
                }else{
                    servicesRow.setPadding(0, 10, 0, 10);
                }*/

                passengerSSRMealList.addView(servicesRow);
            }

        }

        RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());

    }

    public void changeMeal() {

        //getMealCode

        ChangeSSR changeSSR = new ChangeSSR();
        changeSSR.setSignature(obj.getSignature());
        changeSSR.setPnr(obj.getItenerary_information().getPnr());

        changeSSR.setBooking_id(obj.getBooking_id());

        if (!ssrObj.getMeal().get(0).getFlight_status().equals("departed")) {

            ArrayList<PassengerMeal> departMeal = new ArrayList<PassengerMeal>();
            for (int y = 0; y < ssrObj.getMeal().get(0).getPassenger().size(); y++) {

                TextView mealCodePerPassenger = (TextView) view.findViewWithTag("passenger_depart" + Integer.toString(y + 1));
                String departMealCode = getMealCode(mealCodePerPassenger.getText().toString(), "Depart");

                PassengerMeal passengerMeal = new PassengerMeal();
                passengerMeal.setPassenger_number(Integer.toString(y));
                passengerMeal.setMeal_code(departMealCode);

                departMeal.add(passengerMeal);

            }
            changeSSR.setGoing_flightMeal(departMeal);
        }


            ArrayList<PassengerMeal> returnMeal = new ArrayList<PassengerMeal>();

            if(ssrObj.getMeal().size() > 1){

                if (!ssrObj.getMeal().get(1).getFlight_status().equals("departed")) {

                    for (int x = 0; x < ssrObj.getMeal().get(1).getPassenger().size(); x++) {

                        //getMealCode
                        TextView mealCodePerPassenger2 = (TextView) view.findViewWithTag("passenger_return" + Integer.toString(x + 1));
                        String returnMealCode = getMealCode(mealCodePerPassenger2.getText().toString(), "Return");

                        PassengerMeal passengerMeal = new PassengerMeal();
                        passengerMeal.setPassenger_number(Integer.toString(x));
                        passengerMeal.setMeal_code(returnMealCode);

                        returnMeal.add(passengerMeal);

                    }

                    changeSSR.setReturn_flightMeal(returnMeal);

                }

            }

        initiateLoading(getActivity());
        presenter.changeMeal(changeSSR);

    }

    @Override
    public void onChangeSSRReceive(ManageChangeContactReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onSSRReceive(SSRReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            ssrObj = obj;
            displaySSRMeal();
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(recreate){
            if(result.size() > 0){
                Gson gson = new Gson();
                SSRReceive obj = gson.fromJson(result.get(0).getCachedResult(), SSRReceive.class);
                onSSRReceive(obj);
            }
        }else{
            if(result.size() > 0){
                Gson gson = new Gson();
                ManageChangeContactReceive obj = gson.fromJson(result.get(0).getCachedResult(), ManageChangeContactReceive.class);
                onChangeSSRReceive(obj);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
