package com.cnm.cnmrc.config;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.CheckableLinearLayout;
import com.cnm.cnmrc.data.ItemConfigProduct;

// Custom Adapter
public class ConfigProductAdapter extends ArrayAdapter<ItemConfigProduct> {
	private String TAG = ConfigProductAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;
    
    private ArrayList<ItemConfigProduct> itemist;
    
    int selectedIndex = 0;

    public ConfigProductAdapter(Context context, int layoutResId, ArrayList<ItemConfigProduct> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemist = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
        } 
        
        // product
        TextView region = (TextView)row.findViewById(R.id.config_product);
        region.setText(itemist.get(position).getProduct());
        
        // radio button
        RadioButton radio = (RadioButton)row.findViewById(R.id.config_product_radio);
//		if (selectedIndex == position) {
//			radio.setChecked(true);
//		} else {
//			radio.setChecked(false);
//		}
		
//		if (selectedIndex == position) {
//			((CheckableLinearLayout) row).setChecked(true);
//		}
        
        return row;
    }
    
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    @Override
    public int getCount() {
        return itemist.size();
    }
    
}

