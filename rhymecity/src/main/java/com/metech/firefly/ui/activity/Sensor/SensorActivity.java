package com.metech.firefly.ui.activity.Sensor;

import android.app.FragmentManager;
import android.os.Bundle;

import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.ui.activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class SensorActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.main_activity_fragment_container, RelativeFragment.newInstance()).commit();
        //CompassFragment
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
