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

public class ProductParser {
	// URL
	private static String URL;
	
	// XML Depth
	// deth 1
	private final static String RESULT_CODE			=	"resultCode";
	private final static String PRODUCT_ITEM		=	"product_item";
	
	// deth 2
	private final static String PRODUCT_CODE		=	"productCode";	
	private final static String PRODUCT_NAME		=	"productName";	
	private final static String PRODUCT_INFO		=	"productInfo";
	
	private static ProductList list;

	public ProductParser(String url) {
		list	=	new ProductList();
		URL = url;
	}

	public Boolean start() {
		Integer	mCurrentCount=0;
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();

			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

			HttpClient httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, UrlAddress.XML_CONNETION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, UrlAddress.XML_CONNETION_TIME_OUT);

			HttpGet httpGet =  new HttpGet(URL);
			HttpResponse httpResponse = null;
			httpResponse = httpclient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity != null) parser.setInput(httpEntity.getContent(), null);
			else return false;
			
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_DOCUMENT) {
				} else if(eventType == XmlPullParser.END_DOCUMENT) {
				} else if(eventType == XmlPullParser.START_TAG) {

					if (parser.getName().equals(RESULT_CODE)) {
						eventType = parser.next();
						list.setResultCode(parser.getText());
					} else if (parser.getName().equals(PRODUCT_ITEM)) {
						list.getList().add(new Product());			// 리스트 추가
						mCurrentCount = list.getList().size()-1;			// 현재 추가된 리스트의 위치 반환
					} else if (parser.getName().equals(PRODUCT_CODE)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProductCode(parser.getText());
					} else if (parser.getName().equals(PRODUCT_NAME)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProductName(parser.getText());
					} else if (parser.getName().equals(PRODUCT_INFO)) {
						eventType = parser.next();
						list.getList().get(mCurrentCount).setProductInfo(parser.getText());
						eventType = parser.next();
					}
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {	// 파싱 중 발생하는 예외 상황을 받다.
			e.printStackTrace();
		} catch (IOException e) {				// 통신과 관련된 에외 상황을 받는다.
			e.printStackTrace();
		}

		return true;
	}

	public ProductList getList() {
		return list;
	}
}
