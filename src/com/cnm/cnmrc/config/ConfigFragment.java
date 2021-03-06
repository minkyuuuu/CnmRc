package com.cnm.cnmrc.config;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.SlideToggleButton;

@SuppressWarnings("deprecation")
public class ConfigFragment extends Fragment implements View.OnClickListener {

	public static ConfigFragment newInstance(String type) {
		ConfigFragment f = new ConfigFragment();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout mConfigLayout; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	Button mDone;				// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.

	SlideToggleButton mVibrate, mSound, mVodUpdate, mWatchingRes, mAutoAdultCert;
	SeekBar seekbar;
	
	TextView mAdultCertStatus;	// 성인인증상태 문구, 성인인증화면으로 이동함.
	RelativeLayout mAdultWarningMsg;
	View mDefaultBlankLine;
	
	TextView mRegionProduct;	// 지역/상품 문구, 지역/상품화면으로 이동함.
	
	Button mAllClear;			// 초기화

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_fragment, container, false);

		// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
		mConfigLayout = (LinearLayout) layout.findViewById(R.id.config);
		mConfigLayout.setOnClickListener(this);
		
		// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.
		mDone = (Button) layout.findViewById(R.id.config_done);
		mDone.setOnClickListener(this);
		
		// 성인인증상태 문구, 성인인증화면으로 이동함.
		mAdultCertStatus = (TextView) layout.findViewById(R.id.config_adult_cert_status);
		mAdultCertStatus.setOnClickListener(this);
		mAdultWarningMsg = (RelativeLayout) layout.findViewById(R.id.config_adult_warning_msg);
		mDefaultBlankLine = (View) layout.findViewById(R.id.config_default_blank_line);

		// 지역/상품 문구, 지역/상품화면으로 이동함.
		mRegionProduct = (TextView) layout.findViewById(R.id.config_region_product);
		mRegionProduct.setOnClickListener(this);
		
		// 초기화
		mAllClear = (Button) layout.findViewById(R.id.config_all_clear);
		mAllClear.setOnClickListener(this);
		
		// slide toggle button
		mVibrate = (SlideToggleButton) layout.findViewById(R.id.slide_vibrate);
		mVibrate.setOnCheckChangedListner(slideToogleButtonListener);
		mVibrate.setChecked(true);	// drawer is closed!!!, "png is off image"

		mSound = (SlideToggleButton) layout.findViewById(R.id.slide_sound);
		mSound.setOnCheckChangedListner(slideToogleButtonListener);
		mSound.setChecked(true);	// drawer is opened!!!, "png is on image"

		mVodUpdate = (SlideToggleButton) layout.findViewById(R.id.slide_vod_update);
		mVodUpdate.setOnCheckChangedListner(slideToogleButtonListener);
		mVodUpdate.setChecked(true);	// drawer is opened!!!, "png is on image"

		mWatchingRes = (SlideToggleButton) layout.findViewById(R.id.slide_watching_res);
		mWatchingRes.setOnCheckChangedListner(slideToogleButtonListener);
		mWatchingRes.setChecked(true);	// drawer is opened!!!, "png is on image"

		mAutoAdultCert = (SlideToggleButton) layout.findViewById(R.id.slide_auto_adult_cert);
		mAutoAdultCert.setOnCheckChangedListner(slideToogleButtonListener);
		mAutoAdultCert.setChecked(false);	// drawer is closed!!!, "png is off image"
		
		// seekbar
		seekbar = (SeekBar) layout.findViewById(R.id.seekbar);
		seekbar.incrementProgressBy(1);
		seekbar.setProgress(70);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Log.i("hwang", "seek position --->" + progress);
			}
			public void onStartTrackingTouch(SeekBar seekBar) { }
			public void onStopTrackingTouch(SeekBar seekBar) { }
		});
		
		return layout;
	}

	private SlideToggleButton.OnCheckChangedListner slideToogleButtonListener = new SlideToggleButton.OnCheckChangedListner() {
		@Override
		public void onCheckChanged(View v, boolean isChecked) {
			if (v instanceof SlideToggleButton) {
				if (v.getTag().equals("slide_vibrate")) {
					Log.i("hwang", "slide_vibrate");
					Log.i("hwang", "is open --->" + mVibrate.isChecked());	// true means "opened", "png is on image"
				}
				if (v.getTag().equals("slide_sound")) {
					Log.i("hwang", "slide_sound");
				}
				if (v.getTag().equals("slide_vod_update")) {
					Log.i("hwang", "slide_vod_update");
				}
				if (v.getTag().equals("slide_watching_res")) {
					Log.i("hwang", "slide_watching_reservation");
				}
				if (v.getTag().equals("slide_auto_adult_cert")) {
					boolean b = ((SlideToggleButton) v).isOpened();	// on image로 변할 때 fasle값임. 아마 그전의 상태를 의미하는것 같다.
					if(b) {		// off image로 변경될 
						mAdultWarningMsg.setVisibility(View.VISIBLE);
						mDefaultBlankLine.setVisibility(View.VISIBLE);
					} else {	// on image로 변경될 
						mAdultWarningMsg.setVisibility(View.GONE);
						mDefaultBlankLine.setVisibility(View.GONE);
					}
					Log.i("hwang", "slide_auto_adult_certification : " + b);
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
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_CONFIG);
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
		case R.id.config: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
			break;
		case R.id.config_done:
			getActivity().onBackPressed();
			break;
		case R.id.config_adult_cert_status:
			goToAdultCert();
			break;
		case R.id.config_region_product:
			showRegion();
			break;
		case R.id.config_all_clear:
			clearAll();
			break;
		}

	}

	private void goToAdultCert() {
		Log.i("hwang", "entering adult certification !!!");
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_CONFIG);
		if (f != null) {
			ConfigAdultCert adultCert;
			if(isFromVodTvch()) adultCert = ConfigAdultCert.newInstance("from_vod");		// type means "whence do you come?" or "Where do you come from?"
			else adultCert = ConfigAdultCert.newInstance("from_config");
			
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			
			//  ft.replace전에 animation을 설정해야 한다.
			ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
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

	private void showRegion() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_CONFIG);
		if (f != null) {
			ConfigRegion configRegion = ConfigRegion.newInstance("강남");
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			
			//  ft.replace전에 animation을 설정해야 한다.
			ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
			ft.add(R.id.config_panel, configRegion, "config_region");	// config_panel은 main에 있는것으로 현재 설정화면의 fragment에서 사용한 뷰 그룹이다.
			ft.addToBackStack(null);									// backstack이란 본인 fragment가 소속된 activity stack을 의미한다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}
		
	}

	private void clearAll() {
		mVibrate.setChecked(true);			// drawer is closed!!!, "png is off image"
		mSound.setChecked(true);			// drawer is opened!!!, "png is on image"
		mVodUpdate.setChecked(true);		// drawer is opened!!!, "png is on image"
		mWatchingRes.setChecked(true);		// drawer is opened!!!, "png is on image"
		mAutoAdultCert.setChecked(false);	// drawer is closed!!!, "png is off image"
		
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
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}
