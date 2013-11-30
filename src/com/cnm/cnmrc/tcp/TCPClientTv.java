package com.cnm.cnmrc.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import android.os.Handler;
import android.os.Message;

import com.cnm.cnmrc.util.Util;

public class TCPClientTv extends Thread {
	private InputStream is = null;
	private OutputStream os = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	Socket socket = null;
	Handler handler = null;
	String hostAddress = "";
	String vodAssetId = "";
	
	String size = "0108";
	String trNo = "CM02";
	
	public TCPClientTv(Handler handler, String hostAddress, String vodAssetId) {
		this.handler = handler;
		this.hostAddress = hostAddress;
		this.vodAssetId = vodAssetId;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(hostAddress, 27351);
			sendMessage(socket);
			receiveMessage(socket);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void sendMessage(Socket socket) {
		try {
			os = socket.getOutputStream();
			
			StringBuffer sb = new StringBuffer();
			sb.append(size);
			sb.append(trNo);
			sb.append(vodAssetId);
			
			// assetId의 크기가 100이다. 빈 공간은 space로 채운다.
			int count = vodAssetId.length();
			char[] whiteSpace = new char[100-count];
			Arrays.fill(whiteSpace, ' ');
			sb.append(whiteSpace);
			
			byte[] buffer = sb.toString().getBytes("utf-8");
			os.write(buffer);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void receiveMessage(Socket socket) {
		try {
			is = socket.getInputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			char[] ch = new char[4096];
			int count = br.read(ch);
			//String msg = ch.toString(); xxx
			String msg = new String(ch);
			
            Message toMsg = handler.obtainMessage();	// 메시지 얻어오기
            toMsg.what = 1;			// 메시지 ID 설정
            toMsg.obj = msg;	 	// 메시지 정보 설정3 (Object 형식)
             
            handler.sendMessage(toMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
