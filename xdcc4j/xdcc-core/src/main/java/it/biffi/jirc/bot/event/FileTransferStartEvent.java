package it.biffi.jirc.bot.event;

public class FileTransferStartEvent extends GenericEvent {
	public final static String TRANSFER = "TRANSFER";

	public FileTransferStartEvent() {

	}

	@Override
	public int getType() {
		return FILE_TRASNFER_START_EVENT;
	}
}
