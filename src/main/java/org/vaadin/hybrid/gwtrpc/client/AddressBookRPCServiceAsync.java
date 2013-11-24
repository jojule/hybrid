package org.vaadin.hybrid.gwtrpc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface AddressbookRPCServiceAsync extends RemoteService {

	public void getAddressess(AsyncCallback<AddressTO[]> callback);

	public void getAddress(int id, AsyncCallback<AddressTO> callback);

	public void storeAddress(AddressTO address, AsyncCallback<?> callback);

	public void deleteAddress(int id, AsyncCallback<?> callback);

	public void newAddress(AsyncCallback<AddressTO> callback);

}
