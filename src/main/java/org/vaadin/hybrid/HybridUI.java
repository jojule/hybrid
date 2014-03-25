package org.vaadin.hybrid;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.vaadin.hybrid.backend.AddressbookBackend;
import org.vaadin.hybrid.backend.DummyAddressbookBackendImpl;
import org.vaadin.hybrid.serversideexample.AddressbookEditor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Theme("hybrid")
@SuppressWarnings("serial")
public class HybridUI extends UI {

	@WebServlet(value = "/*")
	@VaadinServletConfiguration(productionMode = false, ui = HybridUI.class, widgetset = "org.vaadin.hybrid.Client")
	public static class Servlet extends VaadinServlet {

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            // Serve offline.html and hybrid.appcache with the default servlet
            String path = request.getPathInfo();
            if (path.equals("/offline.html") || path.equals("/hybrid.appcache")) {
                getServletContext().getNamedDispatcher("default").forward(request, response);
            }
            super.service(request, response);
        }

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();

            // Manage offline caching by modifying the generated page
            getService().addSessionInitListener(new SessionInitListener() {

                @Override
                public void sessionInit(SessionInitEvent event)
                        throws ServiceException {
                    event.getSession().addBootstrapListener(
                            new BootstrapListener() {

                                @Override
                                public void modifyBootstrapPage(BootstrapPageResponse response) {

                                    // Add cache manifest to html tag of the generated page
                                    Element html = response.getDocument()
                                            .getElementsByTag("html").first();
                                    html.attr("manifest", "hybrid.appcache");

                                    // Insert widgetsetUrl to be able to cache nocache.js
                                    Element scriptTag = response.getDocument().getElementsByTag("script").last();
                                    String script = scriptTag.html();
                                    String vaadinDir = getAppConfigParameter("vaadinDir", script);
                                    String widgetset = getAppConfigParameter("widgetset", script);
                                    String widgetsetUrl = String.format(
                                            "%swidgetsets/%s/%s.nocache.js", vaadinDir, widgetset,
                                            widgetset);
                                    scriptTag.html("");
                                    scriptTag.appendChild(new DataNode(script.replace("\n});", String.format(",\n    \"widgetsetUrl\": \"%s\"\n});", widgetsetUrl)), scriptTag.baseUri()));

                                }

                                private String getAppConfigParameter(String parameter, String script) {
                                    Matcher m = Pattern.compile(
                                            String.format(".*\"%s\": \"(.*)\"", parameter)).matcher(script);
                                    if (m.find()) {
                                        return m.group(1);
                                    }
                                    return null;
                                }

                                @Override
                                public void modifyBootstrapFragment(
                                        BootstrapFragmentResponse response) {
                                }
                            });
                }
            });
        }
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

	public AddressbookBackend getAddressBookService() {
		return DummyAddressbookBackendImpl
				.getAddressBookService(((WrappedHttpSession) getSession()
                        .getSession()).getHttpSession());
	}

	private void initLayout() {
		setContent(mainLayout);

		mainLayout.addComponent(buttonBar);
		mainLayout.addComponent(exampleLayout);

		buttonBar.setWidth("100%");

		exampleLayout.setWidth("100%");
		exampleLayout.setMargin(true);
		exampleLayout.setSpacing(true);
		HomeView firstView = new HomeView();
		exampleLayout.addComponent(firstView);
		exampleLayout.setExpandRatio(firstView, 1);
		exampleLayout.addComponent(exampleDescription);

		exampleDescription.setWidth("300px");
		exampleDescription.setContentMode(ContentMode.HTML);

	}

	private void initNavigation() {

		navigator = new Navigator(this, new ViewDisplay() {
			public void showView(View view) {
				exampleLayout.replaceComponent(exampleLayout.getComponent(0),
						(Component) view);
				exampleLayout.setExpandRatio((Component) view, 1);

				showDescription(view.getClass());
			}
		});

		navigator.addView("", HomeView.class);
		addView("server", "Server-side", AddressbookEditor.class);
		addView("gwtrpc", "GWT-RPC",
        org.vaadin.hybrid.gwtrpcexample.AddressbookView.class);
		addView("connector", "Connector",
                org.vaadin.hybrid.connectorexample.AddressbookView.class);
		addView("offline",
				"Offline",
                org.vaadin.hybrid.offlineexample.AddressbookView.class);
	}

	private void showDescription(Class<? extends View> viewClass) {
		InputStream is = getClass().getResourceAsStream(
				"descriptions/" + viewClass.getName() + ".html");
		if (is == null) {
			exampleDescription.setValue("");
			return;
		}
		final char[] buffer = new char[1024];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			} finally {
				in.close();
			}
		} catch (UnsupportedEncodingException ignored) {
		} catch (IOException ignored) {
		}
		exampleDescription.setValue(out.toString());
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
		button.setWidth("100%");
		button.addStyleName("menubutton");
	}
}
