package it.luigibifulco.xdcc4j.ui.client.search.view;

import it.luigibifulco.xdcc4j.ui.client.search.event.SearchHandler;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventHandler;
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
	Label modalContent;

	@UiField
	Button clearSearchBt;

	public HeaderUi() {
		initWidget(uiBinder.createAndBindUi(this));
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

	private void alert(String text) {

		modalContent.setText(text);
		modal.show();
	}

}
