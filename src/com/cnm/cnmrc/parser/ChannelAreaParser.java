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

import com.cnm.cnmrc.util.UrlAddress;

public class ChannelAreaParser {

	// URL
	private final static String URL = UrlAddress.Channel.getGetChannelArea();

	// XML Depth
	// deth 1
	private final static String RESULT_CODE = "resultCode";
	private final static String AREA_ITEM = "area_item";
	
	// deth 2
	private final static String AREA_CODE = "areaCode";
	private final static String AREA_NAME = "areaName";
	private final static String AREA_NAME_DETAIL = "areaNameDetail";

	private static ChannelAreaList list;

	public ChannelAreaParser() {
		list = new ChannelAreaList();
	}

	public Boolean start() {
		int mCurrentCount = 0;
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

			if (httpEntity != null)
				parser.setInput(httpEntity.getContent(), null);
			else
				return false;

			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {

					if (parser.getName().equals(RESULT_CODE)) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
					} else if (parser.getName().equals(AREA_ITEM)) {
						list.getList().add(new ChannelArea()); 			// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
					} else if (parser.getName().equals(AREA_CODE)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setAreaCode(parser.getText());
					} else if (parser.getName().equals(AREA_NAME)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setAreaName(parser.getText());
					} else if (parser.getName().equals(AREA_NAME_DETAIL)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setAreaNameDetail(parser.getText());
						eventType = parser.next();
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) { 	// 파싱 중 발생하는 예외 상황을 받다.
			e.printStackTrace();
		} catch (IOException e) { 				// 통신과 관련된 에외 상황을 받는다.
			e.printStackTrace();
		}

		return true;
	}

	public ChannelAreaList getList() {
		return list;
	}

}
