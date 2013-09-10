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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.VodSemiAdapter;
import com.cnm.cnmrc.item.ItemVodSemi;
import com.cnm.cnmrc.item.ItemVodSemiList;
import com.cnm.cnmrc.parser.VodSemiParser;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class SearchVodSemi extends Fragment implements View.OnClickListener {
	
	public static SearchVodSemi newInstance(String search) {
		SearchVodSemi f = new SearchVodSemi();
		Bundle args = new Bundle();
		args.putString("search", search); // vod or tvch
		f.setArguments(args);
		return f;
	}
	
	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	ListView 	listView;
	VodSemiAdapter adapter;
	ArrayList<ItemVodSemi> mResult = null;
	
	String search;
	
	/**
	 * 통신 모듈의 명명 규칙
	 * 1) 파서관련 클래스 이름은 프로토콜문서의 Api이름을 기준으로 만든다.
	 * 2) 통신모듈을 사용하는 Activity나 Fragment의 클래스이름은 이미 화면구성과 기능에 따라 이름이 정해져있다. (통신모듈과 이름이 같을 수 있는데...)
	 * 3) 파서관련 클래스의 내부 필드명은 프로토콜문서의 elements 이름을 기준으로 한다. 어차피 해당프로토콜를 의미하는 단어는 생략한다. (예, VOD_ID --> ID)
	 *    : 이름은 첫글자는 소문자로 하고 다음 단어는 시작 글자만 대문자로 한다. "_"은 사용하지 않는다. (예, areaCode)
	 *    : element이름과 연관된 string 상수는 모두 대문자로 하고 단어와 단어사이는 "_"을 사용한다. (예, AREA_CODE)
	 * 4) 요청 url은 요청하는 클래스에서 만들어 파서로 전달하는걸 원칙으로 하자.
	 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.vod_semi, container, false);
		
		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		// listview
		listView   = (ListView) layout.findViewById(R.id.vod_semi_listview);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
        		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity)getActivity()).TAG_FRAGMENT_SEARCH);
        		if (f != null) {
        			Bundle bundle = UiUtil.makeVodDetailBundle(getActivity(), adapter, view, position); 
        			((SearchMain) f).showSearchVodDetail(bundle);
        		}
            }

        });

		return layout;
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim;

		if (enter) anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_entering);
		else anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_exit);

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) { }
			public void onAnimationEnd(Animation animation) { 
				((MainActivity)getActivity()).getMyProgressBar().show();
				showSearchVodSemi(); 
			}
			public void onAnimationRepeat(Animation animation) { }
		});

		return anim;
	}

	private void showSearchVodSemi() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			search = getArguments().getString("search");
			new SearchVodSemiAsyncTask().execute(search);
		}
	}

	long elapsedTime;
	ItemVodSemiList list;
	private class SearchVodSemiAsyncTask extends AsyncTask<String, Void, ArrayList<ItemVodSemi>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected ArrayList<ItemVodSemi> doInBackground(String... params) {
			String requestURL = UrlAddress.Search.getSearchVod(params[0], "0", "0", "TitleAsc");
			VodSemiParser parser = new VodSemiParser(requestURL);
			
			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of search vod : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemVodSemi> result) {
			mResult = result;
			
			if (mResult.size() == 0) {
				Toast.makeText(getActivity(), "데이타가 없습니다!", Toast.LENGTH_LONG).show();
				((MainActivity) getActivity()).getMyProgressBar().dismiss();
				return;
			}

			if (mResult == null)
				onCancelled();
			else {
				Log.i("hwang", "search vod result count : " + list.getTotalCount());
				
				if (!list.getResultCode().equals("100")) {
					String desc = Util.getErrorCodeDesc(list.getResultCode());
					Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
				} else {
					adapter = new VodSemiAdapter(getActivity(), R.layout.list_item_vod_semi, mResult);
					listView.setAdapter(adapter);
				}
				
				((MainActivity) getActivity()).getMyProgressBar().dismiss();
			}
		}

		@Override
		protected void onCancelled() {
			Util.onCancelled(getActivity());
			
			super.onCancelled();
		}
	}
	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        
        // ----------------------------------------------------------------------------------------------
        // vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
        // 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
        // ----------------------------------------------------------------------------------------------
//        {
//	        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("search_vod_detail");
//	        if (f != null) {
//	        	getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
//	        }
//        }
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
		// TODO Auto-generated method stub
		
	}
	

	
	
}
