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

public class ConfigAdultCert extends Fragment implements View.OnClickListener {

	public static ConfigAdultCert newInstance(String type) {
		ConfigAdultCert f = new ConfigAdultCert();
		Bundle args = new Bundle();
		args.putString("type", type); 	// vod or config (from)
		f.setArguments(args);
		return f;
	}

	LinearLayout mAdultCertLayout; 		// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	LinearLayout mNotShowFromVod; 		// vod화면에서 진입시 안보이게 할려는 구문.

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_adult_cert, container, false);

		mAdultCertLayout = (LinearLayout) layout.findViewById(R.id.adult_cert);
		mAdultCertLayout.setOnClickListener(this);
		
		mNotShowFromVod = (LinearLayout) layout.findViewById(R.id.adult_cert_not_show_from_vod);
		
		String type = getArguments().getString("type");
		if(type.equals("from_vod")) mNotShowFromVod.setVisibility(View.GONE);
		else mNotShowFromVod.setVisibility(View.VISIBLE);

		return layout;
	}


	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
			break;
		case R.id.adult_cert_done:
			getActivity().onBackPressed();
			break;
		}

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
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}
