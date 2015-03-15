package it.luigibifulco.xdcc4j.ft.impl;

import it.biffi.jirc.bot.BotException;
import it.biffi.jirc.bot.FileTransferBot;
import it.biffi.jirc.bot.FileTransferConfig;
import it.biffi.jirc.bot.listener.FileTransferListener;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;

import java.io.File;
import java.text.Normalizer.Form;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jibble.pircbot.DccFileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XdccFileTransferImpl implements XdccFileTransfer {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(XdccFileTransferImpl.class);

	private TransferState state;

	protected FileTransferBot bot;

	private final XdccRequest request;

	// private Timer t;

	ScheduledFuture currentTask;

	ScheduledExecutorService scheduler;

	private long connectTimeout;

	private FileTransferStatusListener listener;

	public XdccFileTransferImpl(XdccRequest request, long connectTimeout,
			long requestTTL) throws BotException {
		state = TransferState.IDLE;
		this.request = request;
		this.request.setTtl(requestTTL);
		this.connectTimeout = connectTimeout;
		scheduler = Executors.newScheduledThreadPool(1);

	}

	protected void init() throws BotException {
		LOGGER.info("Request: " + request);
		bot = new FileTransferBot(false);

		String name = UUID.randomUUID().toString();

		LOGGER.info("connecting to host: " + request.getHost());
		boolean connected = bot.connect("[Bot" + name.substring(0, 5) + "]",
				request.getHost(), connectTimeout);
		if (connected) {
			state = TransferState.RUNNABLE;
			LOGGER.info("connected");
			FileTransferConfig config = new FileTransferConfig();

			String chan = request.getChannel().replace("#", "");
			config.setSourceChannel(chan);
			config.setSourcePeer(request.getPeer());

			config.setInputCommand("xdcc send #" + request.getResource());
			// TODO pass timeout to start method
			bot.start(config);
		}
	}

	@Override
	public boolean start(final FileTransferStatusListener l) {
		if (this.listener != null) {
			throw new RuntimeException(
					"Listener alreay attached cancel download first");
		}
		this.listener = l;

		if (state == TransferState.IDLE) {
			try {
				init();
			} catch (BotException e) {
				return false;
			}
		}
		if (state == TransferState.RUNNABLE) {
			final DccFileTransfer transfer = bot.requestPacket(
					request.getTtl(), new FileTransferListener() {

						@Override
						public void onFinish(Exception e) {
							state = TransferState.FINISHED;
							if (e == null) {
								LOGGER.info("FINISH TRANSFER SUCCESS");
								l.onFinish();
							} else {
								LOGGER.info("FINISH TRANSFER ERROR: "
										+ e.getMessage());
								l.onError(e);
								// e.printStackTrace();
							}

						}

						@Override
						public void onPreStartUpdate(String data) {
							LOGGER.info("onPreStartUpdate:" + data);
							l.onStatusUpdate(data);

						}
					});
			if (transfer != null) {
				state = TransferState.WORKING;

				File file = new File(request.getDestination() + "/"
						+ transfer.getFile().getName());
				LOGGER.info("Receiveing file: " + file.getAbsolutePath());

				transfer.receive(file, true);
				l.onStart(file.getAbsolutePath());
				Runnable task = () -> {

					int perc = (int) transfer.getProgressPercentage();
					int rate = (int) transfer.getTransferRate();
					if (perc >= 100 || state == TransferState.FINISHED) {
						state = TransferState.FINISHED;
						cancel();
						l.onFinish();
						return;
					}
					LOGGER.info("Transfer state: " + perc + "%" + "rate: "
							+ rate);
					l.onProgress(perc, rate);

				};
				currentTask = scheduler.scheduleAtFixedRate(task, 3, 1,
						TimeUnit.SECONDS);

				return true;
			} else {
				LOGGER.info("packet requested but transfer not started");
				// state = TransferState.FINISHED;
				cancel();
				l.onError(new RuntimeException(
						"packet requested but transfer not started please restart download"));
				// l.onFinish();
				return false;
			}
		} else {
			LOGGER.info("state of transfer is not in runnable");
			state = TransferState.ABORTED;
			l.onError(new RuntimeException(
					"state of transfer is not in runnable please restart download or change server"));
			return false;
		}

	}

	@Override
	public boolean cancel() {
		boolean fromworking = false;
		if (state == TransferState.WORKING) {
			fromworking = true;
		}
		bot.sendMessage(request.getPeer(), "xdcc remove");
		bot.sendMessage(request.getPeer(), "xdcc remove #" + request.getResource());
		state = TransferState.ABORTED;		
		try {
			if (bot != null) {
				bot.stop();
			}
		} catch (Exception e) {

		}
		try {
			if (currentTask != null) {
				currentTask.cancel(true);
			}
		} catch (Exception e) {

		}
		try {
			scheduler.shutdownNow();
			scheduler = null;
		} catch (Exception e) {

		}
		if (fromworking) {
			listener.onCancel();
		}
		this.listener = null;
		// request = null;
		return true;
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TransferState getState() {
		return state;
	}

	@Override
	public boolean restart() {
		this.cancel();
		try {
			this.init();
		} catch (BotException e) {
			return false;
		}
		return true;
	}

	public XdccRequest getRequest() {
		return request;
	}

}
