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
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.VodTvchMainAdapter;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;
import com.cnm.cnmrc.slidingmenu.SlidingMenu;

public class VodTvchMain extends Fragment implements View.OnClickListener, SlidingMenu.Listener {

	/**
	 * vodtvchmaind의 loadingDataForSidrebar()은 각각 vod, tvch의 4개의 대분류에 대한 화면이고...
	 * 대분류 화면에서 리스트아이템을 선택했을 때 다음화면에 대한 처리는 Base의 loadingData()를 통해서 이루어진다.
	 * isFirstDepth???
	 */
	public static VodTvchMain newInstance(String type) {
		VodTvchMain f = new VodTvchMain();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	SlidingMenu mSlidingMenu;

	FrameLayout mCategoryCover, mLoadingData;
	ListView mCategory;

	String[] mCategoryArray = null;
	String[] mClassTypeArray = null;

	int selectedCategory = 0; // 처음 시작하는 vod 화면 (예고편:0, 최신영화:1, TV다시보기:2)

	public int mBackStackEntry = 100;

	public int currentDepth = 1;
	
	// 2013-12-10 아무런 의미도 없는것 같다.
	boolean isSkipTitle = true;

	String type;
	
	boolean isSidebarItemClick = false;

	public HashMap<Integer, String> mMapTitle = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.vod_tvch, container, false);

		mSlidingMenu = (SlidingMenu) layout.findViewById(R.id.animation_layout);
		mSlidingMenu.setListener(this);

		mLoadingData = (FrameLayout) layout.findViewById(R.id.loading_data_panel);

		mCategoryCover = (FrameLayout) layout.findViewById(R.id.category_cover);
		mCategoryCover.setOnClickListener(this);
		mCategoryCover.setVisibility(View.GONE);

		mCategory = (ListView) layout.findViewById(R.id.category_list);

		//        FragmentManager fm = getActivity().getSupportFragmentManager();
		//        fm.addOnBackStackChangedListener(mAddOnBackStackChangedListener);
		//        fm.removeOnBackStackChangedListener(mRemoveOnBackStackChangedListener);

		// --------------------------
		// type check (vod or tvch)
		// --------------------------
		type = getArguments().getString("type");
		if (type.equals("vod")) {
			mCategoryArray = getActivity().getResources().getStringArray(R.array.vod_category);
			mClassTypeArray = getActivity().getResources().getStringArray(R.array.vod_class_type);
		}
		if (type.equals("tvch")) {
			mCategoryArray = getActivity().getResources().getStringArray(R.array.tvch_category);
			mClassTypeArray = getActivity().getResources().getStringArray(R.array.tvch_class_type);
		}

		ArrayList<String> arrayList = new ArrayList<String>(mCategoryArray.length);
		for (String item : mCategoryArray) {
			arrayList.add(item);
		}

		// -------------------------
		// set title : category
		// -------------------------
		// 타이틀을 설정할 때, 나중에 back key 처리때 필요하므로 저장해야 한다. depth와 함께 저장하면 좋다.
		mMapTitle = new HashMap<Integer, String>();

		setTitle(selectedCategory);

		// -------------------
		// sidebar category
		// -------------------
		VodTvchMainAdapter adapter = new VodTvchMainAdapter(getActivity(), R.layout.list_item_vod_category, arrayList);
		mCategory.setAdapter(adapter);
		mCategory.setDivider(null);
		mCategory.setDividerHeight(0);
		mCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// sidebar가 닫혀있으면 return... 그러지 않으면 밑에있는 sidebar 메뉴 아이템이 클릭된다...
				// 2013-12-06 comment later
				// ㅎㅎ
				if (!mSlidingMenu.isOpening())
					return;
				
				// 2013-12-10 사이드바 오픈후에 그냥 close 되면 다시 데이타로딩을 막기 위해서...
				isSidebarItemClick = true;

				// sidebar가 닫힐때까지 다른 item선택되지 않게하기 위해서...
				mCategoryCover.setVisibility(View.VISIBLE);

				// change category text color
				view.setSelected(true);
				selectedCategory = position;

				// change title
				clearCurrentDepth();
				isSkipTitle = true;
				setTitle(position);

				// close sidebar
				mSlidingMenu.toggleSidebar();

				Log.i("hwang", "category is selected");

