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

import com.cnm.cnmrc.item.ItemAdultCert;
import com.cnm.cnmrc.item.ItemChannelProduct;
import com.cnm.cnmrc.item.ItemChannelProductList;
import com.cnm.cnmrc.util.UrlAddress;

public class AdultCertParser {
	
	private String URL;
	private ItemAdultCert item;

	public AdultCertParser(String url) {
		item	=	new ItemAdultCert();
		URL = url;
	}

	public Boolean start() {
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

					if (parser.getName().equalsIgnoreCase("resultCode")) {
						eventType = parser.next();
						item.setResultCode(parser.getText());
					} else if (parser.getName().equalsIgnoreCase("errString")) {
						item.setErrString(parser.getText());			 
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

	public ItemAdultCert getItem() {
		return item;
	}
}
