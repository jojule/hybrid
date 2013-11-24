package org.vaadin.hybrid;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UnimplementedView extends VerticalLayout implements View {

	public UnimplementedView() {
		Label msg = new Label(
				"Not implemented yet");
		addComponent(msg);

		setComponentAlignment(msg, Alignment.MIDDLE_CENTER);
		setHeight("400px");
	}

	public void enter(ViewChangeEvent event) {
	}
}
