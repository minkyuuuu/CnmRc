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

import android.annotation.SuppressLint;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemSearchNaver;
import com.cnm.cnmrc.item.ItemSearchNaverList;
import com.cnm.cnmrc.parser.SearchNaverParser;
import com.cnm.cnmrc.util.Util;

public class SearchNaver extends Fragment implements View.OnClickListener {

	public static SearchNaver newInstance(String search) {
		SearchNaver f = new SearchNaver();
		Bundle args = new Bundle();
		args.putString("search", search); // vod or tvch
		f.setArguments(args);
		return f;
	}

	FrameLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	WebView webview;
	ListView listView;

	String search;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.search_naver, container, false);

		preventClickDispatching = (FrameLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);

		webview = (WebView) layout.findViewById(R.id.webView);
		webview.setVisibility(View.INVISIBLE);

		// listview
		listView = (ListView) layout.findViewById(R.id.listview);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Intent intent = new Intent(Intent.ACTION_VIEW);
				//Uri uri = Uri.parse(mResult.get(position).getLink());
				//intent.setData(uri);
				//getActivity().startActivity(intent);

				//webview use to call own site
				webview.setWebViewClient(new WebViewClient());
				WebSettings set = webview.getSettings();
				set.setJavaScriptEnabled(true);
				set.setBuiltInZoomControls(true);
				webview.loadUrl(mResult.get(position).getLink());
				webview.setVisibility(View.VISIBLE);
				
				// zoom all
				webview.getSettings().setLoadWithOverviewMode(true);
				webview.getSettings().setUseWideViewPort(true);
				
				//webview.setVerticalScrollBarEnabled(false);
				//webview.setHorizontalScrollBarEnabled(false);
			}

		});

		return layout;
	}

	class WebClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
	
	public WebView getWebView() {
		return webview;
	}
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim;

		if (enter)
			anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_entering);
		else
			anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_exit);

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				((MainActivity) getActivity()).getMyProgressBar().show();
				showSearchNaver();
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});

		return anim;
	}

	private void showSearchNaver() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			search = getArguments().getString("search");
			new SearchNaverAsyncTask().execute(search);
		}
	}

	long elapsedTime;
	ItemSearchNaverList list;
	SearchNaverAdapter adapter;
	ArrayList<ItemSearchNaver> mResult = null;

	private class SearchNaverAsyncTask extends AsyncTask<String, Void, ArrayList<ItemSearchNaver>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected ArrayList<ItemSearchNaver> doInBackground(String... params) {
			SearchNaverParser parser = new SearchNaverParser(params[0], "100", "1"); // serach, display count, start

			elapsedTime = System.currentTimeMillis();
			parser.start();
			Log.i("hwang", "elapsedTime of search vod : " + (System.currentTimeMillis() - elapsedTime));

			list = parser.getList();
			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ItemSearchNaver> result) {
			mResult = result;

			if (mResult == null)
				onCancelled();
			else {
				Log.i("hwang", "search naver result count : " + list.getTotal());

				if (mResult.size() == 0) {
				} else {
					adapter = new SearchNaverAdapter(getActivity(), R.layout.list_item_search_naver, mResult);
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
