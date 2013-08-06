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

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.slidingmenu.SlidingMenu;

public class Vod extends Fragment implements SlidingMenu.Listener {
	
	public ListView      mList;
	protected SlidingMenu 	mLayout;
	protected String[] mStrings = {"오늘의 추천", "영화VOD", "인기케이블&미드", "TV다시보기VOD", "애니메애션&키즈", "교육&EBS", "마이TV"};
	
	public static Vod newInstance(long num) {
		Vod f = new Vod();
		Bundle args = new Bundle();
		args.putLong("num", num);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.vod, container, false);

        mLayout = (SlidingMenu) layout.findViewById(R.id.animation_layout);
        mLayout.setListener(this);
		
        mList   = (ListView) layout.findViewById(R.id.sidebar_list);
        
        ArrayList<String> arrayList = new ArrayList<String>(mStrings.length);
        for(String file: mStrings) {
        	arrayList.add(file);
        }
        
        VodMainAdapter adapter = new VodMainAdapter(getActivity(), R.layout.list_item_vod_main_big_list, arrayList);
        mList.setAdapter(adapter);
        
        mList.setDivider(null);
        mList.setDividerHeight(0);
        //mList.setSelector(getActivity().getResources().getDrawable(R.drawable.aaa));    
        //mList.setSelector(Color.RED);  
        mList.setDrawSelectorOnTop(false);
        //mList.setOverScrollMode(ListView.OVER_SCROLL_NEVER); 
        
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	Log.i("hwang", "category is selected");
            	view.setSelected(true);
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
	
	/*
	 * back key처리에대한 확인에 대한 메소드이다. 
	 */
	public boolean allowBackPressed() {
		return true;
	}

	@Override
	public void onSidebarOpened() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSidebarClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onContentTouchedWhenOpening() {
		// TODO Auto-generated method stub
		return false;
	}

}
