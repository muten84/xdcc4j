package it.biffi.jirc.bot.event;

public class XdccEvent extends GenericEvent {

	public static final String XDCC_NOTICE_LINES_TO_READ = "XDCC_NOTICE_LINES_TO_READ";

	public XdccEvent() {

	}

	@Override
	public int getType() {
		return XDCC_EVENT;
	}

}
