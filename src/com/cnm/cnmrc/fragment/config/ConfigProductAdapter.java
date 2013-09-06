package com.cnm.cnmrc.fragment.config;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.parser.ChannelProduct;

// Custom Adapter
public class ConfigProductAdapter extends ArrayAdapter<ChannelProduct> {
	
    private Activity context;
    private int layoutResId;
    
    private ArrayList<ChannelProduct> itemList;
    
    public ConfigProductAdapter(Context context, int layoutResId, ArrayList<ChannelProduct> arrayList) {
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
        
        // product
        TextView product = (TextView)row.findViewById(R.id.config_product);
        String productText = itemList.get(position).getProductName();
        product.setText(productText);
        
        // radio button
        // RadioButton radio = (RadioButton)row.findViewById(R.id.config_product_radio);
        
        return row;
    }
    
    @Override
    public int getCount() {
        return itemList.size();
    }
    
}

