package it.luigibifulco.xdcc4j.downloader.util;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.ft.XdccFileTransfer.TransferState;
import it.luigibifulco.xdcc4j.ft.impl.XdccFileTransferImpl;

public class ConvertUtil {

	public static DownloadBean convertFromXdccRequest(XdccRequest r) {
		DownloadBean bean = new DownloadBean(r.getId(), r.getDescription());
		bean.setServer(r.getHost());
		bean.setFrom(r.getPeer());
		bean.setResource(r.getResource());
		bean.setChannel(r.getChannel());
		return bean;
	}

	public static DownloadBean convertFromXdccRequest(Download d) {
		DownloadBean db = new DownloadBean(d.getId(), d.getDescription());
		db.setPerc(d.getPercentage());
		db.setRate(d.getRate());
		db.setState(TransferState.IDLE.name());
		if (d.getCurrentTransfer() != null) {
			XdccFileTransferImpl transfer = (XdccFileTransferImpl) d
					.getCurrentTransfer();
			if (transfer != null) {
				db.setServer(transfer.getRequest().getHost());
				db.setFrom(transfer.getRequest().getPeer());
				db.setResource(transfer.getRequest().getResource());
				db.setChannel(transfer.getRequest().getChannel());
			}
		}
		return db;
	}

}
