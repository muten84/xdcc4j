package it.luigibifulco.xdcc4j.ui.client.search.view;

import it.luigibifulco.xdcc4j.ui.client.Registry;
import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavList;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.github.gwtbootstrap.client.ui.base.UnorderedList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
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
	NavSearch searchText;

	@UiField
	Dropdown dropdown;

	@UiField
	Dropdown toolsDropdown;

	@UiField
	Label modalContent;

	@UiField
	Button clearSearchBt;

	@UiField
	Dropdown dropdownChannel;

	public HeaderUi() {
		initWidget(uiBinder.createAndBindUi(this));
		// int links = dropdown.getWidgetCount();
		final UnorderedList servers = dropdown.getMenuWiget();
		int links = dropdown.getMenuWiget().getWidgetCount();
		for (int i = 0; i < links; i++) {
			if (!(dropdown.getMenuWiget().getWidget(i) instanceof NavLink)) {
				continue;
			}
			final NavLink slink = (NavLink) dropdown.getMenuWiget()
					.getWidget(i);
			slink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// final NavLink link = ((NavLink) event.getSource());

					final String name = slink.getText();
					RequestBuilder builder = new RequestBuilder(
							RequestBuilder.GET,
							"services/downloader/setServer?server="
									+ name.trim());
					try {

						builder.setCallback(new RequestCallback() {

							@Override
							public void onResponseReceived(Request request,
									Response response) {
								for (Widget w : servers) {
									final NavLink tmp = (NavLink) w;
									tmp.setActive(false);
								}
								slink.setActive(true);
								if (response.getStatusCode() == 200) {
									alert("Correttamente connesso a: " + name);
								}

							}

							@Override
							public void onError(Request request,
									Throwable exception) {
								alert("Non riesco a collegarmi al server "
										+ name);
							}
						});
						builder.send();
					} catch (RequestException e) {
						alert("Un disturbo nella forza.... : " + e.getMessage());
					}

				}
			});
		}
		NavLink downloadsLink = (NavLink) toolsDropdown.getMenuWiget()
				.getWidget(0);
		downloadsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new ShowDownloadsEvent());

			}
		});

		final UnorderedList channels = (UnorderedList) dropdownChannel
				.getMenuWiget();
		for (Widget widget : channels) {
			final NavLink chLink = (NavLink) widget;
			Registry.register(chLink.getText(), chLink);
			chLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (Widget w : channels) {
						final NavLink tmp = (NavLink) w;
						Registry.<NavLink> get(tmp.getText()).setActive(false);
					}
					chLink.setActive(true);
					Registry.register("searchType", chLink.getText());
				}

			});
		}

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

	@UiHandler("clearSearchBt")
	void onClick(ClickEvent e) {

		fireEvent(new ClearSearchEvent());
	}

	@UiHandler("searchText")
	void onSubmit(SubmitEvent e) {
		String text = searchText.getTextBox().getText();
		if (text == null || text.isEmpty()) {
			clearSearchBt.setEnabled(false);
			alert("Prova ad inserire una o piu' parole chiave per avviare la ricerca...");
			return;
		}
		e.cancel();
		fireEvent(new SearchEvent(text));
		clearSearchBt.setEnabled(true);
		// alert.setVisible(true);
		// fillTable();

	}

	public void alert(String text) {

		modalContent.setText(text);
		modal.show();
	}

}
