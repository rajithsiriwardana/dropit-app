package com.dropit;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import android.os.Environment;
import android.util.Log;
import com.anghiari.dropit.commons.DropItPacket;

public class ClientHandler extends SimpleChannelUpstreamHandler {

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		DropItPacket pkt = (DropItPacket) e.getMessage();
		String method = pkt.getMethod();

		Log.d("Pahan", "GOT :" + method);

		if (method.equals(Utils.RES_GET_METHOD)) {

			String filename = (String) pkt.getAttribute(Utils.ATTR_FILENAME);
			String ip = (String) pkt.getAttribute(Utils.ATTR_NODEIP);
			String port = (String) pkt.getAttribute(Utils.ATTR_NODEPORT);

			DropItPacket pac = new DropItPacket(Utils.RETEIEVE_METHOD);
			pac.setAttribute(Utils.ATTR_FILENAME, filename);
			sendMessageToFileServer(pac, ip, Integer.parseInt(port));
		}

		else if (method.equals(Utils.TRANSFER_METHOD)) {
			String filename = (String) pkt.getAttribute(Utils.ATTR_FILENAME);
			writeToFile(filename, null);
		}
		
		if (method.equals(Utils.RES_PUT_METHOD)) {

			String filename = (String) pkt.getAttribute(Utils.ATTR_FILENAME);
			String ip = (String) pkt.getAttribute(Utils.ATTR_NODEIP);
			String port = (String) pkt.getAttribute(Utils.ATTR_NODEPORT);

			DropItPacket pac = new DropItPacket(Utils.STORE_METHOD);
			pac.setAttribute(Utils.ATTR_FILENAME, filename);
			sendMessageToFileServer(pac, ip, Integer.parseInt(port));
		}

		else if (method.equals(Utils.ACK_STORE_METHOD)) {
			Log.d("Pahan", "Download successfully");
		}

		super.messageReceived(ctx, e);
	}

//	private void sendMessage(DropItPacket packt, ChannelHandlerContext ctx,
//			MessageEvent e) {
//
//		Channel channel = e.getChannel();
//		ChannelFuture channelFuture = Channels.future(e.getChannel());
//		ChannelEvent responseEvent = new DownstreamMessageEvent(channel,
//				channelFuture, packt, channel.getRemoteAddress());
//		ctx.sendDownstream(responseEvent);
//	}

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
			Log.d("Pahan", "ERROR " + e.getMessage());
		}
	}


}
