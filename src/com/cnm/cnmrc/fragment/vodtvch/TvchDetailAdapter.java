package com.cnm.cnmrc.fragment.vodtvch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemTvchDetail;

// Custom Adapter
public class TvchDetailAdapter extends ArrayAdapter<ItemTvchDetail> {
	private String TAG = TvchDetailAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemTvchDetail> itemist;

    public TvchDetailAdapter(Context context, int layoutResId, ArrayList<ItemTvchDetail> arrayList) {
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
        
        // current time
        TextView currentTime = (TextView)row.findViewById(R.id.current_time);
        currentTime.setText(itemist.get(position).getCurrent_time());
        
        // current title
        TextView currentTitle = (TextView)row.findViewById(R.id.current_title);
        currentTitle.setText(" " + itemist.get(position).getCurrent_title());
        
        // hdicon
        ImageView hdiconResId = (ImageView)row.findViewById(R.id.hd_icon);
        hdiconResId.setBackgroundResource(itemist.get(position).getHdIconResId());
//        if(itemist.get(position).getHdIconResId() == 0) {
//        	hdiconResId.setVisibility(View.GONE);
//        }
        
        /*if(position == 0) {
        	((ListView)parent).performItemClick(row, 0, getItemId(0));
        }*/

        return row;
    }

    @Override
    public int getCount() {
        return itemist.size();
    }
    
}

