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
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

public class TvchDetail extends Base implements View.OnClickListener {

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

	ImageButton prev;
	ImageButton next;
	TextView titleDate; // 중간 타이들 날짜

	String channelId;
	String channelNumber;
	String channelName;
	String dateIndex;

	List<ArrayFragment> fragments;	// 뷰페이저의 전체페이지
	private ViewPager mPager; 		// 뷰 페이저
	private FragmentPagerAdapterClass pagerAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.tvch_detail, container, false);

		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);

		channelName = getArguments().getString("title");
		isFirstDepth = getArguments().getBoolean("isFirstDepth");

		Bundle bundle = getArguments().getBundle("bundle");
		channelId = bundle.getString("id");
		dateIndex = bundle.getString("dateIndex");
		
		// 좌, 우 날짜변경 버튼
		prev = (ImageButton) layout.findViewById(R.id.date_pre); 	//이전 아이템으로 이동 버튼
		prev.setOnClickListener(this);
		prev.setVisibility(View.INVISIBLE);

		next = (ImageButton) layout.findViewById(R.id.date_next); 	//다음 아이템으로 이동 버튼
		next.setOnClickListener(this);

		// 중간 타이들 날짜, 이미 오늘날짜를 알고 있으므로 여기서 보여주면 된다.
		titleDate = (TextView) layout.findViewById(R.id.date);
		Date date = new Date(System.currentTimeMillis());
		titleDate.setText(Util.getMMDDE(date));
		
        fragments = new ArrayList<ArrayFragment>();
        fragments.add(new ArrayFragment(0, channelId));
        fragments.add(new ArrayFragment(1, channelId));
        fragments.add(new ArrayFragment(2, channelId));
        fragments.add(new ArrayFragment(3, channelId));
        fragments.add(new ArrayFragment(4, channelId));
        fragments.add(new ArrayFragment(5, channelId));
        fragments.add(new ArrayFragment(6, channelId));
        
//        fragments.add(ArrayFragment.newInstance(0, channelId));
//        fragments.add(ArrayFragment.newInstance(1, channelId));
//        fragments.add(ArrayFragment.newInstance(2, channelId));
//        fragments.add(ArrayFragment.newInstance(3, channelId));
//        fragments.add(ArrayFragment.newInstance(4, channelId));
//        fragments.add(ArrayFragment.newInstance(5, channelId));
//        //fragments.add(ArrayFragment.newInstance(6, channelId));

		mPager = (ViewPager) layout.findViewById(R.id.pager); 		//뷰 페이저
		pagerAdapter  = new FragmentPagerAdapterClass(getActivity().getSupportFragmentManager(), fragments);
		mPager.setAdapter(pagerAdapter);	//PagerAdapter로 설정
		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//아이템이 변경되면, gallery나 listview의 onItemSelectedListener와 비슷
			//아이템이 선택이 되었으면
			@Override 
			public void onPageSelected(int position) {
				Date date = new Date(System.currentTimeMillis());
				date = Util.getNextDAY(date, position);
				titleDate.setText(Util.getMMDDE(date));
				
				((ArrayFragment)pagerAdapter.getItem(position)).showTvchDetail();
				
				if(position == 0) prev.setVisibility(View.INVISIBLE);
				else prev.setVisibility(View.VISIBLE);
				
				if(position == 5) next.setVisibility(View.INVISIBLE);
				else next.setVisibility(View.VISIBLE);
			}
			@Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {
				// flicking이 일어나면 계속 콜백된다.
				//Log.i("hwang", "onPageScrolled");
			}
			@Override public void onPageScrollStateChanged(int state) {
				// flicking이 일어나면 마지막에 한번만 콜백된다. 
				// 마지막페이지, 처음페이지에서 페이지의 변화가 없어도 콜백이 한번 발생한다.
				// flicking의 방향까지 체크되어야 마지막 페이지가 체크가능하다....
				// Log.v("hwang", "onPageScrollStateChanged");
			}
		});
		
//		mPager.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				//pagerAdapter.getItem(mPager.getCurrentItem());
//				// getsture를 이용해서 마지막페이지 처리???
//				return false;
//			}
//		});
		//((ArrayFragment)pagerAdapter.getItem(0)).showTvchDetail(); // 여기선 안된다...

		return layout;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
//		pagerAdapter = null;
//		fragments = null;
		
		// 여기서는 fragments가 null이다.
//		for(Fragment f : fragments) {
//			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
//		}

		// ----------------------------------------------------------------------------------------------
		// vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
		// 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
		// ----------------------------------------------------------------------------------------------
