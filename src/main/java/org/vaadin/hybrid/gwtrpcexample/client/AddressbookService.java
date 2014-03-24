package org.vaadin.hybrid.gwtrpcexample.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../../../addressbookrpc")
public interface AddressbookService extends RemoteService {
	
	public AddressTO[] getAddressess();

	public AddressTO getAddress(int id);

	public void storeAddress(AddressTO address);

	public void deleteAddress(int id);

}
