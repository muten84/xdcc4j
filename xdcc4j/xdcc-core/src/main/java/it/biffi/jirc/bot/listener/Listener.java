package it.biffi.jirc.bot.listener;

import java.util.Map;

public interface Listener<EventType> {

	public void onIrcEvent(EventType event, Map data);

	public void onIrcEvent(EventType event, Object data);
}
