package com.cnm.cnmrc.fragment.search;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.cnm.cnmrc.fragment.popup.PopupSearchRecentlyDelete;
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
	ArrayList<String> arrayList = null;

	FrameLayout mResultPanelFrameLayout;
	FrameLayout mDetilPanelFrameLayout;

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

		mResultPanelFrameLayout = (FrameLayout) layout.findViewById(R.id.search_result_panel);
		mResultPanelFrameLayout.setVisibility(View.INVISIBLE);

		mDetilPanelFrameLayout = (FrameLayout) layout.findViewById(R.id.detail_panel);
		mDetilPanelFrameLayout.setVisibility(View.INVISIBLE);

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
		//edit.requestFocus();

		edit.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				Log.i("hwang", "edit focus changed!!!");
				if (hasFocus) {
					Util.showSoftKeyboard(getActivity(), edit);
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
		mArray = getActivity().getResources().getStringArray(R.array.search_recently_word);
		arrayList = new ArrayList<String>(mArray.length);
		for (String item : mArray) {
			arrayList.add(item);
		}

		// ----------------------
		// Search Recently List
		// ----------------------
		SearchRecentlyAdapter adapter = new SearchRecentlyAdapter(getActivity(), R.layout.list_item_search_recently, arrayList);
		mListView.setAdapter(adapter);
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

				Util.showSoftKeyboard(getActivity(), edit);
			}

		});

		return layout;
	}

	private void performSearch() {
		int checkedId = radioGroup.getCheckedRadioButtonId();
		Util.hideSoftKeyboard(getActivity());
		mResultPanelFrameLayout.setVisibility(View.VISIBLE);
		radioGroup.setVisibility(View.VISIBLE);
		// Toast.makeText(getActivity(), "검색" + checkedId,
		// Toast.LENGTH_SHORT).show();

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
		SearchVodSub searchVod = SearchVodSub.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.setCustomAnimations(R.anim.fragment_entering, 0);
		// ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.search_result_panel, searchVod, "search_vod");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
	}

	public void showDetailVod(Bundle bundle) {
		mTitleLayout.setVisibility(View.VISIBLE);
		mSearchTitle.setText("상세보기");
		mDetilPanelFrameLayout.setVisibility(View.VISIBLE);
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
		SearchTvchSub searchTvch = SearchTvchSub.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.replace(R.id.search_result_panel, searchTvch, "search_tvch");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
	}

	private void searchNaver() {
		String search = edit.getText().toString();
		SearchNaverSub searchTvch = SearchNaverSub.newInstance(search);
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		// ft.replace전에 animation을 설정해야 한다.
		ft.replace(R.id.search_result_panel, searchTvch, "search_naver");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
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
		/*
		 * if (checkedId == R.id.search_tvch || checkedId == R.id.search_naver)
		 * { TextView word = (TextView)
		 * layout.findViewById(R.id.search_recently_word);
		 * word.setVisibility(View.GONE); } else { TextView word = (TextView)
		 * layout.findViewById(R.id.search_recently_word);
		 * word.setVisibility(View.VISIBLE); }
		 */

		// /**
		// * show buttons according to card or cash
		// * card : 카드관리 and 할부조회
		// * cash : 통장관리
		// */
		// if (checkedId == R.id.button_segfirst) { // 카드 지출
		// mCard.setVisibility(View.VISIBLE);
		// mCash.setVisibility(View.GONE);
		// } else { // 현금지출
		// mCard.setVisibility(View.GONE);
		// mCash.setVisibility(View.VISIBLE);
		// }
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
		mResultPanelFrameLayout.setVisibility(View.INVISIBLE);
		mDetilPanelFrameLayout.setVisibility(View.INVISIBLE);
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

	public void resetAll() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("search_vod_detail");
		if (f != null) {
			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}

		radioGroup.setVisibility(View.GONE);
		mTitleLayout.setVisibility(View.GONE);
		edit.setText("");
		mResultPanelFrameLayout.setVisibility(View.INVISIBLE);
	}

	public void resetTitle() {
		mTitleLayout.setVisibility(View.INVISIBLE);
	}

}
