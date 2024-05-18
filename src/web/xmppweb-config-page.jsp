<%--
  - Copyright (C) 2024 Ignite Realtime Foundation. All rights reserved.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
--%>
<%--@elvariable id="httpBindManager" type="org.jivesoftware.openfire.http.HttpBindManager"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%--@elvariable id="success" type="java.lang.String"--%>
<%--@elvariable id="pluginCsrf" type="java.lang.String"--%>
<%--@elvariable id="securedAddress" type="java.lang.String"--%>
<%--@elvariable id="unsecuredAddress" type="java.lang.String"--%>
<%--@elvariable id="name" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="has_registered_access" type="org.jivesoftware.util.SystemProperty<java.lang.Boolean>"--%>
<%--@elvariable id="transports_websocket" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="transports_bosh" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="anonymous_host" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="is_transports_user_allowed" type="org.jivesoftware.util.SystemProperty<java.lang.Boolean>"--%>
<%--@elvariable id="has_http_auto_discovery" type="org.jivesoftware.util.SystemProperty<java.lang.Boolean>"--%>
<%--@elvariable id="resource" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="default_domain" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="default_muc" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="is_styling_disabled" type="org.jivesoftware.util.SystemProperty<java.lang.Boolean>"--%>
<%--@elvariable id="has_sending_enter_key" type="org.jivesoftware.util.SystemProperty<java.lang.Boolean>"--%>
<%--@elvariable id="connect_timeout" type="org.jivesoftware.util.SystemProperty<java.lang.Integer>"--%>
<%--@elvariable id="pinned_mucs" type="org.jivesoftware.util.SystemProperty<java.util.List<org.xmpp.packet.JID>>"--%>
<%--@elvariable id="logo_url" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>
<%--@elvariable id="guest_description" type="org.jivesoftware.util.SystemProperty<java.lang.String>"--%>


<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="admin" prefix="admin" %>
<html>
<head>
    <title><fmt:message key="config.page.title"/></title>
    <meta name="pageID" content="xmppweb-config"/>
</head>
<body>

<c:if test="${not httpBindManager.httpBindEnabled}">
    <admin:infobox type="warning">
        <fmt:message key="warning.httpbinding.disabled">
            <fmt:param value="<a href=\"../../http-bind.jsp\">"/>
            <fmt:param value="</a>"/>
        </fmt:message>
    </admin:infobox>
</c:if>

<c:choose>
    <c:when test="${not empty error}">
        <admin:infobox type="error">
            <c:choose>
                <c:when test="${error eq 'csrf'}">
                    <fmt:message key="global.csrf.failed" />
                </c:when>
                <c:otherwise>
                    <fmt:message key="admin.error" />: <c:out value="${error}"/>
                </c:otherwise>
            </c:choose>
        </admin:infobox>        
    </c:when>
    <c:when test="${not empty success}">
        <admin:infobox type="success">
            <fmt:message key="properties.save.success" />
        </admin:infobox>
    </c:when>
</c:choose>

<p>
    <fmt:message key="config.page.description"/>
    <c:if test="${httpBindManager.httpBindActive}">
        <fmt:message key="config.page.link.unsecure">
            <fmt:param><c:out value="${unsecuredAddress}"/></fmt:param>
        </fmt:message>
    </c:if>
    <c:if test="${httpBindManager.httpsBindActive}">
        <fmt:message key="config.page.link.secure">
            <fmt:param><c:out value="${securedAddress}"/></fmt:param>
        </fmt:message>
    </c:if>
</p>

<br>

