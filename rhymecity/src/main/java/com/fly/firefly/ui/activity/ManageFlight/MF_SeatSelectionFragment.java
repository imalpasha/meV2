package com.fly.firefly.ui.activity.ManageFlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ChangeSearchFlightReceive;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.ItinenaryActivity;
import com.fly.firefly.ui.activity.BookingFlight.PaymentFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV1;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV2;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV3;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV4;
import com.fly.firefly.ui.module.ManageChangeSeatModule;
import com.fly.firefly.ui.module.SeatSelectionModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.PasssengerInfoV2;
import com.fly.firefly.ui.object.SeatAvailabilityRequest;
import com.fly.firefly.ui.object.SeatInfo;
import com.fly.firefly.ui.object.SeatSelect;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SeatSetup;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;

public class MF_SeatSelectionFragment extends BaseFragment implements ManageFlightPrenter.ChangeSeatView{

    @Inject
    ManageFlightPrenter presenter;

    @InjectView(R.id.btnSeat)
    Button btnSeat ;

    @InjectView(R.id.seatListDepart)
    LinearLayout seatListDepart;

    @InjectView(R.id.seatListReturn)
    LinearLayout seatListReturn;

    @InjectView(R.id.listPassengerDepart)
    ExpandAbleGridView listPassengerDepart;

    @InjectView(R.id.listPassengerReturn)
    ExpandAbleGridView listPassengerReturn;

    @InjectView(R.id.txtSeatDeparture)
    TextView txtSeatDeparture;

    @InjectView(R.id.txtSeatReturn)
    TextView txtSeatReturn;

    @InjectView(R.id.passengerSeatListReturn)
    LinearLayout passengerSeatListReturn;

    @InjectView(R.id.seatPriceList)
    LinearLayout seatPriceList;

    private SharedPrefManager pref;
    private PassengerSeatAdapterV3 passengerSeatListV1;
    private PassengerSeatAdapterV4 passengerSeatListV2;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Edit Seat Selection";
    private List<String> seatTag1;
    private List<String> seatTag2;

    private List<String> selectedSeatTag;

    //newLogin2/22
    private int passengerNoV1 = 0;
    private int passengerNoV2;
    private Boolean next1 = true;
    private Boolean next2 = false;
    private int passengerSize;

    private View view;
    private String bookingID,signature;
    private List<SeatInfo> seatInfoReturn;
    private List<SeatInfo> seatInfoDepart;
    private boolean twoWay = false;
    private String pnr,username,bookingId;
    private boolean retrieveSeat;
    private boolean clicked = false;
    private boolean seatDisplayed = false;
    private String activeSeatType,activeSeatType2;
    private boolean clickable,clickable2;
    private String originalSeatType,originalSeatType2;

    int h = 0;
    List<ContactInfoReceive.PasssengerInfo> passengers =  new ArrayList<ContactInfoReceive.PasssengerInfo>();
    List<PasssengerInfoV2> objV2 = new ArrayList<PasssengerInfoV2>();
    List<PasssengerInfoV2> objV3 = new ArrayList<PasssengerInfoV2>();

    public static MF_SeatSelectionFragment newInstance(Bundle bundle) {

        MF_SeatSelectionFragment fragment = new MF_SeatSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageChangeSeatModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.seat_selection, container, false);
        ButterKnife.inject(this, view);


