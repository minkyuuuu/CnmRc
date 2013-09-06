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

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.parser.SearchVod;

public class VodSemiDetail extends Base {

	View layout;
	
	ListView 	mListView;
	ArrayList<SearchVod> arrayList = null;
	
	public VodSemiDetail newInstance(String type, boolean isFirstDepth) {
		VodSemiDetail f = new VodSemiDetail();
		Bundle args = new Bundle();
		args.putString("type", type);
		args.putBoolean("isFirstDepth", isFirstDepth);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.vod_semidetail, container, false);
		
		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		
		// listview
		mListView   = (ListView) layout.findViewById(R.id.vod_semidetail);
		
		// make item data
		arrayList = new ArrayList<SearchVod>(10);
		
		// -------------------
		// vod semidetail
		// -------------------
        VodSemiDetailAdapter adapter = new VodSemiDetailAdapter(getActivity(), R.layout.list_item_vod_semidetail, arrayList);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	increaseCurrentDepth();
            	
            	loadingData(2, "상세보기", false); // 0 : VodList, false : 1 depth가 아님.
            }

        });

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
	

	
	
}
