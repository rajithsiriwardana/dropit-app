package com.dropit;

import java.util.ArrayList;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anghiari.dropit.commons.DropItPacket;

public class SearchResponseHandler extends SimpleChannelUpstreamHandler {

	private Context context;

	public SearchResponseHandler(Context c) {
		context = c;
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {

		DropItPacket pkt = (DropItPacket) e.getMessage();
		String method = pkt.getMethod();

		Log.d("Pahan", method);

		if (method.equals("RES_SEARCH")) {

			String listnullll = String.valueOf(pkt.getAttribute("LIST_NULL"));
			Log.d("Pahan", "LIST_NULL :"+listnullll);
			boolean islistnull = true;
			
			if(listnullll.equalsIgnoreCase("FALSE")){
				islistnull = false;
			}
			
			String suug = "";

			
			if (!islistnull) {
				
				ArrayList<String> suggestions = (ArrayList<String>) pkt
						.getAttribute("SEARCH_RESULTS");
				
				for (int i = 0; i < suggestions.size() - 1; i++) {
					suug += suggestions.get(i) + ",";
				}

				suug += suggestions.get(suggestions.size() - 1);
				islistnull = false;
				Log.d("Pahan", "SUGG :" +suug);
				
				
				
			} else {
				
				islistnull = true;
				Log.d("Pahan", "list null :"+ islistnull);

			}
			
			Intent in = new Intent(context, SearchActivity.class);
			in.putExtra("list", suug);
			in.putExtra("list_null", islistnull);
			context.startActivity(in);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Intent in = new Intent(context, DownloadResultsActivity.class);
		in.putExtra("status", false);
		context.startActivity(in);
		Channels.close(e.getChannel());
	}
}
