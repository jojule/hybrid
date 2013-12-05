package org.vaadin.hybrid.vaadinrpc.client;

import org.vaadin.hybrid.vaadinrpc.ClientSideAddressbookVaadinRPCView;
import org.vaadin.hybrid.vaadinrpc.shared.AddressbookEditorClientRpc;
import org.vaadin.hybrid.vaadinrpc.shared.AddressbookEditorServerRpc;
import org.vaadin.hybrid.vaadinrpc.shared.AddressbookEditorState;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ClientSideAddressbookVaadinRPCView.class)
public class ClientSideAddressbookVaadinRPCViewConnector extends
		AbstractComponentConnector {

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		getWidget().updateAddressList(getState().addresses);

		registerRpc(AddressbookEditorClientRpc.class, getWidget().clientRpc);
		getWidget().setServerRpc(
				RpcProxy.create(AddressbookEditorServerRpc.class, this));
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
