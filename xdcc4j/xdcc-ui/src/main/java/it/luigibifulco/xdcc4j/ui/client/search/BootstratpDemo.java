package it.luigibifulco.xdcc4j.ui.client.search;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.NavSearch;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.incubator.Table;
import com.github.gwtbootstrap.client.ui.incubator.TableHeader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BootstratpDemo extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	Modal modal;

	@UiField
	NavSearch searchText;

	@UiField
	Label modalContent;

	@UiField
	Table list;

	// @UiField
	// Table listButtons;

	@UiField
	TableHeader listHeader;

	@UiField
	Alert alert;

	static interface Binder extends UiBinder<Widget, BootstratpDemo> {
	}

	public BootstratpDemo() {
		initWidget(uiBinder.createAndBindUi(this));

		listHeader
				.setText("Inserisci una parola chiave per avviare la ricerca e non dimenticare di collegarti ad un server se non l'hai ancora fatto...");

	}

	@UiHandler("searchText")
	void onSubmit(SubmitEvent e) {
		String text = searchText.getTextBox().getText();
		if (text == null || text.isEmpty()) {
			alert("Prova ad inserire una o piu' parole chiave per avviare la ricerca...");
			return;
		}
		// alert.setVisible(true);
		fillTable();

	}

	private void alert(String text) {
		if (alert.isVisible()) {
			alert.setVisible(false);
		}
		modalContent.setText(text);
		modal.show();
	}

	private void fillTable() {
		listHeader.setText("Risultato della ricerca:");

		for (int i = 0; i < 100; i++) {
			Row r = new Row();
			r.setHeight("20px");
			Row r2 = new Row();
			r.setHeight("20px");
			// r.setShowOn(Device.DESKTOP);
			com.github.gwtbootstrap.client.ui.Label l = new com.github.gwtbootstrap.client.ui.Label(
					"Row " + i);
			Button b = new Button();
			b.setBlock(false);
			b.setHeight("14px");
			b.setText("Download");
			b.addStyleName("downloadButton");
			l.addStyleName("text-center");
			// l.setHeight("13px");
			l.addStyleName("rowResult");
			l.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					alert(((com.github.gwtbootstrap.client.ui.Label) event
							.getSource()).getText());
				}
			});
			r.addStyleName("text-left");
			r.setPullRight(false);
			r.setMarginTop(3);
			r.add(l);
			r2.add(b);
			list.add(r);
			// listButtons.add(r2);
		}
	}
}
