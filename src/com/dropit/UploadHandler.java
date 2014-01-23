package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.anghiari.dropit.commons.DropItPacket;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

public class UploadHandler {

	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;
	private String IP = Utils.IP;
	private int PORT = Utils.PORT;
	
	@SuppressLint("NewApi")
	public UploadHandler() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	public boolean uploadFile(String filepath) {
 
		boolean status = false;
		try {

			File file = new File(filepath);
			byte[] filedata = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			bufferedInputStream.read(filedata, 0, filedata.length);

			ClientBootstrap clientBootstrap = ChannelHandler
					.getChannelHandler().getClientBootstrap();

			InetSocketAddress addressToConnectTo = new InetSocketAddress(IP,
					PORT);
			ChannelFuture cf = clientBootstrap.connect(addressToConnectTo);

			final DropItPacket getPackts = new DropItPacket(Utils.PUT_METHOD);
			getPackts.setAttribute(Utils.ATTR_FILENAME, file.getName());

			cf.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {

					if (future.isSuccess()) {
						Channel channel = future.getChannel();
						channel.write(getPackts);

					}
				}
			});
			
		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
		
		return status;
	}
}
