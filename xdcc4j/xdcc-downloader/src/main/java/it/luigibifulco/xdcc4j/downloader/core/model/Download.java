package it.luigibifulco.xdcc4j.downloader.core.model;

import it.luigibifulco.xdcc4j.ft.XdccFileTransfer;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.FileTransferStatusListener;

import java.io.Serializable;

public class Download implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5214765781983881516L;

	private String id;

	private String description;

	private XdccFileTransfer currentTransfer;

	private FileTransferStatusListener statusListener;

	private int percentage;

	private int rate;

	private String state;

	public Download(String id, String d, XdccFileTransfer ft,
			FileTransferStatusListener l) {
		this.id = id;
		description = d;
		currentTransfer = ft;
		statusListener = l;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
