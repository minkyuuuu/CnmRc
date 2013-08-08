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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;
import com.cnm.cnmrc.slidingmenu.SlidingMenu;

public class Vod extends Fragment implements View.OnClickListener, SlidingMenu.Listener {
	
	public SlidingMenu mSlidingMenu;
	
	FrameLayout mCategoryCover;
	ListView 	mCategory;
	
	String[] mCategoryArray = null;
	int selectedCategory = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.vod, container, false);

		mSlidingMenu = (SlidingMenu) layout.findViewById(R.id.animation_layout);
		mSlidingMenu.setListener(this);
		
		mCategoryCover = (FrameLayout) layout.findViewById(R.id.category_cover);
		mCategoryCover.setOnClickListener(this);
		mCategoryCover.setVisibility(View.GONE);
		
        mCategory   = (ListView) layout.findViewById(R.id.category_list);
        
        mCategoryArray= getActivity().getResources().getStringArray(R.array.vod_category);
        ArrayList<String> arrayList = new ArrayList<String>(mCategoryArray.length);
        for(String item: mCategoryArray) {
        	arrayList.add(item);
        }
        
        VodAdapter adapter = new VodAdapter(getActivity(), R.layout.list_item_vod_category, arrayList);
        mCategory.setAdapter(adapter);
        mCategory.setDivider(null);
        mCategory.setDividerHeight(0);
        mCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	// sidebar가 닫힐때까지 다른 item선택되지 않게하기 위해서...
            	mCategoryCover.setVisibility(View.VISIBLE);
                
            	// change category text color
            	view.setSelected(true);
            	selectedCategory = position;
            	
            	// change title
        		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_top_menu);
        		if (f != null) ((VodTopMenu)f).setTitle(position);
        		
        		// close sidebar
        		mSlidingMenu.toggleSidebar();
        			
            	Log.i("hwang", "category is selected");
            	
				/*View v;
				int count = parent.getChildCount();
				v = parent.getChildAt(position);*/
            }
        });
        
        // -----------------------
        // 하단의 bottom menu 설정
        // -----------------------
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null) ((RcBottomMenu)f).setVodTvchMode();
        
		return layout;
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
    public void onDestroyView() {
        super.onDestroyView();
        
        // ----------------------------------------------------------------------------------------------
        // vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
        // 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
        // ----------------------------------------------------------------------------------------------
        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_top_menu);
        if (f != null) getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
        
        // -----------------------
        // 하단의 bottom menu 설정
        // -----------------------
		f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null) ((RcBottomMenu)f).setRemoconMode();
    }
	
	/*
	 * back key처리에대한 확인절차의 의미이다. 
	 */
	public boolean allowBackPressed() {
		if(mSlidingMenu.isOpening()) return false;
		else return true;
	}

	@Override
	public void onSidebarOpened() {
		
		// 현재 선택된 category를 clear...
		clearSelectedAll();
		
		// listview의 getChildAt()은 화면에 리스트가 보여야지만 view를 가져온다.
		// 보이지 않으면 null을 리턴한다.
		View selectedItem = mCategory.getChildAt(selectedCategory);
		if(selectedItem != null) selectedItem.setSelected(true);
		
		//mCategory.performItemClick(mCategory.getChildAt(selectedCategory), selectedCategory, mCategory.getAdapter().getItemId(selectedCategory));
		
		Log.i("hwang", "onSidebarOpened");
		
	}

	private void clearSelectedAll() {
		for (int i = 0; i < mCategoryArray.length; i++) {
			mCategory.getChildAt(i).setSelected(false);
		}
		
	}

	@Override
	public void onSidebarClosed() {
		mCategoryCover.setVisibility(View.GONE);
		
	}

	@Override
	public boolean onContentTouchedWhenOpening() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
