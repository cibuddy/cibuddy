/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
