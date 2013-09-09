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
import com.cnm.cnmrc.item.ItemVodList;
import com.cnm.cnmrc.item.ItemVodListList;
import com.cnm.cnmrc.parser.VodListParser;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class VodList extends Base implements View.OnClickListener {

	/**
	 * vod (1st arg : 2nd arg) : (0:예고편) / (1:최신영화) / (2:TV다시보기) / (3:장르별)
	 * 지금은 3의 장르별에 대한 화면이다. 장르별 첫 화면은 장르정보 리스트(0:VodList)이고,
	 * more가 yes일때까지 장르정보 리스트(0:VodList)를 보이고, yno이면 vod정보를 보여주는 VodSemi(1) 화면으로 전환된다.
	 */
	public VodList newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		VodList f = new VodList();
		Bundle args = new Bundle();
		args.putBoolean("isFirstDepth", isFirstDepth);
		args.putBundle("bundle", bundle);
		f.setArguments(args);
		return f;
	}

	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	ListView listView;
	VodListAdapter adapter;
	ArrayList<ItemVodList> mResult = null;
	
	String genreId = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.vod_list, container, false);

		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.search_vod_prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);

		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		Bundle bundle = getArguments().getBundle("bundle");
		genreId = bundle.getString("genreId");

		// listview
		listView = (ListView) layout.findViewById(R.id.vod__list_listview);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				increaseCurrentDepth();
				
				Bundle bundle = new Bundle();
				
				String title = adapter.getItem(position).getTitle();
				String genreId = adapter.getItem(position).getGenreId();
				bundle.putString("genreId", genreId);
				String more = adapter.getItem(position).getMore();
				// more가 yes일때까지 장르정보 리스트(0:VodList)를 보이고, no이면 vod정보를 보여주는 VodSemi(1) 화면으로 전환된다.
				if(more.equalsIgnoreCase("yes")) {
					loadingData(0, title, false, bundle); // (0:VodList) or (1:VodSemi), false : 1 depth가 아님, 즉 메인화면(예고편,최신영화,TV다시보기,장르별)이 아니다.
				} else {
					loadingData(1, title, false, bundle); // (0:VodList) or (1:VodSemi), false : 1 depth가 아님, 즉 메인화면(예고편,최신영화,TV다시보기,장르별)이 아니다.
				}
			}

		});
		
		showVodList(genreId);

		return layout;
	}
	
	private void showVodList(String genreId) {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			new VodListAsyncTask().execute(genreId);
		}
	}

	long elapsedTime;
	ItemVodListList list;
	private class VodListAsyncTask extends AsyncTask<String, Void, ArrayList<ItemVodList>> {
		@Override
		protected void onPreExecute() {
			((MainActivity)getActivity()).getMyProgressBar().show();
			super.onPreExecute();
		}

		protected ArrayList<ItemVodList> doInBackground(String... params) {
			String requestURL = UrlAddress.Vod.getGetVodGenre(params[0]);
			VodListParser parser = new VodListParser(requestURL);

			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of vod semi : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemVodList> result) {
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
					adapter = new VodListAdapter(getActivity(), R.layout.list_item_vod_list, mResult);
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

		switch (v.getId()) {
		}
	}

}
