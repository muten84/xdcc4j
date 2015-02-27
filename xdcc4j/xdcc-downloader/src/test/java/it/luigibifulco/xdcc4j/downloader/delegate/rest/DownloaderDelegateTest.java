package it.luigibifulco.xdcc4j.downloader.delegate.rest;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;
import it.luigibifulco.xdcc4j.downloader.core.service.DownloaderServiceModule;
import it.luigibifulco.xdcc4j.downloader.service.DownloaderServletModule;
import it.luigibifulco.xdcc4j.search.engine.SearchEngineType;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ DownloaderServiceModule.class, DownloaderServletModule.class })
public class DownloaderDelegateTest {

	private Server server;

	private int port = 8080;

	private ReentrantLock lock = new ReentrantLock();

	@Inject
	private XdccDownloader downloaderCore;

	private static Logger logger = LoggerFactory
			.getLogger(DownloaderDelegateTest.class);

	@Before
	public void startServer() throws Exception {
		logger.info("<<<<<STARTing SERVER");
		if (server != null && server.isStarted()) {
			return;
		}
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				lock.lock();
				server = new Server(port);

				ServletContextHandler context = new ServletContextHandler(
						server, "/", ServletContextHandler.SESSIONS);
				context.addFilter(GuiceFilter.class, "/*", EnumSet
						.<javax.servlet.DispatcherType> of(
								javax.servlet.DispatcherType.REQUEST,
								javax.servlet.DispatcherType.ASYNC));

				context.addServlet(DefaultServlet.class, "/*");

				try {
					server.start();
					logger.info("SERVER STARTED");

					lock.unlock();
					server.join();
					// lock.unlock();
				} catch (Exception e) {

				} finally {
					lock.unlock();
				}

			}
		});

		t.start();
	}

	@After
	public void destroy() throws Exception {

	}

	@Test
	public void testStartDownload() throws InterruptedException,
			ClientProtocolException, IOException {
		Thread.sleep(1000);
		try {
			lock.lock();
			String id = downloaderCore.search("xdccit", "divx").keySet()
					.iterator().next();
			HttpClient client = HttpClients.createDefault();
			String method = "startDownload";

			HttpGet getRequest = new HttpGet("http://127.0.0.1:" + port
					+ "/downloader/" + method + "?" + "id=" + id);
			// getRequest.setParams(params);
			HttpResponse response = client.execute(getRequest);

			Assert.assertTrue("response status code is: "
					+ response.getStatusLine().getStatusCode(), response
					.getStatusLine().getStatusCode() == 200);

			InputStream stream = response.getEntity().getContent();
			Assert.assertNotNull(stream);
			byte[] bufer = new byte[stream.available()];
			int read = stream.read(bufer);
			Assert.assertTrue(read == bufer.length);
			String result = new String(bufer);
			System.out.println(result);
			// Assert.assertTrue(.equals(id));
		} finally {
			lock.unlock();
		}
	}

	@Test
	public void testGetSearch() throws ClientProtocolException, IOException,
			InterruptedException {
		Thread.sleep(1000);
		try {
			lock.lock();
			HttpClient client = HttpClients.createDefault();
			String method = "search";
			String what = "divx";
			String where = SearchEngineType.xdccfinder.name();
			String params = "what=" + what + "&where=" + where;
			HttpGet getRequest = new HttpGet("http://127.0.0.1:" + port
					+ "/services/downloader/" + method + "?" + params);
			// getRequest.setParams(params);
			HttpResponse response = client.execute(getRequest);
			Assert.assertTrue("response status code is: "
					+ response.getStatusLine().getStatusCode(), response
					.getStatusLine().getStatusCode() == 200);
		} finally {
			lock.unlock();
		}
	}
}
