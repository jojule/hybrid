package org.vaadin.hybrid.gwtrpc.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../../../addressbookrpc")
public interface AddressbookRPCService extends RemoteService {
	
	public AddressTO[] getAddressess();

	public AddressTO getAddress(int id);

	public void storeAddress(AddressTO address);

	public void deleteAddress(int id);

	public AddressTO newAddress();

}
