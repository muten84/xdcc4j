package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;

import java.util.Map;

public abstract class ConnectionListener extends
		AbstractListener<ConnectionEvent> {

	@Override
	public void onIrcEvent(ConnectionEvent event, Map data) {
		if (event instanceof ConnectionEvent) {
			onConnect();
		}
	}

	@Override
	public void onIrcEvent(ConnectionEvent event, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean match(int type) {
		return type == ConnectionEvent.CONNECTION_EVENT;
	}

	public abstract void onConnect();

}
