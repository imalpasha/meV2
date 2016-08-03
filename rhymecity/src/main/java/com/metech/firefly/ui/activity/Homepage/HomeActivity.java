package com.metech.firefly.ui.activity.Homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class HomeActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //implements ToolbarActivity, ProgressIndicatorActivity, FragmentContainerActivity {
    //@InjectView(R.id.main_activity_toolbar) Toolbar toolbar;
    //@InjectView(R.id.main_activity_progress_indicator) ProgressBar progressIndicator;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance(),"Home").commit();

        setMenuButton();
        setTitleImage(R.drawable.firefly_text_only);
        hideTitle();
        unlockDrawer();
        BaseFragment.removeLogoHeader(this);
    }

   /* private void goToSearchFragment() {
        fragmentManager.beginTransaction()
                .add(R.id.main_activity_fragment_container, SearchFragment.newInstance())
                .commit();
    }*/

    @Override
    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        HomeFragment fragment = (HomeFragment) manager.findFragmentByTag("Home");
        fragment.registerBackFunction();
    }

    /*@Override
    public ProgressBar getProgressIndicator() {
        return progressIndicator;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }*/
    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
