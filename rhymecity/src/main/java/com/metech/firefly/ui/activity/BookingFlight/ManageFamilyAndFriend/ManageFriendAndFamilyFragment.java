package com.metech.firefly.ui.activity.BookingFlight.ManageFamilyAndFriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.presenter.BookingPresenter;
import com.metech.firefly.utils.RealmObjectController;
import java.util.ArrayList;
import butterknife.ButterKnife;
import javax.inject.Inject;
import butterknife.InjectView;

public class ManageFriendAndFamilyFragment extends BaseFragment {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.appendFamilyFriends)
    LinearLayout appendFamilyFriends;

    private ArrayList<DefaultPassengerObj> friendAndFamilyObj = new ArrayList<DefaultPassengerObj>();
    private int fragmentContainerId;
    private int ffLoop;

    public static ManageFriendAndFamilyFragment newInstance(Bundle bundle) {

        ManageFriendAndFamilyFragment fragment = new ManageFriendAndFamilyFragment();
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

        View view = inflater.inflate(R.layout.manage_family_friend, container, false);
        ButterKnife.inject(this, view);

        friendAndFamilyObj = getActivity().getIntent().getParcelableArrayListExtra("FRIEND_AND_FAMILY");

        if(friendAndFamilyObj.size() > 0){
            appendFF(friendAndFamilyObj);
        }else{
            //display no ff
        }


        return view;
    }


    public void appendFF(ArrayList<DefaultPassengerObj> obj){


        LinearLayout.LayoutParams half06 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);
        LinearLayout.LayoutParams half04 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 0.75f);
        LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        for(int ffList = 0 ; ffList < obj.size() ; ffList++){

            ffLoop = ffList;

            LinearLayout topFF = new LinearLayout(getActivity());
            topFF.setOrientation(LinearLayout.HORIZONTAL);
            topFF.setPadding(5,15, 5, 15);
            topFF.setWeightSum(1);
            topFF.setBackgroundResource(R.drawable.drawable_login_bottom_border);
            topFF.setLayoutParams(matchParent);

            LinearLayout nameFF = new LinearLayout(getActivity());
            nameFF.setOrientation(LinearLayout.VERTICAL);
            nameFF.setPadding(2, 2, 2, 2);
            nameFF.setWeightSum(1);
            nameFF.setLayoutParams(half06);

            LinearLayout actionFF = new LinearLayout(getActivity());
            actionFF.setOrientation(LinearLayout.VERTICAL);
            actionFF.setPadding(2, 2, 2, 2);
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

            TextView txtEditFF = new Button(getActivity());
            txtEditFF.setText(actionEdit);
            txtEditFF.setGravity(Gravity.CENTER);
            txtEditFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent manageFF = new Intent(getActivity(), EditFamilyFriendsActivity.class);
                    manageFF.putExtra("FRIEND_AND_FAMILY_", (new Gson()).toJson(friendAndFamilyObj.get(ffLoop)));
                    getActivity().startActivity(manageFF);
                }
            });

            TextView txtDelete = new Button(getActivity());
            txtDelete.setText(actionDelete);
            txtDelete.setGravity(Gravity.CENTER);
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //delete

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
