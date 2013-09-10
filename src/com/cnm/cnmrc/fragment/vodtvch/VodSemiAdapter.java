package com.cnm.cnmrc.fragment.vodtvch;

import java.util.ArrayList;

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
import com.cnm.cnmrc.item.ItemVodSemi;
import com.cnm.cnmrc.util.ImageDownloader;
import com.cnm.cnmrc.util.Util;

// Custom Adapter
public class VodSemiAdapter extends ArrayAdapter<ItemVodSemi> {
	private String TAG = VodSemiAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemVodSemi> itemList;
    
    private ImageDownloader imageDownloader;

    public VodSemiAdapter(Context context, int layoutResId, ArrayList<ItemVodSemi> arrayList) {
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
            holder.topDelimiter = (ImageView)convertView.findViewById(R.id.top_delimiter);
            holder.logoImg = (ImageView)convertView.findViewById(R.id.logo_img);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.hdIcon = (ImageView)convertView.findViewById(R.id.hd_icon);
            holder.gradeIcon = (ImageView)convertView.findViewById(R.id.grade_icon);
            holder.director = (TextView)convertView.findViewById(R.id.director);
            holder.directorName = (TextView)convertView.findViewById(R.id.director_name);
            holder.actor = (TextView)convertView.findViewById(R.id.actor);
            holder.actorName = (TextView)convertView.findViewById(R.id.actor_name);
            
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        // 상단의 delimiter는 최초 한번만 보여준다.
        if (position == 0) {
        	holder.topDelimiter.setVisibility(View.VISIBLE);
        } else {
        	holder.topDelimiter.setVisibility(View.GONE);
        }
        
        // 좌측의 큰 이미지 포스터
        //titleResId.setImageBitmap(Util.getNoBitmap(context));
        String str = itemList.get(position).getImg();
        if(str != null && !str.equals("")) {
        	Bitmap loadBitmap = Util.BitmapLoadFromFile(context, itemList.get(position).getImg());	
        	if(loadBitmap == null) {
	        	String grade = itemList.get(position).getGrade();
	        	if(grade != null) {
	        		if(Util.isAdultGrade(grade)) holder.logoImg.setImageBitmap(Util.getAdultBitmap(context));
	        		else imageDownloader.download(itemList.get(position).getImg().trim(), holder.logoImg, Util.getNoBitmap(context));
	        	}	// 기본적으로 no image 포스터가 적용되어 있다.
        	} else 
        		holder.logoImg.setImageBitmap(loadBitmap);	// use disk cache
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
        
        // diretor name
        holder.director.setText("감독 : ");
        if(itemList.get(position).getDirector() != null)
        	holder.directorName.setText(itemList.get(position).getDirector());
        
        // actor name
        holder.actor.setText("출연 : ");
        if(itemList.get(position).getActor() != null)
        	holder.actorName.setText(itemList.get(position).getActor());
        
        return convertView;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }
    
	class ViewHolder {
		ImageView topDelimiter;
		ImageView logoImg;
		TextView title;
		ImageView hdIcon;
		ImageView gradeIcon;
		TextView director;
		TextView directorName;
		TextView actor;
		TextView actorName;
	}

    
}

