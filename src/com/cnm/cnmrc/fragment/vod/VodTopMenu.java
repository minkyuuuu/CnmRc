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

package com.cnm.cnmrc.fragment.vod;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;

public class VodTopMenu extends Fragment implements View.OnClickListener{

	TextView mTitle;
	
	ImageButton mVodTopCategory;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//View layout = inflater.inflate(R.layout.fragment_main_top_menu, null);
		View layout = inflater.inflate(R.layout.vod_top_menu, container, false);
		
		mTitle = (TextView) layout.findViewById(R.id.vod_top_title);
		mTitle.setText(getString(R.string.vod_main_title));
		
		mVodTopCategory = (ImageButton) layout.findViewById(R.id.vod_top_category);
		mVodTopCategory.setOnClickListener(this);

		return layout;
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
		
		switch(v.getId()){
		case R.id.vod_top_category:
			Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.TAG_FRAGMENT_VOD);
			if (fragment != null) {
				((Vod)fragment).mLayout.toggleSidebar();
				Log.i("hwang", "sliding menu");
			}
			//((MainActivity) getActivity()).mLayout.toggleSidebar();
			break;
		}
	}
	
}
