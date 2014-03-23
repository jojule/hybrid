package org.vaadin.hybrid.vaadinrpcoffline;

import java.util.ArrayList;

import org.vaadin.hybrid.service.Address;
import org.vaadin.hybrid.service.AddressbookService;
import org.vaadin.hybrid.service.DummyAddressbookServiceImpl;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressTO;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorClientRpc;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorServerRpc;
import org.vaadin.hybrid.vaadinrpcoffline.shared.AddressbookEditorState;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.AbstractComponent;

/* In pure client-side application this would be replaced with static HTML page for 
 * setting up the layout where AddressbookEditor widget would be placed in. */

@SuppressWarnings("serial")
public class ClientSideAddressbookVaadinRPCOfflineView extends AbstractComponent
		implements View, AddressbookEditorServerRpc {

	public ClientSideAddressbookVaadinRPCOfflineView() {
		setWidth("100%");
		registerRpc(this, AddressbookEditorServerRpc.class);

	}

	private void updateAddresses() {
		ArrayList<AddressTO> addresses = new ArrayList<AddressTO>();
		for (Address address : getBackend().getAddressess()) {
			addresses.add(asAddressTO(address));
		}

		getState().addresses = addresses;
	}

	@Override
	protected AddressbookEditorState getState() {
		return (AddressbookEditorState) super.getState();
	}

	@Override
	public void storeAddress(AddressTO address) {
		// TODO Potential security problem as we do allow any id - not
		// just ones listed before
		Address a = new Address(address.getId(), address.getFirstName(),
				address.getLastName(), address.getPhoneNumber(),
				address.getEmailAddress());
		getBackend().storeAddress(a);
		updateAddresses();
		getRpcProxy(AddressbookEditorClientRpc.class).storeAddressCallback(
				address);
	}

	@Override
	public void deleteAddress(int id) {
		// TODO Potential security problem as we do allow any id - not
		// just ones listed before
		getBackend().deleteAddress(id);
		updateAddresses();
	}

	@Override
	public void newAddress() {
		getRpcProxy(AddressbookEditorClientRpc.class).newAddressCallback(
				asAddressTO(getBackend().newAddress()));
		updateAddresses();
	}

	private AddressbookService getBackend() {
		return DummyAddressbookServiceImpl
				.getAddressBookService(((WrappedHttpSession) VaadinSession
						.getCurrent().getSession()).getHttpSession());
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

	@Override
	public void enter(ViewChangeEvent event) {
		updateAddresses();
	}

}
