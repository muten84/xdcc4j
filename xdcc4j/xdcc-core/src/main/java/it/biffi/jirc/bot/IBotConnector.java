package it.biffi.jirc.bot;

import it.biffi.jirc.bot.listener.ConnectionListener;

public interface IBotConnector extends Bot {

	public void addConnectionListener(ConnectionListener listener);

}
