package it.luigibifulco.xdcc4j.downloader.core.util;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;
import it.luigibifulco.xdcc4j.ft.impl.XdccFileTransferImpl;

public class ConvertUtil {

	public static Download convert(DownloadBean db) {
		Download d = new Download(db.getId(), db.getDesc(), null, null);
		d.setCurrentTransfer(null);
		d.setPercentage((int) db.getPerc());
		d.setRate((int) db.getRate());
		d.setState(db.getState());
		return d;
	}

	public static DownloadBean convert(Download down) {
		DownloadBean bean = new DownloadBean(down.getId(),
				down.getDescription());
		bean.setPerc(down.getPercentage());
		bean.setRate(down.getRate());
		bean.setState(down.getState());
		if (down.getCurrentTransfer() != null) {
			XdccFileTransferImpl transfer = (XdccFileTransferImpl) down
					.getCurrentTransfer();
			if (transfer != null) {
				bean.setServer(transfer.getRequest().getHost());
				bean.setFrom(transfer.getRequest().getPeer());
				bean.setResource(transfer.getRequest().getResource());
				bean.setChannel(transfer.getRequest().getChannel());
			}
		}
		return bean;
	}

}
