package org.vaadin.hybrid.gwtrpcexample.client;

import org.vaadin.hybrid.gwtrpcexample.AddressbookView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/* In pure client-side application this would be replaced with EntryPoint class that
 * would initialize AddressbookEditor widget. */

@SuppressWarnings("serial")
@Connect(AddressbookView.class)
public class AddressbookConnector extends
		AbstractComponentConnector {

	protected Widget createWidget() {
		return GWT.create(AddressbookEditor.class);
	}
}
