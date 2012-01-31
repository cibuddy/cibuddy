package com.cibuddy.web.ui.impl;

import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.core.build.server.IServer;

/**
 *
 * @author mirkojahn
 */
public class HtmlUtil {
    
    public static String prettyPrintServerList() {
        IServer[] servers = Activator.getServers();
        if (servers != null && servers.length > 0) {
            StringBuilder sb = new StringBuilder();
            // table def and header
            sb.append("<table class=\"zebra-striped\">");
            sb.append("<thead>");
            sb.append("<tr>");
            sb.append("<th style=\"width: 150px;\">Source</th>");
            sb.append("<th>Alias</th>");
            sb.append("<th>Type</th>");
            sb.append("<th>URL</th>");
            sb.append("</tr>");
            sb.append("</thead>");
            // core table
            sb.append("<tbody>");
            for (int i = 0; i < servers.length; i++) {
                sb.append("<tr>");
                IServer server = servers[i];
                sb.append("<td>").append("file://somefile").append("</td>");
                sb.append("<td>").append(server.getBuildServerAlias()).append("</td>");
                sb.append("<td>").append(server.getBuildServerType()).append("</td>");
                sb.append("<td>").append(server.getBuildServerURL().toString()).append("</td>");
                sb.append("</tr>");
            }
            sb.append("</table>");
            return sb.toString();
        } else {
            return null;
        }
    }
    
    public static IBuildProject prettyPrintBuildProject() {
        return null;
    }
    
    
}