//        {
//	        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.vod_tvch_panel);
//	        if (f != null) {
//	        	getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
//	        }
//        }
		
		
	}

	@Override
	public void onStop() {
		// Fragment에서 Fragment를 만들어 사용하게되면 나갈때 만든 Fragment를 remove 해주어야한다. 아니면 다시 진입할 때 기존의 Fragment가 그대로 사용되어 다시 만들어지지 않는다.
		for(Fragment f : fragments) {
			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}

		super.onStop();
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
		// 2013-12-06 comment later
		// ㅎㅎ
		if (UiUtil.isSlidingMenuOpening(getActivity())) return;

		int cur = mPager.getCurrentItem(); //현재 아이템 포지션
		switch (v.getId()) {
		case R.id.date_pre:
			if (cur > 0) mPager.setCurrentItem(cur - 1, true); //이전 페이지로 이동
			else Toast.makeText(getActivity(), "맨 처음 페이지 입니다.", Toast.LENGTH_SHORT).show(); //메시지 출력
			break;
			
		case R.id.date_next:
			if (cur < fragments.size() - 1) mPager.setCurrentItem(cur + 1, true); //다음 페이지로 이동
			else Toast.makeText(getActivity(), "맨 마지막 페이지 입니다.", Toast.LENGTH_SHORT).show(); //메시지 출력
			break;
		}
	}

	// -----------------------------------------
	// FragmentPager 구현
	// -----------------------------------------
	private class FragmentPagerAdapterClass extends FragmentPagerAdapter{
		private List<ArrayFragment> fragments;
		
		//생성자
		public FragmentPagerAdapterClass(FragmentManager fm, List<ArrayFragment> fragments) {
			//public FragmentPagerAdapterClass(FragmentManager fm, String channelId, String dateIndex) {
			super(fm);
			
			this.fragments = fragments;
			
		}
		
		/**
		 * 실제 뷰페이저에서 보여질 fragment를 반환.
		 * 일반 아답터(갤러리, 리스트뷰 등)의 getView와 같은 역할
		 * @param position - 뷰페이저에서 보여저야할 페이지 값( 0부터 )
		 * @return 보여질 fragment
		 */
		@Override 
		public Fragment getItem(int position) {	// 여기서 position대신 dataIndex가 사용된다.
			//if(position == 0) this.fragments.get(position).showTvchDetail();
			return fragments.get(position);
			// return ArrayFragment.newInstance(position, channelId, dateIndex);
		}
		
		//뷰페이저에서 보여질 총 페이지 수
		@Override 
		public int getCount() { return this.fragments.size(); }
		
		// destroyItem 하면 안된다.
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//		    getActivity().getSupportFragmentManager().beginTransaction().remove((Fragment)object).commit();
//		  super.destroyItem(container, position, object);
//		}
		
//		@Override
//		public void setPrimaryItem(ViewGroup container, int position, Object object) {
//			// TODO Auto-generated method stub
//			super.setPrimaryItem(container, position, object);
//		}
//
//		@Override
//		public void finishUpdate(ViewGroup container) {
//			// TODO Auto-generated method stub
//			super.finishUpdate(container);
//		}
//
//		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
////		@Override
////		public boolean isViewFromObject(View view, Object object) {
////			// TODO Auto-generated method stub
////			return view == object;
////			// return super.isViewFromObject(view, object);
////		}
////		
//		@Override
//		public void restoreState(Parcelable state, ClassLoader loader) {
//			// TODO Auto-generated method stub
//			super.restoreState(state, loader);
//		}
//
//		@Override
//		public Parcelable saveState() {
//			// TODO Auto-generated method stub
//			return super.saveState();
//		}
//
//		@Override
//		public void startUpdate(ViewGroup container) {
//			// TODO Auto-generated method stub
//			super.startUpdate(container);
//		}
//
//		@Override
//		public void registerDataSetObserver(DataSetObserver observer) {
//			// TODO Auto-generated method stub
//			super.registerDataSetObserver(observer);
//		}


	}
	
	// -----------------------------------------
	//	뷰 페이저의 페이지에 맞는 fragment를 생성하는 객체
	// -----------------------------------------
	public class ArrayFragment extends Fragment {
		
		ListView listView;
		TvchDetailAdapter adapter;
		ArrayList<ItemTvchDetail> mResult = null;

		int position;
		String dateIndex; 	// "조회할 날짜"를 의미한다.
		String channelId;	// "조회할 채널 id" 를 의미한다.
		
		//fragment 생성하는 static 메소드 뷰페이저의 position을 값을 받는다.
//		static ArrayFragment newInstance(int position, String channelId) {
//			ArrayFragment f = new ArrayFragment(); 	//객체 생성
//			Bundle args = new Bundle(); 			//해당 fragment에서 사용될 정보 담을 번들 객체
//			args.putInt("position", position); 		//"조회할 날짜"를 의미한다.
//			args.putString("channelId", channelId); //"조회할 채널 id"를 의미한다.
//			f.setArguments(args); 					//fragment에 정보 전달.
//			return f; 								//fragment 반환
//		}
		
		
		ArrayFragment(int position, String channelId) {
			this.position = position;
			this.channelId = channelId;
		}
		

		//fragment가 만들어질 때
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//channelId = getArguments() != null ? getArguments().getString("channelId") : ""; 	// 뷰페이저의 "조회할 채널 id" 값을 넘겨 받음
			//position = getArguments() != null ? getArguments().getInt("position") : 0; 			// 뷰페이저의 "조회할 날짜" 값을 넘겨 받음
			position++;
			dateIndex = String.valueOf(position);
		}

		//fragment의 UI 생성
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View layout = inflater.inflate(R.layout.tvch_detail_viewpager, container, false); 	//미리 알고 있는 레이아웃을 inflate 한다.
			
			// listview
			listView = (ListView) layout.findViewById(R.id.listview);
			listView.setDivider(null);
			listView.setDividerHeight(0);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// sidebar가 열려있으면 return한다.
					// 2013-12-06 comment later
					// ㅎㅎ
					if (UiUtil.isSlidingMenuOpening(getActivity()))
						return;

				}

			});

			if(dateIndex.equals("1")) showTvchDetail();

			return layout;
		}
		
		
		private void showTvchDetail() {
			// check network and data loading
			if (Util.GetNetworkInfo(getActivity()) == 99) {
				Util.AlertShow(getActivity());
			} else {
				new VodSemiAsyncTask().execute();
			}
			
			//new VodSemiAsyncTask().execute();
		}

		long elapsedTime;
		ItemTvchDetailList list;

		private class VodSemiAsyncTask extends AsyncTask<Void, Void, ArrayList<ItemTvchDetail>> {
			@Override
			protected void onPreExecute() {
				((MainActivity) getActivity()).getMyProgressBar().show();
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
	}

}

