package com.cnm.cnmrc.util;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.adapter.TvchSemiAdapter;
import com.cnm.cnmrc.adapter.VodSemiAdapter;
import com.cnm.cnmrc.fragment.vodtvch.VodTvchMain;
import com.cnm.cnmrc.slidingmenu.SlidingMenu;

public class UiUtil {
	
	public static boolean isSlidingMenuOpening(Context context) {
		Fragment f = ((MainActivity)context).getFragmentVodTvch();
		if (f != null) {
			SlidingMenu slidingMenu = ((VodTvchMain)f).getSlidingMenu();
			if (slidingMenu.isOpening()) return true;
		}
		
		return false;
	}
	
	public static Bundle makeVodDetailBundle(Context context, VodSemiAdapter adapter, View view, int position) {
		String title = adapter.getItem(position).getTitle();
		String hd = adapter.getItem(position).getHd();
		String grade = adapter.getItem(position).getGrade();
		String director = adapter.getItem(position).getDirector();
		String actor = adapter.getItem(position).getActor();
		String price = adapter.getItem(position).getPrice();
		String contents = adapter.getItem(position).getContents();
		
		ImageView logo = (ImageView)view.findViewById(R.id.logo_img);
		Bitmap logoImage = ((BitmapDrawable)logo.getDrawable()).getBitmap();	// 기본적으로 no image 포스터가 적용되어 있다.
		
		Bundle bundle = new Bundle();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bitmap = logoImage;
		boolean result = bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		if (result) {	// 바이트어레이로 변환 실패하면... not all Formats support all bitmap configs directly
		} else {
			bitmap = Util.getNoBitmap(context);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		}
		byte[] logoImg = baos.toByteArray();
		
		bundle.putByteArray("logoImg", logoImg);
		bundle.putString("title", title); 
		bundle.putString("hd", hd); 
		bundle.putString("grade", grade); 
		bundle.putString("director", director); 
		bundle.putString("actor", actor); 
		bundle.putString("price", price); 
		bundle.putString("contents", contents);
		
		return bundle;
	}
	
	public static Bundle makeTvchDetailBundle(Context context, TvchSemiAdapter adapter, View view, int position, String dateIndex) {
		String number = adapter.getItem(position).getNumber();
		String id = adapter.getItem(position).getId();
		
		Bundle bundle = new Bundle();
		
		//bundle.putString("number", number); 
		bundle.putString("id", id);
		bundle.putString("dateIndex", dateIndex);
		
		return bundle;
	}

	
}
