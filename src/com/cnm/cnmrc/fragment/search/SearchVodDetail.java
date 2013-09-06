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

package com.cnm.cnmrc.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;

public class SearchVodDetail extends Fragment implements View.OnClickListener{

	View layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.vod_detail, container, false);
		
		return layout;
	}
	
	public boolean allowBackPressed() {
		return true;
	}
	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // ----------------------------------------------------------------------------------------------
        // vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
        // 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
        // ----------------------------------------------------------------------------------------------
        {
	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("search_vod_detail");
	        if (f != null) {
	        	getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
	        }
        }
        
        {
        	Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_SEARCH);
        	if (f != null) {
        		((SearchMain)f).resetTitle();
        	}
        }
        
        
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
