package org.vaadin.hybrid.offlineexample.client;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.hybrid.offlineexample.shared.AddressTO;
import org.vaadin.hybrid.offlineexample.shared.AddressbookEditorServerRpc;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;

public class DataStore {

	public AddressbookEditorServerRpc serverRpc;
	private String PENDING_COMMANDS_STORAGE_KEY = "Addressbook_queue";
	private String ADDRESS_DATA_KEY = "Addressbook_addresses";

	public DataStore() {
		if (!Storage.isSessionStorageSupported()) {
			throw new UnsupportedOperationException(
					"This class requires local storage support in the browser");
		}
	}

	public void deleteAddress(final int id) {
		if (OfflineRedirector.onOfflinePage()) {
			queueOperation("{\"op\": \"delete\", \"id\": " + id + "}");
		} else {
			serverRpc.deleteAddress(id);
			GWT.log("Executing deleteAddress(" + id + ")");
		}
	}

	private void queueOperation(String commandAsJson) {
		JSONArray pendingCommands = getPendingCommands();
		pendingCommands.set(pendingCommands.size(),
				JSONParser.parseStrict(commandAsJson));
		setPendingCommands(pendingCommands);

		GWT.log("Queued operation " + commandAsJson);
	}

	private JSONArray getPendingCommands() {
		Storage storage = Storage.getSessionStorageIfSupported();
		String pendingCommandsJson = storage
				.getItem(PENDING_COMMANDS_STORAGE_KEY);
		if (pendingCommandsJson == null) {
			pendingCommandsJson = "[]";
		}
		return JSONParser.parseStrict(pendingCommandsJson).isArray();
	}

	private void setPendingCommands(JSONArray pendingCommands) {
		Storage storage = Storage.getSessionStorageIfSupported();
		storage.setItem(PENDING_COMMANDS_STORAGE_KEY,
				pendingCommands.toString());
	}

	private void setAddressData(JSONArray addressData) {
		Storage storage = Storage.getSessionStorageIfSupported();
		storage.setItem(ADDRESS_DATA_KEY, addressData.toString());
	}

	private JSONArray getAddressData() {
		Storage storage = Storage.getSessionStorageIfSupported();
		String storedJson = storage.getItem(ADDRESS_DATA_KEY);
		if (storedJson == null || storedJson.equals("")) {
			return new JSONArray();
		}
		JSONValue addressData = JSONParser.parseStrict(storedJson);
		return addressData.isArray();
	}

	private void clearPendingCommands() {
		Storage storage = Storage.getSessionStorageIfSupported();
		storage.removeItem(PENDING_COMMANDS_STORAGE_KEY);
	}

	/**
	 * @return true if queue contained items, false otherwise
	 */
	public boolean purgeQueue() {
		JSONArray pendingCommands = getPendingCommands();
		if (pendingCommands.size() == 0)
			return false;

		clearPendingCommands();
		for (int i = 0; i < pendingCommands.size(); i++) {
			JSONObject pendingCommand = pendingCommands.get(i).isObject();
			String operation = pendingCommand.get("op").isString()
					.stringValue();
			if ("delete".equals(operation)) {
				deleteAddress((int) pendingCommand.get("id").isNumber()
						.doubleValue());
			} else if ("new".equals(operation)) {
				newAddress();
			} else if ("store".equals(operation)) {
				AddressTO address = deserialize(pendingCommand.get("address")
						.isObject());
				storeAddress(address);
			}
		}
		return true;
	}

	public void newAddress() {
		// TODO This whole method makes no sense
		if (OfflineRedirector.onOfflinePage()) {
			queueOperation("{\"op\": \"new\"}");
		} else {
			serverRpc.newAddress();
			GWT.log("Executing newAddress()");
		}
	}

	public void storeAddress(AddressTO a) {
		if (OfflineRedirector.onOfflinePage()) {
			queueOperation("{\"op\": \"store\", \"address\": " + serialize(a)
					+ "}");
		} else {
			serverRpc.storeAddress(a);
			GWT.log("Executing storeAddress(" + serialize(a) + ")");
		}
	}

	private JSONObject serialize(AddressTO a) {
		JSONObject json = new JSONObject();
		json.put("id", new JSONNumber(a.getId()));
		json.put("first", new JSONString(a.getFirstName()));
		json.put("last", new JSONString(a.getLastName()));
		json.put("phone", new JSONString(a.getPhoneNumber()));
		json.put("email", new JSONString(a.getEmailAddress()));
		return json;
	}

	private AddressTO deserialize(JSONObject json) {
		AddressTO address = new AddressTO();
		address.setId((int) json.get("id").isNumber().doubleValue());
		address.setFirstName(json.get("first").isString().stringValue());
		address.setLastName(json.get("last").isString().stringValue());
		address.setPhoneNumber(json.get("phone").isString().stringValue());
		address.setEmailAddress(json.get("email").isString().stringValue());

		return address;
	}

	public void storeAddresses(List<AddressTO> addresses) {
		JSONArray jsonAddresses = GWT.create(JSONArray.class);
		for (int i = 0; i < addresses.size(); i++) {
			AddressTO address = addresses.get(i);
			jsonAddresses.set(i, serialize(address));
		}
		setAddressData(jsonAddresses);
	}

	public List<AddressTO> getAddresses() {
		JSONArray jsonAddresses = getAddressData();
		ArrayList<AddressTO> addresses = new ArrayList<AddressTO>(
				jsonAddresses.size());
		for (int i = 0; i < jsonAddresses.size(); i++) {
			addresses.add(deserialize(jsonAddresses.get(i).isObject()));
		}

		return addresses;
	}
}
