package com.cnm.cnmrc.http;

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

import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class SearchVodParser {

	// URL
	private String URL = "";

	// XML Depth
	// deth 1
	private final static String RESULT_CODE 	= "resultCode";
	private final static String ERROR_STRING 	= "errorString";
	private final static String TOTAL_COUNT 	= "totalCount";
	private final static String TOTAL_PAGE 		= "totalPage";
	
	private final static String VOD_SEARCH_ITEM = "VodSearch_Item";

	// deth 2
	private final static String ID 			= "VOD_ID";
	private final static String CATEGORY 	= "VOD_CATEGORY";
	private final static String TAG 		= "VOD_TAG";
	private final static String TITLE 		= "VOD_Title";
	private final static String IMG 		= "VOD_IMG";		// vod 포스터 이미지경로
	private final static String VIDEO_PATH_ANDROID = "VOD_Video_Path_android";	// 동영상 파일경로
	private final static String DIRECTOR 	= "VOD_Director";
	private final static String ACTOR 		= "VOD_Actor";
	private final static String GRADE 		= "VOD_Grade";		// 상영등급
	private final static String HD 			= "VOD_HD";
	private final static String PRICE 		= "VOD_Price";
	private final static String DURATION 	= "VOD_Duration";	// run-time
	private final static String CONTENTS 	= "VOD_Contents";
	private final static String MORE 		= "VOD_More";

	private SearchVodList list;
	private	Activity context;
	
	
	public SearchVodParser(String url, Context context) {
		list = new SearchVodList();
		URL = url;
		this.context = (Activity)context;
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
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.i("hwang", "start document : " + parser.getText());
					
				} else if (eventType == XmlPullParser.START_TAG) {
					// System.out.println("Start tag "+xpp.getName());

					if (parser.getName().equals(RESULT_CODE)) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
						parser.next();
					} else if (parser.getName().equals(ERROR_STRING)) {
						eventType = parser.next();
						list.setErrorString(parser.getText());
						parser.next();
					} else if (parser.getName().equals(TOTAL_COUNT)) {
						eventType = parser.next();
						list.setTotalCount(parser.getText());
						parser.next();
					} else if (parser.getName().equals(TOTAL_PAGE)) {
						eventType = parser.next();
						list.setTotalPage(parser.getText());
						parser.next();
						
					} else if (parser.getName().equals(VOD_SEARCH_ITEM)) {
						list.getList().add(new SearchVod()); 			// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 	// 현재 추가된 리스트의 위치 반환
						
					} else if (parser.getName().equals(ID)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setId(parser.getText());
						parser.next();
					} else if (parser.getName().equals(CATEGORY)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setCategory(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(TAG)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setTag(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(TITLE)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setTitle(parser.getText());
						parser.next();
					} else if (parser.getName().equals(IMG)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setImg(parser.getText());
						parser.next();
					} else if (parser.getName().equals(VIDEO_PATH_ANDROID)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setVideoPathAndroid(parser.getText());
						parser.next();
					} else if (parser.getName().equals(DIRECTOR)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setDirector(parser.getText());
						parser.next();
					} else if (parser.getName().equals(ACTOR)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setActor(parser.getText());
						parser.next();
					} else if (parser.getName().equals(GRADE)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setGrade(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(HD)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setHd(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(PRICE)) {
						eventType = parser.next();
						if (parser.getText() != null) list.getList().get(mCurrentCount).setPrice(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(DURATION)) {
						eventType = parser.next();
						if(parser.getText() != null) list.getList().get(mCurrentCount).setDuration(parser.getText().trim());
						parser.next();
					} else if (parser.getName().equals(CONTENTS)) {
						eventType = parser.next();
						if (!parser.getText().equals(null)) list.getList().get(mCurrentCount).setContents(parser.getText());
						parser.next();
					} else if (parser.getName().equals(MORE)) {
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

	public SearchVodList getList() {
		return list;
	}
}
