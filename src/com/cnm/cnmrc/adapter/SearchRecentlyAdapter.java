package com.cnm.cnmrc.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cnm.cnmrc.R;

// Custom Adapter
public class SearchRecentlyAdapter extends ArrayAdapter<String> {
	private String TAG = SearchRecentlyAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<String> arrayist;

    public SearchRecentlyAdapter(Context context, int layoutResId, ArrayList<String> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.arrayist = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
        } 
        
        TextView vod = (TextView)row.findViewById(R.id.recently_word);

        try {
        	vod.setText(arrayist.get(position));
        } catch (Exception e) {
            Log.e(TAG, "sever throw null point error <----");
        }
        
        /*if(position == 0) {
        	((ListView)parent).performItemClick(row, 0, getItemId(0));
        }*/

        return row;
    }

    @Override
    public int getCount() {
        return arrayist.size();
    }
    
}

