package it.biffi.jirc.bot.event;

public class ChannelInfoEvent extends GenericEvent {

	/**/
	public final static String HOSTNAME = "HOSTNAME";
	public final static String CHANNEL = "CHANNEL";
	public final static String CHANNEL_USER_COUNT = "CHANNEL_USER_COUNT";
	public final static String CHANNEL_TOPIC = "CHANNEL_TOPIC";
	public final static String STATUS = "STATUS";
	public final static String USERS = "USERS";

	@Override
	public int getType() {
		return CHANNEL_INFO_EVENT;
	}
}
