package com.cnm.cnmrc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cnm.cnmrc.fragment.popup.PopupMirroringEnter;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;
import com.cnm.cnmrc.fragment.rc.RcChannelVolume;
import com.cnm.cnmrc.fragment.rc.RcFourWay;
import com.cnm.cnmrc.fragment.vodtvch.Base;
import com.cnm.cnmrc.fragment.vodtvch.VodTvch;


public class SearchActivity extends FragmentActivity implements View.OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

        
	}
	

	@Override
	protected void onPause() {
	}

	private void initUI() {
	}

	private void setEvent() {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onBackPressed() {
        
	}


}
