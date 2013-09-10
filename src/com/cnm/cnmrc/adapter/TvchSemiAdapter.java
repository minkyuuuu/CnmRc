package com.cnm.cnmrc.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.item.ItemTvchSemi;
import com.cnm.cnmrc.util.ImageDownloader;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class TvchSemiAdapter extends ArrayAdapter<ItemTvchSemi> {

	private Activity context;
	private int layoutResId;
	private ArrayList<ItemTvchSemi> itemList;
	private ImageDownloader imageDownloader;

	public TvchSemiAdapter(Context context, int layoutResId, ArrayList<ItemTvchSemi> arrayList) {
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
		
		//if(row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResId, null);
			
			// 채널 번호
			TextView number = (TextView) row.findViewById(R.id.number);
			if (itemList.get(position).getNumber() != null)
				number.setText(itemList.get(position).getNumber());
			
			// logo image
			ImageView logoImg = (ImageView) row.findViewById(R.id.logo_img);
			String str = itemList.get(position).getLogoImg();
			if (str != null && !str.equals("")) {
				Bitmap loadBitmap = Util.BitmapLoadFromFile(context, str);	
				if(loadBitmap == null) {
					imageDownloader.download(str, logoImg, null);
				} else {
					logoImg.setImageBitmap(loadBitmap);	// use disk cache
				}
				
//			String str1 = "[%s] : image path ---> %s";
//		 	str1 = String.format(str1, position, itemList.get(position).getLogoImg());
//			Log.v("hwang",str1);
//			if(str.equals("")) {
//				Log.v("hwang","\"\"");
//			} else {
//				imageDownloader.download(itemList.get(position).getLogoImg().trim(), logoImg, null);
//			}
			}
			
			// onAir Time
			TextView onAirTime = (TextView) row.findViewById(R.id.onair_time);
			if (itemList.get(position).getProgramOnAirTime() != null) {
				Date date = Util.getFromStringToDate1(itemList.get(position).getProgramOnAirTime());
				if (date == null) {
					onAirTime.setText(" ");
				} else {
					onAirTime.setText(Util.getHHmm(date) + " ");
				}
			}
			
			// onAir Title
			TextView onAirTitle = (TextView) row.findViewById(R.id.onair_title);
			if (itemList.get(position).getProgramOnAirTitle() != null)
				onAirTitle.setText(" " + itemList.get(position).getProgramOnAirTitle());
			
			// onAir Time
			TextView nextTime = (TextView) row.findViewById(R.id.next_time);
			if (itemList.get(position).getProgramNextTime() != null) {
				Date date = Util.getFromStringToDate1(itemList.get(position).getProgramNextTime());
				if (date == null) {
					nextTime.setText(" ");
				} else {
					nextTime.setText(Util.getHHmm(date) + " ");
				}
			}
			
			// onAir Title
			TextView nextTitle = (TextView) row.findViewById(R.id.next_title);
			if (itemList.get(position).getProgramNextTitle() != null)
				nextTitle.setText(" " + itemList.get(position).getProgramNextTitle());
			
		//}



		return row;
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

}
