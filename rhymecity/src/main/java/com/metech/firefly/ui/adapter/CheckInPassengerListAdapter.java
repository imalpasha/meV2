package com.metech.firefly.ui.adapter;

/*
public class CheckInPassengerListAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener{

    private final Activity context;
    private final List<MobileCheckinReceive.Passenger> obj;
    private MobileCheckInFragment2 fragment;
    private FragmentManager fm;
    private ViewHolder vh;
    private int selectedPosition;
    private String DATEPICKER_TAG = "DATEPICKER_TAG";

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    final DatePickerDialog datePickerExpire = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    public CheckInPassengerListAdapter(Activity context, List<MobileCheckinReceive.Passenger> paramObj,MobileCheckInFragment2 frag,FragmentManager passFM) {
        this.context = context;
        this.obj = paramObj;
        this.fragment = frag;
        fm = passFM;
        datePickerExpire.setYearRange(year, year+20);
    }

    @Override
    public int getCount() {
        return obj == null ? 0 : obj.size();
    }

    @Override
    public Object getItem(int position) {
        return obj == null ? null : obj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.txtPassengerName) TextView txtPassengerName;
        @InjectView(R.id.txtSeat) TextView txtSeat;
        @InjectView(R.id.passengerCheckInCheckBox) CheckBox passengerCheckInCheckBox;
        @InjectView(R.id.checkInPassenger) LinearLayout checkInPassenger;
        @InjectView(R.id.checkInPassengerIssuingCountry) TextView checkInPassengerIssuingCountry;
        @InjectView(R.id.checkInPassengerTravelDoc) TextView checkInPassengerTravelDoc;
        @InjectView(R.id.checkInPassengerDocNo) EditText checkInPassengerDocNo;
        @InjectView(R.id.linearCheckInExpireDate) LinearLayout linearCheckInExpireDate;
        @InjectView(R.id.checkInExpireDate) TextView checkInExpireDate;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.check_in_passenger_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        }
        else {
            vh = (ViewHolder) view.getTag();
       }


        if(obj.get(position).getStatus().equals("Checked In")){
            vh.passengerCheckInCheckBox.setVisibility(View.INVISIBLE);
        }else{
            vh.passengerCheckInCheckBox.setVisibility(View.VISIBLE);
        }

        //obj.get(position).setChecked("N");
        vh.passengerCheckInCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    obj.get(position).setChecked("Y");
                    vh.checkInPassenger.setVisibility(View.VISIBLE);
                    Log.e("PositionY", Integer.toString(position));
                    notifyDataSetChanged();

                } else {
                    obj.get(position).setChecked("N");
                    vh.checkInPassenger.setVisibility(View.GONE);
                    Log.e("PositionN", Integer.toString(position));
                    notifyDataSetChanged();

                }
            }
        });

        vh.checkInPassengerTravelDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.staticPopup(BaseFragment.getTravelDoc(context), context, vh.checkInPassengerTravelDoc, false, vh.linearCheckInExpireDate, "Malaysia IC", CheckInPassengerListAdapter.this);
                selectedPosition = position;

            }
        });

        vh.checkInPassengerIssuingCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(context, BaseFragment.getStaticCountry(context));
                selectedPosition = position;
            }
        });

        vh.checkInExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerExpire.show(fm, DATEPICKER_TAG);
                selectedPosition = position;
            }
        });




        vh.txtPassengerName.setText(obj.get(position).getTitle() + " " + obj.get(position).getFirst_name() + " " + obj.get(position).getLast_name());
        vh.checkInPassengerIssuingCountry.setText(BaseFragment.getCountryName(context, obj.get(position).getIssuing_country()));
        vh.checkInPassengerTravelDoc.setText(BaseFragment.getTravelDocument(context, obj.get(position).getTravel_document()));
        vh.checkInPassengerDocNo.setTag("checkInPassenger" + position);
        vh.checkInPassengerDocNo.setText(obj.get(position).getDocument_number());
        vh.txtSeat.setText(obj.get(position).getSeat());
        vh.checkInExpireDate.setText(BaseFragment.reformatDOB(obj.get(position).getExpiration_date()));

        return view;

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
*/
