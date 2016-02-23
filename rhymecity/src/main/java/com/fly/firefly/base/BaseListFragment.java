package com.fly.firefly.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

public class BaseListFragment extends ListFragment {
	private com.fly.firefly.base.AQuery aq;
	private SharedPreferences pref;

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		aq = new com.fly.firefly.base.AQuery(getActivity());
		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public void onActivityCreated(Bundle icicle)
	{
		super.onActivityCreated(icicle);
		ListView listView = getListView();
		listView.setHorizontalScrollBarEnabled(false);
		listView.setVerticalScrollBarEnabled(false);
		//listView.setEmptyView(aq.id(android.R.id.empty).getView());
	}

	public com.fly.firefly.base.AQuery getAquery()
	{
		return aq;
	}

	public SharedPreferences getPref()
	{
		return pref;
	}
}
