/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnm.cnmrc.fragment.rc;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.ExtendedImageButton;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.util.Action;

public class RcTopMenu extends Fragment implements View.OnClickListener {

	ExtendedImageButton mStbPower, mIntegrationUiMain; 	// top menu (셋탑전원, 통합ui메인, vod, TV채널, 검색)
	ImageButton mVod, mTvch, mSearch; 					// top menu (셋탑전원, 통합ui메인, vod, TV채널, 검색)

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.rc_top_menu, container, false);

		// top menu
		mStbPower = (ExtendedImageButton) layout.findViewById(R.id.stb_power);
		mIntegrationUiMain = (ExtendedImageButton) layout.findViewById(R.id.integration_ui_main);
		mVod = (ImageButton) layout.findViewById(R.id.vod);
		mTvch = (ImageButton) layout.findViewById(R.id.tvch);
		mSearch = (ImageButton) layout.findViewById(R.id.search);

		// top menu
		mStbPower.setOnClickListener(this);
		mIntegrationUiMain.setOnClickListener(this);
		mVod.setOnClickListener(this);
		mTvch.setOnClickListener(this);
		mSearch.setOnClickListener(this);

		return layout;
	}

	Activity a;

	@Override
	public void onAttach(Activity activity) {
		a = activity;
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public void backToRc() {
		mStbPower.startAnimation(((MainActivity) getActivity()).aniRcTopMenuFadein);
		mIntegrationUiMain.startAnimation(((MainActivity) getActivity()).aniRcTopMenuFadein);
		mVod.startAnimation(((MainActivity) getActivity()).aniRcTopMenuFadein);
		mTvch.startAnimation(((MainActivity) getActivity()).aniRcTopMenuFadein);
		mSearch.startAnimation(((MainActivity) getActivity()).aniRcTopMenuFadein);

		mStbPower.setVisibility(View.VISIBLE);
		mIntegrationUiMain.setVisibility(View.VISIBLE);
		mVod.setVisibility(View.VISIBLE);
		mTvch.setVisibility(View.VISIBLE);
		mSearch.setVisibility(View.VISIBLE);
	}

	public void goToVodTvch() {
		mStbPower.startAnimation(((MainActivity) getActivity()).aniRcPanelFadeout);
		mIntegrationUiMain.startAnimation(((MainActivity) getActivity()).aniRcPanelFadeout);
		mVod.startAnimation(((MainActivity) getActivity()).aniRcPanelFadeout);
		mTvch.startAnimation(((MainActivity) getActivity()).aniRcPanelFadeout);
		mSearch.startAnimation(((MainActivity) getActivity()).aniRcPanelFadeout);

		mStbPower.setVisibility(View.INVISIBLE);
		mIntegrationUiMain.setVisibility(View.INVISIBLE);
		mVod.setVisibility(View.INVISIBLE);
		mTvch.setVisibility(View.INVISIBLE);
		mSearch.setVisibility(View.INVISIBLE);
	}

	boolean toogle = false;
	AudioManager amanager;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stb_power:
			Action.POWER_STB.execute(((BaseActivity) getActivity()).getCommands()); // POWER_TV(x) POWER(o) POWER_AVR(x) POWER_BD(x) POWER_STB(x)
			break;
		case R.id.integration_ui_main:
			Action.GO_TO_GUIDE.execute(((BaseActivity) getActivity()).getCommands()); // KEYCODE_MOVE_HOME은 안된다. ???
			break;
		case R.id.vod:
			((MainActivity) getActivity()).goToVodTvch("vod");
			break;
		case R.id.tvch:
			((MainActivity) getActivity()).goToVodTvch("tvch");
			break;
		case R.id.search:
			((MainActivity) getActivity()).goToSearch("rc", true);
			break;
		}
	}

}
