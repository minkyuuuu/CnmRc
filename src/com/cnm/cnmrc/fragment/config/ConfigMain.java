package com.cnm.cnmrc.fragment.config;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.SlideToggleButton;
import com.cnm.cnmrc.popup.PopupConfigAllReset;
import com.cnm.cnmrc.popup.PopupConfigGtvConnection;
import com.cnm.cnmrc.popup.PopupGtvNotAlive;
import com.urbanairship.push.PushManager;

@SuppressWarnings("deprecation")
public class ConfigMain extends Fragment implements View.OnClickListener {

	public static ConfigMain newInstance(String type) {
		ConfigMain f = new ConfigMain();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}
	
	CnmPreferences pref;
	
	LinearLayout preventClickDispatching; 	// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	Button mDone;							// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.

	SlideToggleButton mVibrate, mSound, mVodUpdate, mTvWatchRes, mAutoAdultCert;
	SeekBar seekbar;
	
	TextView mAdultCertStatus;		// 성인인증상태 문구, 성인인증화면으로 이동함.
	RelativeLayout mAdultWarningMsg;
	View mDefaultBlankLine;
	
	TextView mAreaProduct;			// 지역/상품 문구, 지역/상품화면으로 이동함.
	
	Button mAllClear;				// 초기화
	Button mGtv;					// Gtv 설정

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_fragment, container, false);
		
		pref = CnmPreferences.getInstance();

		// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.config_prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.
		mDone = (Button) layout.findViewById(R.id.config_done);
		mDone.setOnClickListener(this);
		
		// 성인인증상태 문구, 성인인증화면으로 이동함.
		mAdultCertStatus = (TextView) layout.findViewById(R.id.config_adult_cert_status);
		mAdultCertStatus.setOnClickListener(this);
		mAdultWarningMsg = (RelativeLayout) layout.findViewById(R.id.config_adult_warning_msg);
		mDefaultBlankLine = (View) layout.findViewById(R.id.config_default_blank_line);
		
		// 지역/상품 문구, 지역/상품화면으로 이동함.
		mAreaProduct = (TextView) layout.findViewById(R.id.config_area_product);
		refreshAreaProduct();
		mAreaProduct.setOnClickListener(this);
		
		// 초기화
		mAllClear = (Button) layout.findViewById(R.id.config_all_clear);
		mAllClear.setOnClickListener(this);
		
		// Gtv 설정
		mGtv = (Button) layout.findViewById(R.id.config_gtv);
		mGtv.setOnClickListener(this);
		
		// slide toggle button
		mVibrate = (SlideToggleButton) layout.findViewById(R.id.slide_vibrate);
		mVibrate.setOnCheckChangedListner(slideToogleButtonListener);
		mVibrate.setChecked(pref.loadConfigVibrateEffect(getActivity()));	

		mSound = (SlideToggleButton) layout.findViewById(R.id.slide_sound);
		mSound.setOnCheckChangedListner(slideToogleButtonListener);
		mSound.setChecked(pref.loadConfigSoundEffect(getActivity()));	 

		mVodUpdate = (SlideToggleButton) layout.findViewById(R.id.slide_vod_update);
		mVodUpdate.setOnCheckChangedListner(slideToogleButtonListener);
		mVodUpdate.setChecked(pref.loadConfigVodUpdateNoti(getActivity()));	 
		
		// push service
		// 여기서는 설정할 필요가 없다. 앱 시작시 설정해야한다.
//		if(pref.loadConfigVodUpdateNoti(getActivity())) PushManager.enablePush();
//		else PushManager.disablePush();
		
		mTvWatchRes = (SlideToggleButton) layout.findViewById(R.id.slide_watching_res);
		mTvWatchRes.setOnCheckChangedListner(slideToogleButtonListener);
		mTvWatchRes.setChecked(pref.loadConfigTvWatchResNoti(getActivity()));	 

		mAutoAdultCert = (SlideToggleButton) layout.findViewById(R.id.slide_auto_adult_cert);
		mAutoAdultCert.setOnCheckChangedListner(slideToogleButtonListener);
		
		onUpdateCertStatus();

		
		// seekbar
		// 2013-12-06 comment
