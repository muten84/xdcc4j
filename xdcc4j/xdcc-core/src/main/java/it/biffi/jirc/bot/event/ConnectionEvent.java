package it.biffi.jirc.bot.event;

public class ConnectionEvent extends GenericEvent {

	

	@Override
	public int getType() {
		return CONNECTION_EVENT;
	}

}
