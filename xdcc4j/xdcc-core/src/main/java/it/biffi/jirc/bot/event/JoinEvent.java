package it.biffi.jirc.bot.event;

import java.util.HashMap;
import java.util.Map;

public class JoinEvent extends GenericEvent {
	public final static String CHANNEL = "CHANNEL";
	public final static String SENDER = "SENDER";
	public final static String LOGIN = "LOGIN";
	public final static String HOSTNAME = "HOSTNAME";

	

	public JoinEvent() {
	
	}

	

	@Override
	public int getType() {
		return JOIN_EVENT;
	}


	
	

}
