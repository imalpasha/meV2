package com.fly.firefly.ui.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.BaseExpandableListAdapter;

import com.androidquery.AQuery;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInFragment2;
import com.fly.firefly.utils.LazyList.ImageLoader;
import com.fly.firefly.utils.SharedPrefManager;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.android.gms.analytics.ecommerce.Product;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CheckInAdapter extends BaseExpandableListAdapter implements DatePickerDialog.OnDateSetListener{

    private AQuery aq;
    private Activity _context;
    private List<Product> _listDataChild;
    private static SharedPrefManager prefer;
    private Bundle _bundle;
    private ImageLoader imageLoader;
    private final Activity context;
    private final List<MobileCheckinReceive.Passenger> obj;
    private MobileCheckInFragment2 fragment;
    private FragmentManager fm;
    private ViewHolderHeader vh;
    private ViewHolderContent v2;
    private Boolean bonusLinkNeedUpdate,docNoNeedUpdate;

    private int selectedPosition;
    private String DATEPICKER_TAG = "DATEPICKER_TAG";

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    final DatePickerDialog datePickerExpire = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    static class ViewHolderHeader {
        @InjectView(R.id.txtPassengerName) TextView txtPassengerName;
        @InjectView(R.id.txtSeat) TextView txtSeat;
        @InjectView(R.id.passengerCheckInCheckBox)CheckBox passengerCheckInCheckBox;
        @InjectView(R.id.txtCheckInStatus)TextView txtCheckInStatus;
    }

    static class ViewHolderContent {
        @InjectView(R.id.checkInPassenger) LinearLayout checkInPassenger;
        @InjectView(R.id.checkInPassengerIssuingCountry) TextView checkInPassengerIssuingCountry;
        @InjectView(R.id.checkInPassengerTravelDoc) TextView checkInPassengerTravelDoc;
        @InjectView(R.id.checkInPassengerDocNo)EditText checkInPassengerDocNo;
        @InjectView(R.id.linearCheckInExpireDate) LinearLayout linearCheckInExpireDate;
        @InjectView(R.id.checkInExpireDate) TextView checkInExpireDate;
        @InjectView(R.id.checkInPassengerBonuslink)EditText checkInPassengerBonuslink;

    }

    public CheckInAdapter(Activity context, List<MobileCheckinReceive.Passenger> paramObj,MobileCheckInFragment2 frag,FragmentManager passFM) {
        this.context = context;
        this.obj = paramObj;
        this.fragment = frag;
        fm = passFM;
        datePickerExpire.setYearRange(year, year+20);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<MobileCheckinReceive.Passenger> xxx = obj;
        return xxx.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public Product getItem(int arg0) {

        return _listDataChild.get(arg0);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.check_in_passenger_child, parent, false);
            v2 = new ViewHolderContent();
            ButterKnife.inject(v2, convertView);
            convertView.setTag(v2);
        }
        else {
            v2 = (ViewHolderContent) convertView.getTag();
        }
        if(!obj.get(groupPosition).getTravel_document().equals("NRIC")){
            v2.linearCheckInExpireDate.setVisibility(View.VISIBLE);
            v2.checkInExpireDate.setText(BaseFragment.reformatDOB(obj.get(groupPosition).getExpiration_date()));
        }

        v2.checkInPassengerTravelDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.staticPopup(BaseFragment.getTravelDoc(context), context, v2.checkInPassengerTravelDoc, false, v2.linearCheckInExpireDate, "Malaysia IC", CheckInAdapter.this);
                selectedPosition = groupPosition;

            }
        });

        v2.checkInPassengerIssuingCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(context, BaseFragment.getStaticCountry(context));
                selectedPosition = groupPosition;
            }
        });

        v2.checkInExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerExpire.show(fm, DATEPICKER_TAG);
                selectedPosition = groupPosition;
            }
        });

        /* Document No Update */
        final EditText editCheckInPassengerDocNo = (EditText) convertView.findViewById(R.id.checkInPassengerDocNo);
        v2.checkInPassengerDocNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                obj.get(groupPosition).setDocument_number(editCheckInPassengerDocNo.getText().toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

        });

        /* Document No Update */
        final EditText editCheckInPassengerBonusLink = (EditText) convertView.findViewById(R.id.checkInPassengerBonuslink);
        v2.checkInPassengerBonuslink.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                obj.get(groupPosition).setBonuslink(editCheckInPassengerBonusLink.getText().toString());

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

        });

        /*BonusLink Update on loss focus*/
        /*v2.checkInPassengerBonuslink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean gainFocus) {
                //onFocus
                if (gainFocus) {
                    //Log.e("Gain Focus", "true");
                    //bonusLinkNeedUpdate = true;
                }
                //onBlur
                else {
                   // if (bonusLinkNeedUpdate) {
                        obj.get(groupPosition).setBonuslink(v2.checkInPassengerBonuslink.getText().toString());
                        bonusLinkNeedUpdate = false;
                        //Log.e("Gain Focus", "false");
                    //}
                }
            }
        });*/

        v2.checkInPassengerIssuingCountry.setText(BaseFragment.getCountryName(context, obj.get(groupPosition).getIssuing_country()));
        v2.checkInPassengerTravelDoc.setText(BaseFragment.getTravelDocument(context, obj.get(groupPosition).getTravel_document()));
        v2.checkInPassengerDocNo.setText(obj.get(groupPosition).getDocument_number());
        v2.checkInPassengerBonuslink.setText(obj.get(groupPosition).getBonuslink());
        convertView.setTag(v2);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       // List<Product> chList = obj.get(groupPosition).getItems();
       // return chList.size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.obj.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.obj.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //LayoutInflater li = LayoutInflater.from(context);
        //convertView = li.inflate(R.layout.check_in_passenger_list, null);
        //convertView = LayoutInflater.from(context).inflate(R.layout.check_in_passenger_list, parent, false);
       // vh = new ViewHolder();
        final ExpandableListView eLV = (ExpandableListView) parent;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.check_in_passenger_list, parent, false);
            vh = new ViewHolderHeader();
            ButterKnife.inject(vh, convertView);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolderHeader) convertView.getTag();
        }


        if(obj.get(groupPosition).getStatus().equals("Checked In")){
            vh.passengerCheckInCheckBox.setVisibility(View.INVISIBLE);
            vh.txtCheckInStatus.setVisibility(View.VISIBLE);
            vh.txtCheckInStatus.setText(obj.get(groupPosition).getStatus());


        }else{
            vh.passengerCheckInCheckBox.setVisibility(View.VISIBLE);
            vh.txtCheckInStatus.setVisibility(View.GONE);
            vh.txtCheckInStatus.setText(obj.get(groupPosition).getStatus());
        }

        //obj.get(position).setChecked("N");
        vh.passengerCheckInCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    obj.get(groupPosition).setChecked("Y");
                    eLV.expandGroup(groupPosition);
                } else {
                    obj.get(groupPosition).setChecked("N");
                    eLV.collapseGroup(groupPosition);
                }
                notifyDataSetChanged();
            }
        });

        vh.txtPassengerName.setText(obj.get(groupPosition).getTitle() + " " + obj.get(groupPosition).getFirst_name() + " " + obj.get(groupPosition).getLast_name());
        vh.txtSeat.setText(obj.get(groupPosition).getSeat());

        if(isExpanded){
            vh.passengerCheckInCheckBox.setChecked(true);
        }
        return convertView;
    }

    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                CountryListDialogFragment countryListDialogFragment = CountryListDialogFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(fragment, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10) { varMonth = "0";}
        if (day < 10) { varDay = "0";}

        obj.get(selectedPosition).setExpiration_date(year + "-" + varMonth + "" + (month+1) + "-" + varDay + "" + day);
        notifyDataSetChanged();
    }

    public void setSelectedCountry(String selectedCountry, String selectedCountryCode){
        obj.get(selectedPosition).setIssuing_country(selectedCountryCode);
        notifyDataSetChanged();
    }

    public void returnNotifyDataChanged(String selected){
        obj.get(selectedPosition).setTravel_document(selected);
        notifyDataSetChanged();
    }
}
