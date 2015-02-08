package it.biffi.jirc.bot.listener;

import it.biffi.jirc.bot.event.ConnectionEvent;
import it.biffi.jirc.bot.event.FileTransferStartEvent;
import it.biffi.jirc.bot.event.MessageEvent;

import java.util.Map;

import org.jibble.pircbot.DccFileTransfer;

public abstract class FileTransferStartListener extends
		AbstractListener<FileTransferStartEvent> {

	@Override
	public void onIrcEvent(FileTransferStartEvent event, Map data) {

	}

	@Override
	public void onIrcEvent(FileTransferStartEvent event, Object data) {
		if (event instanceof FileTransferStartEvent) {
			onTransfer((DccFileTransfer) data);
		}

	}

	@Override
	protected boolean match(int type) {
		return type == FileTransferStartEvent.FILE_TRASNFER_START_EVENT;
	}

	public abstract void onTransfer(DccFileTransfer transfer);

}
