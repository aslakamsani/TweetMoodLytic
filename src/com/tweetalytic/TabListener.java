package com.tweetalytic;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TabListener<T extends Fragment> implements ActionBar.TabListener{
	private final Activity ACTIVITY;
	private final String TAG;
	private final Class<T> CLS;
	private final Bundle ARGS;
	private Fragment frag;
	
	public TabListener(Activity act, String tag, Class<T> cls){
		this(act, tag, cls, null);
	}
	
	public TabListener(Activity act, String tag, Class<T> cls, Bundle args){
		ACTIVITY = act;
		TAG = tag;
		CLS = cls;
		ARGS = args;
		
		frag = ACTIVITY.getFragmentManager().findFragmentByTag(TAG);
		if(frag != null && !frag.isDetached()){
			FragmentTransaction ft = ACTIVITY.getFragmentManager().beginTransaction();
			ft.detach(frag);
			ft.commit();
		}
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(frag == null){
			frag = Fragment.instantiate(ACTIVITY, CLS.getName(), ARGS);
			ft.add(android.R.id.content, frag, TAG);
		}else{
			ft.attach(frag);
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if(frag != null)
			ft.detach(frag);
	}
}
