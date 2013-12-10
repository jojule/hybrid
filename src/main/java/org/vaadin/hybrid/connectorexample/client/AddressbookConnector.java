package org.vaadin.hybrid.connectorexample.client;

import org.vaadin.hybrid.connectorexample.AddressbookView;
import org.vaadin.hybrid.connectorexample.shared.AddressbookEditorClientRpc;
import org.vaadin.hybrid.connectorexample.shared.AddressbookEditorServerRpc;
import org.vaadin.hybrid.connectorexample.shared.AddressbookEditorState;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(AddressbookView.class)
public class AddressbookConnector extends
		AbstractComponentConnector {

	@Override
	protected void init() {
		super.init();

		registerRpc(AddressbookEditorClientRpc.class, getWidget().clientRpc);
		getWidget().setServerRpc(
				RpcProxy.create(AddressbookEditorServerRpc.class, this));
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		
		getWidget().updateAddressList(getState().addresses);

	}

	@Override
	public AddressbookEditor getWidget() {
		return (AddressbookEditor) super.getWidget();
	}

	@Override
	public AddressbookEditorState getState() {
		return (AddressbookEditorState) super.getState();
	}
}
