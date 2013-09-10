package com.cnm.cnmrc.parser;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.cnm.cnmrc.item.ItemTvchSemi;
import com.cnm.cnmrc.item.ItemTvchSemiList;
import com.cnm.cnmrc.util.UrlAddress;

public class TvchSemiParser {

	// URL
	private String URL = "";

	private ItemTvchSemiList list;
	
	public TvchSemiParser(String url) {
		list = new ItemTvchSemiList();
		URL = url;
	}

	public Boolean start() {
		Integer mCurrentCount = 0;
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();

			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			HttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, UrlAddress.XML_CONNETION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, UrlAddress.XML_CONNETION_TIME_OUT);

			HttpGet httpGet = new HttpGet(URL);
			HttpResponse httpResponse = null;
			httpResponse = httpclient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity != null) {
				parser.setInput(httpEntity.getContent(), null);
			} else {
				return false;
			}
			
			int eventType = parser.getEventType();
			//int number = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();

					if (parser.getName().equalsIgnoreCase("resultCode")) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
						parser.next();
					} else if (parser.getName().equalsIgnoreCase("errorString")) {
						eventType = parser.next();
						list.setErrorString(parser.getText());
						
					} else if (parser.getName().equalsIgnoreCase("All_Channel_Item")) {
						list.getList().add(new ItemTvchSemi()); 		// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
						
					} else if (tag.equalsIgnoreCase("Channel_ID")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_number")) {
						eventType = parser.next();
						if (parser.getText() != null) {
							list.getList().get(mCurrentCount).setNumber(parser.getText());
							//number = Integer.parseInt(list.getList().get(mCurrentCount).getNumber());
						}
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_name")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setName(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_info")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setInfo(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_onAir_HD")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setOnAirHd(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_logo_img")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setLogoImg(parser.getText());
						/*String str = "[parsing %04d] : image path ---> %s";
					 	str = String.format(str, number, list.getList().get(mCurrentCount).getLogoImg());
						Log.i("hwang",str);*/
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_onAir_ID")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramOnAirId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_onAir_Time")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramOnAirTime(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_onAir_Title")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramOnAirTitle(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_next_ID")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramNextId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_next_Time")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramNextTime(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Program_next_Title")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setProgramNextTitle(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_View")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setView(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Channel_Recording")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setRecording(parser.getText());
						parser.next();
						
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) { 	// 파싱 중 발생하는 예외 상황을 받다.
			e.printStackTrace();
			Log.i("hwang", "파싱 중 발생하는 예외 상황");
		} catch (IOException e) { 				// 통신과 관련된 에외 상황을 받는다.
			e.printStackTrace();
			Log.i("hwang", "통신과 관련된 에외 상황");
		}

		return true;
	}

	public ItemTvchSemiList getList() {
		return list;
	}

}
