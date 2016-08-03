package com.metech.firefly.ui.activity.BookingFlight.ManageFamilyAndFriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.DeleteFFReceive;
import com.metech.firefly.api.obj.ListBookingReceive;
import com.metech.firefly.api.obj.SearchFlightReceive;
import com.metech.firefly.api.obj.SelectFlightReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.module.DeleteFFModule;
import com.metech.firefly.ui.object.BoardingPassPNRList;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.object.FamilyFriendList;
import com.metech.firefly.ui.object.FamilyFriendObj;
import com.metech.firefly.ui.object.FriendFamilyDelete;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import javax.inject.Inject;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

public class ManageFriendAndFamilyFragment extends BaseFragment implements BookingPresenter.DeleteFamilyView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.appendFamilyFriends)
    LinearLayout appendFamilyFriends;

    @InjectView(R.id.btnAddInfant)
    Button btnAddInfant;

    @InjectView(R.id.btnAddAdult)
    Button btnAddAdult;

    @InjectView(R.id.btnReturnPassenger)
    Button btnReturnPassenger;

    @InjectView(R.id.noRecordFound)
    LinearLayout noRecordFound;

    private ArrayList<DefaultPassengerObj> friendAndFamilyObj = new ArrayList<DefaultPassengerObj>();
    private int fragmentContainerId;
    private int ffLoop;
    private SharedPrefManager pref;
    private String email;

    public static ManageFriendAndFamilyFragment newInstance(Bundle bundle) {

        ManageFriendAndFamilyFragment fragment = new ManageFriendAndFamilyFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new DeleteFFModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_family_friend, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());
        HashMap<String, String> initEmail = pref.getUserEmail();
        email = initEmail.get(SharedPrefManager.USER_EMAIL);

        //retrieve family & friend from realm object

        btnAddInfant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageFF = new Intent(getActivity(), EditFamilyFriendsActivity.class);
                manageFF.putExtra("ADD_FF","Infant");
                getActivity().startActivity(manageFF);

            }
        });

        btnAddAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageFF = new Intent(getActivity(), EditFamilyFriendsActivity.class);
                manageFF.putExtra("ADD_FF","Adult");
                getActivity().startActivity(manageFF);
            }
        });

        btnReturnPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });



        return view;
    }


    public void appendFF(final ArrayList<DefaultPassengerObj> obj){


        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.75f);
        LinearLayout.LayoutParams half05 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int ffList = 0 ; ffList < obj.size() ; ffList++){

            ffLoop = ffList;

            LinearLayout topFF = new LinearLayout(getActivity());
            topFF.setOrientation(LinearLayout.HORIZONTAL);
            topFF.setPadding(5,30, 5, 30);
            topFF.setWeightSum(1);
            topFF.setBackgroundResource(R.drawable.drawable_login_bottom_border);
            topFF.setLayoutParams(matchParent);

            LinearLayout nameFF = new LinearLayout(getActivity());
            nameFF.setOrientation(LinearLayout.VERTICAL);
            nameFF.setPadding(2, 12, 2, 12);
            nameFF.setWeightSum(1);
            nameFF.setLayoutParams(half06);

            LinearLayout actionFF = new LinearLayout(getActivity());
            actionFF.setOrientation(LinearLayout.HORIZONTAL);
            actionFF.setPadding(3, 10, 3, 8);
            actionFF.setWeightSum(1);
            actionFF.setLayoutParams(half04);

            String title = obj.get(ffList).getTitle();
            String firstName = obj.get(ffList).getFirst_name();
            String lastName = obj.get(ffList).getLast_name();

            String actionEdit = "Edit";
            String actionDelete = "Delete";

            TextView txtServicesName = new TextView(getActivity());
            txtServicesName.setText(title + " " + firstName + " " + lastName);
            txtServicesName.setLayoutParams(half06);
            txtServicesName.setGravity(Gravity.CENTER | Gravity.LEFT);

            final ImageView txtEditFF = new ImageView(getActivity());
            txtEditFF.setImageDrawable(getResources().getDrawable(R.drawable.drawable_edit_ff));
            txtEditFF.setTag("LOOP_"+Integer.toString(ffLoop));
            txtEditFF.setLayoutParams(half05);
            txtEditFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //get loop value from tag.
                    String[] splitTag = txtEditFF.getTag().toString().split("_");
                    Intent manageFF = new Intent(getActivity(), EditFamilyFriendsActivity.class);
                    manageFF.putExtra("FRIEND_AND_FAMILY_", (new Gson()).toJson(friendAndFamilyObj.get(Integer.parseInt(splitTag[1]))));
                    getActivity().startActivity(manageFF);

                }
            });

            ImageView txtDelete = new ImageView(getActivity());
            //txtDelete.setImageDrawable(getResources().getDrawable(R.drawable.delete_ff));
            txtDelete.setImageDrawable(getResources().getDrawable(R.drawable.drawable_delete_ff));
            txtDelete.setTag("LOOP2_"+Integer.toString(ffLoop));
            txtDelete.setLayoutParams(half05);
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get loop value from tag.
                    final String[] splitTag = txtEditFF.getTag().toString().split("_");
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Confirm delete?")
                            .setConfirmText("Confirm!")
                            .setCancelText("Cancel!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    FriendFamilyDelete thisDelete = new FriendFamilyDelete();
                                    thisDelete.setDeleteID(obj.get(Integer.parseInt(splitTag[1])).getId());
                                    thisDelete.setUser_email(email);

                                    deleteFamilyFriend(thisDelete);
                                    sDialog.dismiss();

                                }
                            })
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();


                }
            });

            nameFF.addView(txtServicesName);
            actionFF.addView(txtEditFF);
            actionFF.addView(txtDelete);

            topFF.addView(nameFF);
            topFF.addView(actionFF);

            appendFamilyFriends.addView(topFF);

        }
    }

    public void appendFF(){

        appendFamilyFriends.removeAllViews();

        RealmResults<FamilyFriendList> cachedListResult = RealmObjectController.getFamilyFriends(MainFragmentActivity.getContext());
        if(cachedListResult.size() > 0){
            Gson gson = new Gson();
            FamilyFriendObj obj = gson.fromJson(cachedListResult.get(0).getCachedList(), FamilyFriendObj.class);
            friendAndFamilyObj = obj.getFamily_and_friend();

            if(friendAndFamilyObj.size() > 0){
                appendFF(friendAndFamilyObj);
                noRecordFound.setVisibility(View.GONE);
                Log.e("APPEND","TRUE");

            }else{
                noRecordFound.setVisibility(View.VISIBLE);
                Log.e("APPEND","false1");

            }

        }else{
            noRecordFound.setVisibility(View.VISIBLE);
            Log.e("APPEND","false2");

        }



    }

    public void deleteFamilyFriend(FriendFamilyDelete thisDelete){
        Log.e(thisDelete.getUser_email(),thisDelete.getDeleteID());
        initiateLoading(getActivity());
        presenter.onDeleteFF(thisDelete);
    }

    @Override
    public void onDeleteFF(DeleteFFReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //save into realm object
            FamilyFriendObj thisObj = new FamilyFriendObj();
            thisObj.setFamily_and_friend(obj.getFamily_and_friend());

            RealmObjectController.saveFamilyFriends(getActivity(),thisObj);
            setSuccessDialogNoFinish(getActivity(), obj.getMessage(),null,"Deleted!!");

            //reset list
            appendFF();
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
        appendFF();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
