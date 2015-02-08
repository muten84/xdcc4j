package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.GenericEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventListenerManager {

	protected Map<GenericEvent, List<AbstractListener<?>>> listeners;

	public EventListenerManager() {
		if (listeners == null) {
			listeners = new HashMap<GenericEvent, List<AbstractListener<?>>>();
		}

	}

	public void removeAll() {
		listeners.clear();
		// listeners = null;
	}

	public void addEventListener(GenericEvent event,
			AbstractListener<?> listener) {

		List<AbstractListener<?>> list = listeners.get(event.getType());
		if (list == null) {
			listeners.put(event, new ArrayList<AbstractListener<?>>());
		}
		list = listeners.get(event);
		list.add(listener);
		if (listeners.containsKey(event)) {
			Set<GenericEvent> events = listeners.keySet();
			for (GenericEvent genericEvent : events) {
				if (genericEvent.getType() == event.getType()) {
					// /System.out.println("addinge listener " + listener
					// + " to event: " + genericEvent);
					genericEvent.addObserver(listener);
				}
			}
		}

	}

	public void addEvent(GenericEvent event, Object o) {
		System.out.println("adding event: " + event);
		if (listeners.containsKey(event)) {
			Set<GenericEvent> events = listeners.keySet();
			for (GenericEvent genericEvent : events) {
				if (genericEvent.getType() == event.getType()) {
					// System.out.println("adding event data: " +
					// event.getData());
					genericEvent.notifyObservers(o);
					break;
				}
			}
		}
	}

	public void addEvent(GenericEvent event) {
		// System.out.println("adding event: " + event);
		if (listeners.containsKey(event)) {
			Set<GenericEvent> events = listeners.keySet();
			for (GenericEvent genericEvent : events) {
				if (genericEvent.getType() == event.getType()) {
					// System.out.println("adding event data: " +
					// event.getData());
					genericEvent.notifyObservers(event.getData());
					break;
				}
			}
		}
	}
}
