package it.luigibifulco.xdcc4j.ft;

public interface XdccFileTransfer {

	public static enum TransferState {
		RUNNABLE, WORKING, IDLE, ABORTED, FINISHED
	}

	public interface FileTransferStatusListener {
		public void onStatusUpdate(String status);

		public void onProgress(int perc, int rate);

		public void onStart();

		public void onFinish();

		public void onCancel();

		public void onError(Throwable e);
	}

	public boolean start(FileTransferStatusListener listener);

	public boolean restart();

	public boolean cancel();

	public boolean pause();

	public boolean resume();

	public TransferState getState();

}
