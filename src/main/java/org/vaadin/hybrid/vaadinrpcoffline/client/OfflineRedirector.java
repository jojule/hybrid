package org.vaadin.hybrid.vaadinrpcoffline.client;

import com.google.gwt.user.client.Window.Location;

public class OfflineRedirector {

	/**
	 * Redirects the browser to the online URL if not already at that URL
	 * 
	 * @return true if the browser was redirected
	 */
	public static boolean redirectToOnline() {
		if (!onOnlinePage()) {
			String onlineUrl = Location.createUrlBuilder().setPath("/")
					.buildString();
			Location.replace(onlineUrl);
			return true;
		}
		return false;
	}

	/**
	 * Redirects the browser to the offline URL if not already at that URL
	 * 
	 * @return true if the browser was redirected
	 */
	public static boolean redirectToOffline() {
		if (!onOfflinePage()) {
			String offlineUrl = Location.createUrlBuilder()
					.setPath("/offline/").buildString();
			Location.replace(offlineUrl);
			return true;
		}
		return false;
	}

	public static boolean onOfflinePage() {
		return Location.getPath().equals("/offline/");
	}

	public static boolean onOnlinePage() {
		return Location.getPath().equals("/");
	}

}
