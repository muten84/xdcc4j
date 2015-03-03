package it.luigibifulco.xdcc4j.ui.client.search.view;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DownloadsUI extends Composite {
	private static DownloadsUiBinder uiBinder = GWT
			.create(DownloadsUiBinder.class);

	interface DownloadsUiBinder extends UiBinder<Widget, DownloadsUI> {
	}
	
	public DownloadsUI() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}
}
