package org.vaadin.hybrid.vaadinrpcoffline.client;

import org.vaadin.hybrid.vaadinrpcoffline.ClientSideAddressbookVaadinRPCOfflineView;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorClientRpc;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorServerRpc;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorState;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ClientSideAddressbookVaadinRPCOfflineView.class)
public class ClientSideAddressbookVaadinRPCOfflineViewConnector extends
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

		getWidget().onAddressListUpdate(getState().addresses);

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
