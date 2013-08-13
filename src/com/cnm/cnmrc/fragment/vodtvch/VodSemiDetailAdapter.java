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
import com.cnm.cnmrc.data.ItemTvchSemiDetail;
import com.cnm.cnmrc.data.ItemVodSemiDetail;

// Custom Adapter
public class VodSemiDetailAdapter extends ArrayAdapter<ItemVodSemiDetail> {
	private String TAG = VodSemiDetailAdapter.class.getSimpleName();
	
    private Activity context;
    private int layoutResId;

    private ArrayList<ItemVodSemiDetail> itemist;

    public VodSemiDetailAdapter(Context context, int layoutResId, ArrayList<ItemVodSemiDetail> arrayList) {
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
        
        // 상단의 delimiter는 최초 한번만 보여준다.
        ImageView topDelimiter = (ImageView)row.findViewById(R.id.top_delimiter);
        if (position == 0) {
        	topDelimiter.setVisibility(View.VISIBLE);
        } else {
        	topDelimiter.setVisibility(View.GONE);
        }
        
        // 좌측의 큰 이미지 포스터
        ImageView titleResId = (ImageView)row.findViewById(R.id.title_res_id);
        titleResId.setBackgroundResource(itemist.get(position).getTitleResId());
        
        
        // title
        TextView title = (TextView)row.findViewById(R.id.title);
        title.setText(itemist.get(position).getTitle());
        
        // hdicon
        ImageView hdiconResId = (ImageView)row.findViewById(R.id.hdicon_res_id);
        hdiconResId.setBackgroundResource(itemist.get(position).getHdIconResId());
        if(itemist.get(position).getHdIconResId() == 0) {
        	hdiconResId.setVisibility(View.GONE);
        }
        
        // age icon
        ImageView ageResId = (ImageView)row.findViewById(R.id.age_res_id);
        ageResId.setBackgroundResource(itemist.get(position).getAgeResId());
        
        // diretor name
        TextView director = (TextView)row.findViewById(R.id.director);
        director.setText(itemist.get(position).getDirector());
        TextView directorName = (TextView)row.findViewById(R.id.director_name);
        directorName.setText(itemist.get(position).getDirector_name());
        
        // cast name
        TextView cast = (TextView)row.findViewById(R.id.cast);
        cast.setText(itemist.get(position).getCast());
        TextView castName = (TextView)row.findViewById(R.id.cast_name);
        castName.setText(itemist.get(position).getCast_name());
        
        
        try {
        	title.setText(itemist.get(position).getTitle());
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
        return itemist.size();
    }
    
}

