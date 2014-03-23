package org.vaadin.hybrid.vaadinrpcoffline.shared;

import com.vaadin.shared.communication.ClientRpc;

public interface AddressbookEditorClientRpc extends ClientRpc {

	public void storeAddressCallback(AddressTO newAddress);

	public void newAddressCallback(AddressTO newAddress);

}
