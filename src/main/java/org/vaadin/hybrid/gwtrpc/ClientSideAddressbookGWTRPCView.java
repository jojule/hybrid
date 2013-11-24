package org.vaadin.hybrid.gwtrpc;


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;

/* In pure client-side application this would be replaced with static HTML page for 
 * setting up the layout where AddressbookEditor widget would be placed in. */

@SuppressWarnings("serial")
public class ClientSideAddressbookGWTRPCView extends AbstractComponent implements
		View {

	public ClientSideAddressbookGWTRPCView() {
		setWidth("100%");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

}
