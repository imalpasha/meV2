package com.fly.firefly.base;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.fly.firefly.R;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.RealmObjectController;

//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.actionbarsherlock.internal.widget.ScrollingTabContainerView.TabView;

public class BaseFragmentActivity extends FragmentActivity {

    public com.fly.firefly.base.AQuery aq;
    //TabView tabsView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        aq = new com.fly.firefly.base.AQuery(this);


       /*if ((getApplicationContext().getResources().getConfiguration().screenLayout &
                android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getApplicationContext().getResources().getConfiguration().orientation = 2;

        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getApplicationContext().getResources().getConfiguration().orientation = 1;
        }*/


        try
        {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
           // actionBar.setElevation(0);
            //actionBar.setBackgroundDrawable(null);
           // tabsView = new ScrollingTabContainerView(actionBar.getThemedContext());
             actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setCustomView(R.layout.actionbar);
            View actionBarView = actionBar.getCustomView();
            aq.recycle(actionBarView);
            aq.id(R.id.title).typeface(Typeface.createFromAsset(getAssets(), App.FONT_HELVETICA_NEUE)).textSize(22).textColor(Color.WHITE);
           // if(Utils.getDeviceType(this) == "1")
            //{
             //   aq.id(R.id.tabContainerTablet).visible();
                //display tab here
           // }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

   /* public void tabSearch(View v)
    {
        Intent intent = new Intent(BaseFragmentActivity.this, GlobalSearchView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseFragmentActivity.this.startActivity(intent);
    }

    public void tabWish(View v)
    {
        Intent intent = new Intent(BaseFragmentActivity.this, MyWishListList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseFragmentActivity.this.startActivity(intent);
    }

    public void tabCart(View v)
    {
        Intent intent = new Intent(BaseFragmentActivity.this, CartsView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseFragmentActivity.this.startActivity(intent);
    }*/

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(title);
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.title).text(title);
    }

    public void hideTitle()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.title).visibility(View.GONE);
    }

    public void setLogOutButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.btnLogOut).visible();
    }



    public void setARButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.btnAR).visible();
    }

    public void setBackButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.backbutton).visible();
        aq.id(R.id.backbutton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setGlobalSearchButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.globalSearchBoxTablet).visible();
        aq.id(R.id.tabMySearch1).gone();
        aq.id(R.id.centerPart).gone();

        /*RelativeLayout leftPart = (RelativeLayout) actionBarView.findViewById(R.id.leftPart);
        RelativeLayout.LayoutParams lp = new RelativeLayout().LayoutParams(0,1);
        lp.weight = 0.05f;
        leftPart.setLayoutParams(lp);*/

        /*aq.id(R.id.tabBackButton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });*/
    }

    public void setTabBackButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.tabBackButton).visible();
        aq.id(R.id.tabBackButton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }






    @Override
    public void setTitle(int titleId)
    {
        super.setTitle(titleId);
    }

    public void setTitleImage(String imageUrl)
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.icon).image(imageUrl);
    }

    public void setTitleImage(int imageId)
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.icon).image(imageId);
    }

    @Override
    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        RealmObjectController.clearCachedResult(this);

    }

    @Override
    public void finish()
    {
        super.finish();
        //overridePendingTransition(R.anim.fadeout,R.anim.fadein);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        RealmObjectController.clearCachedResult(this);

        //setResult(RESULT_CANCELED);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }
}
