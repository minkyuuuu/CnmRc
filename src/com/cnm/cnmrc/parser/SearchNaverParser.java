package com.cnm.cnmrc.parser;

import java.io.IOException;
import java.io.StringReader;

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

import com.cnm.cnmrc.item.ItemSearchNaver;
import com.cnm.cnmrc.item.ItemSearchNaverList;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class SearchNaverParser {

	private String URL = "";
	private ItemSearchNaverList list;

	public SearchNaverParser(String search, String count, String start) {
		list = new ItemSearchNaverList();

		String searchEncoder = Util.getURLEncoder(search);
		URL = "http://openapi.naver.com/search?key=ba5c945583e735931b33012306147b6c" 
							+ "&query=" + searchEncoder 
							+ "&display=" + count 
							+ "&start=" + start 
							+ "&target=webkr";
		//URL = "http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f" + "&query=" + searchEncoder + "&display=" + count + "&start=" + start + "&target=webkr";
	}

	public Boolean start() {
		int currentCount = 0;

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

//			XmlPullParser parser = factory.newPullParser();
//			parser.setInput(url.openStream(), null);

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
//				parser.setInput( new StringReader ( "<total>3607507</total>" ) );
			} else {
				return false;
			}

			// 읽어온 정보를 파싱하여 데이터를 만든다.
			int eventType = parser.getEventType();
			boolean skip = true; // title과 description이 channel 컨테이너에도 있다. item 컨테이너의 title, descripion이 필요하다.
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					String tag = parser.getName();	
				
					if (tag.equals("total")) {
						list.setTotal(parser.nextText());
					}

					if (tag.compareTo("item") == 0) {
						list.getList().add(new ItemSearchNaver()); 			// 리스트 추가
						currentCount = list.getList().size() - 1; 		// 현재 추가된 리스트의 위치 반환
					}

					if (tag.compareTo("title") == 0) {
						if (!skip) {
							list.getList().get(currentCount).setTitle(parser.nextText().trim());
						}
					}
					if (tag.compareTo("link") == 0) {
						if (!skip) {
							list.getList().get(currentCount).setLink(parser.nextText());
						}
					}
					if (tag.compareTo("description") == 0) {
						if (!skip) {
							list.getList().get(currentCount).setDescription(parser.nextText());
						} else {
							skip = false;
						}
					}

				}
				eventType = parser.next(); // 다음 데이터로 넘어간다.. END_DOCUMENT일때까지..
			}
			Log.i("hwang", "wawoo!!! arrive at END_DOCUMENT");

		} catch (XmlPullParserException e) { // 파싱 중 발생하는 예외 상황을 받다.
			e.printStackTrace();
			Log.i("hwang", "파싱 중 발생하는 예외 상황 : " + e.toString());
		} catch (IOException e) { // 통신과 관련된 에외 상황을 받는다.
			e.printStackTrace();
			Log.i("hwang", "통신과 관련된 에외 상황 : " + e.toString());
		}

		return true;
	}

	public ItemSearchNaverList getList() {
		return list;
	}

}
