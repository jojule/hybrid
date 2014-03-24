package org.vaadin.hybrid.offlineexample.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event;

public class OfflineDetector {
	public interface OfflineStateChangeHandler extends EventHandler {
		void onOfflineStateChange(OfflineStateEvent event);
	}

	public static class OfflineStateEvent extends
			Event<OfflineStateChangeHandler> {
		public static Type<OfflineStateChangeHandler> TYPE = new Type<OfflineStateChangeHandler>();
		private boolean offline;

		public OfflineStateEvent(boolean offline) {
			this.offline = offline;
		}

		public boolean isOffline() {
			return offline;
		}

		@Override
		public Type<OfflineStateChangeHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(OfflineStateChangeHandler handler) {
			handler.onOfflineStateChange(this);
		}

	}

	private static OfflineDetector instance = GWT.create(OfflineDetector.class);

	/** Event bus for offline events */
	private EventBus eventBus = GWT.create(SimpleEventBus.class);

	public OfflineDetector() {
		registerListeners();
	}

	private void onOnline() {
		eventBus.fireEvent(new OfflineStateEvent(false));
	}

	private void onOffline() {
		eventBus.fireEvent(new OfflineStateEvent(true));
	}

	public void addOnlineStateChangeHandler(OfflineStateChangeHandler handler) {
		eventBus.addHandler(OfflineStateEvent.TYPE, handler);
	}

	public static boolean isOffline() {
		return !isBrowserOnline();
	}

	private static native boolean isBrowserOnline()
	/*-{
	    if($wnd.navigator.onLine != undefined) {
	        return $wnd.navigator.onLine;
	    }
	    return true;
	}-*/;

	private native void registerListeners()
	/*-{
	 	var detector = this;
		var onOnline = $entry(function() {
		detector.@org.vaadin.hybrid.offlineexample.client.OfflineDetector::onOnline()();
		});
		var onOffline = $entry(function() {
		detector.@org.vaadin.hybrid.offlineexample.client.OfflineDetector::onOffline()();
		});
		$wnd.ononline = onOnline;
		$wnd.onoffline = onOffline;
	}-*/;

	public static OfflineDetector get() {
		return instance;
	}
}
