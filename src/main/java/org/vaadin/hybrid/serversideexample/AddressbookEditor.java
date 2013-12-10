package org.vaadin.hybrid.serversideexample;

import org.vaadin.hybrid.HybridUI;
import org.vaadin.hybrid.backend.Address;
import org.vaadin.hybrid.backend.AddressbookBackend;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class AddressbookEditor extends CustomComponent implements View {

	VerticalLayout layout = new VerticalLayout();
	Table addressList = new Table();
	HorizontalLayout tableActions = new HorizontalLayout();
	Button editButton = new Button("Edit");
	Button deleteButton = new Button("Delete");
	Button newButton = new Button("New");
	GridLayout form = new GridLayout(2, 2);
	FieldGroup fields = new FieldGroup();
	TextField firstName = new TextField("First Name");
	TextField lastName = new TextField("Last Name");
	TextField phoneNumber = new TextField("Phone");
	TextField emailAddress = new TextField("Email");
	HorizontalLayout formActions = new HorizontalLayout();
	Button saveButton = new Button("Save");
	Button cancelButton = new Button("Cancel");
	AddressbookBackend service = ((HybridUI) UI.getCurrent())
			.getAddressBookService();

	public AddressbookEditor() {
		initLayout();
		updateAddressList();
		initAddressListActions();
		initForm();
	}

	private void initLayout() {
		setCompositionRoot(layout);

		layout.setWidth("100%");
		layout.setSpacing(true);

		layout.addComponent(addressList);
		layout.addComponent(tableActions);
		layout.addComponent(form);
		layout.addComponent(formActions);

		addressList.setWidth("100%");

		tableActions.setSpacing(true);
		tableActions.addComponent(editButton);
		tableActions.addComponent(deleteButton);
		tableActions.addComponent(newButton);
		layout.setComponentAlignment(tableActions, Alignment.MIDDLE_RIGHT);

		form.setWidth("100%");
		form.addComponent(firstName);
		form.addComponent(lastName);
		form.addComponent(phoneNumber);
		form.addComponent(emailAddress);
		form.setSpacing(true);

		firstName.setWidth("100%");
		lastName.setWidth("100%");
		phoneNumber.setWidth("50%");
		emailAddress.setWidth("70%");

		formActions.setSpacing(true);
		formActions.addComponent(saveButton);
		formActions.addComponent(cancelButton);
		layout.setComponentAlignment(formActions, Alignment.MIDDLE_RIGHT);
	}

	private void updateAddressList() {
		BeanItemContainer<Address> addresses = new BeanItemContainer<Address>(
				Address.class);
		addresses.addAll(service.getAddressess());
		addressList.setContainerDataSource(addresses);
		addressList.setVisibleColumns("firstName", "lastName", "emailAddress",
				"phoneNumber");
		addressList.setSelectable(true);
		updateActionVisibility(false);
	}

	private void initAddressListActions() {
		editButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				Address a = (Address) addressList.getValue();
				if (a != null)
					editAddress(a);
			}
		});
		deleteButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				Address a = (Address) addressList.getValue();
				if (a != null) {
					service.deleteAddress(a.getId());
					updateAddressList();
				}
			}
		});
		newButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				Address a = service.newAddress();
				editAddress(a);
			}
		});
		addressList.setImmediate(true);
		addressList.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				updateActionVisibility(false);
			}

		});
	}

	private void updateActionVisibility(boolean editingAddress) {
		boolean selected = addressList.getValue() != null;
		editButton.setEnabled(selected);
		deleteButton.setEnabled(selected);

		addressList.setEnabled(!editingAddress);
		tableActions.setEnabled(!editingAddress);
		formActions.setEnabled(editingAddress);
		form.setEnabled(editingAddress);
	}

	private void initForm() {
		fields.bind(firstName, "firstName");
		fields.bind(lastName, "lastName");
		fields.bind(emailAddress, "emailAddress");
		fields.bind(phoneNumber, "phoneNumber");

		formActions.setEnabled(false);
		form.setEnabled(false);

		saveButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					fields.commit();
					@SuppressWarnings("unchecked")
					Address a = ((BeanItem<Address>) fields.getItemDataSource())
							.getBean();
					service.storeAddress(a);
					updateAddressList();
					addressList.select(a);
					addressList.setCurrentPageFirstItemId(a);
				} catch (CommitException ignored) {
				}
				updateActionVisibility(false);
				fields.setItemDataSource(null);
			}
		});

		cancelButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				fields.discard();
				updateActionVisibility(false);
				fields.setItemDataSource(null);
			}
		});
	}

	private void editAddress(Address a) {
		fields.setItemDataSource(new BeanItem<Address>(a));
		updateActionVisibility(true);
	}

	public void enter(ViewChangeEvent event) {
	}

}
