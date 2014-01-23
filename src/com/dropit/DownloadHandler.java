package com.dropit;

import java.net.InetSocketAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectDecoder;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectEncoder;
import com.anghiari.dropit.commons.DropItPacket;
import android.annotation.SuppressLint;
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

			ClientBootstrap clientBootstrap = ChannelHandler
					.getChannelHandler().getClientBootstrap();

			InetSocketAddress addressToConnectTo = new InetSocketAddress(IP,
					PORT);
			ChannelFuture cf = clientBootstrap.connect(addressToConnectTo);

			final DropItPacket getPackts = new DropItPacket(Utils.GET_METHOD);
			getPackts.setAttribute(Utils.ATTR_FILENAME, filename);

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

		return true;
	}

}
