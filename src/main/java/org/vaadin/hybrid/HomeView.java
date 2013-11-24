package org.vaadin.hybrid;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class HomeView extends VerticalLayout implements View {

	public HomeView() {
		Label msg = new Label(
				"All of the examples implement the same UI. Choose examples from the tabs and compare implementations and performance.");
		addComponent(msg);

		setComponentAlignment(msg, Alignment.MIDDLE_CENTER);
		setHeight("400px");
	}

	public void enter(ViewChangeEvent event) {
	}
}
