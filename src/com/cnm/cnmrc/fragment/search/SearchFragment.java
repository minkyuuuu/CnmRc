package com.cnm.cnmrc.fragment.search;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.util.Util;

public class SearchFragment extends Fragment implements View.OnClickListener {

	public static SearchFragment newInstance(String type) {
		SearchFragment f = new SearchFragment();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}
	
	View layout;
	
	public TextView mSearchTitle;
	
	EditText edit;
	RadioGroup radioGroup;
	RadioButton vod, tvch, naver;
	
	ListView 	mListView;
	String[] mArray = null;
	ArrayList<String> arrayList = null;
	
	FrameLayout mResultPanelFrameLlayout;
	public FrameLayout mDetilPanelFrameLlayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = (View) inflater.inflate(R.layout.fragment_search, container, false);
		
		mResultPanelFrameLlayout = (FrameLayout) layout.findViewById(R.id.result_panel);
		mResultPanelFrameLlayout.setVisibility(View.INVISIBLE);
		
		mDetilPanelFrameLlayout = (FrameLayout) layout.findViewById(R.id.detail_panel);
		mDetilPanelFrameLlayout.setVisibility(View.INVISIBLE);
		
		mSearchTitle = (TextView) layout.findViewById(R.id.search_title);
		
		// android:windowSoftInputMode="adjustPan"
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // not work
		
		edit = (EditText) layout.findViewById(R.id.search_edit);
		edit.requestFocus();
		
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	
		        	String str = v.getText().toString();
		        	if(str.equals("")) {
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
		
		// listview
		mListView   = (ListView) layout.findViewById(R.id.search_recently_listview);
		mArray= getActivity().getResources().getStringArray(R.array.search_recently_word);
        arrayList = new ArrayList<String>(mArray.length);
        for(String item: mArray) {
        	arrayList.add(item);
        }
        
		// -------------------
		// Search Result List
		// -------------------
        SearchRecentlyAdapter adapter = new SearchRecentlyAdapter(getActivity(), R.layout.list_item_search_recently, arrayList);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	TextView textView = (TextView) view.findViewById(R.id.recently_word);
            	edit.setText(textView.getText().toString());
            	//edit.setSelection(edit.getText().length());  // OK
            	
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
    	mResultPanelFrameLlayout.setVisibility(View.VISIBLE);
    	//Toast.makeText(getActivity(), "검색" + checkedId, Toast.LENGTH_SHORT).show();
    	
		switch (checkedId) {
		case R.id.search_vod:
			searchVod();
			break;
		case R.id.search_tvch:
			searchTvch();
			break;
		case R.id.search_naver:
			//naver.setSelected(true);
			break;
		}
		
	}

	private void searchVod() {
		SearchVod searchVod = new SearchVod();
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		//ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		//ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.result_panel, searchVod, "search_vod");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
	}
	
	public void showDetailVod() {
		mSearchTitle.setText("상세보기");
		mDetilPanelFrameLlayout.setVisibility(View.VISIBLE);
		
		SearchVodDetail searchVod = new SearchVodDetail();
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		//ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.detail_panel, searchVod, "search_vod_detail");
		ft.commit();
		getActivity().getSupportFragmentManager().executePendingTransactions();
	}
	
	private void searchTvch() {
		SearchTvch searchTvch = new SearchTvch();
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		
		//  ft.replace전에 animation을 설정해야 한다.
		//ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
		//ft.addToBackStack(null);	// fragment stack에 넣지 않으면 백키가 activity stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
		ft.replace(R.id.result_panel, searchTvch, "search_tvch");
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
		if (checkedId == R.id.search_tvch || checkedId == R.id.search_tvch) { 
			TextView word = (TextView) layout.findViewById(R.id.search_recently_word);
			word.setVisibility(View.GONE);
		} else {
			TextView word = (TextView) layout.findViewById(R.id.search_recently_word);
			word.setVisibility(View.VISIBLE);
		}
		
		
//		/**
//		 * show buttons according to card or cash 
//		 * card : 카드관리 and 할부조회
//		 * cash : 통장관리
//		 */
//		if (checkedId == R.id.button_segfirst) {	// 카드 지출
//			mCard.setVisibility(View.VISIBLE);
//			mCash.setVisibility(View.GONE);
//		} else { 									// 현금지출
//			mCard.setVisibility(View.GONE);
//			mCash.setVisibility(View.VISIBLE);
//		}
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
		mResultPanelFrameLlayout.setVisibility(View.INVISIBLE);
		mDetilPanelFrameLlayout.setVisibility(View.INVISIBLE);
		vod.setSelected(false);
		tvch.setSelected(false);
		naver.setSelected(false);
	}
	
	@Override
	public void onClick(View v) {
//		clearSelectedAll();
//		
//		switch (v.getId()) {
//		case R.id.search_vod:
//			v.setSelected(true);
//			break;
//		case R.id.search_tvch:
//			v.setSelected(true);
//			break;
//		case R.id.search_naver:
//			v.setSelected(true);
//			break;
//		}

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
	        	//getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
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
		// TODO Auto-generated method stub
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

}
