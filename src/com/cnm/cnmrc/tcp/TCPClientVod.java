package com.cnm.cnmrc.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnm.cnmrc.util.Util;

public class TCPClientVod extends Thread {
	private InputStream is = null;
	private OutputStream os = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private BufferedWriter bw = null;
	private BufferedReader br = null;

	Socket socket = null;
	Handler handler = null;
	String hostAddress = "";
	String vodAssetId = "";
	
	String size = "0122";
	String trNo = "CM01";
	
	public TCPClientVod(Handler handler, String hostAddress, String vodAssetId) {
		this.handler = handler;
		this.hostAddress = hostAddress;
		this.vodAssetId = vodAssetId;
		
		Log.d("hwang", "vodAssetId : " + vodAssetId + " <--- " + System.currentTimeMillis());
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
			//oos = new ObjectOutputStream(os);
			//bw = new BufferedWriter(new OutputStreamWriter(os));
			
			StringBuffer sb = new StringBuffer();
			sb.append(size);
			sb.append(trNo);
			sb.append(Util.getYYYYMMDDHHmmss(new Date(System.currentTimeMillis())));
			sb.append(vodAssetId);
			
			// assetId의 크기가 100이다. 빈 공간은 space로 채운다.
			int count = vodAssetId.length();
			char[] whiteSpace = new char[100-count];
			Arrays.fill(whiteSpace, ' ');
			sb.append(whiteSpace);
			
			//oos.writeObject(sb.toString());
			//bw.write(sb.toString());
			byte[] buffer = sb.toString().getBytes("utf-8");
			os.write(buffer);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void receiveMessage(Socket socket) {
		try {
			is = socket.getInputStream();
			//ois = new ObjectInputStream(is);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			char[] ch = new char[4096];
			int count = br.read(ch);
			//String msg = ch.toString(); xxx
			String msg = new String(ch);
			
			/*try {
				msg = (String) ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			//br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			//String msg = "";
			
//			while( (msg=br.readLine()) != null)
//				System.out.println(msg);
			
			//char[] msg = null;
			//int count = br.read(msg);
			
            Message toMsg = handler.obtainMessage();	// 메시지 얻어오기
            toMsg.what = 1;			// 메시지 ID 설정
            toMsg.obj = msg;	 	// 메시지 정보 설정3 (Object 형식)
             
            handler.sendMessage(toMsg);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
