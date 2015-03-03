package it.biffi.jirc.bot;

import it.biffi.jirc.bot.event.GenericEvent;
import it.biffi.jirc.bot.listener.AbstractListener;

public interface Bot {

	public void start(BotConfig config) throws BotException;

	public void stop();

	public void sendMessage(String to, String msg);

	public void addEventListener(GenericEvent event,
			AbstractListener<?> listener);

}
