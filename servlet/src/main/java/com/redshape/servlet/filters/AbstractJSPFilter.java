/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.redshape.servlet.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: cyril
 * Date: 4/18/12
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJSPFilter implements Filter {

    private static final Logger log = Logger.getLogger( I18NFilter.class );

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initialized");
        this.config = filterConfig;
    }

    protected FilterConfig getConfig() {
        return config;
    }

    protected String[] getAllowedExtensions() {
        return this.config.getInitParameter("extensions") == null ? new String[] {}
                : this.config.getInitParameter("extensions").split(",");
    }

    protected boolean isMatch( String path ) {
        for ( String extension : this.getAllowedExtensions() ) {
            if ( path.endsWith(extension) ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String srcPath = (String) request.getRequestURI();
        if (srcPath == null) {
            srcPath = request.getServletPath();
        }

        if ( !this.isMatch(srcPath) ) {
            chain.doFilter(req, res);
            return;
        }

        String realSrcPath = this.config.getServletContext().getRealPath(srcPath);
        if (realSrcPath == null) {
            throw new FileNotFoundException("Could not get real path for "
                    + "source JSP file '" + srcPath + "'");
        }

        String path = this.config.getServletContext().getRealPath(srcPath);
        if (realSrcPath == null) {
            throw new FileNotFoundException("Could not get real path for "
                    + "source JSP file '" + srcPath + "'");
        }

        File src = new File(realSrcPath);
        if (!src.exists()) {
            chain.doFilter(req, res);
            return;
        }

        this.process(request, response, new FileInputStream(src), new FileOutputStream(src));

        chain.doFilter(req, res);

    }

    protected void writeString( String data, OutputStream stream ) throws IOException {
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(stream) );
        writer.write(data);
        writer.close();
    }

    protected String readString( InputStream stream ) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader( new InputStreamReader(stream) );
        String tmp;
        while ( null != ( tmp = reader.readLine() ) ) {
            builder.append(tmp);
        }

        return builder.toString();
    }

    @Override
    public void destroy() {}

    abstract protected void process( HttpServletRequest request,
                                     HttpServletResponse response,
                                     InputStream in,
                                     OutputStream out )
            throws IOException, ServletException;

}
