package org.vaadin.hybrid.connectorexample.shared;

import com.vaadin.shared.communication.ClientRpc;

public interface AddressbookEditorClientRpc extends ClientRpc {

	public void storeAddressCallback(AddressTO newAddress);


}
