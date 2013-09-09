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
import com.cnm.cnmrc.item.ItemTvchSemiDetail;

// Custom Adapter
public class TvchSemiAdapter extends ArrayAdapter<ItemTvchSemiDetail> {
	private String TAG = TvchSemiAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemTvchSemiDetail> itemist;

    public TvchSemiAdapter(Context context, int layoutResId, ArrayList<ItemTvchSemiDetail> arrayList) {
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
        
        // channel no
        TextView channelNo = (TextView)row.findViewById(R.id.channel_no);
        channelNo.setText(Integer.toString(itemist.get(position).getChannel_no()));
        
        // channel icon
        ImageView tvchIcon = (ImageView)row.findViewById(R.id.tvchicon_res_id);
        tvchIcon.setBackgroundResource(itemist.get(position).getTvchIcon());
        
        // current time
        TextView currentTime = (TextView)row.findViewById(R.id.current_time);
        currentTime.setText(itemist.get(position).getCurrent_time());
        
        // current title
        TextView currentTitle = (TextView)row.findViewById(R.id.current_title);
        currentTitle.setText(" " + itemist.get(position).getCurrent_title());
        
        // next time
        TextView nextTime = (TextView)row.findViewById(R.id.next_time);
        nextTime.setText(itemist.get(position).getNext_time());
        
        // next title
        TextView nextTitle = (TextView)row.findViewById(R.id.next_title);
        nextTitle.setText(" " + itemist.get(position).getNext_title());
        
        
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

