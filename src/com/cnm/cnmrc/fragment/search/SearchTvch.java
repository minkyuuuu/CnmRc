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

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.data.ItemTvchSemiDetail;
import com.cnm.cnmrc.fragment.vodtvch.TvchSemiDetailAdapter;

public class SearchTvch extends Fragment {

	View layout;
	
	ListView 	mListView;
	ArrayList<ItemTvchSemiDetail> arrayList = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.tvch_semidetail, container, false);
		
		// listview
		mListView   = (ListView) layout.findViewById(R.id.tvch_semidetail);
		
		// make item data
		arrayList = new ArrayList<ItemTvchSemiDetail>(20);
		for (int i = 0; i < 20; i++) {
			ItemTvchSemiDetail item = new ItemTvchSemiDetail();
			
			item.setChannel_no(i+1);
			item.setTvchIcon(R.drawable.sbs_icon);
			
			item.setCurrent_time("13:20");
			item.setCurrent_title("스마일");
			
			item.setNext_time("13:40");
			item.setNext_title("(자막)우리동네이모저모");
			
			arrayList.add(item);
		}
		
		// -------------------
		// tvch semidetail
		// -------------------
        TvchSemiDetailAdapter adapter = new TvchSemiDetailAdapter(getActivity(), R.layout.list_item_tvch_semidetail, arrayList);
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
	
	
}
