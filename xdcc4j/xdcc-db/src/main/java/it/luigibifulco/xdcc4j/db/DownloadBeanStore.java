package it.luigibifulco.xdcc4j.db;

import java.util.List;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;

public class DownloadBeanStore extends ObjectStore<DownloadBean> {

	public DownloadBeanStore(String name) {
		super(name, DownloadBean.class);

	}

	@Override
	protected Class<DownloadBean> getEntityType() {
		return DownloadBean.class;
	}

	@Override
	public List<DownloadBean> searchByExample(DownloadBean e) {
		// TODO Auto-generated method stub
		return null;
	}

}
