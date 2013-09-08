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
import com.cnm.cnmrc.fragment.vodtvch.TvchSemiDetailAdapter;
import com.cnm.cnmrc.parser.SearchProgram;
import com.cnm.cnmrc.parser.SearchProgramList;
import com.cnm.cnmrc.parser.SearchProgramParser;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class SearchTvchSub extends Fragment implements View.OnClickListener  {
	
	public static SearchTvchSub newInstance(String search) {
		SearchTvchSub f = new SearchTvchSub();
		Bundle args = new Bundle();
		args.putString("search", search); // vod or tvch
		f.setArguments(args);
		return f;
	}
	
	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	ListView 	listView;
	
	
	String search;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.tvch_semidetail, container, false);
		
		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.search_tvch_prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		// listview
		listView   = (ListView) layout.findViewById(R.id.tvch_semidetail);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
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
				showSearchProgram(); 
			}
			public void onAnimationRepeat(Animation animation) { }
		});

		return anim;
	}

	private void showSearchProgram() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			search = getArguments().getString("search");
			new SearchProgramAsyncTask().execute(search);
		}
	}
	

	long elapsedTime;
	SearchProgramList list;
	SearchTvchSemiAdapter adapter;
	ArrayList<SearchProgram> mResult = null;
	
	private class SearchProgramAsyncTask extends AsyncTask<String, Void, ArrayList<SearchProgram>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected ArrayList<SearchProgram> doInBackground(String... params) {
			String areaCode = Util.getChannelAreaCode(getActivity());
			String productCode = Util.getChannelProductCode(getActivity());
			String requestURL = UrlAddress.Search.getSearchProgram(params[0], "0", "0", areaCode, productCode);
			SearchProgramParser parser = new SearchProgramParser(requestURL);
			
			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of search vod : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<SearchProgram> result) {
			mResult = result;

			if (mResult == null)
				onCancelled();
			else {
				Log.i("hwang", "search program result count : " + list.getTotalCount());
				
				if (!list.getResultCode().equals("100")) {
					String desc = Util.getErrorCodeDesc(list.getResultCode());
					Toast.makeText(getActivity(), desc, Toast.LENGTH_LONG).show();
				} else {
					adapter = new SearchTvchSemiAdapter(getActivity(), R.layout.list_item_search_program, mResult);
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
