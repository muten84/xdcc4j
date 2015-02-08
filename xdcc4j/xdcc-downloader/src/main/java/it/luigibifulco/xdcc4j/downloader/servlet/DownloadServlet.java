package it.luigibifulco.xdcc4j.downloader.servlet;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.downloader.core.XdccDownloader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DownloadServlet extends HttpServlet {

	@Inject
	private XdccDownloader service;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5886967164739667805L;

	private org.slf4j.Logger logger = LoggerFactory
			.getLogger(DownloadServlet.class);

	@Override
	public void init() throws ServletException {
		logger.info("init downdload sdervlet service: " + service);

		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("do get: " + req + " - " + resp);
		String operation = req.getParameter("op");

		switch (operation) {
		case "search":
			String where = req.getParameter("where");
			String text = req.getParameter("text");
			Map<String, XdccRequest> map = service.search(where, text);
			StringBuffer buf = new StringBuffer();
			// Collection<XdccRequest> reqs = map.values();
			Set<String> s = map.keySet();
			for (String string : s) {
				buf.append(string);
				buf.append("\n");
			}
			resp.getOutputStream().write(buf.toString().getBytes());
			resp.flushBuffer();
			break;

		default:
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
