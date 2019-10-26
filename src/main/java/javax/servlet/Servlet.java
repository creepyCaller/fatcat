/*
 * Copyright (c) 1997-2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.servlet;

import java.io.IOException;

/**
 * Servlet
 *
 * @author Various
 *
 * @see 	GenericServlet
 * @see 	javax.servlet.http.HttpServlet
 */
public interface Servlet {

    /**
     * 在调用service()前调用，进行Servlet初始化
     */
    public void init() throws IOException;

    /**
     * 对request进行处理并构造response
     *
     * @param request  HTTP请求报文
     * @param response HTTP响应报文
     */
    public void service(String request, String response);

    /**
     * 将实例交给GC前调用，关闭各种流
     */
    public void destroy() throws IOException;

}
