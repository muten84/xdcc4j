package it.biffi.jirc.bot;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.listener.ConnectionListener;

public class BotConnector extends AbstractPircBot implements IBotConnector {

	@Override
	public void addConnectionListener(ConnectionListener listener) {
		ConnectionEvent event = new ConnectionEvent();
		event.addObserver(listener);
		getEbus().addEventListener(event, listener);

	}

}
