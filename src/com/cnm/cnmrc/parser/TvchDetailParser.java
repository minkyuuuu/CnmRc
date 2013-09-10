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

import com.cnm.cnmrc.item.ItemTvchDetail;
import com.cnm.cnmrc.item.ItemTvchDetailList;
import com.cnm.cnmrc.util.UrlAddress;

public class TvchDetailParser {

	// URL
	private String URL = "";

	private ItemTvchDetailList list;
	
	public TvchDetailParser(String url) {
		list = new ItemTvchDetailList();
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
						
					} else if (parser.getName().equalsIgnoreCase("Channel_Item") ) {
						list.getList().add(new ItemTvchDetail()); 		// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
						
					} else if (tag.equalsIgnoreCase("Schedule_seq")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setScheduleSeq(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Broadcasting_Date")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setBroadcastingDate(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_ID")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_Title")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setTitle(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_Content")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setContent(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_Broadcasting_Time")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setBroadcastingTime(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_HD")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setHd(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("Program_Grade")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setGrade(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("SeriesId")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setSeriesId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("ProgramKey")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setKey(parser.getText());
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

	public ItemTvchDetailList getList() {
		return list;
	}

}
