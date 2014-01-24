package com.dropit;

import java.util.ArrayList;

import org.jboss.netty.channel.ChannelHandlerContext;
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
			ArrayList<String> suggestions = (ArrayList<String>) pkt
					.getAttribute("SEARCH_RESULTS");
			String suug = "";
			for (int i = 0; i < suggestions.size() - 1; i++) {
				suug += suggestions.get(i) + ",";
				Log.d("Pahan", suggestions.get(i));
			}
			suug += suggestions.get(suggestions.size() - 1);
			Intent in = new Intent(context, SearchActivity.class);
			in.putExtra("list", suug);
			context.startActivity(in);
		}

	}
}
