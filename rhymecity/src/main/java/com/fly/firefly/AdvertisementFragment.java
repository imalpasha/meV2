package com.fly.firefly;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

public class AdvertisementFragment extends Fragment
{

	public static AdvertisementFragment newInstance()
	{
		AdvertisementFragment fragment = new AdvertisementFragment();
		return fragment;
	}

	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		aq = new AQuery(getActivity());
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle)
	{
		View layout = inflater.inflate(R.layout.advertisement_layout, null);
		aq.recycle(layout);

		return layout;
	}
}