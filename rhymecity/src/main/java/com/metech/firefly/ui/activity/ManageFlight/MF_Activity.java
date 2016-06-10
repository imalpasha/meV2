package com.metech.firefly.ui.activity.ManageFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.ui.activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class MF_Activity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, MF_Fragment.newInstance()).commit();

        hideTitle();
        setMenuButton();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

}