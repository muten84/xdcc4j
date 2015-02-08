package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.GenericEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public abstract class AbstractListener<EventType> implements Observer,
		Listener<EventType> {

	/**
	 * @param args
	 */

	@SuppressWarnings("unchecked")
	@Override
	public final void update(Observable o, Object arg) {
		if (o instanceof GenericEvent) {
			GenericEvent e = (GenericEvent) o;
			int type = ((GenericEvent) o).getType();
			if (match(type)) {
				Map<String, String> data = null;
				if (arg == null) {
					data = new HashMap<String, String>();
				}
				if (arg instanceof Map) {
					onIrcEvent((EventType) e, (Map<String, String>) arg);
				} else {
					onIrcEvent((EventType) e, arg);
				}
			}
		}
	}

	protected abstract boolean match(int type);

}
