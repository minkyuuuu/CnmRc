package com.cnm.cnmrc.fragment.config;

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
import com.cnm.cnmrc.parser.ChannelArea;
import com.cnm.cnmrc.parser.ChannelAreaList;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class ConfigAreaAdapter extends ArrayAdapter<ChannelArea> {
	private String TAG = ConfigAreaAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;
    
    int selectedIndex = 0;

    private ArrayList<ChannelArea> itemList;

    public ConfigAreaAdapter(Context context, int layoutResId, ArrayList<ChannelArea> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        //if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
        //} 
        
        // region
        TextView region = (TextView)row.findViewById(R.id.config_region);
        String regionText = itemList.get(position).getAreaName();
        region.setText(regionText);
        
        // gu
        TextView gu = (TextView)row.findViewById(R.id.config_region_gu);
        gu.setText(itemList.get(position).getAreaNameDetail());
        if(gu.getText().equals("") || gu.getText().equals(null)) gu.setVisibility(View.GONE);
        
        // radio button
        RadioButton radio = (RadioButton)row.findViewById(R.id.config_area_radio);
        
        if(Util.getChannelAreaName(context).equals(regionText)) {
        	radio.setChecked(true);
        }
        
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

