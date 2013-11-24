package org.vaadin.hybrid.gwtrpc;


import java.util.Collection;

import javax.servlet.annotation.WebServlet;

import org.vaadin.hybrid.gwtrpc.client.AddressbookRPCService;
import org.vaadin.hybrid.gwtrpc.client.AddressTO;
import org.vaadin.hybrid.service.Address;
import org.vaadin.hybrid.service.AddressbookService;
import org.vaadin.hybrid.service.DummyAddressbookServiceImpl;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
@WebServlet(value = "/addressbookrpc")
public class AddressbookRPCServiceImpl extends RemoteServiceServlet implements
		AddressbookRPCService {

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
		// TODO Potential security problem as we do allow any id - not just ones listed before
		return asAddressTO(getBackend().getAddress(id));
	}

	@Override
	public void storeAddress(AddressTO address) {
		// TODO Potential security problem as we do allow any id - not just ones listed before
		Address a = new Address(address.getId(), address.getFirstName(), address.getLastName(), address.getPhoneNumber(), address.getEmailAddress());
		getBackend().storeAddress(a);
	}

	@Override
	public void deleteAddress(int id) {
		// TODO Potential security problem as we do allow any id - not just ones listed before
		getBackend().deleteAddress(id);
	}

	@Override
	public AddressTO newAddress() {
		return asAddressTO(getBackend().newAddress());
	}

	private AddressbookService getBackend() {
		return DummyAddressbookServiceImpl
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
