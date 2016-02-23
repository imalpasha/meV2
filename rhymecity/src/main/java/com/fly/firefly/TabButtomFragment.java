package com.fly.firefly;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
/*import com.silverlake.ifb.module.carts.CartsView;
import com.silverlake.ifb.module.globalsearch.GlobalSearchView;
import com.silverlake.ifb.module.myprofile.MyProfileView;
import com.silverlake.ifb.module.promotionslanding.PromotionsLandingView;
import com.silverlake.ifb.module.wishlist.MyWishListList;*/

public class TabButtomFragment extends Fragment
{
	public static TabButtomFragment newInstance()
	{
		TabButtomFragment fragment = new TabButtomFragment();
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
		View layout = inflater.inflate(R.layout.layout_tab_container, null);
		aq.recycle(layout);

		/* My Profile */
		/*aq.id(R.id.tabMyProfile).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), MyProfileView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(intent);
			}
		});


		aq.id(R.id.tabMyCart).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), CartsView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(intent);
			}
		});


		aq.id(R.id.tabMyHome).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), PromotionsLandingView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(intent);
			}
		});


		aq.id(R.id.tabMyWishList).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), MyWishListList.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(intent);
			}
		});


		aq.id(R.id.tabMySearch).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), GlobalSearchView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(intent);
			}
		});*/

		return layout;
	}
}