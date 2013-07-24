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

package com.cnm.cnmrc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cnm.cnmrc.R;

public class RcNumericPad extends Fragment implements View.OnClickListener {

	View layout;

	ImageButton m1, m2, m3, m4, m5, m6, m7, m8, m9, m0, mBack, mOk; // numeric
																	// menu

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_numeric_pad, container, false);

		// remocon icon
		m1 = (ImageButton) layout.findViewById(R.id.numeric_n1);
		m2 = (ImageButton) layout.findViewById(R.id.numeric_n2);
		m3 = (ImageButton) layout.findViewById(R.id.numeric_n3);
		m4 = (ImageButton) layout.findViewById(R.id.numeric_n4);
		m5 = (ImageButton) layout.findViewById(R.id.numeric_n5);
		m6 = (ImageButton) layout.findViewById(R.id.numeric_n6);
		m7 = (ImageButton) layout.findViewById(R.id.numeric_n7);
		m8 = (ImageButton) layout.findViewById(R.id.numeric_n8);
		m9 = (ImageButton) layout.findViewById(R.id.numeric_n9);
		m0 = (ImageButton) layout.findViewById(R.id.numeric_n0);

		mBack = (ImageButton) layout.findViewById(R.id.numeric_nback); // 지우기
		mOk = (ImageButton) layout.findViewById(R.id.numeric_nok); // 확인

		m1.setOnClickListener(this);
		m2.setOnClickListener(this);
		m3.setOnClickListener(this);
		m4.setOnClickListener(this);
		m5.setOnClickListener(this);
		m6.setOnClickListener(this);
		m7.setOnClickListener(this);
		m8.setOnClickListener(this);
		m9.setOnClickListener(this);
		m0.setOnClickListener(this);

		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);

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

		switch (v.getId()) {
		case R.id.numeric_n1:
			Toast.makeText(getActivity(), v.getTag().toString().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n2:
			Toast.makeText(getActivity(), v.getTag().toString().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n3:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n4:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n5:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n6:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n7:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n8:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n9:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_n0:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.numeric_nback:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.numeric_nok:
			Toast.makeText(getActivity(), v.getTag().toString(), Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
