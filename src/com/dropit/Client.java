package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

public class Client {

	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;

	@SuppressLint("NewApi")
	public Client() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	public void sendFile(String filepath, String ip) {

		try {
			
			File file = new File(filepath);
			byte[] filedata = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			bufferedInputStream.read(filedata, 0, filedata.length);

			Socket clientSocket = new Socket(ip, 8000);

			ObjectOutputStream outToServer = new ObjectOutputStream(
					clientSocket.getOutputStream());
			
			DropItPacket packet = new DropItPacket();
			packet.setFILE_NAME(file.getName());
			packet.setDATA(filedata);
			outToServer.writeObject(packet);
			
			clientSocket.close();

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
	}

}
