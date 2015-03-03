package it.biffi.jirc.bot.event;

public class FileTrasnferFinishEvent extends GenericEvent {
	public final static String TRANSFER_FINISH = "TRANSFER_FINISH";

	public FileTrasnferFinishEvent() {

	}

	@Override
	public int getType() {
		return FILE_TRASNFER_END_EVENT;
	}
}
