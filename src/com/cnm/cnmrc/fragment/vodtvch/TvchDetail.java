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
import java.util.Date;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.TvchDetailAdapter;
import com.cnm.cnmrc.item.ItemTvchDetail;
import com.cnm.cnmrc.item.ItemTvchDetailList;
import com.cnm.cnmrc.parser.TvchDetailParser;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class TvchDetail extends Base implements View.OnClickListener{

	public TvchDetail newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		TvchDetail f = new TvchDetail();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putBoolean("isFirstDepth", isFirstDepth);
		args.putBundle("bundle", bundle);
		f.setArguments(args);
		return f;
	}
	
	LinearLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	ListView 	listView;
	TvchDetailAdapter adapter;
	ArrayList<ItemTvchDetail> mResult = null;
	
	TextView titleDate;	// 중간 타이들 날짜
	
	String channelId;
	String channelNumber;
	String channelName;
	String dateIndex;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.tvch_detail, container, false);
		
		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		channelName = getArguments().getString("title");
		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		
		Bundle bundle = getArguments().getBundle("bundle");
		channelId = bundle.getString("id");
		//channelName = bundle.getString("name");
		dateIndex = bundle.getString("dateIndex");
		
		// 중간 타이들 날짜
		titleDate = (TextView)layout.findViewById(R.id.date);
		titleDate.setVisibility(View.INVISIBLE);
		
		// listview
		listView   = (ListView) layout.findViewById(R.id.listview);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
				// sidebar가 열려있으면 return한다.
				if (UiUtil.isSlidingMenuOpening(getActivity())) return;
				
            }

        });
		
        showTvchDetail();
        
		return layout;
	}
	
	private void showTvchDetail() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			new VodSemiAsyncTask().execute();
		}
	}

	long elapsedTime;
	ItemTvchDetailList list;
	private class VodSemiAsyncTask extends AsyncTask<Void, Void, ArrayList<ItemTvchDetail>> {
		@Override
		protected void onPreExecute() {
			((MainActivity)getActivity()).getMyProgressBar().show();
			super.onPreExecute();
		}

		protected ArrayList<ItemTvchDetail> doInBackground(Void... params) {
			String requestURL = UrlAddress.Channel.getGetChannelSchedule(channelId, dateIndex);
			TvchDetailParser parser = new TvchDetailParser(requestURL);

			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of vod semi : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemTvchDetail> result) {
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
					adapter = new TvchDetailAdapter(getActivity(), R.layout.list_item_tvch_detail, mResult);
					listView.setAdapter(adapter);
					
					// 중간 타이들 날짜
					titleDate.setVisibility(View.VISIBLE);
					if (mResult.get(0).getBroadcastingDate() != null) {
						Date date = Util.getFromStringToDate2(mResult.get(0).getBroadcastingDate());
						if (date == null) {
							titleDate.setText(" ");
						} else {
							titleDate.setText(Util.getMMDDE(date));
						}
					}
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
		// sidebar가 열려있으면 return한다.
    	if (UiUtil.isSlidingMenuOpening(getActivity())) return;
    	
		switch(v.getId()){
		}
	}
	
}
