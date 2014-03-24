package org.vaadin.hybrid.backend;

import java.util.Collection;

public interface AddressbookBackend {

	public Collection<Address> getAddressess();

	public Address getAddress(int id);

	public void storeAddress(Address address);

	public void deleteAddress(int id);

}
