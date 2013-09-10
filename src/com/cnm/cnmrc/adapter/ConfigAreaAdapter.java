package com.cnm.cnmrc.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemChannelArea;
import com.cnm.cnmrc.item.ItemChannelAreaList;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class ConfigAreaAdapter extends ArrayAdapter<ItemChannelArea> {
	private String TAG = ConfigAreaAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;
    
    int selectedIndex = 0;

    private ArrayList<ItemChannelArea> itemList;

    public ConfigAreaAdapter(Context context, int layoutResId, ArrayList<ItemChannelArea> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
        } 
        
        // area
        TextView area = (TextView)row.findViewById(R.id.config_area);
        String areaText = itemList.get(position).getAreaName();
        area.setText(areaText);
        
        // gu
        TextView gu = (TextView)row.findViewById(R.id.config_area_gu);
        gu.setText(itemList.get(position).getAreaNameDetail());
        if(gu.getText().equals("") || gu.getText().equals(null)) gu.setVisibility(View.GONE);
        
        // radio button
        RadioButton radio = (RadioButton)row.findViewById(R.id.config_area_radio);
       
        // 결국 Adapter안에서 원하는 특정(기본 송파) 아이템을 setChecked하면 안된다. 
		// adapter가 만들어지는 시점에서 즉 통신이 끝난후에 checked해야한다.
        /*if(Util.getChannelAreaName(context).equals(regionText)) {
        	radio.setChecked(true);
        }*/
        
//		if (selectedIndex == position) {
//			radio.setChecked(true);
//		} else {
//			radio.setChecked(false);
//		}
        
        return row;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
    
}

