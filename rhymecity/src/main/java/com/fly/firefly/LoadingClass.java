package com.fly.firefly;

import android.app.ProgressDialog;

import com.fly.firefly.base.BaseFragment;

/**
 * Created by Dell on 11/5/2015.
 */
public class LoadingClass extends BaseFragment {

    public void initiateLoading()
    {
        ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
    }
}
