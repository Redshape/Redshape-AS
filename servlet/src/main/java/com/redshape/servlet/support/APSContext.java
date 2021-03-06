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

package com.redshape.servlet.support;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.IEvaluator;
import com.redshape.ascript.context.IEvaluationContext;
import com.redshape.ascript.evaluation.EvaluationMode;
import com.redshape.servlet.WebApplication;
import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.core.IHttpResponse;
import com.redshape.servlet.core.context.ContextId;
import com.redshape.servlet.core.context.IResponseContext;
import com.redshape.servlet.core.SupportType;
import com.redshape.servlet.core.controllers.ProcessingException;
import com.redshape.servlet.views.IView;
import com.redshape.utils.config.IConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.servlet.core.context.support
 * @date 8/13/11 12:36 AM
 */
public class APSContext implements IResponseContext {
    public static final String EXTENSION = "aps";

    public static class ContextType extends ContextId {

        protected ContextType(String contextId) {
            super(contextId);
        }

        public static final ContextType APS = new ContextType("ContextId.APS");
    }

    @Autowired( required = true )
    private IEvaluator evaluator;

    @Override
    public ContextId getContextType() {
        return ContextType.APS;
    }

    public IEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(IEvaluator evaluator) throws EvaluationException {
        this.evaluator = evaluator;
    }

    @Override
    public SupportType isSupported(IHttpRequest request) {
        return SupportType.MAY;
    }

    @Override
    public boolean doRedirectionHandling() {
        return true;
    }

    @Override
    public boolean doExceptionsHandling() {
        return false;
    }

    @Override
    public SupportType isSupported(IView view) {
        if ( !view.getExtension().equals(EXTENSION) ) {
            return SupportType.NO;
        }

        return SupportType.MAY;
    }

    @Override
    public void proceedResponse(IView view, IHttpRequest request,
                                IHttpResponse response) throws ProcessingException {
        try {
            IEvaluationContext context = this.getEvaluator().createContext("web");
            context.exportBean("context", ApplicationContext.class, WebApplication.getContext() );
            context.exportBean("config", IConfig.class, WebApplication.getContext().getBean(IConfig.class) );

            context.exportBean("view", IView.class, view );
            context.exportBean("request", IHttpRequest.class, request );
            context.exportBean("response", IHttpResponse.class, response );

            response.getWriter().write(
                    String.valueOf(
                            this.getEvaluator()
                                    .evaluateFile(view.getLayout().getScriptPath(), EvaluationMode.EMBED)));
        } catch ( IOException e ) {
            throw new ProcessingException("IO related exception", e );
        } catch ( EvaluationException e ) {
            throw new ProcessingException("Script evaluation exception", e );
        }
    }
}
