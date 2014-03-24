package org.vaadin.hybrid.offlineexample.client;

import java.util.List;

import org.vaadin.hybrid.offlineexample.client.OfflineDetector.OfflineStateChangeHandler;
import org.vaadin.hybrid.offlineexample.client.OfflineDetector.OfflineStateEvent;
import org.vaadin.hybrid.offlineexample.shared.AddressTO;
import org.vaadin.hybrid.offlineexample.shared.AddressbookEditorClientRpc;
import org.vaadin.hybrid.offlineexample.shared.AddressbookEditorServerRpc;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class AddressbookEditor extends Composite {

	VerticalPanel layout = new VerticalPanel();
	DataGrid<AddressTO> addressList = new DataGrid<AddressTO>();
	SingleSelectionModel<AddressTO> selectionModel;
	HorizontalPanel tableActions = new HorizontalPanel();
	Button editButton = new Button("Edit");
	Button deleteButton = new Button("Delete");
	Button newButton = new Button("New");
	HorizontalPanel form = new HorizontalPanel();
	VerticalPanel formL = new VerticalPanel();
	VerticalPanel formR = new VerticalPanel();
	TextBox firstName = new TextBox();
	TextBox lastName = new TextBox();
	TextBox phoneNumber = new TextBox();
	TextBox emailAddress = new TextBox();
	HorizontalPanel formActions = new HorizontalPanel();
	Button saveButton = new Button("Save");
	Button cancelButton = new Button("Cancel");
	private DataStore store = GWT.create(DataStore.class);
	private int editId;

	AddressbookEditorClientRpc clientRpc = new AddressbookEditorClientRpc() {

		@Override
		public void storeAddressCallback(AddressTO newAddress) {
			clearForm();
			// TODO reselect the value in addressList
		}
	};

	public AddressbookEditor() {
		initLayout();
		initWidget(layout);
		initAddressList();
		initAddressListActions();
		initForm();

		updateAddressList(store.getAddresses());

		OfflineDetector.get().addOnlineStateChangeHandler(
				new OfflineStateChangeHandler() {
					@Override
					public void onOfflineStateChange(OfflineStateEvent event) {
						if (event.isOffline()) {
							// Offline event
							if (OfflineRedirector.onOfflinePage())
								return;

							boolean goOffline = Window
									.confirm("Do you want to go offline?");
							if (goOffline) {
								OfflineRedirector.redirectToOffline();
							}
						} else {
							// Online event
							if (OfflineRedirector.onOnlinePage())
								return;

							boolean goOnline = Window
									.confirm("Do you want to go online?");
							if (goOnline) {
								OfflineRedirector.redirectToOnline();
							}
						}
					}
				});
	}

	public void setServerRpc(AddressbookEditorServerRpc serverRpc) {
		store.serverRpc = serverRpc;

		if (!OfflineRedirector.onOfflinePage()) {
			// Send any pending updates to the server
			boolean updated = store.purgeQueue();
			if (updated) {
				Window.alert("Updates sent to the server");
			}
		}

	}

	private void initLayout() {
		layout.setWidth("100%");
		layout.add(addressList);
		layout.add(tableActions);
		layout.add(form);
		layout.add(formActions);
		layout.setSpacing(8);

		addressList.setWidth("600px"); // TODO 100% width
		addressList.setHeight("320px"); // TODO 15 lines high

		tableActions.add(editButton);
		tableActions.add(deleteButton);
		tableActions.add(newButton);
		// TODO layout.setComponentAlignment(tableActions,
		// Alignment.MIDDLE_RIGHT);

		form.setWidth("100%");
		formL.setWidth("100%");
		formR.setWidth("100%");
		formL.setSpacing(8);
		formR.setSpacing(8);
		form.add(formL);
		form.add(formR);
		formL.add(new Label("First Name"));
		formL.add(firstName);
		formR.add(new Label("Last Name"));
		formR.add(lastName);
		formL.add(new Label("Phone Number"));
		formL.add(phoneNumber);
		formR.add(new Label("Email Address"));
		formR.add(emailAddress);

		firstName.setWidth("100%");
		lastName.setWidth("100%");
		phoneNumber.setWidth("50%");
		emailAddress.setWidth("70%");

		formActions.add(saveButton);
		formActions.add(cancelButton);
		// TODO layout.setComponentAlignment(formActions,
		// Alignment.MIDDLE_RIGHT);
	}

	private void initAddressList() {

		// Add a selection model so we can select rows
		selectionModel = new SingleSelectionModel<AddressTO>(
				new ProvidesKey<AddressTO>() {
					public Object getKey(AddressTO item) {
						return item.getId();
					}
				});
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				clearForm();
			}
		});
		addressList.setSelectionModel(selectionModel);

		// First name.
		Column<AddressTO, String> firstNameColumn = new Column<AddressTO, String>(
				new TextCell()) {
			@Override
			public String getValue(AddressTO a) {
				return a.getFirstName();
			}
		};
		addressList.addColumn(firstNameColumn,
				SafeHtmlUtils.fromSafeConstant("First Name"));
		// addressList.setColumnWidth(firstNameColumn, 20, Unit.PCT);

		// First name.
		Column<AddressTO, String> lastNameColumn = new Column<AddressTO, String>(
				new TextCell()) {
			@Override
			public String getValue(AddressTO a) {
				return a.getLastName();
			}
		};
		addressList.addColumn(lastNameColumn,
				SafeHtmlUtils.fromSafeConstant("Last Name"));
		// addressList.setColumnWidth(lastNameColumn, 20, Unit.PCT);

		// Email Address
		Column<AddressTO, String> emailAddressColumn = new Column<AddressTO, String>(
				new TextCell()) {
			@Override
			public String getValue(AddressTO a) {
				return a.getEmailAddress();
			}
		};
		addressList.addColumn(emailAddressColumn,
				SafeHtmlUtils.fromSafeConstant("Email"));
		// addressList.setColumnWidth(emailAddressColumn, 20, Unit.PCT);

		// Phone number
		Column<AddressTO, String> phoneNumberColumn = new Column<AddressTO, String>(
				new TextCell()) {
			@Override
			public String getValue(AddressTO a) {
				return a.getPhoneNumber();
			}
		};
		addressList.addColumn(phoneNumberColumn,
				SafeHtmlUtils.fromSafeConstant("Phone"));
		// addressList.setColumnWidth(phoneNumberColumn, 20, Unit.PCT);
	}

	public void onAddressListUpdate(List<AddressTO> addresses) {
		store.storeAddresses(addresses);
		updateAddressList(addresses);
	}

	public void updateAddressList(List<AddressTO> addresses) {
		int nrAddresses = addresses.size();
		addressList.setRowCount(nrAddresses);
		addressList.setRowData(addresses);
		updateActionVisibility(false);
	}

	private void initAddressListActions() {

		editButton.addClickHandler(new ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				AddressTO a = selectionModel.getSelectedObject();
				if (a != null)
					editAddress(a);
			}
		});

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				AddressTO a = selectionModel.getSelectedObject();
				if (a != null) {
					if (!store.deleteAddress(a.getId())) {
						updateAddressList(store.getAddresses());
					}
				}
			}
		});

		newButton.addClickHandler(new ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				selectionModel.clear();
				AddressTO newAddress = new AddressTO();
				newAddress.setId(-1);
				editAddress(newAddress);
			}
		});
	}

	private void updateActionVisibility(boolean editingAddress) {
		boolean selected = selectionModel.getSelectedObject() != null;
		editButton.setEnabled(selected && !editingAddress);
		deleteButton.setEnabled(selected && !editingAddress);

		// addressList.setEnabled(!editingAddress);
		newButton.setEnabled(!editingAddress);

		// formActions
		saveButton.setEnabled(editingAddress);
		cancelButton.setEnabled(editingAddress);

		// form
		firstName.setEnabled(editingAddress);
		lastName.setEnabled(editingAddress);
		emailAddress.setEnabled(editingAddress);
		phoneNumber.setEnabled(editingAddress);
	}

	private void initForm() {

		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				AddressTO a = new AddressTO();
				a.setId(editId);
				a.setFirstName(firstName.getValue());
				a.setLastName(lastName.getValue());
				a.setPhoneNumber(phoneNumber.getValue());
				a.setEmailAddress(emailAddress.getValue());
				if (!store.storeAddress(a)) {
					clearForm();
					updateAddressList(store.getAddresses());
				}
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				clearForm();
			}
		});

	}

	protected void clearForm() {
		firstName.setValue("");
		lastName.setValue("");
		emailAddress.setValue("");
		phoneNumber.setValue("");
		updateActionVisibility(false);
	}

	private void editAddress(AddressTO a) {
		editId = a.getId();
		firstName.setValue(a.getFirstName());
		lastName.setValue(a.getLastName());
		emailAddress.setValue(a.getEmailAddress());
		phoneNumber.setValue(a.getPhoneNumber());
		updateActionVisibility(true);
	}

}
