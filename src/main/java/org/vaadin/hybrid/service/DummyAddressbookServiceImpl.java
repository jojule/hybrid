package org.vaadin.hybrid.service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;

public class DummyAddressbookServiceImpl implements AddressbookService {

	private final LinkedHashMap<Integer, Address> addresses = new LinkedHashMap<Integer, Address>();
	private int nextId = 0;

	public DummyAddressbookServiceImpl() {
		initWithDummyData(300);
	}

	@Override
	public Collection<Address> getAddressess() {
		return Collections.unmodifiableCollection(addresses.values());
	}

	@Override
	public Address getAddress(int id) {
		return addresses.get(Integer.valueOf(id));
	}

	@Override
	public void storeAddress(Address address) {
		addresses.put(address.getId(), address);
	}

	@Override
	public void deleteAddress(int id) {
		addresses.remove(Integer.valueOf(id));
	}

	@Override
	public Address newAddress() {
		Address a = new Address(nextId++);
		storeAddress(a);
		return a;
	}

	private void initWithDummyData(int rows) {
		String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
				"Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
				"Lisa", "Marge" };
		String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
				"Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
				"Barks", "Ross", "Schneider", "Tate" };
		for (int i = 0; i < rows; i++) {
			Address a = newAddress();
			a.setFirstName(fnames[(int) (fnames.length * Math.random())]);
			a.setLastName(lnames[(int) (lnames.length * Math.random())]);
			a.setPhoneNumber("0" + ((int) (999999999 * Math.random())));
			a.setEmailAddress(a.getFirstName()
					+ "."
					+ a.getLastName()
					+ "@"
					+ lnames[(int) (lnames.length * Math.random())]
							.toLowerCase() + ".com");
			storeAddress(a);
		}

	}
	
	public static AddressbookService getAddressBookService(HttpSession session) {
		AddressbookService serv = (AddressbookService) session
				.getAttribute(DummyAddressbookServiceImpl.class
						.getCanonicalName());
		if (serv == null) {
			serv = new DummyAddressbookServiceImpl();
			session.setAttribute(
					DummyAddressbookServiceImpl.class.getCanonicalName(), serv);
		}
		return serv;
	}


}
