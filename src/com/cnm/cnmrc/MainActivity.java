package com.cnm.cnmrc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cnm.cnmrc.fragment.config.ConfigAdultCert;
import com.cnm.cnmrc.fragment.config.ConfigArea;
import com.cnm.cnmrc.fragment.config.ConfigMain;
import com.cnm.cnmrc.fragment.config.ConfigProduct;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;
import com.cnm.cnmrc.fragment.rc.RcChannelVolume;
import com.cnm.cnmrc.fragment.rc.RcFourWay;
import com.cnm.cnmrc.fragment.rc.RcTopMenu;
import com.cnm.cnmrc.fragment.rc.RcTrickPlay;
import com.cnm.cnmrc.fragment.search.SearchMain;
import com.cnm.cnmrc.fragment.search.SearchNaver;
import com.cnm.cnmrc.fragment.search.SearchVodDetail;
import com.cnm.cnmrc.fragment.vodtvch.Base;
import com.cnm.cnmrc.fragment.vodtvch.VodDetail;
import com.cnm.cnmrc.fragment.vodtvch.VodTvchMain;
import com.cnm.cnmrc.popup.PopupGtvConnection;
import com.cnm.cnmrc.tcp.TCPClientRequestStatus;
import com.cnm.cnmrc.util.CnmPreferences;
import com.cnm.cnmrc.util.Util;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.DeviceFinder;
import com.google.android.apps.tvremote.RemoteDevice;
import com.google.android.apps.tvremote.widget.HighlightView;
import com.google.android.apps.tvremote.widget.KeyCodeButton;
import com.google.anymote.Key;

