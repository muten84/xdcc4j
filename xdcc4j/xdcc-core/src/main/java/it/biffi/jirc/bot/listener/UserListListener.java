package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ChannelInfoEvent;

import java.util.List;
import java.util.Map;

public abstract class UserListListener extends ChannelInfoListener {

	@Override
	public void onIrcEvent(ChannelInfoEvent event, Map data) {
		if (event instanceof ChannelInfoEvent
				&& data.get(ChannelInfoEvent.USERS) != null) {
			onUserList((List<String>) data.get(ChannelInfoEvent.USERS));
		}
	}

	@Override
	public void onChannelInfo(Map<String, String> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndOf() {
		// TODO Auto-generated method stub

	}

	public abstract void onUserList(List<String> users);

}
