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
	
	public final String TAG_FRAGMENT_FOURWAY = "fourway";
	public final String TAG_FRAGMENT_CHANNEL_VOLUME = "channel_volume";
	
	boolean doubleBackKeyPressedToExitApp;
	
	FrameLayout mRcPanel;				// rc_panel(채널/볼륨, 사방향) 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)
	FrameLayout mRcNumericPad;			// rc_numeric_pad 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)
	
	ImageButton mStbPower, mIntegrationUiMain, mVod, mTvch, mSearch;	// top menu (셋탑전원, 통합ui메인, vod, TV채널, 검색)
	ImageButton mMirroring, mFourWay, mQwerty, mChannelVolume, mConfig;		// circle menu (미러TV, 사방향키, 쿼티, 채널/볼륨, 설정)
	
	ImageButton mRcIcon;				// remocon icon
	LinearLayout mCircleMenu;			// circle menu를 담고있는 레이아웃
	FrameLayout mCircleMenuBg;			// circle menu가 보일때 바탕에 깔리는 반투명배경의 레이아웃
	boolean toggleCircleMenu = true; 	// circle menu가 보이는지 or 안보이는지?

	FrameLayout mVodTvchPanel;			// top menu의 vod, tvch 아이콘의 메인화면의 레이아웃
	
	Animation aniRcPanelFadeout;
	Animation aniRcTopMenuFadein;
	Animation aniQwertyFadeout;
	
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
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
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

		// animation
		aniRcPanelFadeout= AnimationUtils.loadAnimation(this, R.anim.rc_panel_fadeout);
		aniRcPanelFadeout.setFillAfter(true);
		
		aniRcTopMenuFadein= AnimationUtils.loadAnimation(this, R.anim.rc_top_menu_fadein);
		aniQwertyFadeout = AnimationUtils.loadAnimation(this, R.anim.qwerty_fadeout);
	}

	private void setEvent() {
		// 하단 rcmenu 이벤트
		mCircleMenuBg.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
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
				mRcIcon.setBackgroundResource(R.drawable.rc_icon); // 리모컨아이콘 이미지가 바뀌어야 한다.
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// remocon icon
		case R.id.rc_icon:
			if (toggleCircleMenu) {
				rcIconOffNoAni();
			} else {
				rcIconOnNoAni();
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
			Log.i("hwang", "search");
			break;

		// bottom menu

		// circle menu
		case R.id.channel_volume:	// start of main display, Fragment at rc_panel framelayout
			openChannelVolume();
			break;
		case R.id.four_way:		// Fragment at rc_panel framelayout
			openFourWay();
			break;
		case R.id.mirroring:	// DialogFragment (popup with entire display)
			openPopupMirroring();
			break;
		case R.id.qwerty:		// Activity
			openQwerty();
			break;
		case R.id.config:
			rcIconOn();
			Log.i("hwang", "config");
			break;

		}

	}

	@Override
	public void onBackPressed() {
		{
			FragmentManager fm = getSupportFragmentManager();
			Log.i("hwang", "mainActivity onBackpressed fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		}
		
		// ---------------------
		// circle menu
		// ---------------------
		if(mCircleMenu.getVisibility() == View.VISIBLE) {
			rcIconOnNoAni();
        	return;
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
		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
		
		VodTvch vodTvch = VodTvch.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit); // 두 번째 진입때 문제발생...
		ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
	
	private void openChannelVolume() {
		rcIconOnNoAni();
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
		chagneFragment(channelVolume, TAG_FRAGMENT_CHANNEL_VOLUME);
	}

	private void openFourWay() {
		rcIconOnNoAni();
		RcFourWay fourWay = RcFourWay.newInstance(2);
		chagneFragment(fourWay, TAG_FRAGMENT_FOURWAY);
	}
	
	private void openPopupMirroring() {
		rcIconOn();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		PopupMirroringEnter mirroringEnter = new PopupMirroringEnter();
		mirroringEnter.show(ft, PopupMirroringEnter.class.getSimpleName());
	}
	
	public void openMirroring() {
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

	private void rcIconOnNoAni() {
		mCircleMenuBg.setVisibility(View.INVISIBLE);
		mCircleMenu.setVisibility(View.INVISIBLE);
		toggleCircleMenu = true;
		mRcIcon.setBackgroundResource(R.drawable.rc_icon); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}

	private void rcIconOffNoAni() {
		mCircleMenuBg.setVisibility(View.VISIBLE);
		mCircleMenu.setVisibility(View.VISIBLE);
		toggleCircleMenu = false;
		mRcIcon.setBackgroundResource(R.drawable.main_remocon_icon_off); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}
	
	private void chagneFragment(Fragment fragment, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.rc_panel, fragment, tag);
		ft.commit();
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
