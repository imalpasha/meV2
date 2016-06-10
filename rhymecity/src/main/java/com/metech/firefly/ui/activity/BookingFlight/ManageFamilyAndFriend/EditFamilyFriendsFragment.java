package com.metech.firefly.ui.activity.BookingFlight.ManageFamilyAndFriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.ContactInfoReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.DropDownItem;
import com.metech.firefly.utils.RealmObjectController;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import javax.inject.Inject;
import butterknife.InjectView;

public class EditFamilyFriendsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.txtFFtitle)
    TextView txtFFtitle;

    @InjectView(R.id.txtFFfirstName)
    EditText txtFFfirstName;

    @InjectView(R.id.txtFFLastName)
    EditText txtFFLastName;

    @InjectView(R.id.txtFFDob)
    TextView txtFFDob;

    @InjectView(R.id.txtFFNationality)
    TextView txtFFNationality;

    @InjectView(R.id.txtFFBonuslink)
    EditText txtFFBonuslink;

    @InjectView(R.id.txtFFTravellingWith)
    TextView txtFFTravellingWith;

    @InjectView(R.id.ffGenderBlock)
    LinearLayout ffGenderBlock;

    @InjectView(R.id.ffTravellingWithBlock)
    LinearLayout ffTravellingWithBlock;



    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);

    private DatePickerDialog datePickerYear1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private ArrayList<DefaultPassengerObj> friendAndFamilyObj = new ArrayList<DefaultPassengerObj>();
    private ArrayList<DropDownItem> titleList;
    private ArrayList<DropDownItem> countrys;

    private String DATEPICKER_TAG = "DATEPICKER_TAG";
    private int fragmentContainerId;
    private int ffLoop;
    View view;

    public static EditFamilyFriendsFragment newInstance(Bundle bundle) {

        EditFamilyFriendsFragment fragment = new EditFamilyFriendsFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new ItinenaryModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.edit_family_friend, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        String familyFriend = bundle.getString("FRIEND_AND_FAMILY_");

        Gson gson = new Gson();
        final DefaultPassengerObj obj = gson.fromJson(familyFriend, DefaultPassengerObj.class);

        titleList = new ArrayList<DropDownItem>();
        countrys = new ArrayList<DropDownItem>();

        /*Display Title Data*/
        titleList = getStaticTitle(getActivity());
        countrys = getStaticCountry(getActivity());

        if(obj.getPassenger_type().equals("ADULT")){
            ffGenderBlock.setVisibility(View.GONE);
            ffTravellingWithBlock.setVisibility(View.GONE);
        }

        //set value ff
        txtFFtitle.setText(getTitleCode(getActivity(),obj.getTitle(),"name"));
        txtFFtitle.setTag(obj.getTitle());
        txtFFfirstName.setText(obj.getFirst_name());
        txtFFLastName.setText(obj.getLast_name());
        txtFFDob.setText(reformatDOB(obj.getDob()));
        txtFFNationality.setText(getCountryName(getActivity(),obj.getNationality()));
        txtFFNationality.setTag(obj.getNationality());
        txtFFBonuslink.setText(obj.getBonuslink_card());

        txtFFtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(titleList, getActivity(), txtFFtitle, true ,view);
            }
        });

        txtFFNationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(countrys, getActivity(), txtFFNationality, true ,view);
            }
        });

        txtFFDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //createDatePickerObject(selectedPassenger);

                String currentDOB = txtFFDob.getText().toString();
                if(!currentDOB.equals("")){
                    String[] splitReturn = currentDOB.split("/");
                    createDateObj(Integer.parseInt(splitReturn[2]), Integer.parseInt(splitReturn[1])-1, Integer.parseInt(splitReturn[0]));
                }else{
                    createDateObj(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        });

        return view;
    }

    public void createDateObj(Integer year , Integer month , Integer day){

        datePickerYear1 = DatePickerDialog.newInstance(this,year,month,day);
        datePickerYear1.setYearRange(calendar.get(Calendar.YEAR) - 80, calendar.get(Calendar.YEAR));

        if(checkFragmentAdded()){
            datePickerYear1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 9) {
            varMonth = "0";

        }
        if(day < 10){
            varDay = "0";
        }

        txtFFDob.setText(varDay+""+day + "/"+varMonth + "" + (month+1)+ "/" + year);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        //presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //
        // presenter.onPause();
    }
}
