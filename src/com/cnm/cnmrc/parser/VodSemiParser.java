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

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cnm.cnmrc.item.ItemVodSemi;
import com.cnm.cnmrc.item.ItemVodSemiList;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class VodSemiParser {

	// URL
	private String URL = "";

	private ItemVodSemiList list;
	
	public VodSemiParser(String url) {
		list = new ItemVodSemiList();
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
				// parser.setInput( new StringReader ( "<foo>Hello World!</foo>" ) );
				// parser.setInput( new StringReader ( "<foo>Hello World!</foo>" ), "UTF-8" );
				parser.setInput(httpEntity.getContent(), null);
			} else {
				return false;
			}
			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();	

					if (tag.equalsIgnoreCase("resultCode")) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("errorString")) {
						eventType = parser.next();
						list.setErrorString(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("totalCount")) {
						eventType = parser.next();
						list.setTotalCount(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("totalPage")) {
						eventType = parser.next();
						list.setTotalPage(parser.getText());
						parser.next();
						
					} else if (tag.equalsIgnoreCase("VodSearch_Item") ||
								tag.equalsIgnoreCase("Trailer_Item") ||
								tag.equalsIgnoreCase("NewMovie_Item") ||
								tag.equalsIgnoreCase("Tv_Item") ) {
						list.getList().add(new ItemVodSemi()); 			// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
						
					} else if (tag.equalsIgnoreCase("VOD_ID")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setId(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_CATEGORY")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setCategory(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_TAG")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setTag(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Title")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setTitle(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_IMG")) {	// 동영상 파일경로
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setImg(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Video_Path_android")) {	// 동영상 파일경로
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setVideoPathAndroid(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Director")) {	// 상영등급
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setDirector(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Actor")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setActor(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Grade")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setGrade(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_HD")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setHd(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Price")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setPrice(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Duration")) {	// run-time
						eventType = parser.next();
						if(parser.getText() != null) list.getList().get(mCurrentCount).setDuration(parser.getText().trim());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_Contents")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setContents(parser.getText());
						parser.next();
					} else if (tag.equalsIgnoreCase("VOD_More")) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setMore(parser.getText().trim());
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

	public ItemVodSemiList getList() {
		return list;
	}
}
