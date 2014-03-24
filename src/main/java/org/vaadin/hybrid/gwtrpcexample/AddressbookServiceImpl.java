package org.vaadin.hybrid.gwtrpcexample;


import java.util.Collection;

import javax.servlet.annotation.WebServlet;

import org.vaadin.hybrid.backend.Address;
import org.vaadin.hybrid.backend.DummyAddressbookBackendImpl;
import org.vaadin.hybrid.gwtrpcexample.client.AddressTO;
import org.vaadin.hybrid.gwtrpcexample.client.AddressbookService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet(value = "/addressbookrpc")
public class AddressbookServiceImpl extends RemoteServiceServlet implements
		AddressbookService {

	@Override
	public AddressTO[] getAddressess() {
		Collection<Address> c = getBackend().getAddressess();
		AddressTO[] array = new AddressTO[c.size()];
		int i = 0;
		for (Address a : c) {
			array[i++] = asAddressTO(a);
		}
		return array;
	}

	@Override
	public AddressTO getAddress(int id) {
		// TODO Authentication and authorisation are not implemented - anyone can call this
		return asAddressTO(getBackend().getAddress(id));
	}

	@Override
	public void storeAddress(AddressTO address) {
        // TODO Authentication and authorisation are not implemented - anyone can call this
		Address a = new Address(address.getId(), address.getFirstName(), address.getLastName(), address.getPhoneNumber(), address.getEmailAddress());
		getBackend().storeAddress(a);
	}

	@Override
	public void deleteAddress(int id) {
        // TODO Authentication and authorisation are not implemented - anyone can call this
		getBackend().deleteAddress(id);
	}

	private org.vaadin.hybrid.backend.AddressbookBackend getBackend() {
		return DummyAddressbookBackendImpl
				.getAddressBookService(getThreadLocalRequest().getSession());
	}

	private AddressTO asAddressTO(Address a) {
		AddressTO ato = new AddressTO();
		ato.setFirstName(a.getFirstName());
		ato.setLastName(a.getLastName());
		ato.setEmailAddress(a.getEmailAddress());
		ato.setPhoneNumber(a.getPhoneNumber());
		ato.setId(a.getId());
		return ato;
	}

}
