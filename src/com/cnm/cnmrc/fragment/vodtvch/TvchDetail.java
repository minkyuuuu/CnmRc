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
import com.cnm.cnmrc.data.ItemTvchDetail;

public class TvchDetail extends Base implements View.OnClickListener{

	View layout;
	
	ListView 	mListView;
	ArrayList<ItemTvchDetail> arrayList = null;
	
	public TvchDetail newInstance(String type, boolean isFirstDepth) {
		TvchDetail f = new TvchDetail();
		Bundle args = new Bundle();
		args.putString("type", type);
		args.putBoolean("isFirstDepth", isFirstDepth);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.tvch_detail, container, false);
		
		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		
		// listview
		mListView   = (ListView) layout.findViewById(R.id.tvch_detail_list);
		
		// make item data
		arrayList = new ArrayList<ItemTvchDetail>(20);
		for (int i = 0; i < 20; i++) {
			ItemTvchDetail item = new ItemTvchDetail();
			
			item.setCurrent_time("13:20");
			item.setCurrent_title("스마일");
			
			item.setHdIconResId(0);
			if(i==3) item.setHdIconResId(R.drawable.hdicon);
			if(i==7) item.setHdIconResId(R.drawable.hdicon);
			if(i==10) item.setHdIconResId(R.drawable.hdicon);
			if(i==12) item.setHdIconResId(R.drawable.hdicon);
			
			arrayList.add(item);
		}
		
		// -------------------
		// tvch semidetail
		// -------------------
        TvchDetailAdapter adapter = new TvchDetailAdapter(getActivity(), R.layout.list_item_tvch_detail, arrayList);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
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
		}
	}
	
}
