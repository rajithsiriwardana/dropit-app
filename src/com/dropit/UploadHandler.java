package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectDecoder;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectEncoder;

import com.anghiari.dropit.commons.DropItPacket;

import android.annotation.SuppressLint;
import android.content.Context;
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

	public boolean uploadFile(final Context c, String filepath) {

		boolean status = false;
		try {

			File file = new File(filepath);

//			ClientBootstrap clientBootstrap = ChannelHandler
//					.getChannelHandler().getClientBootstrap();
			
			Executor bossPool = Executors.newCachedThreadPool();
	        Executor workerPool = Executors.newCachedThreadPool();
	        ChannelFactory channelFactory = new NioClientSocketChannelFactory(bossPool, workerPool);
	        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {
	            public ChannelPipeline getPipeline() throws Exception {
	                return Channels.pipeline(
	                        new CompatibleObjectEncoder(), 
	                        new CompatibleObjectDecoder(),//(ClassResolvers.cacheDisabled(getClass().getClassLoader())),//ObjectDecoder might not work if the client side is not using netty ObjectDecoder for decoding.
	                        new UploadResponseHandler(c));
	            }
	        };
	        ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);
	        clientBootstrap.setPipelineFactory(pipelineFactory);
				        

			InetSocketAddress addressToConnectTo = new InetSocketAddress(IP,
					PORT);
			ChannelFuture cf = clientBootstrap.connect(addressToConnectTo);

			final DropItPacket putPackts = new DropItPacket(Utils.PUT_METHOD);
			putPackts.setAttribute(Utils.ATTR_FILENAME, file.getName());
			putPackts.setAttribute(Utils.ATTR_FILEPATH, filepath);
			
	
			cf.addListener(new ChannelFutureListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {

					if (future.isSuccess()) {

						Channel channel = future.getChannel();
						channel.write(putPackts);

					}
				}
			});

			status = true;
		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}

		return status;
	}
}
