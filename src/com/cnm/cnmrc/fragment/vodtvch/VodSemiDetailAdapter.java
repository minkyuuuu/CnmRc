package com.cnm.cnmrc.fragment.vodtvch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.http.SearchVod;
import com.cnm.cnmrc.item.ItemVodSemiDetailxxx;

// Custom Adapter
public class VodSemiDetailAdapter extends ArrayAdapter<SearchVod> {
	private String TAG = VodSemiDetailAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<SearchVod> itemList;

    public VodSemiDetailAdapter(Context context, int layoutResId, ArrayList<SearchVod> arrayList) {
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
        
        // 상단의 delimiter는 최초 한번만 보여준다.
        ImageView topDelimiter = (ImageView)row.findViewById(R.id.top_delimiter);
        if (position == 0) {
        	topDelimiter.setVisibility(View.VISIBLE);
        } else {
        	topDelimiter.setVisibility(View.GONE);
        }
        
        // 좌측의 큰 이미지 포스터
        ImageView titleResId = (ImageView)row.findViewById(R.id.title_res_id);
        titleResId.setBackgroundResource(R.drawable.mister_go);
        
        // title
        TextView title = (TextView)row.findViewById(R.id.title);
        title.setText(itemList.get(position).getTitle());
        
        // hdicon
        ImageView hdiconResId = (ImageView)row.findViewById(R.id.hdicon_res_id);
        hdiconResId.setBackgroundResource(R.drawable.hdicon);
//        if(itemist.get(position).getHdIconResId() == 0) {
//        	hdiconResId.setVisibility(View.GONE);
//        }
        
        // age icon
        ImageView ageResId = (ImageView)row.findViewById(R.id.age_res_id);
        ageResId.setBackgroundResource(R.drawable.age15);
        
        // diretor name
        TextView director = (TextView)row.findViewById(R.id.director);
        director.setText("감독 : ");
        TextView directorName = (TextView)row.findViewById(R.id.director_name);
        directorName.setText(itemList.get(position).getDirector());
        
        // cast name
        TextView cast = (TextView)row.findViewById(R.id.cast);
        cast.setText("출연 : ");
        TextView castName = (TextView)row.findViewById(R.id.cast_name);
        castName.setText(itemList.get(position).getActor());
        
        
        try {
        	title.setText(itemList.get(position).getTitle());
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
        return itemList.size();
    }
    
}

