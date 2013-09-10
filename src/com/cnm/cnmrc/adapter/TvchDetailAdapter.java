package com.cnm.cnmrc.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemTvchDetail;
import com.cnm.cnmrc.util.ImageDownloader;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class TvchDetailAdapter extends ArrayAdapter<ItemTvchDetail> {
	private String TAG = TvchDetailAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemTvchDetail> itemList;
    
    private ImageDownloader imageDownloader;

    public TvchDetailAdapter(Context context, int layoutResId, ArrayList<ItemTvchDetail> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
        
        imageDownloader = new ImageDownloader();
        imageDownloader.setContext(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
    	final ViewHolder holder;
        
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResId, null);
            
            holder = new ViewHolder();
            holder.broadcastingTime = (TextView)convertView.findViewById(R.id.broadcasting_time);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.hdIcon = (ImageView)convertView.findViewById(R.id.hd_icon);
            holder.gradeIcon = (ImageView)convertView.findViewById(R.id.grade_icon);
            
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        // broadcastin time
        if(itemList.get(position).getBroadcastingTime() != null) {
			Date date = Util.getFromStringToDate1(itemList.get(position).getBroadcastingTime());
			if (date == null) {
				holder.broadcastingTime.setText(" ");
			} else {
				holder.broadcastingTime.setText(Util.getHHmm(date) + " ");
			}
        }
        
        // title
        if(itemList.get(position).getTitle() != null)
        	holder.title.setText(itemList.get(position).getTitle());
        
        // hd icon
        holder.hdIcon.setBackgroundResource(R.drawable.hd_icon);
        holder.hdIcon.setVisibility(View.GONE);
        if(itemList.get(position).getHd() != null) {
	        if(itemList.get(position).getHd().equalsIgnoreCase("yes")) {
	        	holder.hdIcon.setVisibility(View.VISIBLE);
	        }
        }
        
        // grade icon
        if(itemList.get(position).getGrade() != null)
        	holder.gradeIcon.setBackgroundResource(Util.getGrade(itemList.get(position).getGrade()));
        
        
        return convertView;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
	class ViewHolder {
		TextView broadcastingTime;
		TextView title;
		ImageView hdIcon;
		ImageView gradeIcon;
	}

    
}

