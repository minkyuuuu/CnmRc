package com.cnm.cnmrc.fragment.vodtvch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemVodList;

// Custom Adapter
public class VodListAdapter extends ArrayAdapter<ItemVodList> {
	private String TAG = VodListAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemVodList> itemList;

    public VodListAdapter(Context context, int layoutResId, ArrayList<ItemVodList> arrayList) {
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
        
        TextView title = (TextView)row.findViewById(R.id.title);
        title.setText(itemList.get(position).getTitle());

        return row;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
}

