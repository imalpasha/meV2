package com.fly.firefly.ui.activity.Aboutus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.presenter.AboutUsPresenter;
import javax.inject.Inject;

import butterknife.ButterKnife;

public class AboutUsFragment extends BaseFragment {

    @Inject
    AboutUsPresenter presenter;

    public static AboutUsFragment newInstance() {

        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.aboutus, container, false);
        ButterKnife.inject(this, view);

        return view;
    }
}
