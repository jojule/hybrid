package org.vaadin.hybrid;

import javax.servlet.annotation.WebServlet;

import org.vaadin.hybrid.gwtrpc.ClientSideAddressbookGWTRPCView;
import org.vaadin.hybrid.serverside.AddressbookEditor;
import org.vaadin.hybrid.service.AddressbookService;
import org.vaadin.hybrid.service.DummyAddressbookServiceImpl;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedHttpSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("hybrid")
@SuppressWarnings("serial")
public class HybridUI extends UI {

	@WebServlet(value = "/*")
	@VaadinServletConfiguration(productionMode = false, ui = HybridUI.class, widgetset = "org.vaadin.hybrid.Client")
	public static class Servlet extends VaadinServlet {
	}

	final VerticalLayout mainLayout = new VerticalLayout();
	final HorizontalLayout buttonBar = new HorizontalLayout();
	final HorizontalLayout exampleLayout = new HorizontalLayout();
	final Label exampleDescription = new Label("");
	Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {

		initNavigation();
		initLayout();
	}

	public AddressbookService getAddressBookService() {
		return DummyAddressbookServiceImpl
				.getAddressBookService(((WrappedHttpSession) getSession()
						.getSession()).getHttpSession());
	}

	private void initLayout() {
		setContent(mainLayout);

		mainLayout.addComponent(buttonBar);
		mainLayout.addComponent(exampleLayout);

		buttonBar.setWidth("100%");

		exampleLayout.setWidth("100%");
		HomeView firstView = new HomeView();
		exampleLayout.addComponent(firstView);
		exampleLayout.setExpandRatio(firstView, 1);
		exampleLayout.addComponent(exampleDescription);

		exampleDescription.setWidth("300px");

	}

	private void initNavigation() {

		navigator = new Navigator(this, new ViewDisplay() {
			public void showView(View view) {
				exampleLayout.replaceComponent(exampleLayout.getComponent(0),
						(Component) view);
				exampleLayout.setExpandRatio((Component) view, 1);
			}
		});

		navigator.addView("", HomeView.class);
		addView("server", "Server-side", AddressbookEditor.class);
		addView("client-gwtrpc", "Client-side GWT-RPC", ClientSideAddressbookGWTRPCView.class);
		addView("client-vaadinrpc", "Client-side Vaadin-RPC",
				UnimplementedView.class);
		addView("offline", "Offline", UnimplementedView.class);

	}

	private void addView(final String viewName, final String buttonCaption,
			final Class<? extends View> viewClass) {
		navigator.addView(viewName, viewClass);
		NativeButton button = new NativeButton(buttonCaption,
				new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						navigator.navigateTo(viewName);
					}
				});
		buttonBar.addComponent(button);
	}
}
