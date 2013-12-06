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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.TvchSemiAdapter;
import com.cnm.cnmrc.item.ItemTvchSemi;
import com.cnm.cnmrc.item.ItemTvchSemiList;
import com.cnm.cnmrc.parser.TvchSemiParser;
import com.cnm.cnmrc.slidingmenu.SlidingMenu;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class TvchSemi extends Base implements View.OnClickListener {

	/**
	 * tvch (1st arg : 2nd arg) : (0:예고편) / (1:최신영화) / (2:TV다시보기) / (3:장르별)
	 */
	public TvchSemi newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		TvchSemi f = new TvchSemi();
		Bundle args = new Bundle();
		args.putInt("selectedCategory", selectedCategory);
		args.putString("title", title);
		args.putBoolean("isFirstDepth", isFirstDepth);
		args.putBundle("bundle", bundle);
		f.setArguments(args);
		return f;
	}
	
	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	ListView 	listView;
	TvchSemiAdapter adapter;
	ArrayList<ItemTvchSemi> mResult = null;
	
	int selectedCategory;	// where to declare? Base or here / vodtvch메인화면에서 vod의 메인화면을 가르킨다. (0:예고편) / (1:최신영화) / (2:TV다시보기)
	String title;
	String genreCode = "";	// vodtvch메인화면에서는 ""이 전달되고, TvchList화면에서는 genreCode가 전달되어진다.

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.tvch_semi, container, false);
		
		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		selectedCategory = getArguments().getInt("selectedCategory");
		title = getArguments().getString("title");
		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		Bundle bundle = getArguments().getBundle("bundle");
		genreCode = bundle.getString("genreId");

		
		// listview
		listView   = (ListView) layout.findViewById(R.id.listview);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
				// sidebar가 열려있으면 return한다.
				// 2013-12-06 comment later
				// ㅎㅎ
            	if (UiUtil.isSlidingMenuOpening(getActivity())) return;
				
            	increaseCurrentDepth();
            	
            	// Base :: mClassTypeArray = {"VodList", "VodSemi", "VodDetail", "TvchList", "TvchSemi", "TvchDetail"};
            	String dateIndex = "1";	// 처음에는 오늘 날짜을 보여준다.
            	Bundle bundle = UiUtil.makeTvchDetailBundle(getActivity(), adapter, view, position, dateIndex); 
            	String title = "ch" + adapter.getItem(position).getNumber() + ". " + adapter.getItem(position).getName();
            	loadingData(5, title, false, bundle);  	// TvchDetail(5:클래스타입), false : 1 depth가 아님.
            }

        });
        
        setTitle(title);
        showTvchSemi();
        
		return layout;
	}
	private void showTvchSemi() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			new TvchSemiAsyncTask().execute();
		}
	}

	long elapsedTime;
	ItemTvchSemiList list;
	private class TvchSemiAsyncTask extends AsyncTask<Void, Void, ArrayList<ItemTvchSemi>> {
		@Override
		protected void onPreExecute() {
			((MainActivity)getActivity()).getMyProgressBar().show();
			super.onPreExecute();
		}

		protected ArrayList<ItemTvchSemi> doInBackground(Void... params) {
			String requestURL = determineUrl();
			TvchSemiParser parser = new TvchSemiParser(requestURL);

			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of vod semi : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemTvchSemi> result) {
			mResult = result;
			
			if (mResult.size() == 0) {
				Toast.makeText(getActivity(), "데이타가 없습니다!", Toast.LENGTH_LONG).show();
				((MainActivity) getActivity()).getMyProgressBar().dismiss();
				return;
			}

			if (mResult == null)
				onCancelled();
			else {
				Log.i("hwang", "search vod result count : " + mResult.size());

				if (!list.getResultCode().equals("100")) {
					String desc = Util.getErrorCodeDesc(list.getResultCode());
					Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
				} else {
					adapter = new TvchSemiAdapter(getActivity(), R.layout.list_item_tvch_semi, mResult);
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

	private String determineUrl() {
		String url = "";
		String areaCode = Util.getChannelAreaCode(getActivity());
		String productCode = Util.getChannelProductCode(getActivity());
		String mode = "";	// default 전체채널임.

		if(selectedCategory == 0) mode = UrlAddress.Channel.Mode.ALL;	// 전체채널 (from VodTvchMain화면)
		if(selectedCategory == 2) mode = UrlAddress.Channel.Mode.HD;	// HD채널 (from VodTvchMain화면)
		if(selectedCategory == 3) mode = UrlAddress.Channel.Mode.PAY;	// 유료채널 (from VodTvchMain화면)
		
		if(selectedCategory == 4) mode = UrlAddress.Channel.Mode.ALL;	// genreCode에 대한 채널정보는 전체채널이다.  (from TvchList)
		
		url = UrlAddress.Channel.getGetChannelList(areaCode, productCode, genreCode, mode);
		
		return url;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		// ----------------------------------------------------------------------------------------------
		// vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
		// 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
		// ----------------------------------------------------------------------------------------------
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
