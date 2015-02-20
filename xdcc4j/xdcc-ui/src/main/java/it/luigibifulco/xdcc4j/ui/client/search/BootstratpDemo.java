package it.luigibifulco.xdcc4j.ui.client.search;

import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BootstratpDemo extends Composite {

	private static Binder uiBinder = GWT.create(Binder.class);

	@UiField
	Modal searchModal;

	static interface Binder extends UiBinder<Widget, BootstratpDemo> {
	}

	public BootstratpDemo() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("searchText")
	void onSubmit(SubmitEvent e) {
		searchModal.show();
	}

}
