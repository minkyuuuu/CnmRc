package com.cnm.cnmrc.fragment.config;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.parser.ChannelProduct;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class ConfigProductAdapter extends ArrayAdapter<ChannelProduct> {
	private String TAG = ConfigProductAdapter.class.getSimpleName();
	
    private Activity context;
    private Fragment fragment;
    private int layoutResId;
    
    private ArrayList<ChannelProduct> itemList;
    
    int selectedIndex = 0;

    public ConfigProductAdapter(Context context, int layoutResId, ArrayList<ChannelProduct> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
        fragment = ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("config_area");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        //if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
        //} 
        
        // product
        TextView product = (TextView)row.findViewById(R.id.config_product);
        String productText = itemList.get(position).getProductName();
        product.setText(productText);
        
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
        
 
        //row.setId(position);
        

        
        return row;
    }
    
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
}

