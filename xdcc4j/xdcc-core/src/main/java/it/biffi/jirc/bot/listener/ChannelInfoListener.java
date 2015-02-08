package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ChannelInfoEvent;
import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.MessageEvent;

import java.util.Map;

public abstract class ChannelInfoListener extends
		AbstractListener<ChannelInfoEvent> {

	@Override
	public void onIrcEvent(ChannelInfoEvent event, Map data) {
		if (event instanceof ChannelInfoEvent
				&& data.get(ChannelInfoEvent.STATUS) != null
				&& data.get(ChannelInfoEvent.STATUS).equals("FINISHED")) {
			onEndOf();
		} 
		else if (event instanceof ChannelInfoEvent) {
			onChannelInfo(data);
		}
	}

	@Override
	public void onIrcEvent(ChannelInfoEvent event, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean match(int type) {
		return type == ConnectionEvent.CHANNEL_INFO_EVENT;
	}

	public abstract void onChannelInfo(Map<String, String> data);

	public abstract void onEndOf();
}
