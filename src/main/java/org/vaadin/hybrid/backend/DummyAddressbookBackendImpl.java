package org.vaadin.hybrid.backend;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;

public class DummyAddressbookBackendImpl implements AddressbookBackend {

	private final LinkedHashMap<Integer, Address> addresses = new LinkedHashMap<Integer, Address>();
	private AtomicInteger nextId = new AtomicInteger(0);

	public DummyAddressbookBackendImpl() {
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
		if (address.getId() == -1) {
			address.setId(nextId.getAndIncrement());
		}
		addresses.put(address.getId(), address);
	}

	@Override
	public void deleteAddress(int id) {
		addresses.remove(Integer.valueOf(id));
	}

	private void initWithDummyData(int rows) {
		String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
				"Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
				"Lisa", "Marge" };
		String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
				"Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
				"Barks", "Ross", "Schneider", "Tate" };
		for (int i = 0; i < rows; i++) {
			Address a = new Address(nextId.getAndIncrement());
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

	public static AddressbookBackend getAddressBookService(HttpSession session) {
		AddressbookBackend serv = (AddressbookBackend) session
				.getAttribute(DummyAddressbookBackendImpl.class
						.getCanonicalName());
		if (serv == null) {
			serv = new DummyAddressbookBackendImpl();
			session.setAttribute(
					DummyAddressbookBackendImpl.class.getCanonicalName(), serv);
		}
		return serv;
	}

}
