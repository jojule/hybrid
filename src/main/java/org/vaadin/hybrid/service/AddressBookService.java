package org.vaadin.hybrid.service;

import java.util.Collection;

public interface AddressbookService {

	public Collection<Address> getAddressess();

	public Address getAddress(int id);

	public void storeAddress(Address address);

	public void deleteAddress(int id);

	public Address newAddress();

}
