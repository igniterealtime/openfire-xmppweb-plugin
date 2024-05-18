package org.igniterealtime.openfire.plugin.xmppweb;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.openfire.spi.ConnectionType;
import org.jivesoftware.util.CookieUtils;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.ParamUtils;
import org.jivesoftware.util.StringUtils;
import org.xmpp.packet.JID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminConsoleConfigServlet extends HttpServlet
{
    protected void doCommon(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        final String csrfParam = StringUtils.randomString(15);
        CookieUtils.setCookie(request, response, "pluginCsrf", csrfParam, -1);
        request.setAttribute("pluginCsrf", csrfParam);

        request.setAttribute("httpBindManager", HttpBindManager.getInstance());
        request.setAttribute("unsecuredAddress", "http://" + XMPPServer.getInstance().getServerInfo().getHostname() + ":" + XMPPServer.getInstance().getConnectionManager().getPort(ConnectionType.BOSH_C2S, false) + "/xmppweb/");
        request.setAttribute("securedAddress", "https://" + XMPPServer.getInstance().getServerInfo().getHostname() + ":" + XMPPServer.getInstance().getConnectionManager().getPort(ConnectionType.BOSH_C2S, true) + "/xmppweb/");

        request.setAttribute("name", LocalJsServlet.NAME);
        request.setAttribute("has_registered_access", LocalJsServlet.HAS_REGISTERED_ACCESS);
        request.setAttribute("transports_websocket", LocalJsServlet.TRANSPORTS_WEBSOCKET);
        request.setAttribute("transports_bosh", LocalJsServlet.TRANSPORTS_BOSH);
        request.setAttribute("anonymous_host", LocalJsServlet.ANONYMOUS_HOST);
        request.setAttribute("is_transports_user_allowed", LocalJsServlet.IS_TRANSPORTS_USER_ALLOWED);
        request.setAttribute("has_http_auto_discovery", LocalJsServlet.HAS_HTTP_AUTO_DISCOVERY);
        request.setAttribute("resource", LocalJsServlet.RESOURCE);
        request.setAttribute("default_domain", LocalJsServlet.DEFAULT_DOMAIN);
        request.setAttribute("default_muc", LocalJsServlet.DEFAULT_MUC);
        request.setAttribute("is_styling_disabled", LocalJsServlet.IS_STYLING_DISABLED);
        request.setAttribute("has_sending_enter_key", LocalJsServlet.HAS_SENDING_ENTER_KEY);
        request.setAttribute("connect_timeout", LocalJsServlet.CONNECT_TIMEOUT);
        request.setAttribute("pinned_mucs", LocalJsServlet.PINNED_MUCS);
        request.setAttribute("logo_url", LocalJsServlet.LOGO_URL);
        request.setAttribute("guest_description", LocalJsServlet.GUEST_DESCRIPTION);

        request.getRequestDispatcher("xmppweb-config-page.jsp").forward(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        doCommon(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
    {
        final Cookie csrfCookie = CookieUtils.getCookie( request, "pluginCsrf");
        final String csrfParam = ParamUtils.getParameter( request, "pluginCsrf");

        if (csrfCookie == null || csrfParam == null || !csrfCookie.getValue().equals(csrfParam)) {
            request.setAttribute("error", "csrf");
            doCommon(request, response);
            return;
        }

        final String name = ParamUtils.getParameter(request, LocalJsServlet.NAME.getKey());
        final Boolean has_registered_access = ParamUtils.getBooleanParameter(request, LocalJsServlet.HAS_REGISTERED_ACCESS.getKey());
        final String transports_websocket = ParamUtils.getParameter(request, LocalJsServlet.TRANSPORTS_WEBSOCKET.getKey());
        final String transports_bosh = ParamUtils.getParameter(request, LocalJsServlet.TRANSPORTS_BOSH.getKey());
        final String anonymous_host = ParamUtils.getParameter(request, LocalJsServlet.ANONYMOUS_HOST.getKey());
        final Boolean is_transports_user_allowed = ParamUtils.getBooleanParameter(request, LocalJsServlet.IS_TRANSPORTS_USER_ALLOWED.getKey());
        final Boolean has_http_auto_discovery = ParamUtils.getBooleanParameter(request, LocalJsServlet.HAS_HTTP_AUTO_DISCOVERY.getKey());
        final String resource = ParamUtils.getParameter(request, LocalJsServlet.RESOURCE.getKey());
        final String default_domain = ParamUtils.getParameter(request, LocalJsServlet.DEFAULT_DOMAIN.getKey());
        final String default_muc = ParamUtils.getParameter(request, LocalJsServlet.DEFAULT_MUC.getKey());
        final Boolean is_styling_disabled = ParamUtils.getBooleanParameter(request, LocalJsServlet.IS_STYLING_DISABLED.getKey());
        final Boolean has_sending_enter_key = ParamUtils.getBooleanParameter(request, LocalJsServlet.HAS_SENDING_ENTER_KEY.getKey());
        final Integer connect_timeout = ParamUtils.getIntParameter(request, LocalJsServlet.CONNECT_TIMEOUT.getKey(), LocalJsServlet.CONNECT_TIMEOUT.getDefaultValue());
        final String pinned_mucs = ParamUtils.getParameter(request, LocalJsServlet.PINNED_MUCS.getKey());
        final String logo_url = ParamUtils.getParameter(request, LocalJsServlet.LOGO_URL.getKey());
        final String guest_description = ParamUtils.getParameter(request, LocalJsServlet.GUEST_DESCRIPTION.getKey());

        final List<JID> pinnedMucsList;
        if (pinned_mucs != null && !pinned_mucs.isBlank()) {
            pinnedMucsList = Arrays.stream(pinned_mucs.split( "\\s*,\\s*" )).map(JID::new).collect(Collectors.toList());
        } else {
            pinnedMucsList = new ArrayList<>();
        }

        // Only update properties after all input has been collected. This prevents a scenario where some properties are updated and others are not, when one input can't be properly read.
        LocalJsServlet.NAME.setValue(name);
        LocalJsServlet.HAS_REGISTERED_ACCESS.setValue(has_registered_access);
        LocalJsServlet.TRANSPORTS_WEBSOCKET.setValue(transports_websocket);
        LocalJsServlet.TRANSPORTS_BOSH.setValue(transports_bosh);
        LocalJsServlet.ANONYMOUS_HOST.setValue(anonymous_host);
        LocalJsServlet.IS_TRANSPORTS_USER_ALLOWED.setValue(is_transports_user_allowed);
        LocalJsServlet.HAS_HTTP_AUTO_DISCOVERY.setValue(has_http_auto_discovery);
        LocalJsServlet.RESOURCE.setValue(resource);
        LocalJsServlet.DEFAULT_DOMAIN.setValue(default_domain);
        LocalJsServlet.DEFAULT_MUC.setValue(default_muc);
        LocalJsServlet.IS_STYLING_DISABLED.setValue(is_styling_disabled);
        LocalJsServlet.HAS_SENDING_ENTER_KEY.setValue(has_sending_enter_key);
        LocalJsServlet.CONNECT_TIMEOUT.setValue(connect_timeout);
        LocalJsServlet.PINNED_MUCS.setValue(pinnedMucsList);
        LocalJsServlet.LOGO_URL.setValue(logo_url);
        LocalJsServlet.GUEST_DESCRIPTION.setValue(guest_description);

        request.setAttribute("success", "update");
        doCommon(request, response);
    }
}
