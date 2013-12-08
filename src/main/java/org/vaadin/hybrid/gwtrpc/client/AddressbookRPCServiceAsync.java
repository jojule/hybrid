package org.vaadin.hybrid.gwtrpc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddressbookRPCServiceAsync {

	public void getAddressess(AsyncCallback<AddressTO[]> callback);

	public void getAddress(int id, AsyncCallback<AddressTO> callback);

	public void storeAddress(AddressTO address, AsyncCallback<Void> callback);

	public void deleteAddress(int id, AsyncCallback<Void> callback);

	public void newAddress(AsyncCallback<AddressTO> callback);

}