        pref = new SharedPrefManager(getActivity());
        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");

        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);
        pnr = obj.getObj().getItenerary_information().getPnr();
        bookingId = obj.getObj().getBooking_id();

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());


        SeatAvailabilityRequest requestSeatObj = new SeatAvailabilityRequest();
        requestSeatObj.setPnr(pnr);
        requestSeatObj.setSignature(signature);
        requestSeatObj.setBooking_id(bookingId);
        initiateLoading(getActivity());


        getSeatAvailability(requestSeatObj);



        listPassengerDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                PasssengerInfoV2 selectedFromList = (PasssengerInfoV2) (listPassengerDepart.getItemAtPosition(myItemInt));
                //setSeat(seatInfoDepart);

                if(selectedFromList.getCheckedIn().equals("N")){

                seatListReturn.setVisibility(View.GONE);
                seatListDepart.setVisibility(View.VISIBLE);

                passengerSeatListV1.clearSelected();

                if(twoWay){
                    passengerSeatListV2.clearSelected();
                }
                seatTag1 = new ArrayList<>(1);

                if(myItemInt < passengerSize-1){
                    next1 = false;
                }else{
                    next1 = true;
                }

                passengerNoV1 = myItemInt;
                //Set selected
                LinearLayout clickedPassenger = (LinearLayout) myView.findViewById(R.id.passengerLinearLayout);
                clickedPassenger.setBackgroundColor(getResources().getColor(R.color.blue));
                selectedFromList.setSelected(true);
                selectedFromList.setActive(true);

            }else{
                    Utils.toastNotification(getActivity(),"Passenger already checked-in");
                }

                //get seat type
                activeSeatType = getSeatType(selectedFromList.getSeat(),"DEPART");
                originalSeatType = passengerSeatListV1.getNextPassengerOriginalSeatType();

                //resetOnClickSeat1(activeSeatType);
                Log.e("activeSeatType",activeSeatType);

            }
        });

        listPassengerReturn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                PasssengerInfoV2 selectedFromList = (PasssengerInfoV2) (listPassengerReturn.getItemAtPosition(myItemInt));

                if(selectedFromList.getCheckedIn().equals("N")) {

                    seatListReturn.setVisibility(View.VISIBLE);
                    seatListDepart.setVisibility(View.GONE);

                    passengerSeatListV2.clearSelected();
                    passengerSeatListV1.clearSelected();

                    seatTag2 = new ArrayList<>(1);

                    if (myItemInt < passengerSize - 1) {
                        next2 = false;
                    } else {
                        next2 = true;
                    }

                    passengerNoV2 = myItemInt;
                    //Set selected
                    LinearLayout clickedPassenger = (LinearLayout) myView.findViewById(R.id.passengerLinearLayout);
                    clickedPassenger.setBackgroundColor(getResources().getColor(R.color.blue));
                    selectedFromList.setSelected(true);
                    selectedFromList.setActive(true);
                }else{
                    Utils.toastNotification(getActivity(),"Passenger already checked-in");
                }

                //get seat type
                activeSeatType2 = getSeatType(selectedFromList.getSeat(),"RETURN");
                originalSeatType2 = passengerSeatListV2.getNextPassengerOriginalSeatType();
                Log.e(activeSeatType2,originalSeatType2);
            }
        });


        btnSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<SeatSelect> goingSeat = new ArrayList<SeatSelect>();
                ArrayList<SeatSelect> returnSeat = new ArrayList<SeatSelect>();
                Boolean oneWayProceed = true;
                Boolean twoWayProceed = true;

                for(int x = 0 ; x < passengers.size() ; x++){

                    if(objV2.get(x).getSeat() != null)
                    {
                        SeatSelect obj = new SeatSelect();
                        obj.setCompartment_designator(objV2.get(x).getCompartment());
                        obj.setSeat_number(objV2.get(x).getSeat());
                        goingSeat.add(obj);
                        oneWayProceed = true;

                    }else{
                        oneWayProceed = false;
                    }

                }
                if(twoWay && oneWayProceed){
                    for(int x = 0 ; x < passengers.size() ; x++){

                        if(objV3.get(x).getSeat() != null){
                            SeatSelect obj = new SeatSelect();
                            obj.setCompartment_designator(objV3.get(x).getCompartment());
                            obj.setSeat_number(objV3.get(x).getSeat());
                            returnSeat.add(obj);
                            twoWayProceed = true;
                        }else{
                            twoWayProceed = false;
                        }

                    }
                }

                if(!oneWayProceed || !twoWayProceed){
                    Crouton.makeText(getActivity(), "Please select seat", Style.ALERT).show();
                }else{
                    //Validate
                    ManageSeatInfo seatObj = new ManageSeatInfo();
                    seatObj.setBooking_id(bookingId);
                    seatObj.setSignature(signature);
                    seatObj.setGoing(goingSeat);
                    seatObj.setReturnFlight(returnSeat);

                    seatSelect(seatObj);
                }
            }
        });

        return view;
    }

    public void getSeatAvailability(SeatAvailabilityRequest obj){

        retrieveSeat = true;
        presenter.onSeatAvailability(obj);

    }

    public void displaySeatFee(ContactInfoReceive contactObj){

        retrieveSeat = false;

        //Services & Fee
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(34, 34);
        llp.setMargins(0, 0, 8, 0); // llp.setMargins(left, top, right, bottom);

        for(int services = 0 ; services < contactObj.getSeat_fare().size() ; services++){

            LinearLayout servicesRow = new LinearLayout(getActivity());
            servicesRow.setOrientation(LinearLayout.HORIZONTAL);
            servicesRow.setPadding(2, 2, 2, 2);
            servicesRow.setWeightSum(1);
            servicesRow.setLayoutParams(matchParent);

            LinearLayout seatColour = new LinearLayout(getActivity());
            seatColour.setLayoutParams(llp);

            String seatType = contactObj.getSeat_fare().get(services).getName();
            String seatPrice = contactObj.getSeat_fare().get(services).getPrice();

            if(seatType.equals("Standard Seat")){
                seatColour.setBackgroundResource(R.color.standard_seat);
            }else if(seatType.equals("Preferred Seat")){
                seatColour.setBackgroundResource(R.color.preferred_seat);
            }else if(seatType.equals("Desired Seat")){
                seatColour.setBackgroundResource(R.color.desired_seat);
            }


            TextView txtSeatType = new TextView(getActivity());
            txtSeatType.setText(seatType);
            txtSeatType.setTextSize(12);

            TextView txtSeatPrice = new TextView(getActivity());
            txtSeatPrice.setText(seatPrice);
            txtSeatPrice.setTextSize(12);
            txtSeatPrice.setPadding(15,0,0,0);

            servicesRow.addView(seatColour);
            servicesRow.addView(txtSeatType);
            servicesRow.addView(txtSeatPrice);

            seatPriceList.addView(servicesRow);

        }

    }

    public void setPassenger1(String type,ExpandAbleGridView list,TextView txtSeat,List<PasssengerInfoV2> passengers,String depart,String arrival){

        //auto select non check-in passenger
        for(int t = 0; t < passengers.size(); t++){
            if(passengers.get(t).getCheckedIn().equals("N")){
                passengers.get(t).setSelected(true);
                passengerNoV1 = t;
                break;
            }
        }


        txtSeat.setText(depart + " - " + arrival);
        passengerSeatListV1 = new PassengerSeatAdapterV3(getActivity(),passengers,this);
        list.setAdapter(passengerSeatListV1);

    }

    public void setPassenger2(String type,ExpandAbleGridView list,TextView txtSeat,List<PasssengerInfoV2> passengers,String depart,String arrival){

        txtSeat.setText(depart + " - " + arrival);
        passengerSeatListV2 = new PassengerSeatAdapterV4(getActivity(),passengers,this);
        list.setAdapter(passengerSeatListV2);

    }

    public void setSeat1(LinearLayout seatList,List<SeatInfo> seatInfo){

        int seatSize = seatInfo.size();
        int seatCount = 0;
        /*Set Seat Row*/
        List<String> tempSeatStorage = new ArrayList<String>();
        List<SeatSetup> tempSeatValue = new ArrayList<SeatSetup>();

        for(int seatNumber = 0 ; seatNumber < seatSize ; seatNumber++){

            boolean injectSeatSetup;

            String seatNumberEx = seatInfo.get(seatNumber).getSeat_number();
            String seatNumberWithoutAlphabet = seatNumberEx.substring(0, seatNumberEx.length()-1);
            String lastSeatCharacter = seatNumberEx.substring(seatNumberEx.length() - 1);

            if (tempSeatStorage.contains(seatNumberWithoutAlphabet)){
                /*SKIP*/
                injectSeatSetup = false;
            }else {
                tempSeatStorage.add(seatNumberWithoutAlphabet);
                seatCount++;
                injectSeatSetup = true;
            }

            if(injectSeatSetup){
                /*SeatNumberByRow*/
                ArrayList<SeatInfo> seat = new ArrayList<SeatInfo>();

                for(int seatNumberRow = 0 ; seatNumberRow < seatSize ; seatNumberRow++){

                    String seatNumberExx = seatInfo.get(seatNumberRow).getSeat_number();
                    String seatNumberWithoutAlphabex = seatNumberExx.substring(0, seatNumberExx.length() - 1);

                    if(tempSeatStorage.get(seatCount-1).equals(seatNumberWithoutAlphabex)){
                        SeatInfo rebuildSeatInfo = new SeatInfo();
                        rebuildSeatInfo.setCompartment_designator(seatInfo.get(seatNumberRow).getCompartment_designator());
                        rebuildSeatInfo.setSeat_number(seatInfo.get(seatNumberRow).getSeat_number());
                        rebuildSeatInfo.setSeat_type(seatInfo.get(seatNumberRow).getSeat_type());
                        rebuildSeatInfo.setStatus(seatInfo.get(seatNumberRow).getStatus());
                        seat.add(rebuildSeatInfo);
                    }
                }

                SeatSetup seatSetup = new SeatSetup();
                seatSetup.setSeatRow(tempSeatStorage.get(seatCount - 1));
                seatSetup.setSeatRowArray(seat);
                tempSeatValue.add(seatSetup);
            }

        }

        seatTag1 = new ArrayList<>(1);
        selectedSeatTag = new ArrayList<>(2);

        for (int label = 0; label < tempSeatStorage.size(); label++)
        {
            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);


            for (int x = 1; x < tempSeatValue.get(label).getSeatRowArray().size()+1; x++)
            {

                String seatNumber = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_number();
                String seatStatus = tempSeatValue.get(label).getSeatRowArray().get(x-1).getStatus();
                final String compartment = tempSeatValue.get(label).getSeatRowArray().get(x-1).getCompartment_designator();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);

                final TextView txtDetailList = new TextView(getActivity());
                final String seatType = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_type();

                txtDetailList.setText(seatNumber);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag(seatNumber);
                txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));

                    txtDetailList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            clickable = true;
                            //check here

                            if(activeSeatType.equals("desired") && (seatType.equals("standard") || seatType.equals("preferred"))){
                                clickable = false;
                            }else if(activeSeatType.equals("preferred")){
                                if(seatType.equals("standard")){
                                    clickable = false;
                                }else{
                                    clickable = true;
                                }
                            }

                            if(originalSeatType != null){
                                if(originalSeatType.equals(seatType)){
                                    clickable = true;
                                }
                            }

                            if(clickable){
                                if (seatTag1.size() == 1) {

                                    //TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                                    TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatListV1.getSelected(passengerNoV1));

                                    seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                    seatToRemove.setClickable(true);
                                    //seatToRemove.setTextColor(getResources().getColor(R.color.white));

                                    seatTag1.remove(0);
                                    seatTag1.add(txtDetailList.getText().toString());

                                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                                    txtDetailList.setClickable(false);

                                    passengerSeatListV1.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                                    passengerSeatListV1.setSelectedCompartmentSeat(compartment);


                                    //passengerSeatListDepart.setSelectedSeatCompartment(passengerSeatListDepart.getSelected(passengerNo));
                                    //selectedSeatTag.add(txtDetailList.getText().toString());
                                } else {

                                    if (passengerSeatListV1.getSelected(passengerNoV1) != null && passengerSeatListV1.getSelected(passengerNoV1) != "") {
                                        TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatListV1.getSelected(passengerNoV1));
                                        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                        seatToRemove.setClickable(true);
                                    }

                                    seatTag1.add(txtDetailList.getText().toString());
                                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                                    txtDetailList.setClickable(false);

                                    passengerSeatListV1.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                                    passengerSeatListV1.setSelectedCompartmentSeat(compartment);


                                    //selectedSeatTag.add(txtDetailList.getText().toString());
                                }

                                if (next1) {

                                    if (passengerNoV1 < passengerSize - 1) {
                                        passengerSeatListV1.setNextPassengerSelected(passengerNoV1 + 1);
                                        activeSeatType = getSeatType(passengerSeatListV1.getCurrentSelected(),"DEPART");
                                        originalSeatType = passengerSeatListV1.getNextPassengerOriginalSeatType();

                                        Log.e(activeSeatType,originalSeatType);

                                    } else {

                                        if (twoWay) {
                                            autoSelectReturnPassenger();
                                            passengerSeatListV2.autoSelectReturnPassenger();
                                            activeSeatType2 = getSeatType(passengerSeatListV2.getCurrentSelected(),"DEPART");
                                            originalSeatType2 = passengerSeatListV2.getNextPassengerOriginalSeatType();

                                        }
                                    }
                                } else {
                                    if (passengerNoV1 < passengerSize - 1) {
                                        passengerSeatListV1.setNextPassengerSelected(passengerNoV1 + 1);
                                        activeSeatType = getSeatType(passengerSeatListV1.getCurrentSelected(),"DEPART");
                                        originalSeatType = passengerSeatListV1.getNextPassengerOriginalSeatType();

                                        //activeSeatType
                                    }
                                }
                            }else{
                                Utils.toastNotification(getActivity(), "Seat downgraded not allowed");
                            }
                        }
                    });

                //"seat_type":"standard",
                //Set color and clickable

                if(seatType.equals("standard")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_standard));

                }else if(seatType.equals("preferred")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_preferred));

                }else if(seatType.equals("desired")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_desired));
                }

                if(!seatStatus.equals("available")){
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5, 5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20, 5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                seatRow.addView(txtDetailList);
            }

            seatList.addView(seatRow);

        }
    }

    public void setSeat2(LinearLayout seatList,List<SeatInfo> seatInfo){

        int seatSize = seatInfo.size();
        int seatCount = 0;
        /*Set Seat Row*/
        List<String> tempSeatStorage = new ArrayList<String>();
        List<SeatSetup> tempSeatValue = new ArrayList<SeatSetup>();

        for(int seatNumber = 0 ; seatNumber < seatSize ; seatNumber++){

            boolean injectSeatSetup;

            String seatNumberEx = seatInfo.get(seatNumber).getSeat_number();
            String seatNumberWithoutAlphabet = seatNumberEx.substring(0, seatNumberEx.length()-1);
            String lastSeatCharacter = seatNumberEx.substring(seatNumberEx.length() - 1);

            if (tempSeatStorage.contains(seatNumberWithoutAlphabet)){
                /*SKIP*/
                injectSeatSetup = false;
            }else {
                tempSeatStorage.add(seatNumberWithoutAlphabet);
                seatCount++;
                injectSeatSetup = true;
            }

            if(injectSeatSetup){
                /*SeatNumberByRow*/
                ArrayList<SeatInfo> seat = new ArrayList<SeatInfo>();

                for(int seatNumberRow = 0 ; seatNumberRow < seatSize ; seatNumberRow++){

                    String seatNumberExx = seatInfo.get(seatNumberRow).getSeat_number();
                    String seatNumberWithoutAlphabex = seatNumberExx.substring(0, seatNumberExx.length() - 1);

                    if(tempSeatStorage.get(seatCount-1).equals(seatNumberWithoutAlphabex)){
                        SeatInfo rebuildSeatInfo = new SeatInfo();
                        rebuildSeatInfo.setCompartment_designator(seatInfo.get(seatNumberRow).getCompartment_designator());
                        rebuildSeatInfo.setSeat_number(seatInfo.get(seatNumberRow).getSeat_number());
                        rebuildSeatInfo.setSeat_type(seatInfo.get(seatNumberRow).getSeat_type());
                        rebuildSeatInfo.setStatus(seatInfo.get(seatNumberRow).getStatus());
                        seat.add(rebuildSeatInfo);
                    }
                }

                SeatSetup seatSetup = new SeatSetup();
                seatSetup.setSeatRow(tempSeatStorage.get(seatCount - 1));
                seatSetup.setSeatRowArray(seat);
                tempSeatValue.add(seatSetup);
            }

        }

        seatTag2 = new ArrayList<>(1);
        selectedSeatTag = new ArrayList<>(2);

        for (int label = 0; label < tempSeatStorage.size(); label++)
        {
            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);


            for (int x = 1; x < tempSeatValue.get(label).getSeatRowArray().size()+1; x++)
            {

                String seatNumber = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_number();
                final String seatType = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_type();
                String seatStatus = tempSeatValue.get(label).getSeatRowArray().get(x-1).getStatus();
                final String compartment = tempSeatValue.get(label).getSeatRowArray().get(x-1).getCompartment_designator();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);


                final TextView txtDetailList = new TextView(getActivity());
                txtDetailList.setText(seatNumber);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag("RETURN" + "_" + seatNumber);
                txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));

                    txtDetailList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            clickable2 = true;

                            if(activeSeatType2.equals("desired") && (seatType.equals("standard") || seatType.equals("preferred"))){
                                clickable2 = false;
                            }else if(activeSeatType2.equals("preferred")){
                                if(seatType.equals("standard")){
                                    clickable2 = false;
                                }else{
                                    clickable2 = true;
                                }
                            }

                            if(originalSeatType2 != null){
                                if(originalSeatType2.equals(seatType)){
                                    clickable2 = true;
                                    Log.e("Clickable","true");
                                }
                                Log.e("Clickable","false");
                            }

                            if(clickable2){
                                if (seatTag2.size() == 1) {

                                    //TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                                    TextView seatToRemove = (TextView) view.findViewWithTag("RETURN" + "_" + passengerSeatListV2.getSelected(passengerNoV2));

                                    seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                    seatToRemove.setClickable(true);
                                    //seatToRemove.setTextColor(getResources().getColor(R.color.white));

                                    seatTag2.remove(0);
                                    seatTag2.add(txtDetailList.getText().toString());

                                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                                    txtDetailList.setClickable(false);

                                    //    if(oneWay){
                                    passengerSeatListV2.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                                    passengerSeatListV2.setSelectedCompartmentSeat(compartment);
                                    //   }else{
                                    //       passengerSeatList.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                                    //       passengerSeatList.setSelectedCompartmentSeat(compartment);
                                    //   }

                                    //passengerSeatListDepart.setSelectedSeatCompartment(passengerSeatListDepart.getSelected(passengerNo));
                                    //selectedSeatTag.add(txtDetailList.getText().toString());
                                } else {

                                    if (passengerSeatListV2.getSelected(passengerNoV2) != null && passengerSeatListV2.getSelected(passengerNoV2) != "") {

                                        TextView seatToRemove = (TextView) view.findViewWithTag("RETURN" + "_" +
                                                passengerSeatListV2.getSelected(passengerNoV2));
                                        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                        seatToRemove.setClickable(true);
                                    }

                                    seatTag2.add(txtDetailList.getText().toString());
                                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                                    txtDetailList.setClickable(false);

                                    passengerSeatListV2.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                                    passengerSeatListV2.setSelectedCompartmentSeat(compartment);

                                    if (next2) {

                                        if (passengerNoV2 < passengerSize - 1) {
                                            passengerSeatListV2.setNextPassengerSelected(passengerNoV2 + 1);
                                            originalSeatType2 = passengerSeatListV2.getNextPassengerOriginalSeatType();
                                            activeSeatType2 = getSeatType(passengerSeatListV2.getCurrentSelected(),"RETURN");
                                            Log.e("originalSeatType2",originalSeatType2);
                                        }
                                    } else {
                                        passengerSeatListV2.setNextPassengerSelected(passengerNoV2 + 1);
                                        activeSeatType2 = getSeatType(passengerSeatListV2.getCurrentSelected(),"RETURN");
                                        originalSeatType2 = passengerSeatListV2.getNextPassengerOriginalSeatType();
                                        Log.e("originalSeatType2",originalSeatType2);
                                    }
                                }
                            }else{
                                Utils.toastNotification(getActivity(), "Seat downgraded not allowed");
                            }

                        }
                    });

                //"seat_type":"standard",
                //Set color and clickable

                if(seatType.equals("standard")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_standard));

                }else if(seatType.equals("preferred")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_preferred));

                }else if(seatType.equals("desired")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_desired));
                }

                if(!seatStatus.equals("available")){
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5, 5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20, 5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                seatRow.addView(txtDetailList);
            }

            seatList.addView(seatRow);

        }
    }

    public void clearSelectedOnFragmentV1(String seat){

        TextView seatToRemove = (TextView) view.findViewWithTag(seat);
        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
        seatToRemove.setClickable(true);
        seatTag1 = new ArrayList<>(1);

    }

    public void clearSelectedOnFragmentV2(String seat){

        TextView seatToRemove = (TextView) view.findViewWithTag("RETURN_"+seat);
        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
        seatToRemove.setClickable(true);
        seatTag2 = new ArrayList<>(1);

    }

    public void clearSeatTag1(int passed){
        seatTag1 = new ArrayList<>(1);
        passengerNoV1 = passed;
        next1 = true;
        //passengerSeatListV1.clearSelected();
        //if(twoWay){
        //    passengerSeatListV2.clearSelected();
        //}
    }

    public void clearSeatTag2(int passed){

        Log.e("passed",Integer.toString(passed));
        seatTag2 = new ArrayList<>(1);
        passengerNoV2 = passed;
        next2 = true;
        //passengerSeatListV1.clearSelected();
        //if(twoWay){
        //    passengerSeatListV2.clearSelected();
        //}
    }

    public void autoSelectReturnPassenger(){

        seatListReturn.setVisibility(View.VISIBLE);
        seatListDepart.setVisibility(View.GONE);

        //setSeat(seatInfoReturn);
        //Clear Previous
        passengerSeatListV2.clearSelected();
        passengerSeatListV1.clearSelected();

        seatTag2 = new ArrayList<>(1);

    }

    public void seatSelect(ManageSeatInfo obj){

        initiateLoading(getActivity());
        retrieveSeat = false;
        presenter.seatSelect(obj,pnr,username,signature);

    }

    @Override
    public void onRequestSeat(ContactInfoReceive obj) {


        dismissLoading();
        if(!seatDisplayed){
            seatInfoDepart = obj.getJourneys().get(0).getSeat_info();

            List<ContactInfoReceive.Journeys> journeys = obj.getJourneys();

        /*Set Passenger to adapter*/
            passengers = obj.getJourneys().get(0).getPassengers();
            activeSeatType = getSeatType(passengers.get(0).getUnit_designator(),"DEPART");

            for(int v = 0 ; v < passengers.size() ; v++){
                PasssengerInfoV2 obj2 = new PasssengerInfoV2();
                obj2.setFirst_name(passengers.get(v).getFirst_name());
                obj2.setLast_name(passengers.get(v).getLast_name());
                obj2.setTitle(passengers.get(v).getTitle());
                obj2.setSeat(passengers.get(v).getUnit_designator());
                obj2.setOriginalSeatType(getSeatType(passengers.get(v).getUnit_designator(),"DEPART"));
                obj2.setCompartment(passengers.get(v).getCompartment_designator());
                obj2.setCheckedIn(passengers.get(v).getChecked_in());
                objV2.add(obj2);

            }
            passengerSize = objV2.size();
            Log.e("passengerSize",Integer.toString(passengerSize));



            if(obj.getJourneys().size() > 1){
                seatInfoReturn = obj.getJourneys().get(1).getSeat_info();
                passengers = obj.getJourneys().get(1).getPassengers();
                activeSeatType2 = getSeatType(passengers.get(0).getUnit_designator(),"RETURN");

                for(int v = 0 ; v < passengers.size() ; v++){
                    PasssengerInfoV2 obj3 = new PasssengerInfoV2();
                    obj3.setFirst_name(passengers.get(v).getFirst_name());
                    obj3.setLast_name(passengers.get(v).getLast_name());
                    obj3.setTitle(passengers.get(v).getTitle());
                    obj3.setSeat(passengers.get(v).getUnit_designator());
                    obj3.setOriginalSeatType(getSeatType(passengers.get(v).getUnit_designator(),"RETURN"));
                    obj3.setCompartment(passengers.get(v).getCompartment_designator());
                    obj3.setCheckedIn(passengers.get(v).getChecked_in());
                    objV3.add(obj3);
                }

                //set total passenger

            }


            setSeat1(seatListDepart, seatInfoDepart);
            setPassenger1("DEPART", listPassengerDepart, txtSeatDeparture, objV2, journeys.get(0).getDeparture_station(), journeys.get(0).getArrival_station());
            seatListReturn.setVisibility(View.GONE);

            if(journeys.size() > 1){

                twoWay = true;
                //seatInfoReturn = obj.getJourneys().get(1).getSeat_info();
                setPassenger2("RETURN",listPassengerReturn,txtSeatReturn,objV3,journeys.get(1).getDeparture_station(),journeys.get(1).getArrival_station());
                setSeat2(seatListReturn, seatInfoReturn);
                passengerSeatListReturn.setVisibility(View.VISIBLE);
            }

            RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());
            displaySeatFee(obj);
            retrieveSeat = true;
            seatDisplayed = true;
        }

    }


    public String getSeatType(String seatUnit,String way){

        String seatType = "";
        if(way.equals("DEPART")){
            for(int c = 0 ; c < seatInfoDepart.size(); c++){
                if(seatUnit.equals(seatInfoDepart.get(c).getSeat_number())){
                    seatType = seatInfoDepart.get(c).getSeat_type();
                }
            }
        }else{
            for(int v = 0 ; v < seatInfoReturn.size(); v++){
                if(seatUnit.equals(seatInfoReturn.get(v).getSeat_number())){
                    seatType = seatInfoReturn.get(v).getSeat_type();
                }
            }
        }

        return seatType;
    }

    @Override
    public void onSeatChange(ManageChangeContactReceive obj) {
        dismissLoading();
        clicked = true;

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(retrieveSeat){
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                ContactInfoReceive obj = gson.fromJson(result.get(0).getCachedResult(), ContactInfoReceive.class);
                onRequestSeat(obj);
            }
        }else{
            Log.e("result",Integer.toString(result.size()));
            Log.e("result",result.toString());
             if(result.size() > 0){
                 Log.e("x","1");
                 Gson gson = new Gson();
                 ManageChangeContactReceive obj = gson.fromJson(result.get(0).getCachedResult(), ManageChangeContactReceive.class);
                 onSeatChange(obj);
             }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
