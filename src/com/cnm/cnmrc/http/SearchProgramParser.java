package com.cnm.cnmrc.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

import com.cnm.cnmrc.util.UrlAddress;

public class SearchProgramParser {

	// URL
	private String URL = "";

	// XML Depth
	// deth 1
	private final static String RESULT_CODE = "resultCode";
	private final static String ERROR_STRING = "errorString";
	private final static String TOTAL_COUNT = "totalCount";
	private final static String TOTAL_PAGE = "totalPage";
	private final static String AREA_CODE = "areaCode";
	private final static String PRODUCT_CODE = "productCode";
	
	private final static String PROGRAM_SEARCH_ITEM = "ProgramSearch_Item";

	// deth 2
	private final static String ID = "ChannelId";
	private final static String NUMBER = "Channel_number";
	private final static String NAME = "Channel_name";
	private final static String INFO = "Channel_info";
	private final static String LOGO_IMG = "Channel_logo_img";
	private final static String PROGRAM_ID = "Channel_Program_ID";
	private final static String PROGRAM_TIME = "Channel_Program_Time";
	private final static String PROGRAM_NEXT_TIME = "Channel_Program_next_Time";
	private final static String PROGRAM_TITLE = "Channel_Program_Title";
	private final static String PROGRAM_SEQ = "Channel_Program_seq";
	private final static String PROGRAM_GRADE = "Channel_Program_Grade";
	private final static String PROGRAM_HD = "Channel_Program_HD";

	private SearchProgramList list;
	
	
//
//	public SearchProgramParser(String area, String product, String search) {
//		list = new SearchProgramList();
//		String query;
//
//		try {
//			query = URLEncoder.encode(search, "utf-8");
//			URL = "http://58.143.243.91/SMApplicationServer/searchprogram.asp?AreaCode=" + area + "&ProductCode=" + product + "&Search_String=" + query;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}

	public SearchProgramParser(String url) {
		list = new SearchProgramList();
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
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");

				} else if (eventType == XmlPullParser.START_TAG) {
					// System.out.println("Start tag "+xpp.getName());

					if (parser.getName().equals(RESULT_CODE)) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
						parser.next();
					} else if (parser.getName().equals(ERROR_STRING)) {
						eventType = parser.next();
						list.setErrorString(parser.getText());
					} else if (parser.getName().equals(TOTAL_COUNT)) {
						eventType = parser.next();
						parser.next();
						list.setTotalCount(parser.getText());
					} else if (parser.getName().equals(TOTAL_PAGE)) {
						eventType = parser.next();
						list.setTotalPage(parser.getText());
						parser.next();
						
					} else if (parser.getName().equals(PROGRAM_SEARCH_ITEM)) {
						list.getList().add(new SearchProgram()); 		// 리스트 추가
						mCurrentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
						
					} else if (parser.getName().equals(ID)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setId(parser.getText());
						parser.next();
					} else if (parser.getName().equals(NUMBER)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setNumber(parser.getText());
						parser.next();
					} else if (parser.getName().equals(NAME)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setName(parser.getText());
						parser.next();
					} else if (parser.getName().equals(INFO)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setInfo(parser.getText());
						parser.next();
					} else if (parser.getName().equals(LOGO_IMG)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setLogoImg(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_ID)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramId(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_TIME)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramTime(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_NEXT_TIME)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramNextTime(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_TITLE)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramTitle(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_SEQ)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramSeq(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_GRADE)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramGrade(parser.getText());
						parser.next();
					} else if (parser.getName().equals(PROGRAM_HD)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProgramHD(parser.getText());
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

	public SearchProgramList getList() {
		return list;
	}

}
