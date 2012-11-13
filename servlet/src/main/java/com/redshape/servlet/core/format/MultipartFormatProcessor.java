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

package com.redshape.servlet.core.format;

import com.redshape.servlet.core.HttpRequest;
import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.core.SupportType;
import com.redshape.servlet.core.controllers.ProcessingException;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cyril
 * Date: 7/10/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultipartFormatProcessor implements IRequestFormatProcessor {

    @Override
    public SupportType check(IHttpRequest request) throws ProcessingException {
        return request.isMultiPart() ? SupportType.MUST : SupportType.NO;
    }

    @Override
    public void process(IHttpRequest request) throws ProcessingException {
        try {
            Iterator parameterNames  = request.getMultipart().getParameterNames();
            while ( parameterNames.hasNext() ) {
                String parameterName = (String) parameterNames.next();
                request.setParameter( parameterName, request.getMultipart().getParameter(parameterName) );
            }
        } catch ( IOException e ) {
            throw new ProcessingException( e.getMessage(), e );
        }
    }
}
