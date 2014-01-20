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

public class UploadHandler {

	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;
	private String IP = "192.168.43.218";
	private int PORT = 8000;
	
	@SuppressLint("NewApi")
	public UploadHandler() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	public boolean uploadFile(String filepath) {
 
		try {

			File file = new File(filepath);
			byte[] filedata = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			bufferedInputStream.read(filedata, 0, filedata.length);

			Socket clientSocket = new Socket(IP, PORT);

			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			
			ObjectInputStream fromServer = new ObjectInputStream(
					clientSocket.getInputStream());

			DropItPacket putpacket = new DropItPacket(Utils.PUT_METHOD);
			putpacket.setKeyValue("FILENAME",file.getName());
			outToServer.writeObject(putpacket);
			
			DropItPacket resputpackt = (DropItPacket) fromServer.readObject();
			if(resputpackt.getMETHOD().equals(Utils.RES_PUT_METHOD)){
				
				String ip = resputpackt.getKeyValue("NODE_IP");
				String port = resputpackt.getKeyValue("NODE_PORT");
				
				Socket clientSocket2File = new Socket(ip, Integer.parseInt(port));
				ObjectOutputStream outToNodeServer = new ObjectOutputStream(
						clientSocket2File.getOutputStream());
				
				DropItPacket storepacket = new DropItPacket(Utils.STORE_METHOD);
				storepacket.setKeyValue("FILENAME",file.getName());
				storepacket.setDATA(filedata);
				outToNodeServer.writeObject(putpacket);
			}
			
			clientSocket.close();

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
		
		return true;
	}
}
