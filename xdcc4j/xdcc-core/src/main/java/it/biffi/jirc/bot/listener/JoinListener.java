package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.JoinEvent;

import java.util.Map;

public abstract class JoinListener extends AbstractListener<JoinEvent> {

	@Override
	public void onIrcEvent(JoinEvent event, Map data) {
		if (event instanceof JoinEvent) {
			onJoin(data);
		}
	}

	@Override
	public void onIrcEvent(JoinEvent event, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean match(int type) {
		return type == ConnectionEvent.JOIN_EVENT;
	}

	public abstract void onJoin(Map<String, String> data);
}
