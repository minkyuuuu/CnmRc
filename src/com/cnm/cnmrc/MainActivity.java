package com.cnm.cnmrc;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cnm.cnmrc.fragment.remocon.RcChannelVolume;
import com.cnm.cnmrc.fragment.remocon.RcFourWay;

/**
 * 
 * @author hwangminkyu
 * @date   2013.7.17
 * 
 * 명명규칙 :
 * 1) 메인 리모컨관련 화면 : remocon
 * 2) 하단의 리모컨이동 메뉴 : rcmenu
 * 3) 하단 중앙의 리모컨아이콘 : remocon_icon
 *
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {

	ImageButton mStbPower, mIntegrationUiMain, mVod, mTVChannel, mSearch;	// top menu
	ImageButton m1, m2, m3, m4, m5, m6, m7, m8, m9, m0, mClear, mEnter;		// numeric menu
	ImageButton mPrevious, mFavoriteChannel, mViewSwitch, mExit;			// bottom menu
	ImageButton mMirroring, mFourWay, mQwerty, mChannelVolume, mConfig;		// rcmenu
	
	FrameLayout mMiddleBackgound;
	LinearLayout mRemoconMenu;
	ImageButton mRemoconIcon;
	boolean toggleRemoconMenu = true; // 리모컨메뉴가 보이는지???
	
	Animation animationFadeOut, animationZoomOut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		// -------------------------
		// initialize UI
		// -------------------------
		initUI();	

		setEvent();
		
		// --------------------------------
		// remocon 화면 전환 (채널/볼륨 & 사방향키)
		// --------------------------------
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
		chagneFragment(channelVolume);

	}
	
	@Override
	protected void onPause() {
		remoconIconOn();
		Log.i("hwang", "pause");
		super.onPause();
	}
	
	private void initUI() {
		// top menu
		mStbPower = (ImageButton) findViewById(R.id.stb_power);
		mIntegrationUiMain = (ImageButton) findViewById(R.id.integration_ui_main);
		mVod = (ImageButton) findViewById(R.id.vod);
		mTVChannel = (ImageButton) findViewById(R.id.tv_channel);
		mSearch = (ImageButton) findViewById(R.id.search);
		
		// bottom menu
		mPrevious = (ImageButton) findViewById(R.id.previous);
		mFavoriteChannel = (ImageButton) findViewById(R.id.favorite_channel);
		mViewSwitch = (ImageButton) findViewById(R.id.view_switch);
		mExit = (ImageButton) findViewById(R.id.exit);
		
		// rcmenu
		mMirroring = (ImageButton) findViewById(R.id.mirroring);
		mFourWay = (ImageButton) findViewById(R.id.four_way);
		mQwerty = (ImageButton) findViewById(R.id.qwerty);
		mChannelVolume = (ImageButton) findViewById(R.id.channel_volume);
		mConfig = (ImageButton) findViewById(R.id.config);
		
		// remocon icon
		mMiddleBackgound = (FrameLayout) findViewById(R.id.middle_background);
		mRemoconMenu = (LinearLayout) findViewById(R.id.remocon_menu);
		mRemoconIcon = (ImageButton) findViewById(R.id.remocon_icon);
		
		animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.qwerty_fade_out);
		//animationZoomOut = AnimationUtils.loadAnimation(this, R.anim.qwerty_zoom_out);
		
	}
	
	private void setEvent() {
		// 하단 rcmenu 이벤트
		mMiddleBackgound.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;	// 아래에 있는 위젯으로 터치이벤트가 흘러가지 못하도록 한다.
			}
		});

		// remocon icon
		mRemoconIcon.setOnClickListener(this);
		
		// top menu
		mStbPower.setOnClickListener(this);
		mIntegrationUiMain.setOnClickListener(this);
		mVod.setOnClickListener(this);
		mTVChannel.setOnClickListener(this);
		mSearch.setOnClickListener(this);
		
		// numeric menu
		
		// bottom menu
		mPrevious.setOnClickListener(this);
		mFavoriteChannel.setOnClickListener(this);
		mViewSwitch.setOnClickListener(this);
		mExit.setOnClickListener(this);
		
		// rcmenu
		mMirroring.setOnClickListener(this);
		mFourWay.setOnClickListener(this);
		mQwerty.setOnClickListener(this);
		mChannelVolume.setOnClickListener(this);
		mConfig.setOnClickListener(this);
		
		animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	mMiddleBackgound.setVisibility(View.INVISIBLE);
            	mRemoconMenu.setVisibility(View.INVISIBLE);
            	mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
		
	}

	private void chagneFragment(Fragment fragment){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.remocon_panel, fragment);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// remocon icon
		case R.id.remocon_icon:
			if (toggleRemoconMenu) {
				remoconIconOffNoAni();
			} else {
				remoconIconOnNoAni();
			}
			break;
			
		// top menu
		case R.id.stb_power:
			Log.i("hwang", "stb_power");
			break;
		case R.id.integration_ui_main:
			Log.i("hwang", "integration_ui_main");
			break;
		case R.id.vod:
			Log.i("hwang", "vod");
			break;
		case R.id.tv_channel:
			Log.i("hwang", "tv_channel");
			break;
		case R.id.search:
			Log.i("hwang", "search");
			break;
			
		// numeric menu
			
		// bottom menu
		case R.id.previous:
			Log.i("hwang", "previous");
			break;
		case R.id.favorite_channel:
			Log.i("hwang", "favorite_channel");
			break;
		case R.id.view_switch:
			Log.i("hwang", "view_switch");
			break;
		case R.id.exit:
			Log.i("hwang", "exit");
			break;
			
		// rcmenu
		case R.id.mirroring:
			remoconIconOn();
			Log.i("hwang", "mirroring");
			break;
		case R.id.four_way:
			remoconIconOnNoAni();
			RcFourWay fourWay = RcFourWay.newInstance(2);
			chagneFragment(fourWay);
			Log.i("hwang", "four_way");
			break;
		case R.id.qwerty:
			remoconIconOn();
			Intent intent = new Intent(this, QwertyActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.qwerty_zoom_in, 0);
			
			/*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			QwertyFragment qwerty = new QwertyFragment();
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.setCustomAnimations(R.anim.zoom_enter, 0);
			qwerty.show(ft, QwertyFragment.class.getSimpleName());*/
			Log.i("hwang", "qwertyxxx");
			break;
		case R.id.channel_volume:	// start of main display
			remoconIconOnNoAni();
			RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
			chagneFragment(channelVolume);
			Log.i("hwang", "channel volume");
			break;
		case R.id.config:
			remoconIconOn();
			Log.i("hwang", "config");
			break;
			
		}
		
	}

	private void remoconIconOn() {
		if(mMiddleBackgound.getVisibility() == View.VISIBLE) mMiddleBackgound.startAnimation(animationFadeOut);
		if(mRemoconMenu.getVisibility() == View.VISIBLE) mRemoconMenu.startAnimation(animationFadeOut);
		toggleRemoconMenu = true;
	}
	
	private void remoconIconOnNoAni() {
		mMiddleBackgound.setVisibility(View.INVISIBLE);
		mRemoconMenu.setVisibility(View.INVISIBLE);
		toggleRemoconMenu = true;
		mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}

	private void remoconIconOffNoAni() {
		mMiddleBackgound.setVisibility(View.VISIBLE);
		mRemoconMenu.setVisibility(View.VISIBLE);
		toggleRemoconMenu = false;
		mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_off); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}

	/*@Override
	public void onEventQwertySelected() {
		//remoconIconOn();
		
		//mRemoconMenu.setVisibility(View.INVISIBLE);
		//toggleRemoconMenu = true;
		//mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
		
	}*/
	

	
	
	
	
	

}
