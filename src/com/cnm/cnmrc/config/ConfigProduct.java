package com.cnm.cnmrc.config;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.data.ItemConfigProduct;

public class ConfigProduct extends Fragment implements View.OnClickListener {

	public static ConfigProduct newInstance(String type) {
		ConfigProduct f = new ConfigProduct();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout mConfigProduct; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	ListView mListView;
	ArrayList<ItemConfigProduct> arrayList = null;
	ConfigProductAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_product, container, false);

		mConfigProduct = (LinearLayout) layout.findViewById(R.id.config_product);
		mConfigProduct.setOnClickListener(this);

		// listview
		mListView = (ListView) layout.findViewById(R.id.config_product_listview);

		// make item data
		arrayList = new ArrayList<ItemConfigProduct>(20);
		for (int i = 0; i < 20; i++) {
			ItemConfigProduct item = new ItemConfigProduct();

			item.setProduct("HD실속형");

			arrayList.add(item);
		}

		// -------------------
		// region
		// -------------------
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		adapter = new ConfigProductAdapter(getActivity(), R.layout.list_item_config_product, arrayList);
		mListView.setAdapter(adapter);
		mListView.setDivider(null);
		mListView.setDividerHeight(0);
		mListView.setItemChecked(3, true);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				RadioButton radio = (RadioButton) view.findViewById(R.id.config_product_radio);
//				radio.setChecked(true);

//				adapter.setSelectedIndex(position);
//				adapter.notifyDataSetChanged();
				
				((ListView) parent).setItemChecked(position, true);  // OK
			}

		});

		return layout;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
			break;
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
