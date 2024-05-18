package org.igniterealtime.openfire.plugin.xmppweb;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.admin.AdminManager;
import org.jivesoftware.openfire.http.HttpBindManager;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.SystemProperty;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class LocalJsServlet extends HttpServlet
{
    private static final Logger Log = LoggerFactory.getLogger( LocalJsServlet.class );

    public static SystemProperty<String> NAME = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.name")
        .setDefaultValue("XMPP web")
        .setDynamic(true)
        .build();

    public static SystemProperty<Boolean> HAS_REGISTERED_ACCESS = SystemProperty.Builder.ofType(Boolean.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.has_registered_access")
        .setDefaultValue(true)
        .setDynamic(true)
        .build();

    public static SystemProperty<String> TRANSPORTS_WEBSOCKET = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.transports.websocket")
        .setDefaultValue(HttpBindManager.getInstance().getWebsocketSecureAddress())
        .setDynamic(true)
        .build();

    public static SystemProperty<String> TRANSPORTS_BOSH = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.transports.bosh")
        .setDefaultValue(HttpBindManager.getInstance().getHttpBindSecureAddress())
        .setDynamic(true)
        .build();

    public static SystemProperty<String> ANONYMOUS_HOST = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.anonymous_host")
        .setDefaultValue(null)
        .setDynamic(true)
        .build();

    public static SystemProperty<Boolean> IS_TRANSPORTS_USER_ALLOWED = SystemProperty.Builder.ofType(Boolean.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.is_transports_user_allowed")
        .setDefaultValue(false)
        .setDynamic(true)
        .build();

    public static SystemProperty<Boolean> HAS_HTTP_AUTO_DISCOVERY = SystemProperty.Builder.ofType(Boolean.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.has_http_auto_discovery")
        .setDefaultValue(false)
        .setDynamic(true)
        .build();

    public static SystemProperty<String> RESOURCE = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.resource")
        .setDefaultValue("Web XMPP")
        .setDynamic(true)
        .build();

    public static SystemProperty<String> DEFAULT_DOMAIN = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.default_domain")
        .setDefaultValue(XMPPServer.getInstance().getServerInfo().getXMPPDomain())
        .setDynamic(true)
        .build();

    public static SystemProperty<String> DEFAULT_MUC = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.default_muc")
        .setDefaultValue(XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatServices().get(0).getServiceDomain())
        .setDynamic(true)
        .build();

    public static SystemProperty<Boolean> IS_STYLING_DISABLED = SystemProperty.Builder.ofType(Boolean.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.is_styling_disabled")
        .setDefaultValue(false)
        .setDynamic(true)
        .build();

    public static SystemProperty<Boolean> HAS_SENDING_ENTER_KEY = SystemProperty.Builder.ofType(Boolean.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.has_sending_enter_key")
        .setDefaultValue(false)
        .setDynamic(true)
        .build();

    public static SystemProperty<Integer> CONNECT_TIMEOUT = SystemProperty.Builder.ofType(Integer.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.connect_timeout")
        .setDefaultValue(5000)
        .setDynamic(true)
        .build();

    public static final SystemProperty<List<JID>> PINNED_MUCS = SystemProperty.Builder.ofType(List.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.pinned_mucs")
        .setDefaultValue(Collections.emptyList())
        .setSorted(true)
        .setDynamic(true)
        .buildList(JID.class);

    public static SystemProperty<String> LOGO_URL = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.logo_url")
        .setDefaultValue("./img/icons/android-chrome-192x192.png")
        .setDynamic(true)
        .build();

    public static SystemProperty<String> GUEST_DESCRIPTION = SystemProperty.Builder.ofType(String.class)
        .setPlugin("xmppweb")
        .setKey("xmppweb.config.guest_description")
        .setDefaultValue("")
        .setDynamic(true)
        .build();

    public void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        Log.trace( "Processing doGet()" );

        final JSONObject config = new JSONObject();
        final JSONObject transports = new JSONObject();
        config.put("transports", transports);

        config.put("name", NAME.getValue());
        config.put("hasRegisteredAccess", HAS_REGISTERED_ACCESS.getValue());
        transports.put("websocket", TRANSPORTS_WEBSOCKET.getValue());
        transports.put("bosh", TRANSPORTS_BOSH.getValue());
        config.put("anonymousHost", ANONYMOUS_HOST.getValue());
        config.put("isTransportsUserAllowed", IS_TRANSPORTS_USER_ALLOWED.getValue());
        config.put("hasHttpAutoDiscovery", HAS_HTTP_AUTO_DISCOVERY.getValue());
        config.put("resource", RESOURCE.getValue());
        config.put("defaultDomain", DEFAULT_DOMAIN.getValue());
        config.put("defaultMuc", DEFAULT_MUC.getValue());
        config.put("isStylingDisabled", IS_STYLING_DISABLED.getValue());
        config.put("hasSendingEnterKey", HAS_SENDING_ENTER_KEY.getValue());
        config.put("connectTimeout", CONNECT_TIMEOUT.getValue());

        final JSONArray pinnedMucs = new JSONArray();
        PINNED_MUCS.getValue().forEach(muc -> pinnedMucs.put(muc.toString()));
        config.put("pinnedMucs", pinnedMucs);

        config.put("logoUrl", LOGO_URL.getValue());
        config.put("guestDescription", GUEST_DESCRIPTION.getValue());

        try ( final Writer writer = response.getWriter() )
        {
            writer.write( "// Dynamically generated based on Openfire settings\n");
            // Replacing all quotes around property keys to make this match the format used by the upstream project.
            writer.write( "var config = " + config.toString( 2 ).replaceAll("\"(\\w+)\"\\s*:", "$1:") );
            writer.flush();
        }
    }
}
