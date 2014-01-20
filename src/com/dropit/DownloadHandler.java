package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

public class DownloadHandler {

	private String IP = "192.168.43.218";
	private int PORT = 8000;
	
	@SuppressLint("NewApi")
	public DownloadHandler() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}
	
	public boolean downloadFile(String filename) {
		 
		try {

			Socket clientSocket = new Socket(IP, PORT);

			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			
			ObjectInputStream fromServer = new ObjectInputStream(
					clientSocket.getInputStream());

			DropItPacket getpacket = new DropItPacket(Utils.GET_METHOD);
			getpacket.setKeyValue("FILENAME",filename);
			outToServer.writeObject(getpacket);
			
			DropItPacket resgetpackt = (DropItPacket) fromServer.readObject();
			if(resgetpackt.getMETHOD().equals(Utils.RES_GET_METHOD)){
				
				String ip = resgetpackt.getKeyValue("NODE_IP");
				String port = resgetpackt.getKeyValue("NODE_PORT");
				
				Socket clientSocket2File = new Socket(ip, Integer.parseInt(port));
				ObjectOutputStream outToNodeServer = new ObjectOutputStream(
						clientSocket2File.getOutputStream());
				
				DropItPacket reteievepacket = new DropItPacket(Utils.RETEIEVE_METHOD);

			}
			
			clientSocket.close();

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
		
		return true;
	}
}
