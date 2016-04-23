package com.gilo.soko;

import android.app.Activity;

import com.gilo.soko.utils.ActivityHostFragment;

public class FragmentController extends ActivityHostFragment {

	Class<? extends Activity> activity;

	public FragmentController() {
	}

	public void setClass(Class<? extends Activity> activity) {
		this.activity = activity;
	}

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return activity;
	}
}
