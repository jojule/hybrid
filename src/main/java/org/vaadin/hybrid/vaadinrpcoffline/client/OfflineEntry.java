package org.vaadin.hybrid.vaadinrpcoffline.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.ApplicationConfiguration;

public class OfflineEntry extends ApplicationConfiguration {

	@Override
	public void onModuleLoad() {
		if (OfflineDetector.isOffline()) {
			// Offline, redirect to /offline to start offline version
			if (OfflineRedirector.redirectToOffline()) {
				return;
			}
		}

		if (OfflineRedirector.onOfflinePage()) {
			startOfflineVersion();
		} else {
			// Start online version
			super.onModuleLoad();
		}

	}

	private void startOfflineVersion() {
		// Find the Vaadin root div
		Element mainDiv = RootPanel.getBodyElement().getFirstChildElement();
		RootPanel rootPanel = RootPanel.get(mainDiv.getId());
		while (mainDiv.hasChildNodes())
			mainDiv.removeChild(mainDiv.getFirstChild());

		// Hack to make layout the same
		mainDiv.setAttribute("style", "float: left");

		rootPanel.add(new AddressbookEditor());
	}
}
