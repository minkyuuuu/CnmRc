package com.cnm.cnmrc.fragment.search;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.parser.SearchNaver;

public class SearchNaverAdapter extends ArrayAdapter<SearchNaver> {

	private Activity context;
    private int layoutResId;
    private ArrayList<SearchNaver> itemList;

	public SearchNaverAdapter(Context context, int layoutResId, ArrayList<SearchNaver> arrayList) {
		super(context, layoutResId, arrayList);
        this.layoutResId = layoutResId;
        this.context = (Activity) context;
        this.itemList = arrayList;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResId, null);
        
        // serial number (일련 번호)
        TextView index = (TextView)row.findViewById(R.id.index);
        index.setText(Integer.toString(position));
        
        // serial number (일련 번호)
        TextView title = (TextView)row.findViewById(R.id.title);
        //Pattern p = Pattern.compile("<(?:.|\\s)*?>");
        Pattern p = Pattern.compile("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
        
        Matcher m = p.matcher(itemList.get(position).getTitle());
        m.replaceAll("");
        
        String result = ReplaceString(m.replaceAll(""), "&amp;", "&");
        result = ReplaceString(result, "&nbsp;", " ");
        result = ReplaceString(result, "&quot;", "\"");
        result = ReplaceString(result, "&apos;", "'");
        result = ReplaceString(result, "&lt;", "<");
        result = ReplaceString(result, "&gt;", ">");
        result = ReplaceString(result, "&35;", "#");
        
        title.setText(result);
        
        row.setTag(position);
		
		return row;
	}
	
	public String ReplaceString(String Expression, String Pattern, String Rep)
    {
        if (Expression==null || Expression.equals("")) return "";

        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = Expression.indexOf(Pattern, s)) >= 0) {
            result.append(Expression.substring(s, e));
            result.append(Rep);
            s = e + Pattern.length();
        }
        result.append(Expression.substring(s));
        return result.toString();
    }

//	// 리스트에서 보여주기 위한 이미지를 파싱한다.
//	URL imageUrl;
//	try {
//		imageUrl = new URL(item.image);
//		HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
//		BufferedInputStream bis = new BufferedInputStream(con.getInputStream(), 10240); // 뒤에 숫자는 byte of buffer의 크기
//		Bitmap bm = BitmapFactory.decodeStream(bis);
//		bis.close();
//		image.setImageBitmap(bm);
//
//	} catch (MalformedURLException e) {
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
	
	
	
}
