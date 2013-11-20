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
			socket = new Socket(hostAddress, 2751);
			sendMessage(socket);
			receiveMessage(socket);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void receiveMessage(Socket socket) {
		try {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			String msg = (String) ois.readObject();
			//Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
			//VodDetail.receiveMessageFromSTB(msg);
			
			 // 메시지 얻어오기
            Message toMsg = handler.obtainMessage();
             
            // 메시지 ID 설정
            toMsg.what = 1;
             
            // 메시지 정보 설정3 (Object 형식)
            toMsg.obj = msg;
             
            handler.sendMessage(toMsg);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
