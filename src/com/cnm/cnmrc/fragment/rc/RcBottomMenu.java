
package com.cnm.cnmrc.fragment.rc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.util.Action;

public class RcBottomMenu extends Fragment implements View.OnClickListener {
	
	View layout;
	ImageButton mPrevious, mFavoriteChannel, mViewSwitch, mExit;			// bottom menu (이전, 선호채널, 보기전환, 나가기)
	LinearLayout mRemoconMode, mVodTvchMode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_bottom_menu, container, false);
		
		mPrevious = (ImageButton) layout.findViewById(R.id.previous);
		mFavoriteChannel = (ImageButton) layout.findViewById(R.id.favorite_channel);
		mViewSwitch = (ImageButton) layout.findViewById(R.id.view_switch);
		mExit = (ImageButton) layout.findViewById(R.id.exit);
		
		mRemoconMode = (LinearLayout) layout.findViewById(R.id.bottom_remocon_mode);
		mVodTvchMode = (LinearLayout) layout.findViewById(R.id.bottom_vod_tvch_mode);
		
		mPrevious.setOnClickListener(this);
		mFavoriteChannel.setOnClickListener(this);	// 선호채널
		mViewSwitch.setOnClickListener(this);		// 보기전환
		mExit.setOnClickListener(this);
		
		return layout;
	}
	
	public void setRemoconMode() {
		mRemoconMode.setVisibility(View.VISIBLE);
		mVodTvchMode.setVisibility(View.GONE);
	}
	
	public void setVodTvchMode() {
		//setDepthLevelClear();
		
		mRemoconMode.setVisibility(View.GONE);
		mVodTvchMode.setVisibility(View.VISIBLE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.previous:			// 뒤로가기, 이전, 취소
			Action.BACK.execute(((BaseActivity)getActivity()).getCommands());	
			Log.i("hwang", "previous");
			break;
		case R.id.favorite_channel:	// 선호채널 (GREEN)
			Action.COLOR_GREEN.execute(((BaseActivity)getActivity()).getCommands());
			Log.i("hwang", "favorite_channel");
			break;
		case R.id.view_switch:		// 보기전환 (YELLOW)
			Action.COLOR_YELLOW.execute(((BaseActivity)getActivity()).getCommands());
			Log.i("hwang", "view_switch");
			break;
		case R.id.exit:				// 나가기, 이전, 취소
			Action.ESCAPE.execute(((BaseActivity)getActivity()).getCommands());	// 나가기
			Log.i("hwang", "exit");
			break;
		}
	}

	int [] indicator = { R.id.indicator_depth_1, R.id.indicator_depth_2, R.id.indicator_depth_3,
						 R.id.indicator_depth_4, R.id.indicator_depth_5, R.id.indicator_depth_6,
						 R.id.indicator_depth_7, R.id.indicator_depth_8 };
	
	public void addDepthLevel(int backStackEntryCount) {
		if(backStackEntryCount >= indicator.length) return;
		
		ImageView imageView = (ImageView) layout.findViewById(indicator[backStackEntryCount]);
		imageView.setVisibility(View.VISIBLE);
		
		// setDepthLevelClear();
		
		/*for (int i = 0; i < backStackEntryCount+1; i++) {
			ImageView imageView = (ImageView) layout.findViewById(indicator[i]);
			imageView.setVisibility(View.VISIBLE);
		}*/
	}
	
	public void deleteDepthLevel(int backStackEntryCount) {
		if(backStackEntryCount < 1) return;
		
		ImageView imageView = (ImageView) layout.findViewById(indicator[backStackEntryCount]);
		imageView.setVisibility(View.INVISIBLE);
	}
	
	public void setDepthLevelClear() {
		// R.id.indicator_depth_1 is always shown!!!
		// show always 1 depth
		LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.indicator_depth);
		int count = linearLayout.getChildCount();
		for (int i = 1; i < count; i++) {
			ImageView imageView = (ImageView) layout.findViewById(indicator[i]);
			imageView.setVisibility(View.INVISIBLE);
		}
		
	}

}
