package org.vaadin.hybrid.gwtrpcexample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AddressbookServiceAsync {

	public void getAddressess(AsyncCallback<AddressTO[]> callback);

	public void getAddress(int id, AsyncCallback<AddressTO> callback);

	public void storeAddress(AddressTO address, AsyncCallback<Void> callback);

	public void deleteAddress(int id, AsyncCallback<Void> callback);


}
