package org.vaadin.hybrid.vaadinrpcoffline.shared;

import com.vaadin.shared.communication.ServerRpc;

public interface AddressbookEditorServerRpc extends ServerRpc {

	public void storeAddress(AddressTO address);

	public void deleteAddress(int id);

	public void newAddress();

}
