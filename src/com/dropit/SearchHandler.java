package com.dropit;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import com.anghiari.dropit.commons.DropItPacket;

public class SearchHandler {

	
	private String IP = Utils.IP;
	private int PORT = Utils.PORT;

	@SuppressLint("NewApi")
	public SearchHandler() {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	public boolean searchFile(String filename) {

		try {

			ClientBootstrap clientBootstrap = ChannelHandler
					.getChannelHandler().getClientBootstrap();
			

			InetSocketAddress addressToConnectTo = new InetSocketAddress(IP,
					PORT);
			ChannelFuture cf = clientBootstrap.connect(addressToConnectTo);

			final DropItPacket searchPackts = new DropItPacket(Utils.SEARCH_METHOD);
			searchPackts.setAttribute(Utils.ATTR_FILENAME, filename);

			cf.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {

					if (future.isSuccess()) {
						Channel channel = future.getChannel();
						channel.write(searchPackts);

					}
				}
			});

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}

		return true;
	}

}
