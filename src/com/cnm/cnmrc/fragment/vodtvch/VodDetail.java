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

package com.cnm.cnmrc.fragment.vodtvch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnm.cnmrc.R;

public class VodDetail extends Base implements View.OnClickListener{

	View layout;
	
	public VodDetail newInstance(String type, boolean isFirstDepth) {
		VodDetail f = new VodDetail();
		Bundle args = new Bundle();
		args.putString("type", type);
		args.putBoolean("isFirstDepth", isFirstDepth);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.vod_detail, container, false);
		
//		TextView text = (TextView) layout.findViewById(R.id.text);
//		String type = getArguments().getString("type");
//		text.setText("상세보기");
		
		isFirstDepth = getArguments().getBoolean("isFirstDepth");

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
		}
	}
	
}
