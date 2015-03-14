package it.luigibifulco.xdcc4j.ui.client.search.view;

import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderUi extends Composite {

	public final static Type<SearchHandler> SEARCH = new Type<SearchHandler>();

	public final static Type<SearchHandler> DOWNLOADS = new Type<SearchHandler>();

	public final static Type<SearchHandler> CLEAR = new Type<SearchHandler>();

	private static HeaderUiUiBinder uiBinder = GWT
			.create(HeaderUiUiBinder.class);

	public static interface HeaderUiUiBinder extends UiBinder<Widget, HeaderUi> {
	}

	@UiField
	Modal modal;

	@UiField
	Modal modalClosable;

	@UiField
	Button okBtn;

	@UiField
	NavSearch searchText;

	@UiField
	Button searchBt;

	// @UiField
	// Dropdown dropdown;

	// @UiField
	// Dropdown toolsDropdown;

	@UiField
	Label modalContent;

	@UiField
	Label modalClosableContent;

	@UiField
	Button clearSearchBt;

	@UiField
	Button showDownloadsBt;

	// @UiField
	// Dropdown dropdownChannel;

	public HeaderUi() {
		initWidget(uiBinder.createAndBindUi(this));
		// int links = dropdown.getWidgetCount();
		// final UnorderedList servers = dropdown.getMenuWiget();
		// int links = dropdown.getMenuWiget().getWidgetCount();
		// for (int i = 0; i < links; i++) {
		// if (!(dropdown.getMenuWiget().getWidget(i) instanceof NavLink)) {
		// continue;
		// }
		// final NavLink slink = (NavLink) dropdown.getMenuWiget()
		// .getWidget(i);
		// slink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// // final NavLink link = ((NavLink) event.getSource());
		//
		// final String name = slink.getText();
		// RequestBuilder builder = new RequestBuilder(
		// RequestBuilder.GET,
		// "services/downloader/setServer?server="
		// + name.trim());
		// try {
		//
		// builder.setCallback(new RequestCallback() {
		//
		// @Override
		// public void onResponseReceived(Request request,
		// Response response) {
		// for (Widget w : servers) {
		// final NavLink tmp = (NavLink) w;
		// tmp.setActive(false);
		// }
		// slink.setActive(true);
		// if (response.getStatusCode() == 200) {
		// alert("Correttamente connesso a: " + name);
		// }
		//
		// }
		//
		// @Override
		// public void onError(Request request,
		// Throwable exception) {
		// alert("Non riesco a collegarmi al server "
		// + name);
		// }
		// });
		// builder.send();
		// } catch (RequestException e) {
		// alert("Un disturbo nella forza.... : " + e.getMessage());
		// }
		//
		// }
		// });
		// }
		// NavLink downloadsLink = (NavLink) toolsDropdown.getMenuWiget()
		// .getWidget(0);
		// downloadsLink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// fireEvent(new ShowDownloadsEvent());
		//
		// }
		// });

		// final UnorderedList channels = (UnorderedList) dropdownChannel
		// .getMenuWiget();
		// for (Widget widget : channels) {
		// final NavLink chLink = (NavLink) widget;
		// Registry.register(chLink.getText(), chLink);
		// chLink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// for (Widget w : channels) {
		// final NavLink tmp = (NavLink) w;
		// Registry.<NavLink> get(tmp.getText()).setActive(false);
		// }
		// chLink.setActive(true);
		// Registry.register("searchType", chLink.getText());
		// }
		//
		// });
		// }

	}

	public static class ShowDownloadsEvent extends GwtEvent<SearchHandler> {

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<SearchHandler> getAssociatedType() {
			return DOWNLOADS;
		}

		@Override
		protected void dispatch(SearchHandler handler) {
			handler.onViewDownloads();

		}

	}

	public static class ClearSearchEvent extends GwtEvent<SearchHandler> {

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<SearchHandler> getAssociatedType() {
			return CLEAR;
		}

		@Override
		protected void dispatch(SearchHandler handler) {
			handler.onClear();

		}

	}

	public static class SearchEvent extends GwtEvent<SearchHandler> {

		private String text;

		public SearchEvent(String text) {
			this.text = text;
		}

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<SearchHandler> getAssociatedType() {
			return SEARCH;
		}

		@Override
		protected void dispatch(SearchHandler handler) {
			handler.onSearch(text);

		}

	}

	@UiHandler("showDownloadsBt")
	void onClickShowDownloads(ClickEvent e) {
		fireEvent(new ShowDownloadsEvent());
	}

	@UiHandler("searchBt")
	void onClickSearch(ClickEvent e) {
		search();
	}

	@UiHandler("clearSearchBt")
	void onClickClear(ClickEvent e) {

		fireEvent(new ClearSearchEvent());
	}

	private void search() {
		String text = searchText.getTextBox().getText();
		if (text == null || text.isEmpty()) {
			clearSearchBt.setEnabled(false);
			info("Prova ad inserire una o piu' parole chiave per avviare la ricerca...");
			return;
		}

		fireEvent(new SearchEvent(text));
		clearSearchBt.setEnabled(true);
	}

	@UiHandler("searchText")
	void onSubmit(SubmitEvent e) {
		e.cancel();
		search();

	}

	public void info(String text) {
		modalClosableContent.setTitle("");
		modalClosableContent.setText(text);

		modalClosable.setCloseVisible(true);

		modalClosable.show();
	}

	public void wait(String text) {
		modalContent.setTitle("");
		modalContent.setText(text);

		modal.setCloseVisible(false);

		modal.show();
	}

	public void closeWait() {
		// modal.toggle();
		modal.hide();
		// modal.setVisible(false);
	}

	@UiHandler("okBtn")
	public void onCloseAlert(ClickEvent e) {
		modalClosable.hide();
	}

}
