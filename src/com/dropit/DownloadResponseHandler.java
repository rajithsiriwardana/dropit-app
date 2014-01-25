package com.dropit;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectDecoder;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.anghiari.dropit.commons.DropItPacket;

public class DownloadResponseHandler extends SimpleChannelUpstreamHandler {

	private Context context;

	public DownloadResponseHandler(Context c) {
		context = c;
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		DropItPacket pkt = (DropItPacket) e.getMessage();
		String method = pkt.getMethod();

		Log.d("Pahan", method);

		if (method.equals(Utils.RES_GET_METHOD)) {

			String filename = String.valueOf(pkt.getAttribute("FILE_NAME"));
			String ip = String.valueOf(pkt.getAttribute("NODE_IP"));
			String port = String.valueOf(pkt.getAttribute("NODE_PORT"));
			DropItPacket pac = new DropItPacket(Utils.RETEIEVE_METHOD);
			pac.setAttribute(Utils.ATTR_FILENAME, filename);

			Log.d("Pahan", "File server for GET method " + filename + " - "
					+ ip + " : " + port);

			sendMessageToFileServer(pac, ip, Integer.parseInt(port));
		}

		else if (method.equals(Utils.TRANSFER_METHOD)) {
			String filename = String.valueOf(pkt.getAttribute("FILE_NAME"));
			
			String exits = String.valueOf(pkt.getAttribute("FILE_EXISTS"));
			
			Log.d("Pahan", ""+exits);
			
			if(exits.equalsIgnoreCase("TRUE")){
				
			writeToFile(filename, pkt.getData());
			Log.d("Pahan", "Download successfully");
			Intent in = new Intent(context, DownloadResultsActivity.class);
			in.putExtra("status", true);
			context.startActivity(in);
			}
			else{
				Log.d("Pahan", "Fle not found in the server");
				Intent in = new Intent(context, DownloadResultsActivity.class);
				in.putExtra("status", false);
				context.startActivity(in);
			}
		}

	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Intent in = new Intent(context, DownloadResultsActivity.class);
		in.putExtra("status", false);
		context.startActivity(in);
        Channels.close(e.getChannel());
    }

	private void sendMessageToFileServer(final DropItPacket packt, String ip,
			int port) {

		Executor bossPool = Executors.newCachedThreadPool();
		Executor workerPool = Executors.newCachedThreadPool();
		ChannelFactory channelFactory = new NioClientSocketChannelFactory(
				bossPool, workerPool);
		ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new CompatibleObjectEncoder(),
						new CompatibleObjectDecoder(),
						new DownloadResponseHandler(context));
			}
		};
		ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);
		clientBootstrap.setOption("connectTimeoutMillis",80000);
		clientBootstrap.setPipelineFactory(pipelineFactory);
		

		InetSocketAddress addressToConnectTo = new InetSocketAddress(ip, port);
		ChannelFuture cf = clientBootstrap.connect(addressToConnectTo);

		cf.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
					throws Exception {

				if (future.isSuccess()) {
					Channel channel = future.getChannel();
					channel.write(packt);

				}
			}
		});
	}

	private void writeToFile(String filename, byte[] data) {

		try {
			File sdDir = Environment.getExternalStorageDirectory();
			File file = new File(sdDir.getCanonicalPath() + "/"
					+ Utils.DIR_NAME + "/" + filename);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream stream = new FileOutputStream(file);
			stream.write(data);

		} catch (Exception e) {
			Log.d("Pahan", "ERROR WRITING FILE " + e.getMessage());
		}
	}

}
