package com.cnm.cnmrc.fragment.vodtvch;

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
public class VodTvchAdapter extends ArrayAdapter<String> {
    private static final String TAG = VodTvchAdapter.class.getSimpleName();
    
    private Activity context;

    private int layoutResourceId;

    private ArrayList<String> categoryList;

    public VodTvchAdapter(Context context, int layoutResourceId, ArrayList<String> categoryList) {
        super(context, layoutResourceId, categoryList);
        this.layoutResourceId = layoutResourceId;
        this.context = (Activity) context;
        this.categoryList = categoryList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyCourseHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, null);

            holder = new MyCourseHolder();
            holder.subject = (TextView)row.findViewById(R.id.category);

            row.setTag(holder);
        } else {
            holder = (MyCourseHolder)row.getTag();
        }

        try {
            holder.subject.setText(categoryList.get(position));
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
        return categoryList.size();
    }
    
    class MyCourseHolder {
        TextView subject;
    }
}

