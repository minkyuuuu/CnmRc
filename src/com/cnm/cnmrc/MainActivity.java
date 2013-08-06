package com.cnm.cnmrc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.cnm.cnmrc.fragment.popup.PopupMirroringEnter;
import com.cnm.cnmrc.fragment.rc.RcChannelVolume;
import com.cnm.cnmrc.fragment.rc.RcFourWay;
import com.cnm.cnmrc.fragment.vod.Vod;

/**
 * 
 * @author hwangminkyu
 * @date 2013.7.17
 * 
 * 메인화면 구성
 * 1) 상단메뉴(top) / 하단숫자(numeric) / 하단메뉴(bottom) / 하단써클메뉴(circle)
 * 
 * 여기서 pannel이란 용어는 fragment가 대체돼는 FrameLayout viewgroup을 의미한다.
 * 2) vod_tvch_panel : top menu에서 vod, tvch 아이콘의 메인화면이 대체돼는 fragment 영역
 * 3) rc_panel : circle menu에서 미러TV, 사방향키, 채널/볼륨 화면이 대체돼는 fragment 영역
 *               circle menu의 쿼티화면은 activity로 처리함.
 *               
 * 4) 하단중앙 리모컨아이콘 : remocon icon
 *
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
	
	// string constant for fragment tag 
	public final static String TAG_FRAGMENT_VOD = "vod";
	public final static String TAG_FRAGMENT_TVCH = "tvch";
	public final static String TAG_FRAGMENT_FOURWAY = "fourway";
	public final static String TAG_FRAGMENT_CHANNEL_VOLUME = "channel_volume";
	
	boolean doubleBackKeyPressedToExitApp;
	
	ImageButton mStbPower, mIntegrationUiMain, mVod, mTVChannel, mSearch;	// top menu (셋탑전원, 통합ui메인, vod, TV채널, 검색)
	ImageButton mPrevious, mFavoriteChannel, mViewSwitch, mExit;			// bottom menu (이전, 선호채널, 보기전환, 나가기)
	ImageButton mMirroring, mFourWay, mQwerty, mChannelVolume, mConfig;		// circle menu (미러TV, 사방향키, 쿼티, 채널/볼륨, 설정)
	
	ImageButton mRcIcon;				// remocon icon
	LinearLayout mCircleMenu;			// circle menu를 담고있는 레이아웃
	FrameLayout mCircleMenuBg;			// circle menu가 보일때 바탕에 깔리는 반투명배경의 레이아웃
	boolean toggleCircleMenu = true; 	// circle menu가 보이는지 or 안보이는지?

	FrameLayout mVodTvchPanel;				// top menu의 vod, tvch 아이콘의 메인화면의 레이아웃
	
	Animation animationRcMain;
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

		// ----------------------------------
		// remocon 화면 전환 (채널/볼륨 & 사방향키)
		// 첫화면이 채널/볼륨화면이다.
		// ----------------------------------
		RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
		chagneFragment(channelVolume, TAG_FRAGMENT_CHANNEL_VOLUME);
	}
	

	@Override
	protected void onPause() {
		rcIconOn();
		Log.i("hwang", "pause");
		super.onPause();
	}

	private void initUI() {
		// top menu
		mStbPower = (ImageButton) findViewById(R.id.stb_power);
		mIntegrationUiMain = (ImageButton) findViewById(R.id.integration_ui_main);
		mVod = (ImageButton) findViewById(R.id.vod);
		mTVChannel = (ImageButton) findViewById(R.id.tvch);
		mSearch = (ImageButton) findViewById(R.id.vod_top_search);
		
		// numeric menu

		// bottom menu
		mPrevious = (ImageButton) findViewById(R.id.previous);
		mFavoriteChannel = (ImageButton) findViewById(R.id.favorite_channel);
		mViewSwitch = (ImageButton) findViewById(R.id.view_switch);
		mExit = (ImageButton) findViewById(R.id.exit);
		
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

		animationRcMain= AnimationUtils.loadAnimation(this, R.anim.rc_main_fadein);
		
		animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.qwerty_fade_out);
		// animationZoomOut = AnimationUtils.loadAnimation(this, R.anim.qwerty_zoom_out);
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
		mTVChannel.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		// numeric menu

		// bottom menu
		mPrevious.setOnClickListener(this);
		mFavoriteChannel.setOnClickListener(this);	// 선호채널
		mViewSwitch.setOnClickListener(this);		// 보기전환
		mExit.setOnClickListener(this);

		// remocon icon
		mRcIcon.setOnClickListener(this);
		
		// circle menu
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
            	mCircleMenuBg.setVisibility(View.INVISIBLE);
            	mCircleMenu.setVisibility(View.INVISIBLE);
            	mRcIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
            }

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});

	}

	private void chagneFragment(Fragment fragment, String tag) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.rc_panel, fragment, tag);
		ft.commit();
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
			{
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				Vod vodMain = new Vod();
				
				//  ft.replace전에 animation을 설정해야 한다.
				ft.setCustomAnimations(R.anim.vod_chtv_main_entering, 0, 0, R.anim.vod_chtv_main_exit);
				//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(R.id.vod_tvch_panel, vodMain, TAG_FRAGMENT_VOD);
				ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
				ft.commit();
	
				mVodTvchPanel.setVisibility(View.VISIBLE);
			}
			Log.i("hwang", "vod main");
			break ;
		case R.id.tvch:
			Log.i("hwang", "tvch main");
			break;
		case R.id.vod_top_search:
			Log.i("hwang", "search");
			break;

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

		// circle menu
		case R.id.mirroring:
			rcIconOn();

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			PopupMirroringEnter mirroringEnter = new PopupMirroringEnter();
			mirroringEnter.show(ft, PopupMirroringEnter.class.getSimpleName());

			Log.i("hwang", "mirroring");
			break;
		case R.id.four_way:
			rcIconOnNoAni();
			RcFourWay fourWay = RcFourWay.newInstance(2);
			chagneFragment(fourWay, TAG_FRAGMENT_FOURWAY);
			Log.i("hwang", "four_way");
			break;
		case R.id.qwerty:
			rcIconOn();
			{
				Intent intent = new Intent(this, QwertyActivity.class);
				startActivity(intent);
			}
			overridePendingTransition(R.anim.qwerty_zoom_in, 0);
			
			/*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			QwertyFragment qwerty = new QwertyFragment();
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.setCustomAnimations(R.anim.zoom_enter, 0);
			qwerty.show(ft, QwertyFragment.class.getSimpleName());*/
			Log.i("hwang", "qwertyxxx");
			break;
		case R.id.channel_volume: // start of main display
			rcIconOnNoAni();
			RcChannelVolume channelVolume = RcChannelVolume.newInstance(1);
			chagneFragment(channelVolume, TAG_FRAGMENT_CHANNEL_VOLUME);
			Log.i("hwang", "channel volume");
			break;
		case R.id.config:
			rcIconOn();
			Log.i("hwang", "config");
			break;

		}

	}

	private void rcIconOn() {
		if (mCircleMenuBg.getVisibility() == View.VISIBLE)
			mCircleMenuBg.startAnimation(animationFadeOut);
		if (mCircleMenu.getVisibility() == View.VISIBLE)
			mCircleMenu.startAnimation(animationFadeOut);
		toggleCircleMenu = true;
	}

	private void rcIconOnNoAni() {
		mCircleMenuBg.setVisibility(View.INVISIBLE);
		mCircleMenu.setVisibility(View.INVISIBLE);
		toggleCircleMenu = true;
		mRcIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}

	private void rcIconOffNoAni() {
		mCircleMenuBg.setVisibility(View.VISIBLE);
		mCircleMenu.setVisibility(View.VISIBLE);
		toggleCircleMenu = false;
		mRcIcon.setBackgroundResource(R.drawable.main_remocon_icon_off); // 리모컨아이콘 이미지가 바뀌어야 한다.
	}

	/*@Override
	public void onEventQwertySelected() {
		//remoconIconOn();
		
		//mRemoconMenu.setVisibility(View.INVISIBLE);
		//toggleRemoconMenu = true;
		//mRemoconIcon.setBackgroundResource(R.drawable.main_remocon_icon_on); // 리모컨아이콘 이미지가 바뀌어야 한다.
		
	}*/
	

	@Override
	public void onBackPressed() {
		// ---------------------
		// circle menu
		// ---------------------
		if(mCircleMenu.getVisibility() == View.VISIBLE) {
			rcIconOnNoAni();
        	return;
		}
		
		
		
		// ---------------------
		// vod, chtv main
		// ---------------------
		final Vod vodTVch = (Vod) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_VOD);
		if (vodTVch != null) {
			if (vodTVch.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
				mStbPower.startAnimation(animationRcMain);
				mIntegrationUiMain.startAnimation(animationRcMain);
				mVod.startAnimation(animationRcMain);
				mTVChannel.startAnimation(animationRcMain);
				mSearch.startAnimation(animationRcMain);
				super.onBackPressed();
				return;
			}
		}
	    
		// -------------------------------------------
		// back key is checked, whether twice pressed
		// -------------------------------------------
        if (doubleBackKeyPressedToExitApp) {
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

	public void openMirroring() {
		Intent intent = new Intent(this, MirroringActivity.class);
		startActivity(intent);
	}

	
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

}
