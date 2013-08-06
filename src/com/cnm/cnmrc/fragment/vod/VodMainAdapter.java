package com.cnm.cnmrc.fragment.vod;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnm.cnmrc.R;

// Custom Adapter
public class VodMainAdapter extends ArrayAdapter<String> {
    private static final String TAG = VodMainAdapter.class.getSimpleName();
    
    private Context context;

    private int layoutResourceId;

    private ArrayList<String> subjectList;

    public VodMainAdapter(Context context, int layoutResourceId, ArrayList<String> subjectList) {
        super(context, layoutResourceId, subjectList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.subjectList = subjectList;
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
//            holder.subject = (CustomTextViewForListView)row.findViewById(R.id.subject);

            row.setTag(holder);
        } else {
            holder = (MyCourseHolder)row.getTag();
        }

        try {
            holder.subject.setText(subjectList.get(position));
            //holder.subject.setOnClickListener((OnClickListener)context);
            
        } catch (Exception e) {
            Log.e(TAG, "sever throw null point error <----");
        }

        return row;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }
    
    static class MyCourseHolder {
        TextView subject;
//        CustomTextViewForListView subject;
    }
}

