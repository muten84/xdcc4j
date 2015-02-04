package it.luigibifulco.xdcc4j.downloader.impl;

import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;

public class Download {
	private String description;

	private XdccFileTransfer currentTransfer;

	private FileTransferStatusListener statusListener;

	private int percentage;

	private int rate;

	private String state;

	public Download(String d, XdccFileTransfer ft, FileTransferStatusListener l) {
		description = d;
		currentTransfer = ft;
		statusListener = l;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public XdccFileTransfer getCurrentTransfer() {
		return currentTransfer;
	}

	public void setCurrentTransfer(XdccFileTransfer currentTransfer) {
		this.currentTransfer = currentTransfer;
	}

	public FileTransferStatusListener getStatusListener() {
		return statusListener;
	}

	public void setStatusListener(FileTransferStatusListener statusListener) {
		this.statusListener = statusListener;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getState() {
		return this.getCurrentTransfer().getState().name();
	}

	public void setState(String state) {
		this.state = state;
	}

}
