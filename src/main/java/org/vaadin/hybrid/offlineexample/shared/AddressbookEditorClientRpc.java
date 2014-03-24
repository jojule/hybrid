package org.vaadin.hybrid.offlineexample.shared;

import com.vaadin.shared.communication.ClientRpc;

public interface AddressbookEditorClientRpc extends ClientRpc {

	public void storeAddressCallback(AddressTO newAddress);

	public void newAddressCallback(AddressTO newAddress);

}
