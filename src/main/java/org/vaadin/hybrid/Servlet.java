package org.vaadin.hybrid;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/*")
@VaadinServletConfiguration(productionMode = false, ui = HybridUI.class, widgetset = "org.vaadin.hybrid.Client")
public class Servlet extends VaadinServlet {

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
                        }
                );
            }
        });
    }
}
