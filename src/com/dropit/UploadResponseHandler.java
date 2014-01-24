package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

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
import android.util.Log;

import com.anghiari.dropit.commons.DropItPacket;

public class UploadResponseHandler extends SimpleChannelUpstreamHandler {

	private Context context;
	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;

	public UploadResponseHandler(Context c) {
		context = c;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		DropItPacket pkt = (DropItPacket) e.getMessage();
		String method = pkt.getMethod();
		
		Log.d("Pahan", method);

		if (method.equals("RES_PUT")) {

			String filename = String.valueOf(pkt.getAttribute("FILE_NAME"));
			String filepath = String.valueOf(pkt.getAttribute("FILE_PATH"));
			String ip = String.valueOf(pkt.getAttribute("NODE_IP"));
			String port = String.valueOf(pkt.getAttribute("NODE_PORT"));
			
			DropItPacket pac = new DropItPacket(Utils.STORE_METHOD);
			pac.setData(this.readFile(filepath));
			pac.setAttribute(Utils.ATTR_FILENAME, filename);

			Log.d("Pahan", "File server for PUT method " + filename + " - "
					+ ip + " : " + port);
			sendMessageToFileServer(pac, ip, Integer.parseInt(port));
		}

		else if (method.equals(Utils.ACK_STORE_METHOD)) {
			Log.d("Pahan", "Store successfully");
			Intent in = new Intent(context, UploadResultsActivity.class);
			in.putExtra("status", true);
			context.startActivity(in);
		}

	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Intent in = new Intent(context, UploadResultsActivity.class);
		in.putExtra("status", false);
		context.startActivity(in);
        Channels.close(e.getChannel());
    }
	
	private void sendMessageToFileServer(final DropItPacket packt, String ip,
			int port) {

		Executor bossPool = Executors.newCachedThreadPool();
        Executor workerPool = Executors.newCachedThreadPool();
        ChannelFactory channelFactory = new NioClientSocketChannelFactory(bossPool, workerPool);
        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new CompatibleObjectEncoder(), 
                        new CompatibleObjectDecoder(),//(ClassResolvers.cacheDisabled(getClass().getClassLoader())),//ObjectDecoder might not work if the client side is not using netty ObjectDecoder for decoding.
                        new UploadResponseHandler(context));
            }
        };
        ClientBootstrap clientBootstrap = new ClientBootstrap(channelFactory);
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
	
	private byte[] readFile(String filepath) {

		byte[] filedata = null;
		try {
			File file = new File(filepath);
			filedata = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			bufferedInputStream.read(filedata, 0, filedata.length);

		} catch (Exception e) {
			Log.d("Pahan", "ERROR " + e.getMessage());
		}

		return filedata;
	}

}
