package com.cnm.cnmrc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SearchFragment extends Fragment implements View.OnClickListener {

	public static SearchFragment newInstance(String type) {
		SearchFragment f = new SearchFragment();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}
	
	RadioGroup radioGroup;
	RadioButton vod, tvch, naver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.fragment_search, container, false);
		
		vod = (RadioButton) layout.findViewById(R.id.search_vod);
		vod.setOnClickListener(this);
		
		tvch = (RadioButton) layout.findViewById(R.id.search_tvch);
		tvch.setOnClickListener(this);
		
		naver = (RadioButton) layout.findViewById(R.id.search_naver);
		naver.setOnClickListener(this);
		
		// 처음에는 vod 버튼이 활성화되어 있음
		vod.setSelected(true);
		
//		radioGroup = (RadioGroup) layout.findViewById(R.id.search_radio_group);
//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				changeViewList(checkedId);
//			}
//		});

		return layout;
	}
	
	private void changeViewList(int checkedId) {

		/**
		 * change state of RadioButton
		 */
		changeRadioButtonState(checkedId);
//		
//		/**
//		 * show buttons according to card or cash 
//		 * card : 카드관리 and 할부조회
//		 * cash : 통장관리
//		 */
//		if (checkedId == R.id.button_segfirst) {	// 카드 지출
//			mCard.setVisibility(View.VISIBLE);
//			mCash.setVisibility(View.GONE);
//		} else { 									// 현금지출
//			mCard.setVisibility(View.GONE);
//			mCash.setVisibility(View.VISIBLE);
//		}
	}
	
	private void changeRadioButtonState(int checkedId) {
		clearSelectedAll();
		
		switch (checkedId) {
		case R.id.search_vod:
			vod.setSelected(true);
			break;
		case R.id.search_tvch:
			tvch.setSelected(true);
			break;
		case R.id.search_naver:
			naver.setSelected(true);
			break;
		}
	}

	public void onDestroyView() {
		super.onDestroyView();
		
        {
        	Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_SEARCH);
	        if (f != null) {
	        	// commit을 해도되구... 안해도되구...
	        	//getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
	        }
        }
	}

	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onClick(View v) {
		clearSelectedAll();
		
		switch (v.getId()) {
		case R.id.search_vod:
			v.setSelected(true);
			break;
		case R.id.search_tvch:
			v.setSelected(true);
			break;
		case R.id.search_naver:
			v.setSelected(true);
			break;
		}

	}

	private void clearSelectedAll() {
		vod.setSelected(false);
		tvch.setSelected(false);
		naver.setSelected(false);
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
