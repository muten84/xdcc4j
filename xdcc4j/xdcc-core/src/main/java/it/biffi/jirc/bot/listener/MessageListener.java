package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.MessageEvent;

import java.util.Map;

public abstract class MessageListener extends AbstractListener<MessageEvent> {

	@Override
	public void onIrcEvent(MessageEvent event, Map data) {
		if (event instanceof MessageEvent) {
			onMessage(data);
		}
	}

	@Override
	public void onIrcEvent(MessageEvent event, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean match(int type) {
		return type == ConnectionEvent.MESSAGE_EVENT;
	}

	public abstract void onMessage(Map<String, String> data);
}
