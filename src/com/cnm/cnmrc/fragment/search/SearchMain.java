package com.cnm.cnmrc.fragment.search;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.SearchRecentlyAdapter;
import com.cnm.cnmrc.popup.PopupSearchRecentlyDelete;
import com.cnm.cnmrc.provider.CnmRcContract.SearchWord;
import com.cnm.cnmrc.util.Util;

public class SearchMain extends Fragment implements View.OnClickListener {

	public static SearchMain newInstance(String type) {
		SearchMain f = new SearchMain();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	View layout;
	LinearLayout mNoClickBelowLayout;

	RelativeLayout mTitleLayout;

	TextView mSearchTitle;

	EditText edit;
	RadioGroup radioGroup;
	RadioButton vod, tvch, naver;

	ListView mListView;
	String[] mArray = null;
	ArrayList<String> mArrayList = null;
	SearchRecentlyAdapter mAdapter = null;

	FrameLayout mResultPanel;
	FrameLayout mDetilPanel;
	RelativeLayout mNoSearchWordPanel;
	boolean noSearchword = false;

	Button mSearchRecentlyDelete;
	Button mEditSearchIcon;
	Button mEditDeleteIcon;
	ImageButton mTopSearchIcon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = (View) inflater.inflate(R.layout.search_main, container, false);

		mNoClickBelowLayout = (LinearLayout) layout.findViewById(R.id.no_click_below_layout);
		mNoClickBelowLayout.setOnClickListener(this);

		mTitleLayout = (RelativeLayout) layout.findViewById(R.id.search_title_layout);
		mTitleLayout.setVisibility(View.GONE);
		mSearchTitle = (TextView) layout.findViewById(R.id.search_title);
		mTopSearchIcon = (ImageButton) layout.findViewById(R.id.top_search_icon);
		mTopSearchIcon.setOnClickListener(this);

		mResultPanel = (FrameLayout) layout.findViewById(R.id.search_result_panel);
		mResultPanel.setVisibility(View.INVISIBLE);

		mDetilPanel = (FrameLayout) layout.findViewById(R.id.detail_panel);
		mDetilPanel.setVisibility(View.INVISIBLE);

		mNoSearchWordPanel = (RelativeLayout) layout.findViewById(R.id.no_search_word);
		mNoSearchWordPanel.setVisibility(View.INVISIBLE);

		mSearchRecentlyDelete = (Button) layout.findViewById(R.id.search_recently_delete);
		mSearchRecentlyDelete.setOnClickListener(this);

		mEditSearchIcon = (Button) layout.findViewById(R.id.edit_search_icon);
		mEditSearchIcon.setOnClickListener(this);

		mEditDeleteIcon = (Button) layout.findViewById(R.id.edit_delete_icon);
		mEditDeleteIcon.setOnClickListener(this);
		mEditDeleteIcon.setVisibility(View.GONE);

		// android:windowSoftInputMode="adjustPan"
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// // not work

		edit = (EditText) layout.findViewById(R.id.search_edit);

		// 2013-12-04 처음 진입시 키보드가 안보이게...
		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Log.i("hwang", "edit focus changed!!!");
				if (hasFocus) {
					if (noSearchword) {
						if (mNoSearchWordPanel.getVisibility() == View.VISIBLE)
							mNoSearchWordPanel.setVisibility(View.GONE);
					}
				}
			}
		});

		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					String str = v.getText().toString();
					if (str.equals("")) {
						Toast.makeText(getActivity(), "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show();
					} else {
						performSearch();
					}

					return true;
				}
				return false;
			}

		});

		vod = (RadioButton) layout.findViewById(R.id.search_vod);
		vod.setOnClickListener(this);

		tvch = (RadioButton) layout.findViewById(R.id.search_tvch);
		tvch.setOnClickListener(this);

		naver = (RadioButton) layout.findViewById(R.id.search_naver);
		naver.setOnClickListener(this);

		// 처음에는 vod 버튼이 활성화되어 있음
		vod.setSelected(true);

		radioGroup = (RadioGroup) layout.findViewById(R.id.search_radio_group);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				changeViewList(checkedId);
			}
		});
		radioGroup.setVisibility(View.GONE);

		// listview
		mListView = (ListView) layout.findViewById(R.id.search_recently_listview);
		getActivity().getContentResolver().registerContentObserver(SearchWord.CONTENT_URI, true, observer);

		// ---------------------------------------------------------
		// 검색어가 있는지를 체크해서 하나도 없으면 "검색어가 없다는 문구"를 보이고
		// 있으면 리스트형태로 보여준다.
		// ---------------------------------------------------------
		Cursor cursor = getActivity().getContentResolver().query(SearchWord.CONTENT_URI, null, null, null, SearchWord.DEFAULT_SORT);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				mNoSearchWordPanel.setVisibility(View.GONE);
				noSearchword = false;
				mArrayList = new ArrayList<String>(cursor.getCount());
				while (cursor.moveToNext()) {
					mArrayList.add(cursor.getString(1));
				}
			} else {
				mArrayList = new ArrayList<String>(0);
				mNoSearchWordPanel.setVisibility(View.VISIBLE);
				noSearchword = true;
			}
			cursor.close();
		}

		// ----------------------
		// Search Recently List
		// ----------------------
		mAdapter = new SearchRecentlyAdapter(getActivity(), R.layout.list_item_search_recently, mArrayList);
		mListView.setAdapter(mAdapter);
		mListView.setDivider(null);
		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(R.id.recently_word);
				edit.setText(textView.getText().toString());
				// edit.setSelection(edit.getText().length()); // OK

				int length = edit.getText().length();
				Editable etext = edit.getText();
				Selection.setSelection(etext, length);

				//Util.showSoftKeyboard(getActivity(), edit);
			}

		});

		return layout;
	}

	private void performSearch() {
		int checkedId = radioGroup.getCheckedRadioButtonId();

		Util.hideSoftKeyboard(getActivity());
		mResultPanel.setVisibility(View.VISIBLE);
		radioGroup.setVisibility(View.VISIBLE);

		// 검색어를 db에 저장		
		ContentValues values = new ContentValues();
		values.put(SearchWord.SEARCHWORD_ID, edit.getText().toString());
		Uri uri = getActivity().getContentResolver().insert(SearchWord.CONTENT_URI, values);

		switch (checkedId) {
		case R.id.search_vod:
			searchVod();
			break;
		case R.id.search_tvch:
			searchTvch();
			break;
		case R.id.search_naver:
			searchNaver();
			break;
		}

	}

	private void searchVod() {
		String search = edit.getText().toString();
		SearchVodSemi searchVod = SearchVodSemi.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.setCustomAnimations(R.anim.fragment_entering, 0);
		// ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.search_result_panel, searchVod, "search_vod_semi");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
		Util.hideSoftKeyboard(getActivity());
	}

	public void showSearchVodDetail(Bundle bundle) {
		mTitleLayout.setVisibility(View.VISIBLE);
		mSearchTitle.setText("상세보기");
		mDetilPanel.setVisibility(View.VISIBLE);
		Util.hideSoftKeyboard(getActivity());

		SearchVodDetail searchVodDetail = SearchVodDetail.newInstance(bundle);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		// ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.detail_panel, searchVodDetail, "search_vod_detail");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
	}

	private void searchTvch() {
		String search = edit.getText().toString();
		SearchTvchSemi searchTvch = SearchTvchSemi.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.replace(R.id.search_result_panel, searchTvch, "search_tvch_semi");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
		Util.hideSoftKeyboard(getActivity());
	}

	private void searchNaver() {
		String search = edit.getText().toString();
		SearchNaver searchTvch = SearchNaver.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.replace(R.id.search_result_panel, searchTvch, "search_naver");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
		Util.hideSoftKeyboard(getActivity());
	}

	private void changeViewList(int checkedId) {

		/**
		 * change state of RadioButton
		 */
		changeRadioButtonState(checkedId);

		// test
		// 최근검색어 라인이 필요없는것 같다. 하단의 "최근 검색어 삭제" 버튼이 현재 리스트의 내용을 가리키고있다.
		TextView word = (TextView) layout.findViewById(R.id.search_recently_word);
		word.setVisibility(View.GONE);

	}

	private void changeRadioButtonState(int checkedId) {
		clearSelectedAll();

		switch (checkedId) {
		case R.id.search_vod:
			vod.setSelected(true);
			break;
		case R.id.search_tvch:
			tvch.setSelected(true);
			break;
		case R.id.search_naver:
			naver.setSelected(true);
			break;
		}
	}

	private void clearSelectedAll() {
		mResultPanel.setVisibility(View.INVISIBLE);
		mDetilPanel.setVisibility(View.INVISIBLE);
		vod.setSelected(false);
		tvch.setSelected(false);
		naver.setSelected(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_recently_delete:
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			PopupSearchRecentlyDelete searchRecentlyDelete = new PopupSearchRecentlyDelete();
			searchRecentlyDelete.show(ft, PopupSearchRecentlyDelete.class.getSimpleName());
			break;
		case R.id.edit_search_icon:
			Log.i("hwang", "clicked search input icon");
			if (edit.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show();
			} else {
				performSearch();
			}
			break;
		case R.id.edit_delete_icon:
			break;
		case R.id.top_search_icon:
			resetAll();
		case R.id.search_edit:
			if (mNoSearchWordPanel.getVisibility() == View.VISIBLE)
				mNoSearchWordPanel.setVisibility(View.GONE);
			break;
		}

	}

	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_SEARCH);
			if (f != null) {
				// commit을 해도되구... 안해도되구...
				// getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {

		super.onActivityCreated(arg0);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onDestroy() {
		getActivity().getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

	public void resetAll() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (f != null) {
			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}

		radioGroup.setVisibility(View.GONE);
		mTitleLayout.setVisibility(View.GONE);
		edit.setText("");
		mResultPanel.setVisibility(View.INVISIBLE);
	}

	public void resetTitle() {
		mTitleLayout.setVisibility(View.INVISIBLE);
	}

	// 해당 데이터베이스의 데이터값이 변경시에 onChange() method가 호출됩니다.
	private ContentObserver observer = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			Log.d("hwang", "onChanged : " + selfChange);
			
			// db
			Cursor cursor = getActivity().getContentResolver().query(SearchWord.CONTENT_URI, null, null, null, SearchWord.DEFAULT_SORT);
			if (cursor != null) {
				if (cursor.getCount() > 0) {
					mArrayList.clear();
					noSearchword = false;
					while (cursor.moveToNext()) {
						mArrayList.add(cursor.getString(1));
					}
				} else {
					mArrayList.clear();
					noSearchword = true;
				}
				cursor.close();
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(0);
				
			}
			
		}
	};

}
