package it.luigibifulco.xdcc4j.downloader.core.util;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.downloader.core.model.Download;

public class ConvertUtil {

	public static DownloadBean convert(Download down) {
		DownloadBean bean = new DownloadBean(down.getId(),
				down.getDescription());
		bean.setPerc(down.getPercentage());
		bean.setRate(down.getRate());
		bean.setState(down.getState());
		return bean;
	}

}
