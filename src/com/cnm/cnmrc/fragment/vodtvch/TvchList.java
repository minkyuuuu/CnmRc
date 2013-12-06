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
import com.cnm.cnmrc.adapter.TvchListAdapter;
import com.cnm.cnmrc.item.ItemTvchList;
import com.cnm.cnmrc.item.ItemTvchListList;
import com.cnm.cnmrc.parser.TvchListParser;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class TvchList extends Base  implements View.OnClickListener {
	
	/**
	 * tvch (1st arg : 2nd arg) : (0:전체채널) / (1:장르별채널) / (2:HD채널) / (3:유료채널)
	 * 지금은 1의 장르별채널에 대한 화면이다. 장르별채널 첫 화면은 장르정보 리스트(0:TvchList)이다.
	 * 여기선 1st arg인 selectedCategory는 의미가 없다. 즉 1로 고정된 값이다. 즉 장르별 리스트화면이다.
	 * 장르를 선태하면,
	 * 장르코드(genreCode)에 대한 채널정보화면(GetChannelList:프로토콜)인 TvchSemi(4:클래스타입)으로 넘어간다.
	 */
	public TvchList newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		TvchList f = new TvchList();
		Bundle args = new Bundle();
		args.putBoolean("isFirstDepth", isFirstDepth);	
		args.putBundle("bundle", bundle);
		f.setArguments(args);
		return f;
	}

	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	ListView 	listView;
	TvchListAdapter adapter;
	ArrayList<ItemTvchList> mResult = null;
	
	String genreCode = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.tvch_list, container, false);

		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		Bundle bundle = getArguments().getBundle("bundle");
		genreCode = bundle.getString("genreId");	// 여기서 genreCode는 ""이다. 의미가 없다.
		
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
        		
        		String genreName = adapter.getItem(position).getGenreName();
        		
        		String genreCode = adapter.getItem(position).getGenreCode();
        		Bundle bundle = new Bundle();
        		bundle.putString("genreId", genreCode);
        		
        		// Base :: mClassTypeArray = {"VodList", "VodSemi", "VodDetail", "TvchList", "TvchSemi", "TvchDetail"};
            	loadingData(4, genreName, false, bundle); // TvchSemi(4:클래스타입), false : 1 depth가 아님.
            }

        });

        showTvchList();
        
		return layout;
	}
	
	private void showTvchList() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			new TvchListAsyncTask().execute();
		}
	}

	long elapsedTime;
	ItemTvchListList list;
	private class TvchListAsyncTask extends AsyncTask<Void, Void, ArrayList<ItemTvchList>> {
		@Override
		protected void onPreExecute() {
			((MainActivity)getActivity()).getMyProgressBar().show();
			super.onPreExecute();
		}

		protected ArrayList<ItemTvchList> doInBackground(Void... params) {
			String requestURL = UrlAddress.Channel.getGetChannelGenre();
			TvchListParser parser = new TvchListParser(requestURL);

			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of vod semi : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemTvchList> result) {
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
					adapter = new TvchListAdapter(getActivity(), R.layout.list_item_vod_list, mResult);	// list_item_vod_list를 공통으로 사용해도 된다. 
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
