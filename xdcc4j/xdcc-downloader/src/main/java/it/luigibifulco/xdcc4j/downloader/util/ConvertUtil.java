package it.luigibifulco.xdcc4j.downloader.util;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.TransferState;

public class ConvertUtil {

	public static DownloadBean convertFromXdccRequest(XdccRequest r) {
		return new DownloadBean(r.getId(), r.getDescription());
	}

	public static DownloadBean convertFromXdccRequest(Download d) {
		DownloadBean db = new DownloadBean(d.getId(), d.getDescription());
		db.setPerc(d.getPercentage());
		db.setRate(d.getRate());
		db.setState(TransferState.IDLE.name());
		return db;
	}

}
