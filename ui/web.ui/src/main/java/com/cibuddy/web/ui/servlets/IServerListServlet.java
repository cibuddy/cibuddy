package com.cibuddy.web.ui.servlets;

import com.cibuddy.web.ui.impl.HtmlUtil;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IServerListServlet extends HttpServlet {

    /**
     * Serial ID
     */
    private static final long serialVersionUID = -7653896054054421890L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        rsp.setContentType("text/html");
        rsp.setCharacterEncoding("UTF-8");
        OutputStream out = rsp.getOutputStream();
        out.write(HtmlUtil.prettyPrintServerList().getBytes());
    }

    
}
