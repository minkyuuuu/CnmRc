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

public class TvchList extends Base {

	View layout;

	ListView 	mListView;
	String[] mArray = null;
	ArrayList<String> arrayList = null;

	public TvchList newInstance(String type, boolean isFirstDepth) {
		TvchList f = new TvchList();
		Bundle args = new Bundle();
		args.putString("type", type);
		args.putBoolean("isFirstDepth", isFirstDepth);	
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.tvch_list, container, false);

		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		
		
		// listview
		mListView   = (ListView) layout.findViewById(R.id.tvch_list_listview);
		
		// 장르별 채널 정보 
		mArray= getActivity().getResources().getStringArray(R.array.tvch_genre);
		
        arrayList = new ArrayList<String>(mArray.length);
        for(String item: mArray) {
        	arrayList.add(item);
        }
        
		// -------------------
		// vod list type
		// -------------------
        VodListAdapter adapter = new VodListAdapter(getActivity(), R.layout.list_item_vod_list, arrayList);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	increaseCurrentDepth();
        		
            	loadingData(4, arrayList.get(position), false); // 4 : TvchSemiDetail, 		false : 1 depth가 아님.
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
