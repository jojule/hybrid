package org.vaadin.hybrid.offlineexample.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.ApplicationConfiguration;

public class OfflineEntry extends ApplicationConfiguration {

    @Override
    public void onModuleLoad() {

        // Offline version is selected by going to /offline.html
        if (OfflineRedirector.onOfflinePage()) {

            // Start offline by placing the AddressbookEditor on rootPanel
            RootPanel rootPanel = RootPanel.get("offlineui");
            rootPanel.add(new AddressbookEditor());
        } else {

            // When we are offline, we should always try to load offline page
            if (OfflineDetector.isOffline()) {
                OfflineRedirector.redirectToOffline();
            }

            // Start online version
            super.onModuleLoad();
        }

    }
}