//		seekbar = (SeekBar) layout.findViewById(R.id.seekbar);
//		seekbar.incrementProgressBy(1);
//		seekbar.setProgress(70);
//		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//				Log.i("hwang", "seek position --->" + progress);
//			}
//			public void onStartTrackingTouch(SeekBar seekBar) { }
//			public void onStopTrackingTouch(SeekBar seekBar) { }
//		});
		
		return layout;
	}
	
	public void onUpdateCertStatus() {
		// 성인인증상태 보이기입니다, 인증상태가 on이고 자동인증이 on일때만 "인증상태입니다.", 
		// "사실은 성인인증입니다." 즉 성인인증상태란 자동인증이 on이된 상태에서 인증절차에 성공한 상태를 의미한다.
		if(pref.loadConfigAdultCertStatus(getActivity()) && 
				pref.loadConfigAutoAdultCert(getActivity())) {
			mAdultCertStatus.setText(getActivity().getResources().getString(R.string.config_adult_cert_on));
			pref.saveConfigAdultCertStatus(getActivity(), true);
		} else {
			mAdultCertStatus.setText(getActivity().getResources().getString(R.string.config_adult_cert_off));
			pref.saveConfigAdultCertStatus(getActivity(), false);
		}
		
		// 자동성인인증
		mAutoAdultCert.setChecked(pref.loadConfigAutoAdultCert(getActivity()));	 
		
		if(pref.loadConfigAutoAdultCert(getActivity())) {
			mAdultWarningMsg.setVisibility(View.GONE); 
			mDefaultBlankLine.setVisibility(View.GONE);
		} else {
			mAdultWarningMsg.setVisibility(View.VISIBLE);
			mDefaultBlankLine.setVisibility(View.VISIBLE);
		}
		
		super.onResume();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	private SlideToggleButton.OnCheckChangedListner slideToogleButtonListener = new SlideToggleButton.OnCheckChangedListner() {
		@Override
		public void onCheckChanged(View v, boolean isChecked) {
			if (v instanceof SlideToggleButton) {
				if (v.getTag().equals("slide_vibrate")) {
					if(isChecked) pref.saveConfigVibrateEffect(getActivity(), true);
					else pref.saveConfigVibrateEffect(getActivity(), false);
				}
				if (v.getTag().equals("slide_sound")) {
					if(isChecked) pref.saveConfigSoundEffect(getActivity(), true);
					else pref.saveConfigSoundEffect(getActivity(), false);
				}
				if (v.getTag().equals("slide_vod_update")) {
					if(isChecked) {
						PushManager.enablePush();
						pref.saveConfigVodUpdateNoti(getActivity(), true);
					}
					else {
						PushManager.disablePush();
						pref.saveConfigVodUpdateNoti(getActivity(), false);
					}
				}
				if (v.getTag().equals("slide_watching_res")) {
					if(isChecked) pref.saveConfigTvWatchResNoti(getActivity(), true);  
					else pref.saveConfigTvWatchResNoti(getActivity(), false);
				}
				if (v.getTag().equals("slide_auto_adult_cert")) {
					boolean b = ((SlideToggleButton) v).isOpened();	// on image로 변할 때 fasle값임. 아마 그전의 상태를 의미하는것 같다. 이것도 가능하다...
					if(isChecked) {	
						// 자동성인인증이 on일때는 문구는 don't care, 그러나 성인인증화면에 적용해야한다.
						pref.saveConfigAutoAdultCert(getActivity(), true);
						mAdultWarningMsg.setVisibility(View.GONE);
						mDefaultBlankLine.setVisibility(View.GONE);
					} else {	
						// 자동성인인증이 off가 되면 무조건 성인인증문구는 "미인중상태입니다"로 변경되어야한다. 이 의미는 컨텐츠를 볼 때 무조건 성인인증 절차가 필요하다는 의미.
						mAdultCertStatus.setText(getActivity().getResources().getString(R.string.config_adult_cert_off));
						pref.saveConfigAdultCertStatus(getActivity(), false);
						pref.saveConfigAutoAdultCert(getActivity(), false);
						mAdultWarningMsg.setVisibility(View.VISIBLE);
						mDefaultBlankLine.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		// 아래의 처리는 안해주어도 자연스레 시스템에서 remove한다.
		// back key 기준으로 정리하면
		// 1) 먼저 화면에 보이는 현재화면에 해당하는 fragment의 onDestroyView() 수행되고, 여기서 이 곳의 fragmen는 잠시 대기하고 있다가...
		// 2) backstack에 있는 fragment가 화면에 보이게되는데, 먼저 onCreateView()가 콜백된다. onCreate() 콜백은 수행되지 않는다.
		// 3) onCreateView() --> onActivityCreated() --> onStart() --> onResume() 순으로 화면에 복귀된다.
		// 4) 잠시 대기중인 사라지는 이전 fragment의 나머지 콜백이 진행된다. onDestroy() ---> onDetach() ---> "객체삭제"
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_CONFIG_MAIN);
		if (f != null) {
			// getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}
		
	}

	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config_done:
			getActivity().onBackPressed();
			break;
		case R.id.config_adult_cert_status:
			goToAdultCert();
			break;
		case R.id.config_area_product:
			showArea();
			break;
		case R.id.config_all_clear:
			{
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				PopupConfigAllReset popup = new PopupConfigAllReset();
				popup.show(ft, PopupGtvNotAlive.class.getSimpleName());
	
			}
			break;
		case R.id.config_gtv:
			{
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				PopupConfigGtvConnection popup = new PopupConfigGtvConnection();
				popup.show(ft, PopupConfigGtvConnection.class.getSimpleName());
			}
			break;
		}

	}

	private void goToAdultCert() {
		Log.i("hwang", "entering adult certification !!!");
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_CONFIG_MAIN);
		if (f != null) {
			ConfigAdultCert adultCert;
			//의미가 없다.
//			if(isFromVodTvch()) adultCert = ConfigAdultCert.newInstance("from_vod_semi");		// type means "whence do you come?" or "Where do you come from?"
//			else adultCert = ConfigAdultCert.newInstance("from_config");
			adultCert = ConfigAdultCert.newInstance("from_config_main");
			
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			
			//  ft.replace전에 animation을 설정해야 한다.
			ft.setCustomAnimations(R.anim.fragment_entering, 0);
			ft.add(R.id.config_panel, adultCert, "adult_cert");	// config_panel은 main에 있는것으로 현재 설정화면의 fragment에서 사용한 뷰 그룹이다.
			ft.addToBackStack(null);							// backstack이란 본인 fragment가 소속된 activity stack을 의미한다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}
		
	}

	private boolean isFromVodTvch() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_VOD_TVCH);
		if (f != null) return true;	// vod_tvch exists, therefore return false, means "from_vod"
		else return false;
	}

	private void showArea() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_CONFIG_MAIN);
		if (f != null) {
			ConfigArea configArea = ConfigArea.newInstance("강남");
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			
			//  ft.replace전에 animation을 설정해야 한다.
			ft.setCustomAnimations(R.anim.fragment_entering, 0, 0, R.anim.fragment_exit);
			ft.add(R.id.config_panel, configArea, "config_area");	// config_panel은 main에 있는것으로 현재 설정화면의 fragment에서 사용한 뷰 그룹이다.
			ft.addToBackStack(null);									// backstack이란 본인 fragment가 소속된 activity stack을 의미한다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}
		
	}

	public void clearAll() {
		mVibrate.setChecked(false);			
		mSound.setChecked(false);			
		mVodUpdate.setChecked(false);		
		mTvWatchRes.setChecked(false);		
		mAutoAdultCert.setChecked(false);	
		
		pref.saveConfigVibrateEffect(getActivity(), false);
		pref.saveConfigSoundEffect(getActivity(), false);
		pref.saveConfigVodUpdateNoti(getActivity(), false);
		pref.saveConfigTvWatchResNoti(getActivity(), false);
		pref.saveConfigAutoAdultCert(getActivity(), false);
	}
	
	public void refreshAreaProduct() {
		String str = pref.loadChannelAreaName(getActivity()) + "/" + pref.loadChannelProductName(getActivity());
		mAreaProduct.setText(str);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}
