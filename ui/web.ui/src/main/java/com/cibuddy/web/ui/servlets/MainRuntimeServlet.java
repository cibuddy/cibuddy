/**
 *
 */
package com.cibuddy.web.ui.servlets;

import com.cibuddy.web.ui.impl.Activator;
import com.cibuddy.web.ui.impl.ActivatorSample;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

final class MainRuntimeServlet extends HttpServlet {

    private static final long serialVersionUID = -7398914113448648745L;
    String content = null;

    public void doGet(HttpServletRequest req, HttpServletResponse rsp)
            throws IOException {
        rsp.setContentType("text/html");
        rsp.setCharacterEncoding("UTF-8");
        if (req.getRequestURI().startsWith("/cibuddy/")) {
            // redirect
            URL indexUrl = Activator.getBundleContext().getBundle().getEntry("/redirect.html");
            URLConnection connection = indexUrl.openConnection();
            InputStream ins = connection.getInputStream();
            OutputStream out = rsp.getOutputStream();
            byte[] buf = new byte[2048];
            int rd;
            while ((rd = ins.read(buf)) >= 0) {
                out.write(buf, 0, rd);
            }
            return;
        }
        synchronized (this) {
            if (content == null) {
                URL indexUrl = Activator.getBundleContext().getBundle().getEntry("/index.html");
                URLConnection connection = indexUrl.openConnection();
                InputStream ins = connection.getInputStream();
                OutputStream out = new ByteArrayOutputStream();//rsp.getOutputStream();
                byte[] buf = new byte[2048];
                int rd;
                while ((rd = ins.read(buf)) >= 0) {
                    out.write(buf, 0, rd);
                }
                content = out.toString();
                out.close();
            }
        }
//        String output = content.replace("##content##", analyzeRuntime(ActivatorSample.getFrameworkInspector().getRuntimeInfo()));
        rsp.getWriter().print(content);
    }
}