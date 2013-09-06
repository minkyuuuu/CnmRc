package com.cnm.cnmrc.fragment.search;

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
import com.cnm.cnmrc.parser.SearchProgram;
import com.cnm.cnmrc.util.ImageDownloader;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class SearchTvchSemiAdapter extends ArrayAdapter<SearchProgram> {
	private String TAG = SearchTvchSemiAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<SearchProgram> itemList;
    
    private ImageDownloader imageDownloader;

    public SearchTvchSemiAdapter(Context context, int layoutResId, ArrayList<SearchProgram> arrayList) {
        super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
        
        imageDownloader = new ImageDownloader();
        imageDownloader.setContext(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        
        //if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResId, null);
            
            // serial number (일련 번호)
            TextView channelNo = (TextView)row.findViewById(R.id.channel_no);
            channelNo.setText(Integer.toString(position));
            
            // channel logo
            ImageView channelLogo = (ImageView)row.findViewById(R.id.channel_logo);
            if(itemList.get(position).getLogoImg() != null) 
            	imageDownloader.download(itemList.get(position).getLogoImg().trim(), channelLogo);
            
            // channel number
            TextView channelNumber = (TextView)row.findViewById(R.id.channel_number);
            if(itemList.get(position).getNumber() != null) 
            	channelNumber.setText("ch." + itemList.get(position).getNumber());
            
            // program title
            TextView programTitle = (TextView)row.findViewById(R.id.program_title);
            if(itemList.get(position).getProgramTitle() != null) 
            	programTitle.setText(" " + itemList.get(position).getProgramTitle());
            
            // program time
            TextView programTime = (TextView)row.findViewById(R.id.program_time);
            if(itemList.get(position).getProgramTime() != null) 
            	programTime.setText(itemList.get(position).getProgramTime());
        //} 
        
        return row;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
}
