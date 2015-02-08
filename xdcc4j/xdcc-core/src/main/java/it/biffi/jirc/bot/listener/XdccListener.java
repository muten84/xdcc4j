package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.XdccEvent;

import java.util.Map;

public abstract class XdccListener extends AbstractListener<XdccEvent> {

	@Override
	public void onIrcEvent(XdccEvent event, Map data) {
		if (event instanceof XdccEvent) {
			String lines = (String) data
					.get(XdccEvent.XDCC_NOTICE_LINES_TO_READ);
			if (lines != null && !lines.isEmpty()) {
				onLineCount(Integer.valueOf(lines));
			}
		}

	}

	@Override
	public void onIrcEvent(XdccEvent event, Object data) {

	}

	@Override
	protected boolean match(int type) {
		return type == ConnectionEvent.XDCC_EVENT;
	}

	public abstract void onLineCount(int lines);
}
