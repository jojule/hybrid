package org.vaadin.hybrid.gwtrpc.client;

import org.vaadin.hybrid.gwtrpc.ClientSideAddressbookGWTRPCView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/* In pure client-side application this would be replaced with EntryPoint class that
 * would initialize AddressbookEditor widget. */

@SuppressWarnings("serial")
@Connect(ClientSideAddressbookGWTRPCView.class)
public class ClientSideAddressbookGWTRPCViewConnector extends
		AbstractComponentConnector {

	protected Widget createWidget() {
		return GWT.create(AddressbookEditor.class);
	}
}