				/*
				 * View v; int count = parent.getChildCount(); v =
				 * parent.getChildAt(position);
				 */
			}

		});

		// -----------------------
		// 하단의 bottom menu 설정
		// -----------------------
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null)
			((RcBottomMenu) f).setVodTvchMode();

		// ----------------------------------------------------------------------------------------------------------
		// 처음 로딩되는 대분류에 해당하는 fragment는 VodTvch 클래스의 loadingDataForSidrebar()에서 addToBackStack에 저장하지 않는다.
		// ----------------------------------------------------------------------------------------------------------
		loadingDataForSidrebar();

		return layout;
	}

	/**
	 * 처음 로딩되는 대분류에 해당하는 fragment는 VodTvch 클래스의 loadingDataForSidrebar()에서
	 * addToBackStack에 넣지 않구 생성한다. 이유는 MainActivity onBackPressed()에서 back
	 * key처리를 하지않기위해서이다. 이미 VodTvch fragment가 addToBackStack으로 생성되었기 때문에, 여기서
	 * 생성되는 Base는 back key처리가 필요없다. 그렇지만 back key에 대한 처리가 필요없다뿐이지... Base의
	 * destoryView()에 대한 콜백은 처리된다. 중요!!!!!!! 여기서 만들어지는 화면은 isFirstDepth를 true로
	 * 해야한다. 즉 4개의 메인화면(예고편,최신영화,TV다시보기,장르별)이다. vod (1st selectedCategory arg :
	 * 2nd title arg) : (0:예고편) / (1:최신영화) / (2:TV다시보기) / (3:장르별)
	 */
	private void loadingDataForSidrebar() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Log.i("hwang", "before vodTvch fragment count (no add to stack) --> " + Integer.toString(fm.getBackStackEntryCount()));

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		Class<?> classObject;
		try {
			String packageName = this.getClass().getPackage().getName() + ".";
			classObject = Class.forName(packageName + mClassTypeArray[selectedCategory]); // VodList, VodSemi, VodDetail, TvchList, TvchSemi, TvchDetail
			Object obj = classObject.newInstance();

			// vod 	(1st arg : 2nd arg) : (0:예고편) / (1:최신영화) / (2:TV다시보기) / (3:장르별)
			// tvch (1st arg : 2nd arg) : (0:전체채널) / (1:장르별채널) / (2:HD채널) / (3:유료채널)
			// true : 1 depth (vod: VodSemi, VodSemi, VodSemi, VodList)
			// true : 1 depth (tvch: TvchSemi, TvchList, TvchSemi, TvchSemi)
			Bundle bundle = new Bundle();
			bundle.putString("genreId", ""); // 장르별 첫 화면에서 사용할 대분류 장르별 정보를 위해서...
			Base base = ((Base) obj).newInstance(selectedCategory, mCategoryArray[selectedCategory], true, bundle); // 여기서 true란 첫번째depth 즉 대분류를 의미한다.(예고편,최신영화,TV다시보기,장르별)(전체채널,장르별채널,HD채널,유료채)

			//ft.addToBackStack(null);	// addTBackStack하지 않으면 onBackPressed()가 콜백되지 않는것이지... remove fragment하면 onDestroyView()는 콜백된다.

			// 2013-12-10 update vodsemi after config>adultcert change
			if (mClassTypeArray[selectedCategory].equals("VodSemi")) {
				ft.replace(R.id.loading_data_panel, base, "vod_semi_for_config");
			} else {
				ft.replace(R.id.loading_data_panel, base);
			}
			ft.commit(); //fm.executePendingTransactions();	// fragment안에서 또다시 pending하면 recursive error, 가장 상위에서 pending해야 한다.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Log.i("hwang", "after vodTvch fragment count (no add to stack) --> " + Integer.toString(fm.getBackStackEntryCount()));

		// ------------------------------------
		// 하단의 bottom menu에서 depth level 설정
		// ------------------------------------
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
		if (f != null)
			((RcBottomMenu) f).setDepthLevelClear(); // set 1 depth
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

		//		if (((MainActivity) getActivity()).noVodTvchDestroy)
		//			return;

		// ----------------------------------------------------------------------------------------------
		// vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
		// 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
		// ----------------------------------------------------------------------------------------------
		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
			if (f != null) {
				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			}
		}

		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_data_panel);
			if (f != null) {
				// fragment를 제거하면  Base의 onDestroyView callBack된다.
				// 이 때 back stack의 count는 이미 minus되어 destoryView에 진입할 때는 0이된다.
				// 0을 가지고 Base의 onDestroyView()에 진입한다.

				// 이미 이곳 onDestroyView()에 진입될 때 R.id.loading_data_panel에 해당하는 fragment는 제거되고 backstack 카운트가 이미 minus되었다.
				// 위의 말과 동일하다. 아래의 before와 after는 이미 minus가된 count이다.
				// 그러면 remove하지 말고 테스트해보자. rc로 잘 돌아간다. 그러나 Base의 onDestroyView() callBack하지 않는다.
				// 엄밀히 말해서 Base의 onDestroyView()에서 특별히 처리할 일이 없으면 명시적으로 remove 하지 않는게 더 좋을 수도 있다.
				// 일단은 remove해서 Base의 onDestroyView()을 callBack하는것으로 처리하자.
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Log.i("hwang", "before at vodTvch : remove(R.id.loading_data_panel).commit fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));
				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
				Log.i("hwang", "after at vodTvch : remove(R.id.loading_data_panel).commit fragment count --> " + Integer.toString(fm.getBackStackEntryCount()));

				// 여기서 마무리해야지 base에서 remove 되었던 TAG_FRAGMENT_BASE가 완전히 메모리에서 제거된다.
				// 그렇지 않으면 mainActity의 backpress에서 다시 base의 destoryView가 callback된다.
				// 아니다.
				// 현상은 맞으나 현상제거는 위의 방법이다.
				// getActivity().getSupportFragmentManager().executePendingTransactions();	
			}
		}

		// -----------------------
		// 하단의 bottom menu 설정
		// -----------------------
		{
			Fragment f = ((MainActivity) getActivity()).getFragmentRcBottomMenu();
			if (f != null)
				((RcBottomMenu) f).setRemoconMode();
		}
	}

	/*
	 * back key처리에대한 확인절차의 의미이다.
	 */
	public int allowBackPressed() {
		if (mSlidingMenu.isOpening())
			return 1; // 열려있다는 의미는 sidebar가 보인다는 의미임.
		else
			return 0;
	}

	private void clearSelectedAll() {
		for (int i = 0; i < mCategoryArray.length; i++) {
			mCategory.getChildAt(i).setSelected(false);
		}
	}

	public String[] getCategoryArray() {
		return mCategoryArray;
	}

	// 기본 타이틀설정은 VodTvch와 Base 두 곳에 있음. (1)
	public void setTitle(int position) {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
		if (f != null)
			((VodTvchTopMenu) f).setTitle(mCategoryArray[position]);

		saveTitle(currentDepth, mCategoryArray[position]);
	}

	public void setTitle(String title) {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
		if (f != null)
			((VodTvchTopMenu) f).setTitle(title);

		saveTitle(currentDepth, title);
	}

	public void setTitle() {
		if (isSkipTitle) {
			String title = getTitle(--currentDepth);
			Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_vod_tvch_top_menu);
			if (f != null)
				((VodTvchTopMenu) f).setTitle(title);
		} else {
			isSkipTitle = false;
		}
	}

	public void saveTitle(int key, String title) {
		mMapTitle.put(key, title);
	}

	public String getTitle(int key) {
		return mMapTitle.get(key);
	}

	public void clearCurrentDepth() {
		currentDepth = 1;
		mMapTitle.clear();
	}

	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}

	@Override
	public void onSidebarOpenedStart() {
		// 현재 선택된 category를 clear...
		clearSelectedAll();

		// listview의 getChildAt()은 화면에 리스트가 보여야지만 view를 가져온다.
		// 보이지 않으면 null을 리턴한다.
		View selectedItem = mCategory.getChildAt(selectedCategory);
		if (selectedItem != null)
			selectedItem.setSelected(true);

		//mCategory.performItemClick(mCategory.getChildAt(selectedCategory), selectedCategory, mCategory.getAdapter().getItemId(selectedCategory));
	}

	@Override
	public void onSidebarOpenedEnd() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSidebarClosedStart() {
		if(isSidebarItemClick) {
			{
				Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.loading_data_panel);
				if (f != null)
					getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			}
	
			// ------------------------------------
			// 하단의 bottom menu에서 depth level 설정
			// onSidebarClosedEnd() > loadingDataForSidrebar()에서도 하는데 늦다...
			// ------------------------------------
			Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_rc_bottom_menu);
			if (f != null)
				((RcBottomMenu) f).setDepthLevelClear(); // set 1 depth
		}
	}

	@Override
	public void onSidebarClosedEnd() {
		mCategoryCover.setVisibility(View.GONE);

		// ---------------
		// data loading
		// ---------------
		if(isSidebarItemClick) loadingDataForSidrebar();
		isSidebarItemClick = false;
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

	OnBackStackChangedListener mRemoveOnBackStackChangedListener = new OnBackStackChangedListener() {
		@Override
		public void onBackStackChanged() {
			int i = getActivity().getSupportFragmentManager().getBackStackEntryCount();
			BackStackEntry tt = getActivity().getSupportFragmentManager().getBackStackEntryAt(i - 1);
			int checkedId = tt.getBreadCrumbTitleRes();
		}
	};

	OnBackStackChangedListener mAddOnBackStackChangedListener = new OnBackStackChangedListener() {
		@Override
		public void onBackStackChanged() {
			//int i = getActivity().getSupportFragmentManager().getBackStackEntryCount();
			//BackStackEntry tt = getSupportFragmentManager().getBackStackEntryAt(i - 1);
			//int checkedId = tt.getBreadCrumbTitleRes();
		}
	};

}
