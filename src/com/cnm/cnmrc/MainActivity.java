package com.cnm.cnmrc;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cnm.cnmrc.config.ConfigAdultCert;
import com.cnm.cnmrc.config.ConfigFragment;
import com.cnm.cnmrc.config.ConfigProduct;
import com.cnm.cnmrc.config.ConfigRegion;
import com.cnm.cnmrc.fragment.popup.PopupMirroringEnter;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;
import com.cnm.cnmrc.fragment.rc.RcChannelVolume;
import com.cnm.cnmrc.fragment.rc.RcFourWay;
import com.cnm.cnmrc.fragment.rc.RcTrickPlay;
import com.cnm.cnmrc.fragment.search.SearchFragment;
import com.cnm.cnmrc.fragment.search.SearchVodDetail;
import com.cnm.cnmrc.fragment.vodtvch.Base;
import com.cnm.cnmrc.fragment.vodtvch.VodTvch;

/**
 * 
 * @author minkyu
 * @date 2013.7.17
 * 
 * 메인화면 구성
 * 1) 상단메뉴(top) / 하단숫자(numeric) / 하단메뉴(bottom) / 하단써클메뉴(circle)
 * 
 *    여기서 pannel이란 용어는 fragment가 대체되는 FrameLayout viewgroup을 의미한다.
 * 2) vod_tvch_panel : top menu에서 vod, tvch 아이콘의 메인화면이 대체되는 fragment 영역
 * 3) rc_panel : circle menu에서 미러TV, 사방향키, 채널/볼륨 화면이 대체되는 fragment 영역
 *               circle menu의 쿼티화면은 activity로 처리함.
 *               
 * 4) 하단중앙 리모컨아이콘 : rc icon
 * 
 * 5) 처음시작화면이 메인화면이 리모컨화면이므로, 메인화면을 rc용어와 함께 사용함.
 * 
 * 6) 써클메뉴
 *    - 볼륨/채널, 사방향키는 rc_panel의 Fragment로 처리함.
 *    - 미러TV, 쿼티, 설정은 전체화면을 사용하는 Activity로 처리함.
 *    
 * 7) 상단메뉴
 *    - vod, tvch은 vod_tvch_panel의 Fragment로 처리함.
 *    - 검색은 전체화면을 사용하는 Activity로 처리함.
 *    
 * 8) fragment의 id는 fragment_*로 시작됨.
 * 
 * 9) vod, tvch 화면의 depth에 따른 명명규칙 (3가지 타입)
 *    - List
 *    - SemiDetail (Detail화면전에 보이는 list화면)
 *    - Detail
 *    * 만약 클래스명이 변경되면 이를 프로그램으로 변경적용해야한다. 이유는 클래스를 런타임동적로딩으로 처리했음.
 *    
 * 10) VodTvch화면의 
 * 
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
	
	// string constants for fragment tag 
	public final String TAG_FRAGMENT_VOD_TVCH = "vod_tvch";
	public final String TAG_FRAGMENT_BASE = "base";
	
	public final String TAG_FRAGMENT_SEARCH = "search";
	public final String TAG_FRAGMENT_CONFIG = "config";
	
	public final String TAG_FRAGMENT_FOURWAY = "fourway";
	public final String TAG_FRAGMENT_TRICK_PLAY = "trick_play";
	public final String TAG_FRAGMENT_CHANNEL_VOLUME = "channel_volume";
	
	boolean isFourWay = true; // for test, which fourway or trickplay
	
	boolean doubleBackKeyPressedToExitApp;
	
	public boolean noVodTvchDestroy = false;
	
	FrameLayout mRcPanel;				// rc_panel(채널/볼륨, 사방향) 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)
	FrameLayout mRcNumericPad;			// rc_numeric_pad 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)
	
	ImageButton mStbPower, mIntegrationUiMain, mVod, mTvch, mSearch;	// top menu (셋탑전원, 통합ui메인, vod, TV채널, 검색)
	ImageButton mMirroring, mFourWay, mQwerty, mChannelVolume, mConfig;		// circle menu (미러TV, 사방향키, 쿼티, 채널/볼륨, 설정)
	
	ImageButton mRcIcon;				// remocon icon
	LinearLayout mCircleMenu;			// circle menu를 담고있는 레이아웃
	FrameLayout mCircleMenuBg;			// circle menu가 보일때 바탕에 깔리는 반투명배경의 레이아웃
	boolean toggleCircleMenu = true; 	// circle menu가 보이는지 or 안보이는지?

	FrameLayout mVodTvchPanel;			// top menu의 vod, tvch 아이콘의 메인화면의 레이아웃
	FrameLayout mSearchPanel;			// top menu의 search 아이콘의 메인화면의 레이아웃
	FrameLayout mConfigPanel;			// circle menu의 config 아이콘의 메인화면의 레이아웃
	
	Animation aniRcPanelFadeout;
	Animation aniRcTopMenuFadein;
	Animation aniQwertyFadeout;
	
	Animation aniCirCleMenuEnter, aniCirCleMenuExit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// -------------------------
		// initialize UI
		// -------------------------
		initUI();

		setEvent();

		// ----------------------------------
		// remocon 화면 전환 (채널/볼륨 & 사방향키)
		// 첫화면이 채널/볼륨화면이다.
		// ----------------------------------
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(TAG_FRAGMENT_CHANNEL_VOLUME);
		chagneFragment(channelVolume, TAG_FRAGMENT_CHANNEL_VOLUME);
		
		// ----------------------
        // 하단의 bottom menu 설정
		// ----------------------
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null) {
			((RcBottomMenu)f).setRemoconMode();
		}
		
//        FragmentManager fm = getSupportFragmentManager();
//        fm.removeOnBackStackChangedListener(mRemoveOnBackStackChangedListener);
//        fm.addOnBackStackChangedListener(mAddOnBackStackChangedListener);
        
	}
	

	@Override
	protected void onPause() {
		rcIconOn();
		Log.i("hwang", "pause");
		super.onPause();
	}

	private void initUI() {
		// rc_panel (채널/볼륨, 사방향) 
		mRcPanel = (FrameLayout) findViewById(R.id.rc_panel);
		
		// rc_numeric_pad
		mRcNumericPad = (FrameLayout) findViewById(R.id.rc_numeric_pad);
		
		// top menu
		mStbPower = (ImageButton) findViewById(R.id.stb_power);
		mIntegrationUiMain = (ImageButton) findViewById(R.id.integration_ui_main);
		mVod = (ImageButton) findViewById(R.id.vod);
		mTvch = (ImageButton) findViewById(R.id.tvch);
		mSearch = (ImageButton) findViewById(R.id.search);
		
		// numeric menu

		// bottom menu
		
		// remocon icon
		mRcIcon = (ImageButton) findViewById(R.id.rc_icon);
		mCircleMenu = (LinearLayout) findViewById(R.id.circle_menu);
		mCircleMenuBg = (FrameLayout) findViewById(R.id.circle_menu_background);
		
		// circle menu
		mMirroring = (ImageButton) findViewById(R.id.mirroring);
		mFourWay = (ImageButton) findViewById(R.id.four_way);
		mQwerty = (ImageButton) findViewById(R.id.qwerty);
		mChannelVolume = (ImageButton) findViewById(R.id.channel_volume);
		mConfig = (ImageButton) findViewById(R.id.config);
		
		// VOD and TVCh 메인화면
		mVodTvchPanel = (FrameLayout) findViewById(R.id.vod_tvch_panel);
		
		// Search 메인화면
		mSearchPanel = (FrameLayout) findViewById(R.id.search_panel);
		
		// Config 메인화면
		mConfigPanel = (FrameLayout) findViewById(R.id.config_panel);

		// animation
		aniRcPanelFadeout= AnimationUtils.loadAnimation(this, R.anim.rc_panel_fadeout);
		aniRcPanelFadeout.setFillAfter(true);
		
		aniRcTopMenuFadein= AnimationUtils.loadAnimation(this, R.anim.rc_top_menu_fadein);
		aniQwertyFadeout = AnimationUtils.loadAnimation(this, R.anim.qwerty_fadeout);
		
		aniCirCleMenuEnter = AnimationUtils.loadAnimation(this, R.anim.scale_up);
		aniCirCleMenuExit = AnimationUtils.loadAnimation(this, R.anim.scale_down);
	}

	private void setEvent() {
		// 하단 rcmenu 이벤트
		mCircleMenuBg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				hideCircleMenu();
				return true; // 아래에 있는 위젯으로 터치이벤트가 흘러가지 못하도록 한다.
			}
		});

		// top menu
		mStbPower.setOnClickListener(this);
		mIntegrationUiMain.setOnClickListener(this);
		mVod.setOnClickListener(this);
		mTvch.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		// numeric menu

		// bottom menu

		// remocon icon
		mRcIcon.setOnClickListener(this);
		
		// circle menu
		mMirroring.setOnClickListener(this);
		mFourWay.setOnClickListener(this);
		mQwerty.setOnClickListener(this);
		mChannelVolume.setOnClickListener(this);
		mConfig.setOnClickListener(this);

/*		aniRcPanelFadeout.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mRcPanel.setVisibility(View.INVISIBLE);
				mVod.setVisibility(View.INVISIBLE);
				mTvch.setVisibility(View.INVISIBLE);
				mSearch.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
		});*/
		
		aniQwertyFadeout.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mCircleMenuBg.setVisibility(View.INVISIBLE);
				mCircleMenu.setVisibility(View.INVISIBLE);
				mRcIcon.setBackgroundResource(R.drawable.xml_rc_icon); // 리모컨아이콘 이미지가 바뀌어야 한다.
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
		});
		
		aniCirCleMenuEnter.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { 
				mCircleMenu.setVisibility(View.VISIBLE);
				}
			
			@Override
			public void onAnimationEnd(Animation animation) { }
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				mCircleMenu.clearAnimation();
			}
		});
		
		aniCirCleMenuExit.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { 
			}
			
			@Override
			public void onAnimationEnd(Animation animation) { }
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				mCircleMenu.setVisibility(View.GONE);
				//mCircleMenu.clearAnimation();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// remocon icon
		case R.id.rc_icon:
			if (toggleCircleMenu) {
				showCircleMenu();
			} else {
				hideCircleMenu();
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
			goToVodTvch("vod");
			break ;
		case R.id.tvch: 
			goToVodTvch("tvch");
			break;
		case R.id.search:
			goToSearch("rc");
			break;

		// bottom menu

		// circle menu
		case R.id.channel_volume:	// replace하므로 존재체크필요없음. 시작화면, replace개념이므로 show(), Fragment at rc_panel framelayout
			showChannelVolume();
			break;
		case R.id.four_way:			// replace하므로 존재체크필요없음. replace개념이므로 show(), Fragment at rc_panel framelayout
			if(isFourWay) {
				isFourWay = false;
				showFourWay();
			}
			else {
				isFourWay = true;
				showTrickPlay();
			}
			break;
		case R.id.mirroring:	// DialogFragment (popup with entire display)
			openPopupMirroring();
			break;
		case R.id.qwerty:		// Activity
			openQwerty();
			break;
		case R.id.config:
			if(isExistFragment(TAG_FRAGMENT_CONFIG)) {
				Log.i("hwang", "No entering config !!!");
				hideCircleMenu();
			} else {
				Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
				if (f != null) goToConfig("vod_tvch");
				else goToConfig("rc");
			}
			break;

		}

	}
	
	private boolean isExistFragment(String tag) {
		Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
		if (f != null) return true;	// yes, exist!!!
		else return false;
	}


	@Override
	public void onBackPressed() {
		{
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity onBackpressed fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		}
		
		
		
		// ---------------------
		// mirringTV on VodTvch
		// ---------------------
		if(noVodTvchDestroy) {
			noVodTvchDestroy = false;
        	return;
		}
		
		
		// ---------------------
		// circle menu
		// ---------------------
		if(mCircleMenu.getVisibility() == View.VISIBLE) {
			hideCircleMenu();
			return;
		}
		
		
		// ---------------------------
		// adult certification
		// ---------------------------
		final ConfigAdultCert adultCert = (ConfigAdultCert) getSupportFragmentManager().findFragmentByTag("adult_cert");
		if (adultCert != null) {
			super.onBackPressed();	// go to config main
			return;
		}
		
		// ---------------------------
		// config product
		// ---------------------------
		final ConfigProduct configProduct = (ConfigProduct) getSupportFragmentManager().findFragmentByTag("config_product");
		if (configProduct != null) {
			super.onBackPressed();	// go to config main
			return;
		}
		
		// ---------------------------
		// config region
		// ---------------------------
		final ConfigRegion configRegion = (ConfigRegion) getSupportFragmentManager().findFragmentByTag("config_region");
		if (configRegion != null) {
			super.onBackPressed();	// go to config main
			return;
		}
		
		// ---------------------------
		// config와 search간의 순서를 생각해보자. 근본적으로 순서와 관계없이 처리하는 방법을 고려해보자....
		// config
		// ---------------------------
		final ConfigFragment config = (ConfigFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG);
		if (config != null) {
			if (config.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				mConfigPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
			if (config.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mConfigPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
		}
		
		// ---------------------------
		// search vod detail
		// ---------------------------
		final SearchVodDetail searchVodDetail = (SearchVodDetail) getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (searchVodDetail != null) {
			if (searchVodDetail.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
		}
		
		// ---------------------------
		// search
		// ---------------------------
		final SearchFragment search = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (search != null) {
			if (search.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
			if (search.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mSearchPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
		}
		
		// -------------------------------
		// vod, tvch (loading_data_panel)
		// -------------------------------
		Base base = (Base) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_BASE);
		if (base != null) {
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity TAG_FRAGMENT_BASE != null fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
			
			super.onBackPressed();
			return;
		} else {
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity TAG_FRAGMENT_BASE = null fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		}
		
		// ---------------------------
		// vod, tvch (vod_tvch_panel)
		// ---------------------------
		final VodTvch vodTVch = (VodTvch) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		if (vodTVch != null) {
			if (vodTVch.allowBackPressed() == 0) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
			if (vodTVch.allowBackPressed() == 1) {	// sidebar가 열려있는경우...
				Fragment f = getSupportFragmentManager().findFragmentByTag(this.TAG_FRAGMENT_VOD_TVCH);
				if (f != null) {
					((VodTvch)f).mSlidingMenu.toggleSidebar();
					Log.i("hwang", "closing sidebar!!!");
				}
				return;
			}
		}
	    
		// -------------------------------------------
		// back key is checked, whether twice pressed
		// -------------------------------------------
        if (doubleBackKeyPressedToExitApp) {
        	// vodtvch fragment를 제거할 때 addBackToStack하지않은 처음 Base도 제거해주어야한다. (using R.id.loading_data_panel)
        	// 어디에서 했지?
            super.onBackPressed();
            return;
        }
        
        this.doubleBackKeyPressedToExitApp = true;
        Toast.makeText(this, R.string.app_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             doubleBackKeyPressedToExitApp=false;   
            }
        }, 2000);
        
	}

	private void backToRc() {
		mStbPower.startAnimation(aniRcTopMenuFadein);
		mIntegrationUiMain.startAnimation(aniRcTopMenuFadein);
		mVod.startAnimation(aniRcTopMenuFadein);
		mTvch.startAnimation(aniRcTopMenuFadein);
		mSearch.startAnimation(aniRcTopMenuFadein);
		mRcPanel.startAnimation(aniRcTopMenuFadein);
		mRcNumericPad.startAnimation(aniRcTopMenuFadein);
		
		mStbPower.setVisibility(View.VISIBLE);
		mIntegrationUiMain.setVisibility(View.VISIBLE);
		mVod.setVisibility(View.VISIBLE);
		mTvch.setVisibility(View.VISIBLE);
		mSearch.setVisibility(View.VISIBLE);
		mRcPanel.setVisibility(View.VISIBLE);
		mRcNumericPad.setVisibility(View.VISIBLE);
		mVodTvchPanel.setVisibility(View.INVISIBLE);
	}
	
	private void goToVodTvch(String type) {
		clearVodTvchTopMeny();
		
		
		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		
		VodTvch vodTvch = VodTvch.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		ft.replace(R.id.vod_tvch_panel, vodTvch, TAG_FRAGMENT_VOD_TVCH);
		ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();
		
		mStbPower.startAnimation(aniRcPanelFadeout);
		mIntegrationUiMain.startAnimation(aniRcPanelFadeout);
		mVod.startAnimation(aniRcPanelFadeout);
		mTvch.startAnimation(aniRcPanelFadeout);
		mSearch.startAnimation(aniRcPanelFadeout);
		mRcPanel.startAnimation(aniRcPanelFadeout);
		mRcNumericPad.startAnimation(aniRcPanelFadeout);

		mStbPower.setVisibility(View.INVISIBLE);
		mIntegrationUiMain.setVisibility(View.INVISIBLE);
		mVod.setVisibility(View.INVISIBLE);
		mTvch.setVisibility(View.INVISIBLE);
		mSearch.setVisibility(View.INVISIBLE);
		mRcPanel.setVisibility(View.INVISIBLE);
		mRcNumericPad.setVisibility(View.INVISIBLE);
		mVodTvchPanel.setVisibility(View.VISIBLE);
		
		Log.i("hwang", "after when mainActivity fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	}
	
	public void goToSearch(String type) {
		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "(Search) before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		
		SearchFragment search = SearchFragment.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit); // 두 번째 진입때 문제발생...
		ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		ft.add(R.id.search_panel, search, TAG_FRAGMENT_SEARCH);
		ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();
		
		if(type.equals("rc")) {
			mStbPower.startAnimation(aniRcPanelFadeout);
			mIntegrationUiMain.startAnimation(aniRcPanelFadeout);
			mVod.startAnimation(aniRcPanelFadeout);
			mTvch.startAnimation(aniRcPanelFadeout);
			mSearch.startAnimation(aniRcPanelFadeout);
			mRcPanel.startAnimation(aniRcPanelFadeout);
			mRcNumericPad.startAnimation(aniRcPanelFadeout);
			
			mStbPower.setVisibility(View.INVISIBLE);
			mIntegrationUiMain.setVisibility(View.INVISIBLE);
			mVod.setVisibility(View.INVISIBLE);
			mTvch.setVisibility(View.INVISIBLE);
			mSearch.setVisibility(View.INVISIBLE);
			mRcPanel.setVisibility(View.INVISIBLE);
			mRcNumericPad.setVisibility(View.INVISIBLE);
			mVodTvchPanel.setVisibility(View.VISIBLE);
			
			mSearchPanel.setVisibility(View.VISIBLE);
		}
		
		if(type.equals("vod_tvch")) {
			mSearchPanel.setVisibility(View.VISIBLE);
		}
		
		Log.i("hwang", "(Search) after when mainActivity fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	}
	
	public void goToConfig(String type) {
		rcIconOn();
		
		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "(Search) before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		
		ConfigFragment config = ConfigFragment.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit); // 두 번째 진입때 문제발생...
		ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		ft.add(R.id.config_panel, config, TAG_FRAGMENT_CONFIG);
		ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();
		
		if(type.equals("rc")) {
			mStbPower.startAnimation(aniRcPanelFadeout);
			mIntegrationUiMain.startAnimation(aniRcPanelFadeout);
			mVod.startAnimation(aniRcPanelFadeout);
			mTvch.startAnimation(aniRcPanelFadeout);
			mSearch.startAnimation(aniRcPanelFadeout);
			mRcPanel.startAnimation(aniRcPanelFadeout);
			mRcNumericPad.startAnimation(aniRcPanelFadeout);
			
			mStbPower.setVisibility(View.INVISIBLE);
			mIntegrationUiMain.setVisibility(View.INVISIBLE);
			mVod.setVisibility(View.INVISIBLE);
			mTvch.setVisibility(View.INVISIBLE);
			mSearch.setVisibility(View.INVISIBLE);
			mRcPanel.setVisibility(View.INVISIBLE);
			mRcNumericPad.setVisibility(View.INVISIBLE);
			mConfigPanel.setVisibility(View.VISIBLE);
		}
		
		if(type.equals("vod_tvch")) {
			mConfigPanel.setVisibility(View.VISIBLE);
		}
		
		Log.i("hwang", "(Search) after when mainActivity fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	}
	
	private void showChannelVolume() {
		hideCircleMenu();
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(TAG_FRAGMENT_CHANNEL_VOLUME);
		chagneFragment(channelVolume, TAG_FRAGMENT_CHANNEL_VOLUME);
		
		clearAllFragment();
	}

	private void showFourWay() {
		hideCircleMenu();
		RcFourWay fourWay = RcFourWay.newInstance(TAG_FRAGMENT_FOURWAY);
		chagneFragment(fourWay, TAG_FRAGMENT_FOURWAY);
		
		clearAllFragment();
	}
	
	private void showTrickPlay() {
		hideCircleMenu();
		RcTrickPlay trickPlay = RcTrickPlay.newInstance(TAG_FRAGMENT_TRICK_PLAY);
		chagneFragment(trickPlay, TAG_FRAGMENT_TRICK_PLAY);
		
		clearAllFragment();
	}
	
	private void clearAllFragment() {
		
		// ---------------------------
		// adult certification
		// ---------------------------
		final ConfigAdultCert adultCert = (ConfigAdultCert) getSupportFragmentManager().findFragmentByTag("adult_cert");
		if (adultCert != null) {
			super.onBackPressed();	// go to config main
		}
		
		// ---------------------------
		// config product
		// ---------------------------
		final ConfigProduct configProduct = (ConfigProduct) getSupportFragmentManager().findFragmentByTag("config_product");
		if (configProduct != null) {
			super.onBackPressed();	// go to config main
		}
		
		// ---------------------------
		// config region
		// ---------------------------
		final ConfigRegion configRegion = (ConfigRegion) getSupportFragmentManager().findFragmentByTag("config_region");
		if (configRegion != null) {
			super.onBackPressed();	// go to config main
		}
		
		// ---------------------------
		// config와 search간의 순서를 생각해보자. 근본적으로 순서와 관계없이 처리하는 방법을 고려해보자....
		// config
		// ---------------------------
		final ConfigFragment config = (ConfigFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG);
		if (config != null) {
			if (config.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				mConfigPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
			}
			if (config.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mConfigPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
			}
		}
		
		// ---------------------------
		// search vod detail
		// ---------------------------
		final SearchVodDetail searchVodDetail = (SearchVodDetail) getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (searchVodDetail != null) {
			if (searchVodDetail.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				super.onBackPressed();	// go to destroyView of vodtvch
				return;
			}
		}
		
		// ---------------------------
		// search
		// ---------------------------
		final SearchFragment search = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (search != null) {
			if (search.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				
				super.onBackPressed();	// go to destroyView of vodtvch
			}
			if (search.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mSearchPanel.setVisibility(View.INVISIBLE);
				
				super.onBackPressed();	// go to destroyView of vodtvch
			}
		}
		
		// -------------------------------
		// vod, tvch (loading_data_panel)
		// -------------------------------
		Base base = (Base) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_BASE);
		if (base != null) {
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity TAG_FRAGMENT_BASE != null fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
			
			super.onBackPressed();
		} else {
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity TAG_FRAGMENT_BASE = null fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		}
		
		// ---------------------------
		// vod, tvch (vod_tvch_panel)
		// ---------------------------
		final VodTvch vodTVch = (VodTvch) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		if (vodTVch != null) {
			if (vodTVch.allowBackPressed() == 0) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				
				super.onBackPressed();	// go to destroyView of vodtvch
			}
			if (vodTVch.allowBackPressed() == 1) {	// sidebar가 열려있는경우...
				Fragment f = getSupportFragmentManager().findFragmentByTag(this.TAG_FRAGMENT_VOD_TVCH);
				if (f != null) {
					((VodTvch)f).mSlidingMenu.toggleSidebar();
					Log.i("hwang", "closing sidebar!!!");
					
					backToRc();
					super.onBackPressed();	// go to destroyView of vodtvch
				}
			}
		}
		
		clearVodTvchTopMeny();
	}
	
	public void clearVodTvchTopMeny() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
        if (f != null) {
        	getSupportFragmentManager().beginTransaction().remove(f).commit();
        }
	}

	private void openPopupMirroring() {
		rcIconOn();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		PopupMirroringEnter mirroringEnter = new PopupMirroringEnter();
		mirroringEnter.show(ft, PopupMirroringEnter.class.getSimpleName());
	}
	
	public void openMirroring() {
		noVodTvchDestroy = false;
		
		// vod_tvch화면에서 진입하면 down된다...
		final VodTvch vodTVch = (VodTvch) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		if (vodTVch != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.addToBackStack(null);
			ft.commit();
			
			noVodTvchDestroy = true;
		}
		
		Intent intent = new Intent(this, MirroringActivity.class);
		startActivity(intent);
	}
	
	private void openQwerty() {
		rcIconOn();
		
		Intent intent = new Intent(this, QwertyActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.qwerty_zoom_in, 0);
		
		/*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		QwertyFragment qwerty = new QwertyFragment();
		ft.setCustomAnimations(R.anim.zoom_enter, 0);
		qwerty.show(ft, QwertyFragment.class.getSimpleName());*/
	}
	
	private void rcIconOn() {
		if (mCircleMenuBg.getVisibility() == View.VISIBLE)
			mCircleMenuBg.startAnimation(aniQwertyFadeout);
		if (mCircleMenu.getVisibility() == View.VISIBLE)
			mCircleMenu.startAnimation(aniQwertyFadeout);
		toggleCircleMenu = true;
	}
	
	private void showCircleMenu() {
		mCircleMenuBg.setVisibility(View.VISIBLE);
		mCircleMenu.setVisibility(View.VISIBLE);
		
		mCircleMenu.clearAnimation();
		aniCirCleMenuEnter.setFillAfter(true);
		//mCircleMenu.startAnimation(aniCirCleMenuEnter);
		
		toggleCircleMenu = false;
		mRcIcon.setBackgroundResource(R.drawable.xml_rc_icon_off); // 리모컨아이콘 이미지(-)가 바뀌어야 한다.
	}
	
	private void hideCircleMenu() {
		mCircleMenuBg.setVisibility(View.INVISIBLE);
		mCircleMenu.setVisibility(View.INVISIBLE);
		
		mCircleMenu.clearAnimation();
		aniCirCleMenuExit.setFillAfter(true);
		//mCircleMenu.startAnimation(aniCirCleMenuExit);
		
		toggleCircleMenu = true;
		mRcIcon.setBackgroundResource(R.drawable.xml_rc_icon); // 리모컨아이콘 이미지(+)가 바뀌어야 한다.
	}


	
	private void chagneFragment(Fragment fragment, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.rc_panel, fragment, tag);
		ft.commit();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onResume() {
		Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (f != null) {
			boolean b = f.isVisible();
			boolean h = f.isHidden();
		}
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
	    //super.onSaveInstanceState(outState); // vod_tvch --> mirrorTV --> popup exit --> error
	}

	public Fragment getFragmentRcBottomMenu() {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		return f;
	}
	
	public Fragment getFragmentVodTvch() {
		Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		return f;
	}
	
	public Fragment getFragmentVodTvchTopMenu() {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
		return f;
	}
	
	
	
	
	
	

	/*@Override
	public void onEventQwertySelected() {
		//remoconIconOn();
		
		//mRemoconMenu.setVisibility(View.INVISIBLE);
		//toggleRemoconMenu = true;
		//mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
		
	}*/

	
	/*private long lastPressedTime;
	private static final int PERIOD = 2000;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			switch (event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				if (event.getDownTime() - lastPressedTime < PERIOD) {
					finish();
				} else {
					Toast.makeText(this, R.string.app_exit, Toast.LENGTH_SHORT).show();
					lastPressedTime = event.getEventTime();
				}
				return true;
			}
		}
		return false;
	}*/
	
//	OnBackStackChangedListener mOnBackStackChangedListener = new OnBackStackChangedListener() {
//		@Override
//		public void onBackStackChanged() {
//			LOG.i(CLASSNAME, "onBackStackChanged");
//			LOG.d(CLASSNAME, "onBackStackChanged getFragmentManager().getBackStackEntryCount(); : " + getFragmentManager().getBackStackEntryCount());
//			int i = getFragmentManager().getBackStackEntryCount();
//			if (i > 0) {
//				BackStackEntry tt = getFragmentManager().getBackStackEntryAt(i - 1);
//				int checkedId = tt.getBreadCrumbTitleRes();
//				LOG.i(CLASSNAME, "onBackStackChanged  checkedId : " + checkedId);
//				switch (checkedId) {
//				case R.id.btnFirst:
//					mTextView.setText("btnFirst");
//					break;
//				case R.id.btnSecond:
//					mTextView.setText("btnSecond");
//					break;
//				case R.id.btnThree:
//					mTextView.setText("btnThree");
//					break;
//				}
//			} else {
//				mTextView.setText("btnFirst");
//			}
//		}
//	};
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		getFragmentManager().addOnBackStackChangedListener(mOnBackStackChangedListener);
//	}
	
//	OnBackStackChangedListener mRemoveOnBackStackChangedListener = new OnBackStackChangedListener() {
//		@Override
//		public void onBackStackChanged() {
//			int i = getSupportFragmentManager().getBackStackEntryCount();
//			BackStackEntry tt = getSupportFragmentManager().getBackStackEntryAt(i - 1);
//			int checkedId = tt.getBreadCrumbTitleRes();
//		}
//	};
//	
//	OnBackStackChangedListener mAddOnBackStackChangedListener = new OnBackStackChangedListener() {
//		@Override
//		public void onBackStackChanged() {
//			int i = getSupportFragmentManager().getBackStackEntryCount();
//			//BackStackEntry tt = getSupportFragmentManager().getBackStackEntryAt(i - 1);
//			//int checkedId = tt.getBreadCrumbTitleRes();
//		}
//	};


}
