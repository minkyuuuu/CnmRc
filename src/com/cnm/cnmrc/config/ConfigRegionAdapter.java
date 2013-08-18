package com.cnm.cnmrc.config;

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
import com.cnm.cnmrc.data.ItemConfigRegion;

// Custom Adapter
public class ConfigRegionAdapter extends ArrayAdapter<ItemConfigRegion> {
	private String TAG = ConfigRegionAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;
    
    int selectedIndex = 6;

    private ArrayList<ItemConfigRegion> itemist;

    public ConfigRegionAdapter(Context context, int layoutResId, ArrayList<ItemConfigRegion> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemist = arrayList;
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
        region.setText(itemist.get(position).getRegion());
        
        // gu
        TextView gu = (TextView)row.findViewById(R.id.config_region_gu);
        gu.setText(itemist.get(position).getGu());
        
        // radio button
        RadioButton radio = (RadioButton)row.findViewById(R.id.config_region_radio);
//        if(position == 0) {
//        	radio.setChecked(true);
//        	
//        }
        
		if (selectedIndex == position) {
			radio.setChecked(true);
		} else {
			radio.setChecked(false);
		}
        
        return row;
    }

    @Override
    public int getCount() {
        return itemist.size();
    }
    
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }
    
}

