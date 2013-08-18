package com.cnm.cnmrc.config;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.SlideToogleButton;

@SuppressWarnings("deprecation")
public class ConfigFragment extends Fragment implements View.OnClickListener {

	public static ConfigFragment newInstance(String type) {
		ConfigFragment f = new ConfigFragment();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout mConfig; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	Button mConfigComplete;

	SlideToogleButton mVibrate, mSound, mVodUpdate, mWatchingRes, mAutoAdultCert;
	SeekBar seekbar;
	
	RelativeLayout mConfigAdultWarning;
	View mConfigDefaultBlank;;
	
	Button mConfigAllClear;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.fragment_config, container, false);

		mConfig = (LinearLayout) layout.findViewById(R.id.config);
		mConfig.setOnClickListener(this);

		mConfigComplete = (Button) layout.findViewById(R.id.config_complete);
		mConfigComplete.setOnClickListener(this);
		
		mConfigAdultWarning = (RelativeLayout) layout.findViewById(R.id.config_adult_warning);
		mConfigDefaultBlank = (View) layout.findViewById(R.id.config_default_blank_line);

		mConfigAllClear = (Button) layout.findViewById(R.id.config_all_clear);
		mConfigAllClear.setOnClickListener(this);
		
		
		
		mVibrate = (SlideToogleButton) layout.findViewById(R.id.slide_vibrate);
		mVibrate.setOnCheckChangedListner(slideToogleButtonListener);
		mVibrate.setChecked(true);	// drawer is closed!!!, "png is off image"

		mSound = (SlideToogleButton) layout.findViewById(R.id.slide_sound);
		mSound.setOnCheckChangedListner(slideToogleButtonListener);
		mSound.setChecked(true);	// drawer is opened!!!, "png is on image"

		mVodUpdate = (SlideToogleButton) layout.findViewById(R.id.slide_vod_update);
		mVodUpdate.setOnCheckChangedListner(slideToogleButtonListener);
		mVodUpdate.setChecked(true);	// drawer is opened!!!, "png is on image"

		mWatchingRes = (SlideToogleButton) layout.findViewById(R.id.slide_watching_res);
		mWatchingRes.setOnCheckChangedListner(slideToogleButtonListener);
		mWatchingRes.setChecked(true);	// drawer is opened!!!, "png is on image"

		mAutoAdultCert = (SlideToogleButton) layout.findViewById(R.id.slide_auto_adult_cert);
		mAutoAdultCert.setOnCheckChangedListner(slideToogleButtonListener);
		mAutoAdultCert.setChecked(false);	// drawer is closed!!!, "png is off image"
		
		seekbar = (SeekBar) layout.findViewById(R.id.seekbar);
		seekbar.incrementProgressBy(1);
		seekbar.setProgress(70);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Log.i("hwang", "seek position --->" + progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		return layout;
	}

	private SlideToogleButton.OnCheckChangedListner slideToogleButtonListener = new SlideToogleButton.OnCheckChangedListner() {
		@Override
		public void onCheckChanged(View v, boolean isChecked) {
			if (v instanceof SlideToogleButton) {
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
					boolean b = ((SlideToogleButton) v).isOpened();	// on image로 변할 때 fasle값임. 아마 그전의 상태를 의미하는것 같다.
					if(b) {		// off image로 변경될 
						mConfigAdultWarning.setVisibility(View.VISIBLE);
						mConfigDefaultBlank.setVisibility(View.VISIBLE);
					} else {	// on image로 변경될 
						mConfigAdultWarning.setVisibility(View.GONE);
						mConfigDefaultBlank.setVisibility(View.GONE);
					}
					Log.i("hwang", "slide_auto_adult_certification : " + b);
				}
			}
		}
	};

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_CONFIG);
			if (f != null) {
				// getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			}
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
		case R.id.config_complete:
			getActivity().onBackPressed();
			break;
		case R.id.config_all_clear:
			clearAll();
			break;
		}

	}

	private void clearAll() {
		mVibrate.setChecked(true);	// drawer is closed!!!, "png is off image"
		mSound.setChecked(true);	// drawer is opened!!!, "png is on image"
		mVodUpdate.setChecked(true);	// drawer is opened!!!, "png is on image"
		mWatchingRes.setChecked(true);	// drawer is opened!!!, "png is on image"
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
