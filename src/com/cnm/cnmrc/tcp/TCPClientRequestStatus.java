package com.cnm.cnmrc.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.os.Handler;
import android.os.Message;

import com.cnm.cnmrc.util.Util;

public class TCPClientRequestStatus extends Thread {
	private InputStream is = null;
	private OutputStream os = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;

	Socket socket = null;
	Handler handler = null;
	String hostAddress = "";
	
	String size = "0008";
	String trNo = "CM03";
	
	public TCPClientRequestStatus(Handler handler, String hostAddress) {
		this.handler = handler;
		this.hostAddress = hostAddress;
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

	private void sendMessage(Socket socket) {
		try {
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			StringBuffer sb = new StringBuffer();
			sb.append(size);
			sb.append(trNo);
			
			oos.writeObject(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void receiveMessage(Socket socket) {
		try {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			String msg = (String) ois.readObject();
			
            Message toMsg = handler.obtainMessage();	// 메시지 얻어오기
            toMsg.what = 1;			// 메시지 ID 설정
            toMsg.obj = msg;	 	// 메시지 정보 설정3 (Object 형식)
             
            handler.sendMessage(toMsg);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}