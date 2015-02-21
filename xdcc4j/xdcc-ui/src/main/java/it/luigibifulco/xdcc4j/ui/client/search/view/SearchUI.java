package it.luigibifulco.xdcc4j.ui.client.search.view;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.incubator.Table;
import com.github.gwtbootstrap.client.ui.incubator.TableHeader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SearchUI extends Composite {

	private static SearchUIUiBinder uiBinder = GWT
			.create(SearchUIUiBinder.class);

	interface SearchUIUiBinder extends UiBinder<Widget, SearchUI> {
	}

	public SearchUI() {
		initWidget(uiBinder.createAndBindUi(this));
		listHeader
				.setText("Inserisci una parola chiave per avviare la ricerca e non dimenticare di collegarti ad un server se non l'hai ancora fatto...");
	}

	@UiField
	Table list;

	@UiField
	TableHeader listHeader;

	public void clearResult() {
		listHeader
		.setText("Inserisci una parola chiave per avviare la ricerca e non dimenticare di collegarti ad un server se non l'hai ancora fatto...");
		list.clear();
	}

	public void setSearchResult(List<String> result) {
		listHeader.setText("Risultato della ricerca:");
		for (String string : result) {
			Row r = new Row();
			// r.setHeight("20px");
			com.github.gwtbootstrap.client.ui.Label l = new com.github.gwtbootstrap.client.ui.Label(
					string);
			l.addStyleName("text-center");
			l.setHeight("20px");
			l.addStyleName("rowResult");
			r.addStyleName("text-left");
			r.setPullRight(false);
			r.setMarginTop(3);
			l.setType(LabelType.INFO);
			r.add(l);
			list.add(r);
		}
	}

	
}
