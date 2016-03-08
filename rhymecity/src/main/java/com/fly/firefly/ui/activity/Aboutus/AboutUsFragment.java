package com.fly.firefly.ui.activity.Aboutus;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.AboutUsReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.AboutUsModule;
import com.fly.firefly.ui.module.LoginModule;
import com.fly.firefly.ui.object.AboutUs;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.ui.presenter.AboutUsPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutUsFragment extends BaseFragment implements AboutUsPresenter.AboutUsView {

    @Inject
    AboutUsPresenter presenter;

    @InjectView(R.id.txtAboutUs)
    TextView txtAboutUs;

    private int fragmentContainerId;

    public static AboutUsFragment newInstance() {

        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new AboutUsModule(this)).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.aboutus, container, false);
        ButterKnife.inject(this, view);

        AboutUs obj = new AboutUs();
        initiateLoading(getActivity());
        presenter.requestAboutUsInfo(obj);
        return view;
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

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


    @Override
    public void onRequestSuccess(AboutUsReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if(status) {
            txtAboutUs.setText(Html.fromHtml(obj.getData().replaceAll("</br>", "<p>")));
        }
    }
}
