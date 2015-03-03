package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.FileTrasnferFinishEvent;

import java.util.Map;

public abstract class FileTransferListener extends
		AbstractListener<FileTrasnferFinishEvent> {

	@Override
	public void onIrcEvent(FileTrasnferFinishEvent event, Map data) {

	}

	@Override
	public void onIrcEvent(FileTrasnferFinishEvent event, Object data) {
		if (event instanceof FileTrasnferFinishEvent) {
			onFinish((Exception) data);
		}

	}

	@Override
	protected boolean match(int type) {
		return type == FileTrasnferFinishEvent.FILE_TRASNFER_END_EVENT;
	}

	public abstract void onPreStartUpdate(String data);

	public abstract void onFinish(Exception finishedWithThisError);

}
