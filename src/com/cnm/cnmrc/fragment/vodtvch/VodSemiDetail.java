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
import com.cnm.cnmrc.data.ItemVodSemiDetail;

public class VodSemiDetail extends Base implements View.OnClickListener{

	View layout;
	
	ListView 	mListView;
	ArrayList<ItemVodSemiDetail> arrayList = null;
	
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
		arrayList = new ArrayList<ItemVodSemiDetail>(10);
		for (int i = 0; i < 10; i++) {
			ItemVodSemiDetail item = new ItemVodSemiDetail();
			item.setTitleResId(R.drawable.mister_go);
			
			item.setTitle("미스터고");
			item.setHdIconResId(R.drawable.hdicon);
			item.setAgeResId(R.drawable.age12);
			
			item.setDirector("감독 : ");
			item.setDirector_name("김용화");
			item.setCast("출연 : ");
			item.setCast_name("성동일, 서교");
			
			if(i==2) {
				item.setHdIconResId(0);
				item.setAgeResId(R.drawable.age15);
			}
			
			if(i==1) item.setAgeResId(R.drawable.age19);
			if(i==3) item.setAgeResId(R.drawable.age19);
			if(i==4) item.setAgeResId(R.drawable.ageall);
			if(i==5) item.setHdIconResId(0);
			if(i==6) item.setAgeResId(R.drawable.age19);
			if(i==8) item.setAgeResId(R.drawable.age15);
			if(i==8) item.setAgeResId(R.drawable.age19);
			
			arrayList.add(item);
		}
		
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
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.text:
			//loadingData(2, "상세보기", false);	// false : 1 depth가 아님.
			break;
		}
	}

//	private void loadingData(int selectedCategory, String title, boolean isSidebar) {
//		// TODO Auto-generated method stub
//		
//	}
	
	
}
