package it.biffi.jirc.bot.event;

public class MessageEvent extends GenericEvent {
	public final static String CHANNEL = "CHANNEL";
	public final static String SENDER = "SENDER";
	public final static String LOGIN = "LOGIN";
	public final static String HOSTNAME = "HOSTNAME";
	public final static String MESSAGE = "MESSAGE";
	public final static String SERVER_RESPONSE_TYPE = "SERVER_RESPONSE_TYPE";

	/**/
	public final static String _401 = "401";

	public MessageEvent() {

	}

	@Override
	public int getType() {
		return MESSAGE_EVENT;
	}

}
