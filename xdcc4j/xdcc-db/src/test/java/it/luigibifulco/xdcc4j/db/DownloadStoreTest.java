package it.luigibifulco.xdcc4j.db;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DownloadStoreTest {

	@Test
	public final void testDownloadStore() {
		DownloadBeanStore store = new DownloadBeanStore("downloads.db");
		List<DownloadBean> beans = (List<DownloadBean>) store.getAll();
		for (DownloadBean downloadBean : beans) {
			System.out.println(downloadBean);
		}
		// store.remove(new DownloadBean("1e67ac40aca4a36e497dab2c7797b18f",
		// ""));
	}

	@Test
	public final void testRemove() {
		DownloadBeanStore store = new DownloadBeanStore("downloads.db");
		Assert.assertTrue(store.count() == 0);
		store.insert(new DownloadBean("1", "new download"));
		store.saveOrUpdate("1", new DownloadBean("2", "modified download"));
		DownloadBean toBeRemoved = store.get("2");
		Assert.assertTrue(toBeRemoved.getId().equals("2"));
		store.remove(toBeRemoved);
		Assert.assertTrue("Store size is: " + store.count(), store.count() == 0);
	}

	@Test
	public final void testUpdate() {
		DownloadBeanStore store = new DownloadBeanStore("downloads.db");
		store.insert(new DownloadBean("1", "new download"));
		store.saveOrUpdate("1", new DownloadBean("2", "modified download"));

		List<DownloadBean> beans = (List<DownloadBean>) store.getAll();
		for (DownloadBean downloadBean : beans) {
			System.out.println(downloadBean);
		}
		System.out.println(store.count());
		store.clear();
	}
}
