package com.dropit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectDecoder;
import org.jboss.netty.handler.codec.serialization.CompatibleObjectEncoder;

import android.os.Environment;
import android.util.Log;
import com.anghiari.dropit.commons.DropItPacket;

public class ClientHandler extends SimpleChannelUpstreamHandler {

	private FileInputStream fileInputStream;
	private BufferedInputStream bufferedInputStream;

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		DropItPacket pkt = (DropItPacket) e.getMessage();
		String method = pkt.getMethod();

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
			Log.d("Pahan", "DW name "+filename);
			writeToFile(filename, pkt.getData());
		}

		else if (method.equals("RES_PUT")) {

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
		}
		
		else if(method.equals("RES_SEARCH")){
			
		}

		super.messageReceived(ctx, e);
	}

	// private void sendMessage(DropItPacket packt, ChannelHandlerContext ctx,
	// MessageEvent e) {
	//
	// Channel channel = e.getChannel();
	// ChannelFuture channelFuture = Channels.future(e.getChannel());
	// ChannelEvent responseEvent = new DownstreamMessageEvent(channel,
	// channelFuture, packt, channel.getRemoteAddress());
	// ctx.sendDownstream(responseEvent);
	// }

	private void sendMessageToFileServer(final DropItPacket packt, String ip,
			int port) {

		ClientBootstrap clientBootstrap = ChannelHandler.getChannelHandler()
				.getClientBootstrap();

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
