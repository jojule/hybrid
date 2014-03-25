package org.vaadin.hybrid.offlineexample.client;

import com.google.gwt.user.client.Window.Location;

public class OfflineRedirector {

    private static String ONLINE_PAGE = "/";
    private static String OFFLINE_PAGE = "/offline.html";

	/**
	 * Redirects the browser to the online URL if not already at that URL
	 * 
	 * @return true if the browser was redirected
	 */
	public static boolean redirectToOnline() {
		if (!onOnlinePage()) {
			String onlineUrl = Location.createUrlBuilder().setPath(ONLINE_PAGE)
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
					.setPath(OFFLINE_PAGE).buildString();
			Location.replace(offlineUrl);
			return true;
		}
		return false;
	}

	public static boolean onOfflinePage() {
		return Location.getPath().equals(OFFLINE_PAGE);
	}

	public static boolean onOnlinePage() {
		return Location.getPath().equals(ONLINE_PAGE);
	}

}
