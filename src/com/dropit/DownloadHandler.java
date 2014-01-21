package com.dropit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

public class DownloadHandler {

	private String IP = Utils.IP;
	private int PORT = Utils.PORT;
	
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
			
			Thread.sleep(500);
			
			Log.d("Pahan",resgetpackt.getMETHOD());
			
			if(resgetpackt.getMETHOD().equals(Utils.RES_GET_METHOD)){
				
				String ip = resgetpackt.getKeyValue("NODE_IP");
				String port = resgetpackt.getKeyValue("NODE_PORT");
					
				Socket clientSocket2File = new Socket(ip, Integer.parseInt(port));
				ObjectOutputStream outToNodeServer = new ObjectOutputStream(
						clientSocket2File.getOutputStream());
				
				DropItPacket reteievepacket = new DropItPacket(Utils.RETEIEVE_METHOD);
				reteievepacket.setKeyValue("FILENAME",filename);
				outToNodeServer.writeObject(reteievepacket);
				
				ObjectInputStream fromFileServer = new ObjectInputStream(
						clientSocket2File.getInputStream());
				DropItPacket transferpackt = (DropItPacket) fromFileServer.readObject();
				writeToFile(transferpackt.getKeyValue("FILENAME"), transferpackt.getDATA());
			}
			
			clientSocket.close();

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
		
		return true;
	}
	
	private void writeToFile(String filename, byte[] data){
		
		try{
		File sdDir = Environment.getExternalStorageDirectory();
		File file = new File(sdDir.getCanonicalPath() + "/" + Utils.DIR_NAME+"/"+filename);
		if (!file.exists()) {
		  file.createNewFile();
		}
		
		FileOutputStream stream = new FileOutputStream(file); 
        stream.write(data); 
		
		}catch(Exception e){
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
	}
	
	private void testTransaction(){
		
		try {
			
		
		Socket clientSocket = new Socket(IP, PORT);
		ObjectOutputStream outToServer = new ObjectOutputStream(
				clientSocket.getOutputStream());
		
		}catch(Exception e){
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
	}
}
