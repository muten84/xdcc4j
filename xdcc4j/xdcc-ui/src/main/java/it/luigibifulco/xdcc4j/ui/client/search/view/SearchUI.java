package it.luigibifulco.xdcc4j.ui.client.search.view;

import it.luigibifulco.xdcc4j.common.model.DownloadBean;
import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.event.DownloadRequestHandler;
import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.incubator.Table;
import com.github.gwtbootstrap.client.ui.incubator.TableHeader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SearchUI extends Composite {

	public final static Type<DownloadRequestHandler> DOWNLOAD_REQUEST_START = new Type<DownloadRequestHandler>();

	public static class DownloadRequestEvent extends
			GwtEvent<DownloadRequestHandler> {

		private DownloadBean download;

		public DownloadRequestEvent(DownloadBean d) {
			this.download = d;
		}

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<DownloadRequestHandler> getAssociatedType() {
			return DOWNLOAD_REQUEST_START;
		}

		@Override
		protected void dispatch(DownloadRequestHandler handler) {
			handler.onDownloadRequest(download);

		}
	}

	private static SearchUIUiBinder uiBinder = GWT
			.create(SearchUIUiBinder.class);

	interface SearchUIUiBinder extends UiBinder<Widget, SearchUI> {
	}

	public SearchUI() {
		currentResult = new HashMap<Integer, DownloadBean>();
		initWidget(uiBinder.createAndBindUi(this));
		listHeader
				.setText("Inserisci una parola chiave per avviare la ricerca e non dimenticare di collegarti ad un server se non l'hai ancora fatto...");
	}

	@UiField
	Table list;

	@UiField
	TableHeader listHeader;

	private Map<Integer, DownloadBean> currentResult;

	public void clearResult() {
		listHeader
				.setText("Inserisci una parola chiave per avviare la ricerca e non dimenticare di collegarti ad un server se non l'hai ancora fatto...");
		list.clear();
	}

	public void setSearchResult() {
		listHeader.setText("Risultato della ricerca:");
		Map<String, DownloadBean> map = Registry.get("searchresult");
		Collection<DownloadBean> beans = map.values();
		int cnt = 0;
		for (DownloadBean db : beans) {
			if (db == null)
				continue;
			currentResult.put(cnt, db);
			cnt++;
		}
		Set<Integer> idxs = currentResult.keySet();
		for (Integer idx : idxs) {
			Row r = new Row();
			// r.setHeight("20px");
			DownloadBean b = currentResult.get(idx);
			com.github.gwtbootstrap.client.ui.Label lID = new com.github.gwtbootstrap.client.ui.Label(
					"" + idx);
			lID.setVisible(false);
			com.github.gwtbootstrap.client.ui.Label l = new com.github.gwtbootstrap.client.ui.Label(
					b.getDesc());
			l.addStyleName("text-center");
			l.setHeight("20px");
			l.addStyleName("rowResult");
			r.addStyleName("text-left");
			r.setPullRight(false);
			r.setMarginTop(3);
			l.setType(LabelType.INFO);
			r.add(l);
			l.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					com.github.gwtbootstrap.client.ui.Label l = (Label) event
							.getSource();
					Row r = (Row) l.getParent();
					com.github.gwtbootstrap.client.ui.Label lid = (Label) r
							.getWidget(1);
					String id = lid.getText();
					DownloadBean d = currentResult.get(Integer.valueOf(id));
					fireEvent(new DownloadRequestEvent(d));
				}
			});
			r.add(lID);
			list.add(r);
		}
	}
}