/**
 * 
 * @author minkyu
 * @date 2013.7.17
 * 
 *       메인화면 구성 
 *       1) 상단메뉴(top) / 하단숫자(numeric) / 하단메뉴(bottom) / 하단써클메뉴(circle)
 * 
 *       여기서 pannel이란 용어는 fragment가 대체되는 FrameLayout viewgroup을 의미한다. 
 *       2) vod_tvch_panel : top menu에서 vod, tvch 아이콘의 메인화면이 대체되는 fragment 영역 
 *       
 *       3) rc_panel : circle menu에서 미러TV, 사방향키, 채널/볼륨 화면이 대체되는 fragment 영역 circle
 *       menu의 쿼티화면은 activity로 처리함.
 * 
 *       4) 하단중앙 리모컨아이콘 : rc icon
 * 
 *       5) 처음시작화면이 메인화면이 리모컨화면이므로, 메인화면을 rc용어와 함께 사용함.
 * 
 *       6) 써클메뉴 - 볼륨/채널, 사방향키는 rc_panel의 Fragment로 처리함. - 미러TV, 쿼티, 설정은 전체화면을
 *       사용하는 Activity로 처리함.
 * 
 *       7) 상단메뉴 - vod, tvch은 vod_tvch_panel의 Fragment로 처리함. - 검색은 전체화면을 사용하는
 *       Activity로 처리함.
 * 
 *       8) fragment의 id는 fragment_*로 시작됨.
 * 
 *       9) vod, tvch 화면의 depth에 따른 명명규칙 (3가지 타입) - List - SemiDetail
 *       (Detail화면전에 보이는 list화면) - Detail * 만약 클래스명이 변경되면 이를 프로그램으로 변경적용해야한다.
 *       이유는 클래스를 런타임동적로딩으로 처리했음.
 * 
 *       10) VodTvch화면의
 * 
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, KeyCodeButton.KeyCodeHandler {

	// string constants for fragment tag 
	public final String TAG_FRAGMENT_VOD_TVCH = "vod_tvch";
	public final String TAG_FRAGMENT_BASE = "base";

	public final String TAG_FRAGMENT_SEARCH = "search";
	public final String TAG_FRAGMENT_CONFIG = "config";
	public final String TAG_FRAGMENT_CONFIG_AREA = "config_area";

	public final String TAG_FRAGMENT_FOURWAY = "fourway";
	public final String TAG_FRAGMENT_TRICK_PLAY = "trick_play";
	public final String TAG_FRAGMENT_CHANNEL_VOLUME = "channel_volume";

	boolean isFourWay = true; // for test, which fourway or trickplay

	boolean doubleBackKeyPressedToExitApp;

	boolean isMirroringTV = true; 				// for test, which MirroringTV or MirroringVOD
	boolean isChaeWon1 = true; 					// for test, which MirroringTV or MirroringVOD
	public boolean noVodTvchDestroy = false; 	// vod_tvch화면에서 mirroingTV을 진입할 때 VodTvch의 onDestroyView()에서 destory를 막기위해...

	FrameLayout mRcPanel; 		// rc_panel(채널/볼륨, 사방향) 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)
	FrameLayout mRcNumericPad; 	// rc_numeric_pad 화면 (sidebar overshootinterpolator효과시 화면아래에 보이므로 이를 안보이게 할려구...)

	ImageButton mMirroring, mFourWay, mQwerty, mChannelVolume, mConfig; // circle menu (미러TV, 사방향키, 쿼티, 채널/볼륨, 설정)

	ImageButton mRcIcon; 		// remocon icon
	LinearLayout mCircleMenu; 	// circle menu를 담고있는 레이아웃
	FrameLayout mCircleMenuBg; 	// circle menu가 보일때 바탕에 깔리는 반투명배경의 레이아웃
	boolean toggleCircleMenu = true; // circle menu가 보이는지 or 안보이는지?

	FrameLayout mVodTvchPanel; 	// top menu의 vod, tvch 아이콘의 메인화면의 레이아웃
	FrameLayout mSearchPanel; 	// top menu의 search 아이콘의 메인화면의 레이아웃
	FrameLayout mConfigPanel; 	// circle menu의 config 아이콘의 메인화면의 레이아웃

	public Animation aniRcPanelFadeout;
	public Animation aniRcTopMenuFadein;
	Animation aniQwertyFadeout;

	Animation aniCirCleMenuEnter, aniCirCleMenuExit;

	Dialog myProgressDialog;

	Handler handler;

	private HighlightView surface;

	Fragment mRcTopMenu;
	

	public MainActivity() {
		//super();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// java.lang.RuntimeException: Unable to stop activity {com.cnm.cnmrc/com.cnm.cnmrc.MainActivity}: android.os.NetworkOnMainThreadException
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_main);

		// hwang 2013-12-01
		//surface = (HighlightView) findViewById(R.id.HighlightView);

		if (savedInstanceState != null) {
			Bundle bundle = savedInstanceState.getBundle("save_data");
			isMirroringTV = bundle.getBoolean("isMirroringTV");
			isChaeWon1 = bundle.getBoolean("isChaeWon1");
		}

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
			((RcBottomMenu) f).setRemoconMode();
		}

		//        FragmentManager fm = getSupportFragmentManager();
		//        fm.removeOnBackStackChangedListener(mRemoveOnBackStackChangedListener);
		//        fm.addOnBackStackChangedListener(mAddOnBackStackChangedListener);

		// Gtv connection (팝업창)
		showPopupGtvConnection();

		showChannelVolume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.e("hwang-tvremote", "MainActivity : onNewIntent()");
	}

	@Override
	protected void onPause() {
		rcIconOn();
		Log.e("hwang-tvremote", "MainActivity : onPause()");

		super.onPause();
	}

	@Override
	protected void onResume() {
		Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (f != null) {
			boolean b = f.isVisible();
			boolean h = f.isHidden();
		}
		Log.e("hwang-tvremote", "MainActivity : onResume()");

		// hwang
		//Util.hideSoftKeyboard(this);

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.e("hwang-tvremote", "MainActivity : onDestroy()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Log.e("hwang-tvremote", "MainActivity : onConfigurationChanged()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Bundle bundle = new Bundle();

		bundle.putBoolean("isMirroringTV", isMirroringTV);

		if (isMirroringTV)
			isChaeWon1 ^= isChaeWon1;
		bundle.putBoolean("isChaeWon1", isChaeWon1);

		outState.putBundle("save_data", bundle);

		//super.onSaveInstanceState(outState); // vod_tvch --> mirrorTV -->  popup exit --> error

		Log.e("hwang-tvremote", "MainActivity : onSaveInstanceState()");
	}

	protected void onRestroreInstanceState(Bundle outState) {
		isMirroringTV = outState.getBoolean("isMirroringTV");

		Log.e("hwang-tvremote", "MainActivity : onRestroreInstanceState()");
	}

	// ----------------------------
	// prepare app
	// ----------------------------
	public void showPopupGtvConnection() {
		Log.e("hwang-tvremote", "MainActivity : Gtv connection (팝업창)");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		PopupGtvConnection popupGtvConnection = new PopupGtvConnection();
		popupGtvConnection.show(ft, PopupGtvConnection.class.getSimpleName());
	}

	private void initUI() {
		// rc_panel (채널/볼륨, 사방향) 
		mRcPanel = (FrameLayout) findViewById(R.id.rc_panel);

		// rc_numeric_pad
		mRcNumericPad = (FrameLayout) findViewById(R.id.rc_numeric_pad);

		// top menu

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
		mConfig = (ImageButton) findViewById(R.id.config_prevent_click_dispatching);

		// VOD and TVCh 메인화면
		mVodTvchPanel = (FrameLayout) findViewById(R.id.vod_tvch_panel);

		// Search 메인화면
		mSearchPanel = (FrameLayout) findViewById(R.id.search_panel);

		// Config 메인화면
		mConfigPanel = (FrameLayout) findViewById(R.id.config_panel);

		// animation
		aniRcPanelFadeout = AnimationUtils.loadAnimation(this, R.anim.rc_panel_fadeout);
		aniRcPanelFadeout.setFillAfter(true);

		aniRcTopMenuFadein = AnimationUtils.loadAnimation(this, R.anim.rc_top_menu_fadein);
		aniQwertyFadeout = AnimationUtils.loadAnimation(this, R.anim.qwerty_fadeout);

		aniCirCleMenuEnter = AnimationUtils.loadAnimation(this, R.anim.scale_up);
		aniCirCleMenuExit = AnimationUtils.loadAnimation(this, R.anim.scale_down);

		// progress bar
		myProgressDialog = new Dialog(this, R.style.NewProgressDialog);
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.anim_progressbar));
		//progressBar.setInterpolator(new DecelerateInterpolator());
		progressBar.setIndeterminate(true);
		myProgressDialog.addContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ColorDrawable colorDrawable = new ColorDrawable();
		//colorDrawable.setColor(0xFFFF0000); // not 진저브레드
		//myProgressDialog.getWindow().setBackgroundDrawable(colorDrawable);
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

		/*
		 * aniRcPanelFadeout.setAnimationListener(new
		 * Animation.AnimationListener() {
		 * 
		 * @Override public void onAnimationStart(Animation animation) { }
		 * 
		 * @Override public void onAnimationEnd(Animation animation) {
		 * mRcPanel.setVisibility(View.INVISIBLE);
		 * mVod.setVisibility(View.INVISIBLE);
		 * mTvch.setVisibility(View.INVISIBLE);
		 * mSearch.setVisibility(View.INVISIBLE); }
		 * 
		 * @Override public void onAnimationRepeat(Animation animation) { } });
		 */

		aniQwertyFadeout.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCircleMenuBg.setVisibility(View.INVISIBLE);
				mCircleMenu.setVisibility(View.INVISIBLE);
				mRcIcon.setBackgroundResource(R.drawable.xml_rc_icon); // 리모컨아이콘 이미지가 바뀌어야 한다.
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});

		aniCirCleMenuEnter.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mCircleMenu.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

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
			public void onAnimationEnd(Animation animation) {
			}

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

		// bottom menu

		// circle menu
		case R.id.channel_volume: // replace하므로 존재체크필요없음. 시작화면, replace개념이므로 show(), Fragment at rc_panel framelayout
			showChannelVolume();
			break;
		case R.id.four_way: // replace하므로 존재체크필요없음. replace개념이므로 show(), Fragment at rc_panel framelayout
			// hwang 2013-11-20
			// STB에 STB의 상태를 요구한다.
			// wifi check
			if (!Util.isWifiAvailable(this)) {
				Toast.makeText(this, "WiFi not Available", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// hwang 2013-11-26
			RemoteDevice remoteDevice = getConnectionManager().getTarget();
			String hostAddress;
			if (remoteDevice != null) {
				hostAddress = remoteDevice.getAddress().getHostAddress();
			} else {
				// STB alive check
				CnmPreferences pref = CnmPreferences.getInstance();
				hostAddress = pref.loadPairingHostAddress(this);
			}


			// test
			hostAddress = "192.168.0.6";

			try {
				if (hostAddress.equals("")) {
					Toast.makeText(this, "Not connect STB", Toast.LENGTH_SHORT).show();
					return;
				} else {

					InetAddress address = InetAddress.getByName(hostAddress);
					boolean alive = address.isReachable(2000);
					if (alive) {
						mMainHandler = new SendMassgeHandler();

						TCPClientRequestStatus tcpClient = new TCPClientRequestStatus(mMainHandler, hostAddress);
						tcpClient.start();
						return;
					} else {
						Toast.makeText(this, "Not connect STB", Toast.LENGTH_SHORT).show();
						return;

					}
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			if (isFourWay) {
				isFourWay = false;
				showFourWay();
			} else {
				isFourWay = true;
				showTrickPlay();
			}
			break;
		case R.id.mirroring: // DialogFragment (popup with entire display)
			openPopupMirroring();
			break;
		case R.id.qwerty: // Activity
			openQwerty();
			break;
		case R.id.config_prevent_click_dispatching:
			if (isExistFragment(TAG_FRAGMENT_CONFIG)) {
				Log.i("hwang", "No entering config !!!");
				hideCircleMenu();
			} else {
				Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
				if (f != null)
					goToConfig("vod_tvch");
				else
					goToConfig("rc");
			}
			break;

		}

	}

	private boolean isExistFragment(String tag) {
		Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
		if (f != null)
			return true; // yes, exist!!!
		else
			return false;
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
		//		if (noVodTvchDestroy) {
		//			noVodTvchDestroy = false;
		//			return;
		//		}

		// ---------------------
		// circle menu
		// ---------------------
		if (mCircleMenu.getVisibility() == View.VISIBLE) {
			hideCircleMenu();
			return;
		}

		// ---------------------------
		// adult certification
		// ---------------------------
		final ConfigAdultCert adultCert = (ConfigAdultCert) getSupportFragmentManager().findFragmentByTag("adult_cert");
		if (adultCert != null) {
			super.onBackPressed(); // go to config main
			return;
		}

		// ---------------------------
		// config product
		// ---------------------------
		final ConfigProduct configProduct = (ConfigProduct) getSupportFragmentManager().findFragmentByTag("config_product");
		if (configProduct != null) {
			super.onBackPressed(); // go to config main
			return;
		}

		// ---------------------------
		// config area
		// ---------------------------
		final ConfigArea configArea = (ConfigArea) getSupportFragmentManager().findFragmentByTag("config_area");
		if (configArea != null) {
			super.onBackPressed(); // go to config main
			return;
		}

		// ---------------------------
		// config와 search간의 순서를 생각해보자. 근본적으로 순서와 관계없이 처리하는 방법을 고려해보자....
		// config
		// ---------------------------
		final ConfigMain config = (ConfigMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG);
		if (config != null) {
			if (config.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				mConfigPanel.setVisibility(View.INVISIBLE);

				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
			if (config.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mConfigPanel.setVisibility(View.INVISIBLE);

				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
		}

		// ---------------------------
		// search vod detail
		// ---------------------------
		final SearchVodDetail searchVodDetail = (SearchVodDetail) getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (searchVodDetail != null) {
			if (searchVodDetail.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
		}

		// ---------------------------
		// search
		// ---------------------------
		final SearchMain search = (SearchMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (search != null) {
			final SearchNaver searchNaverSub = (SearchNaver) getSupportFragmentManager().findFragmentByTag("search_naver");
			if (searchNaverSub != null && searchNaverSub.getWebView().getVisibility() == View.VISIBLE) {
				searchNaverSub.getWebView().setVisibility(View.INVISIBLE);
				return;
			}
			if (search.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
			if (search.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mSearchPanel.setVisibility(View.INVISIBLE);
				super.onBackPressed(); // go to destroyView of vodtvch
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
		final VodTvchMain vodTVch = (VodTvchMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		if (vodTVch != null) {
			if (vodTVch.allowBackPressed() == 0) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();

				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
			if (vodTVch.allowBackPressed() == 1) { // sidebar가 열려있는경우...
				Fragment f = getSupportFragmentManager().findFragmentByTag(this.TAG_FRAGMENT_VOD_TVCH);
				if (f != null) {
					((VodTvchMain) f).getSlidingMenu().toggleSidebar();
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
				doubleBackKeyPressedToExitApp = false;
			}
		}, 2000);

	}

	private void backToRc() {
		RcTopMenu rcTopMenu = getFragmentRcTopMenu();
		if (rcTopMenu != null)
			rcTopMenu.backToRc();

		mRcPanel.startAnimation(aniRcTopMenuFadein);
		mRcNumericPad.startAnimation(aniRcTopMenuFadein);

		mRcPanel.setVisibility(View.VISIBLE);
		mRcNumericPad.setVisibility(View.VISIBLE);
		mVodTvchPanel.setVisibility(View.INVISIBLE);
	}

	public void goToVodTvch(String type) {
		// hwang 2013-11-26
		RemoteDevice remoteDevice = getConnectionManager().getTarget();
		if (remoteDevice != null) {
			String hostAddress = remoteDevice.getAddress().getHostAddress();
			CnmPreferences pref = CnmPreferences.getInstance();
			pref.savePairingHostAddress(this, hostAddress);
		} 
		
		clearVodTvchTopMenu();

		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));

		VodTvchMain vodTvch = VodTvchMain.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		//  ft.replace전에 animation을 설정해야 한다.
		ft.setCustomAnimations(R.anim.fragment_entering, 0);
		ft.replace(R.id.vod_tvch_panel, vodTvch, TAG_FRAGMENT_VOD_TVCH);
		ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();

		RcTopMenu rcTopMenu = getFragmentRcTopMenu();
		if (rcTopMenu != null)
			rcTopMenu.goToVodTvch();

		mRcPanel.startAnimation(aniRcPanelFadeout);
		mRcNumericPad.startAnimation(aniRcPanelFadeout);

		mRcPanel.setVisibility(View.INVISIBLE);
		mRcNumericPad.setVisibility(View.INVISIBLE);
		mVodTvchPanel.setVisibility(View.VISIBLE);

		Log.i("hwang", "after when mainActivity fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	}

	public void goToSearch(String type) {
		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "(Search) before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));

		SearchMain search = SearchMain.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		//  ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit); // 두 번째 진입때 문제발생...
		ft.setCustomAnimations(R.anim.fragment_entering, 0);
		ft.add(R.id.search_panel, search, TAG_FRAGMENT_SEARCH);
		ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();

		if (type.equals("rc")) {
			RcTopMenu rcTopMenu = getFragmentRcTopMenu();
			if (rcTopMenu != null)
				rcTopMenu.goToVodTvch();

			mRcPanel.startAnimation(aniRcPanelFadeout);
			mRcNumericPad.startAnimation(aniRcPanelFadeout);

			mRcPanel.setVisibility(View.INVISIBLE);
			mRcNumericPad.setVisibility(View.INVISIBLE);
			mVodTvchPanel.setVisibility(View.VISIBLE);

			mSearchPanel.setVisibility(View.VISIBLE);
		}

		if (type.equals("vod_tvch")) {
			mSearchPanel.setVisibility(View.VISIBLE);
		}

		Log.i("hwang", "(Search) after when mainActivity fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
	}

	public void goToConfig(String type) {
		rcIconOn();

		FragmentManager fm = getSupportFragmentManager();
		Log.i("hwang", "(Search) before when mainActivity  fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));

		ConfigMain config = ConfigMain.newInstance(type);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		//  ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit); // 두 번째 진입때 문제발생...
		ft.setCustomAnimations(R.anim.fragment_entering, 0);
		ft.add(R.id.config_panel, config, TAG_FRAGMENT_CONFIG);
		ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.commit();
		fm.executePendingTransactions();

		if (type.equals("rc")) {
			RcTopMenu rcTopMenu = getFragmentRcTopMenu();
			if (rcTopMenu != null)
				rcTopMenu.goToVodTvch();

			mRcPanel.startAnimation(aniRcPanelFadeout);
			mRcNumericPad.startAnimation(aniRcPanelFadeout);

			mRcPanel.setVisibility(View.INVISIBLE);
			mRcNumericPad.setVisibility(View.INVISIBLE);
			mConfigPanel.setVisibility(View.VISIBLE);
		}

		if (type.equals("vod_tvch")) {
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
			super.onBackPressed(); // go to config main
		}

		// ---------------------------
		// config product
		// ---------------------------
		final ConfigProduct configProduct = (ConfigProduct) getSupportFragmentManager().findFragmentByTag("config_product");
		if (configProduct != null) {
			super.onBackPressed(); // go to config main
		}

		// ---------------------------
		// config area
		// ---------------------------
		final ConfigArea configArea = (ConfigArea) getSupportFragmentManager().findFragmentByTag("config_area");
		if (configArea != null) {
			super.onBackPressed(); // go to config main
		}

		// ---------------------------
		// config와 search간의 순서를 생각해보자. 근본적으로 순서와 관계없이 처리하는 방법을 고려해보자....
		// config
		// ---------------------------
		final ConfigMain config = (ConfigMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG);
		if (config != null) {
			if (config.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();
				mConfigPanel.setVisibility(View.INVISIBLE);

				super.onBackPressed(); // go to destroyView of vodtvch
			}
			if (config.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mConfigPanel.setVisibility(View.INVISIBLE);

				super.onBackPressed(); // go to destroyView of vodtvch
			}
		}

		// ---------------------------
		// search vod detail
		// ---------------------------
		final VodDetail searchVodDetail = (VodDetail) getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (searchVodDetail != null) {
			if (searchVodDetail.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				super.onBackPressed(); // go to destroyView of vodtvch
				return;
			}
		}

		// ---------------------------
		// search
		// ---------------------------
		final SearchMain search = (SearchMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);
		if (search != null) {
			if (search.allowBackPressed().equals("rc")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();

				super.onBackPressed(); // go to destroyView of vodtvch
			}
			if (search.allowBackPressed().equals("vod_tvch")) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mSearchPanel.setVisibility(View.INVISIBLE);

				super.onBackPressed(); // go to destroyView of vodtvch
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
		final VodTvchMain vodTVch = (VodTvchMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		if (vodTVch != null) {
			if (vodTVch.allowBackPressed() == 0) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				backToRc();

				super.onBackPressed(); // go to destroyView of vodtvch
			}
			if (vodTVch.allowBackPressed() == 1) { // sidebar가 열려있는경우...
				Fragment f = getSupportFragmentManager().findFragmentByTag(this.TAG_FRAGMENT_VOD_TVCH);
				if (f != null) {
					((VodTvchMain) f).getSlidingMenu().toggleSidebar();
					Log.i("hwang", "closing sidebar!!!");

					backToRc();
					super.onBackPressed(); // go to destroyView of vodtvch
				}
			}
		}

		clearVodTvchTopMenu();
	}

	public void clearVodTvchTopMenu() {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
		if (f != null) {
			getSupportFragmentManager().beginTransaction().remove(f).commit();
		}
	}

	private void openPopupMirroring() {
		rcIconOn();

		openMirroring();
		//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//		PopupMirroringEnter mirroringEnter = new PopupMirroringEnter();
		//		mirroringEnter.show(ft, PopupMirroringEnter.class.getSimpleName());
	}

	public void openMirroring() {
		//noVodTvchDestroy = false;

		// vod_tvch화면에서 진입하면 down된다...
		//		final VodTvchMain vodTVch = (VodTvchMain) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD_TVCH);
		//		if (vodTVch != null) {
		//			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//			ft.addToBackStack(null);
		//			ft.commit();
		//
		//			noVodTvchDestroy = true; // vod_tvch화면에서 mirroingTV을 진입할 때 VodTvch의 onDestroyView()에서 destory를 막기위해...
		//		}

		// hwang 2013-11-20
		// STB에 STB의 상태를 요구한다.
		// wifi check
		if (!Util.isWifiAvailable(this)) {
			Toast.makeText(this, "WiFi not Available", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// hwang 2013-11-26
		RemoteDevice remoteDevice = getConnectionManager().getTarget();
		String hostAddress;
		if (remoteDevice != null) {
			hostAddress = remoteDevice.getAddress().getHostAddress();
		} else {
			// STB alive check
			CnmPreferences pref = CnmPreferences.getInstance();
			hostAddress = pref.loadPairingHostAddress(this);
		}


		// test
		//hostAddress = "192.168.0.6";

		try {
			if (hostAddress.equals("")) {
				Toast.makeText(this, "Not connect STB", Toast.LENGTH_SHORT).show();
				return;
			} else {

				InetAddress address = InetAddress.getByName(hostAddress);
				boolean alive = address.isReachable(2000);
				if (alive) {
					mMainHandler = new SendMassgeHandler();

					TCPClientRequestStatus tcpClient = new TCPClientRequestStatus(mMainHandler, hostAddress);
					tcpClient.start();
					return;
				} else {
					Toast.makeText(this, "Not connect STB", Toast.LENGTH_SHORT).show();
					return;

				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isMirroringTV) {
			Intent intent = new Intent(this, MirroringTvchActivity.class);
			startActivity(intent);
			isMirroringTV = false;
		} else {
			Intent intent = new Intent(this, MirroringVodActivity.class);
			intent.putExtra("isChaeWon1", isChaeWon1);
			startActivity(intent);
			isMirroringTV = true;
		}

	}
	
	// Handler 클래스
	SendMassgeHandler mMainHandler = null;
	
    class SendMassgeHandler extends Handler {
         
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             
            switch (msg.what) {
            case 1:
            	String str = (String)msg.obj;
            	Log.d("hwang", "from server : " + str + " <--- " + System.currentTimeMillis());
                break;
 
            default:
                break;
            }
        }
         
    };

	private void openQwerty() {
		rcIconOn();

		Intent intent = new Intent(this, QwertyActivity.class);
		//		Intent intent = new Intent(this, KeyboardActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.qwerty_zoom_in, 0);

		/*
		 * FragmentTransaction ft =
		 * getSupportFragmentManager().beginTransaction(); QwertyFragment qwerty
		 * = new QwertyFragment(); ft.setCustomAnimations(R.anim.zoom_enter, 0);
		 * qwerty.show(ft, QwertyFragment.class.getSimpleName());
		 */
	}

	// remove hwang 2013-11-28
//	public void openGtvConnection() {
//		rcIconOn();
//
//		Intent intent = new Intent(this, DeviceFinder.class);
//		startActivity(intent);
//		overridePendingTransition(R.anim.qwerty_zoom_in, 0);
//	}

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

	public Dialog getMyProgressBar() {
		return myProgressDialog;
	}

	public RcTopMenu getFragmentRcTopMenu() {
		RcTopMenu f = (RcTopMenu) getSupportFragmentManager().findFragmentById(R.id.fragment_rc_top_menu);
		return f;
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

	public Fragment getFragmentConfig() {
		Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG);
		return f;
	}

	public Fragment getFragmentConfigArea() {
		Fragment f = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_CONFIG_AREA);
		return f;
	}

	// --------------------------------------
	// tvremocon
	// --------------------------------------
	// hwang 2013-12-01
	/*public HighlightView getHighlightView() {
		return surface;
	}*/

	// KeyCode handler implementation.
	public void onRelease(Key.Code keyCode) {
		getCommands().key(keyCode, Key.Action.UP);
	}

	public void onTouch(Key.Code keyCode) {
		//playClick();
		getCommands().key(keyCode, Key.Action.DOWN);
	}

	public void flingIntent(Intent intent) {
		if (intent != null) {
			if (Intent.ACTION_SEND.equals(intent.getAction())) {
				String text = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (text != null) {
					Uri uri = Uri.parse(text);
					if (uri != null && ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
						getCommands().flingUrl(text);
					} else {
						Toast.makeText(this, R.string.error_could_not_send_url, Toast.LENGTH_SHORT).show();
					}
				} else {
					Log.w("hwang", "No URI to fling");
				}
			}
		}
	}

	/*
	 * @Override public void onEventQwertySelected() { //remoconIconOn();
	 * 
	 * //mRemoconMenu.setVisibility(View.INVISIBLE); //toggleRemoconMenu = true;
	 * //mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); //
	 * 리모컨아이콘 이미지가 바뀌어야 한다.
	 * 
	 * }
	 */

	/*
	 * private long lastPressedTime; private static final int PERIOD = 2000;
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { switch
	 * (event.getAction()) { case KeyEvent.ACTION_DOWN: if (event.getDownTime()
	 * - lastPressedTime < PERIOD) { finish(); } else { Toast.makeText(this,
	 * R.string.app_exit, Toast.LENGTH_SHORT).show(); lastPressedTime =
	 * event.getEventTime(); } return true; } } return false; }
	 */

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

	//	  private static final String LOG_TAG = "RemoteActivity";
	//
	//	  private HighlightView surface;
	//
	//	  private final Handler handler;
	//
	//	  /**
	//	   * The enum represents modes of the remote controller with
	//	   * {@link SlidingLayout} screens assignment. In conjunction with
	//	   * {@link ModeSelector} allows sliding between the screens.
	//	   */
	//	  private enum RemoteMode {
	//	    TV(0, R.drawable.icon_04_touchpad_selector),
	//	    TOUCHPAD(1, R.drawable.icon_04_buttons_selector);
	//
	//	    private final int screenId;
	//	    private final int switchButtonId;
	//
	//	    RemoteMode(int screenId, int switchButtonId) {
	//	      this.screenId = screenId;
	//	      this.switchButtonId = switchButtonId;
	//	    }
	//	  }
	//
	//	  /**
	//	   * Mode selector allow sliding across the modes, keeps currently selected mode
	//	   * information, and slides among the modes.
	//	   */
	//	  private static final class ModeSelector {
	//	    private final SlidingLayout slidingLayout;
	//	    private final ImageButton imageButton;
	//	    private RemoteMode mode;
	//
	//	    ModeSelector(
	//	        RemoteMode initialMode, SlidingLayout slidingLayout, ImageButton imageButton) {
	//	      mode = initialMode;
	//
	//	      this.slidingLayout = slidingLayout;
	//	      this.imageButton = imageButton;
	//
	//	      applyMode();
	//	    }
	//
	//	    void slideNext() {
	//	      setMode(RemoteMode.TOUCHPAD.equals(mode) ? RemoteMode.TV : RemoteMode.TOUCHPAD);
	//	    }
	//
	//	    void setMode(RemoteMode newMode) {
	//	      mode = newMode;
	//	      applyMode();
	//	    }
	//
	//	    void applyMode() {
	//	      slidingLayout.snapToScreen(mode.screenId);
	//	      imageButton.setImageResource(mode.switchButtonId);
	//	    }
	//	  }
	//	
	//
	//	
	//	
	//	  public HighlightView getHighlightView() {
	//		    return surface;
	//		  }
	//
	//		  // KeyCode handler implementation.
	//		  public void onRelease(Key.Code keyCode) {
	//		    getCommands().key(keyCode, Key.Action.UP);
	//		  }
	//
	//		  public void onTouch(Key.Code keyCode) {
	//		    playClick();
	//		    getCommands().key(keyCode, Key.Action.DOWN);
	//		  }
	//
	//		  private void playClick() {
	//		    ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).playSoundEffect(
	//		        AudioManager.FX_KEY_CLICK);
	//		  }
	//
	//		  private void flingIntent(Intent intent) {
	//		    if (intent != null) {
	//		      if (Intent.ACTION_SEND.equals(intent.getAction())) {
	//		        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
	//		        if (text != null) {
	//		          Uri uri = Uri.parse(text);
	//		          if (uri != null && ("http".equals(uri.getScheme())
	//		              || "https".equals(uri.getScheme()))) {
	//		            getCommands().flingUrl(text);
	//		          } else {
	//		            Toast.makeText(
	//		                this, R.string.error_could_not_send_url, Toast.LENGTH_SHORT)
	//		                .show();
	//		          }
	//		        } else {
	//		          Log.w(LOG_TAG, "No URI to fling");
	//		        }
	//		      }
	//		    }
	//		  }
	//
	//		  @Override
	//		  protected void onKeyboardOpened() {
	//		    showActivity(KeyboardActivity.class);
	//		  }
	//
	//		  // SUBACTIVITIES
	//
	//		  /**
	//		   * The activities that can be launched from the main screen.
	//		   * <p>
	//		   * These codes should not conflict with the request codes defined in
	//		   * {@link BaseActivity}.
	//		   */
	//		  private enum SubActivity {
	//		    VOICE_SEARCH,
	//		    UNKNOWN;
	//
	//		    public int code() {
	//		      return BaseActivity.FIRST_USER_CODE + ordinal();
	//		    }
	//
	//		    public static SubActivity fromCode(int code) {
	//		      for (SubActivity activity : values()) {
	//		        if (code == activity.code()) {
	//		          return activity;
	//		        }
	//		      }
	//		      return UNKNOWN;
	//		    }
	//		  }
	//	
	//	
	//	  @Override
	//	  protected void onActivityResult(
	//	      int requestCode, int resultCode, Intent data) {
	//	    SubActivity activity = SubActivity.fromCode(requestCode);
	//	    switch (activity) {
	//	      case VOICE_SEARCH:
	//	        onVoiceSearchResult(resultCode, data);
	//	        break;
	//
	//	      default:
	//	        super.onActivityResult(requestCode, resultCode, data);
	//	        break;
	//	    }
	//	  }
	//
	//	  // VOICE SEARCH
	//
	//	  private void showVoiceSearchActivity() {
	//	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	//	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	//	        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	//	    startActivityForResult(intent, SubActivity.VOICE_SEARCH.code());
	//	  }
	//
	//	  private void onVoiceSearchResult(int resultCode, Intent data) {
	//	    String searchQuery;
	//
	//	    if ((resultCode == RESULT_CANCELED) || (data == null)) {
	//	      return;
	//	    }
	//
	//	    ArrayList<String> queryResults =
	//	        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	//	    if ((queryResults == null) || (queryResults.isEmpty())) {
	//	      Log.d(LOG_TAG, "No results from VoiceSearch server.");
	//	      return;
	//	    } else {
	//	      searchQuery = queryResults.get(0);
	//	      if (TextUtils.isEmpty(searchQuery)) {
	//	        Log.d(LOG_TAG, "Empty result from VoiceSearch server.");
	//	        return;
	//	      }
	//	    }
	//
	//	    showVoiceSearchDialog(searchQuery);
	//	  }
	//
	//	  private void showVoiceSearchDialog(final String query) {
	//	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	//	    builder
	//	        .setNeutralButton(
	//	            R.string.voice_send, new DialogInterface.OnClickListener() {
	//	              public void onClick(DialogInterface dialog, int which) {
	//	                getCommands().string(query);
	//	              }
	//	            })
	//	        .setPositiveButton(
	//	            R.string.voice_search_send, new DialogInterface.OnClickListener() {
	//	              public void onClick(DialogInterface dialog, int which) {
	//	                getCommands().keyPress(Key.Code.KEYCODE_SEARCH);
	//	                // Send query delayed
	//	                handler.postDelayed(new Runnable() {
	//	                  public void run() {
	//	                    getCommands().string(query);
	//	                  }
	//	                }, getResources().getInteger(R.integer.search_query_delay));
	//	              }
	//	            })
	//	        .setNegativeButton(
	//	            R.string.pairing_cancel, new DialogInterface.OnClickListener() {
	//	              public void onClick(DialogInterface dialog, int which) {
	//	              }
	//	            })
	//	        .setCancelable(true)
	//	        .setTitle(R.string.voice_dialog_label)
	//	        .setMessage(query);
	//	    builder.create().show();
	//	  }

}
