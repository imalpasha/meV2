package com.fly.firefly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fly.firefly.base.AQuery;
import com.fly.firefly.base.BaseFragmentActivity;
import com.fly.firefly.drawer.DrawerItem;
import com.fly.firefly.drawer.NavigationDrawerFragment;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.activity.Terms.Terms;
import com.fly.firefly.ui.activity.UpdateProfile.UpdateProfileActivity;
import com.fly.firefly.utils.SharedPrefManager;

import butterknife.ButterKnife;

//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;


public class MainFragmentActivity extends BaseFragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPrefManager pref;
    private static MainFragmentActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);
        setMenuButton();
        ButterKnife.inject(this);
        instance = this;

        /*Testing*/
        moveDrawerToTop();
        pref = new SharedPrefManager(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    private void initActionBar() {

    }

    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor3, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.main_activity_fragment_container); // This is the container we defined just now.
        container.addView(child, 0);
        drawer.findViewById(R.id.navigation_drawer).setPadding(0, getStatusBarHeight(), 0, 0);

        // Make the drawer replace the first child
        decor.addView(drawer);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static Activity getContext() {
        //return instance.getApplicationContext();
        //return ActivityName.this;
        return instance;

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (isTaskRoot())
        {
            // clean the file cache with advance option
            long triggerSize = 3000000; // starts cleaning when cache size is
            // larger than 3M
            long targetSize = 2000000; // remove the least recently used files
            // until cache size is less than 2M
            //AQUtility.cleanCacheAsync(this, triggerSize, targetSize);
        }
    }

    public void setMenuButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.menubutton).visible();
        aq.id(R.id.menubutton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerFragment.openDrawer();
            }
        });
    }

    public void hideMenuButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.menubutton).gone();
        aq.id(R.id.menubutton).clicked(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mNavigationDrawerFragment.openDrawer();
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, DrawerItem item)
    {
        // update the main content by replacing fragments
        try
        {
            if (item.getTag().equals("Home"))
            {
                item.setBackgroundColor(getResources().getColor(R.color.white));
                Intent homepage = new Intent(this, HomeActivity.class);
                homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homepage);

            }
            else if (item.getTag().equals("Login"))
            {

                Intent login = new Intent(this, LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

            }
            else if (item.getTag().equals("Logout"))
            {
                pref.setLoginStatus("N");
                Intent login = new Intent(this, HomeActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);

            }
            else if (item.getTag().equals("Register"))
            {
                item.setBackgroundColor(getResources().getColor(R.color.white));
                Intent register = new Intent(this, RegisterActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);

            }
            else if (item.getTag().equals("Profile"))
            {
                Intent register = new Intent(this, UpdateProfileActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);

            }
            else if (item.getTag().equals("Terms"))
            {
                Intent terms = new Intent(this, Terms.class);
                terms.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(terms);

            }
            else if (item.getTag().equals("HEADER"))
            {

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        // actionBar.setDisplayShowTitleEnabled(true);
        // actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                break;

            default:
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }




}