<fmt:message key="config.page.general.header" var="title" />
<admin:contentBox title="${title}">

    <p><fmt:message key="config.page.general.description" /></p>

    <form action="xmppweb-config.jsp" method="post">
        <input type="hidden" name="pluginCsrf" value="<c:out value="${pluginCsrf}"/>">
        <input type="hidden" name="update" value="general"/>

        <table cellpadding="3" cellspacing="0" border="0">
            <tbody>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${name.key}">${name.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="text" name="${name.key}" id="${name.key}" size="30" value="${admin:escapeHTMLTags(name.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <input type="checkbox" name="${has_registered_access.key}" id="${has_registered_access.key}" ${has_registered_access.value ? 'checked' : ''}/>&nbsp;
                        <label for="${has_registered_access.key}">
                            ${has_registered_access.description}
                        </label>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <b><label for="${transports_websocket.key}">${transports_websocket.description}</label></b>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em; padding-left: 2em;">
                        <input type="text" name="${transports_websocket.key}" id="${transports_websocket.key}" size="90" value="${admin:escapeHTMLTags(transports_websocket.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <b><label for="${transports_bosh.key}">${transports_bosh.description}</label></b>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em; padding-left: 2em;">
                        <input type="text" name="${transports_bosh.key}" id="${transports_bosh.key}" size="90" value="${admin:escapeHTMLTags(transports_bosh.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <b><label for="${anonymous_host.key}">${anonymous_host.description}</label></b>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em; padding-left: 2em;">
                        <input type="text" name="${anonymous_host.key}" id="${anonymous_host.key}" size="90" value="${admin:escapeHTMLTags(anonymous_host.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <input type="checkbox" name="${is_transports_user_allowed.key}" id="${is_transports_user_allowed.key}" ${is_transports_user_allowed.value ? 'checked' : ''}/>&nbsp;
                        <label for="${is_transports_user_allowed.key}">
                                ${is_transports_user_allowed.description}
                        </label>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <input type="checkbox" name="${has_http_auto_discovery.key}" id="${has_http_auto_discovery.key}" ${has_http_auto_discovery.value ? 'checked' : ''}/>&nbsp;
                        <label for="${has_http_auto_discovery.key}">
                                ${has_http_auto_discovery.description}
                        </label>
                    </td>
                </tr>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${resource.key}">${resource.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="text" name="${resource.key}" id="${resource.key}" size="30" value="${admin:escapeHTMLTags(resource.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${default_domain.key}">${default_domain.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="text" name="${default_domain.key}" id="${default_domain.key}" size="30" value="${admin:escapeHTMLTags(default_domain.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${default_muc.key}">${default_muc.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="text" name="${default_muc.key}" id="${default_muc.key}" size="30" value="${admin:escapeHTMLTags(default_muc.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <input type="checkbox" name="${is_styling_disabled.key}" id="${is_styling_disabled.key}" ${is_styling_disabled.value ? 'checked' : ''}/>&nbsp;
                        <label for="${is_styling_disabled.key}">
                                ${is_styling_disabled.description}
                        </label>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <input type="checkbox" name="${has_sending_enter_key.key}" id="${has_sending_enter_key.key}" ${is_styling_disabled.value ? 'checked' : ''}/>&nbsp;
                        <label for="${has_sending_enter_key.key}">
                                ${has_sending_enter_key.description}
                        </label>
                    </td>
                </tr>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${connect_timeout.key}">${connect_timeout.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="number" name="${connect_timeout.key}" id="${connect_timeout.key}" min="1" value="${admin:escapeHTMLTags(connect_timeout.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <b><label for="${pinned_mucs.key}">${pinned_mucs.description}</label></b>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em; padding-left: 2em;">
                        <textarea name="${pinned_mucs.key}" id="${pinned_mucs.key}" cols="120" rows="5"><c:forEach var="muc" items="${pinned_mucs.value}" varStatus="status"><c:out value="${muc}"/><c:if test="${not status.last}">, </c:if></c:forEach></textarea>
                    </td>
                </tr>
                <tr valign="top">
                    <td width="1%" nowrap style="padding-top: 1em;">
                        <b><label for="${logo_url.key}">${logo_url.description}</label></b>
                    </td>
                    <td width="99%" style="padding-top: 1em;">
                        <input type="text" name="${logo_url.key}" id="${logo_url.key}" size="30" value="${admin:escapeHTMLTags(logo_url.value)}">
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em;">
                        <b><label for="${guest_description.key}">${guest_description.description}</label></b>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" style="padding-top: 1em; padding-left: 2em;">
                        <textarea name="${guest_description.key}" id="${guest_description.key}" cols="120" rows="5"><c:out value="${guest_description.value}"/></textarea>
                    </td>
                </tr>
            </tbody>
        </table>

        <br>

        <input type="submit" value="<fmt:message key="global.save_settings" />">

    </form>

</admin:contentBox>

</body>
</html>
