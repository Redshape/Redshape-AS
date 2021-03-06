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

package com.redshape.servlet.dispatchers.http;

import com.redshape.servlet.actions.exceptions.PageNotFoundException;
import com.redshape.servlet.actions.exceptions.handling.IPageExceptionHandler;
import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.core.IHttpResponse;
import com.redshape.servlet.core.context.ContextId;
import com.redshape.servlet.core.context.IContextSwitcher;
import com.redshape.servlet.core.context.IResponseContext;
import com.redshape.servlet.core.controllers.FrontController;
import com.redshape.servlet.core.controllers.IAction;
import com.redshape.servlet.core.controllers.ProcessingException;
import com.redshape.servlet.core.controllers.registry.IControllersRegistry;
import com.redshape.servlet.core.restrictions.ContextRestriction;
import com.redshape.servlet.core.restrictions.MethodRestriction;
import com.redshape.servlet.dispatchers.DispatchException;
import com.redshape.servlet.dispatchers.interceptors.IDispatcherInterceptor;
import com.redshape.servlet.views.*;
import com.redshape.utils.Commons;
import com.redshape.utils.ResourcesLoader;
import com.redshape.utils.StringUtils;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10/10/10
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpDispatcher implements IHttpDispatcher {
	private static final Logger log = Logger.getLogger( HttpDispatcher.class );

    private List<IDispatcherInterceptor> interceptors = new ArrayList<IDispatcherInterceptor>();

    private IContextSwitcher contextSwitcher;

    @Autowired( required = true )
    private ResourcesLoader resourcesLoader;

    @Autowired( required = true )
    private IViewsFactory viewFactory;

    @Autowired( required = true )
    private IConfig config;

	@Autowired( required = true )
	private IControllersRegistry registry;

    private IResponseContext defaultContext;

	private FrontController front;
	
	private ApplicationContext context;

    public IPageExceptionHandler exceptionHandler;

    public IResponseContext getDefaultContext() {
        return defaultContext;
    }

    public void setDefaultContext(IResponseContext defaultContext) {
        this.defaultContext = defaultContext;
    }

    public List<IDispatcherInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<IDispatcherInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public IPageExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(IPageExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public IContextSwitcher getContextSwitcher() {
        return contextSwitcher;
    }

    public void setContextSwitcher(IContextSwitcher contextSwitcher) {
        this.contextSwitcher = contextSwitcher;
    }

    public ResourcesLoader getResourcesLoader() {
        return resourcesLoader;
    }

    public void setResourcesLoader(ResourcesLoader resourcesLoader) {
        this.resourcesLoader = resourcesLoader;
    }

    public IViewsFactory getViewFactory() {
        return viewFactory;
    }

    public void setViewFactory(IViewsFactory viewFactory) {
        this.viewFactory = viewFactory;
    }

    public void setRegistry( IControllersRegistry registry ) {
		this.registry = registry;
	}
	
	protected IControllersRegistry getRegistry() {
		return this.registry;
	}
	
	public void setFront( FrontController front ) {
		this.front = front;
	}
	
	protected FrontController getFront() {
		if ( this.front == null ) {
			this.front = this.getContext().getBean( FrontController.class );
		}
		
		return this.front;
	}
	
	protected ApplicationContext getContext() {
		return this.context;
	}
	
	@Override
	public void setApplicationContext( ApplicationContext context ) {
		this.context = context;
	}

    protected IView getView( IHttpRequest request ) {
        return this.getViewFactory().getView( request );
    }

    protected void tryRedirectToView( IHttpRequest request,
                                      IHttpResponse response ) throws ProcessingException {
        String path = String.format("%s" + File.separator + "%s", request.getController(), request.getAction() );

        IView view = this.getView(request);
        view.setViewPath( path );
        request.getSession().setAttribute("view", view );
        request.getSession().setAttribute("layout", this.getFront().getLayout() );

        this.getResourcesLoader().setRootDirectory( this.getFront().getLayout().getBasePath() );

        try {
            String filePath = "views" + File.separator + view.getViewPath() + "." + view.getExtension();
            try {
                this.getResourcesLoader().loadFile( filePath, true );
            } catch ( FileNotFoundException e ) {
                try {
                    view.setViewPath( String.format("%s" + File.separator + "index", request.getController() ) );
                    this.getResourcesLoader().loadFile( "views" + File.separator + view.getViewPath() + "." + view.getExtension(),
                                                    true );
                    request.setAction("index");
                } catch ( FileNotFoundException ex ) {
                    throw new PageNotFoundException( "View file " + filePath + " not found", ex );
                }
            }

            this._invokeInterceptors( null, view, request, response );

            this.redirectToView( view, request, response );
        } catch ( ProcessingException e ) {
            throw e;
        } catch ( Throwable e ) {
            throw new ProcessingException( e.getMessage(), e );
        }
    }

    protected void redirectToView( IView view, IHttpRequest request, IHttpResponse response )
        throws DispatchException {
        try {
            IResponseContext context = this.getContextSwitcher().chooseContext( request, view );
            if ( context == null ) {
                response.sendError(500);
                return;
            }

            request.setAttribute( IHttpRequest.CONTEXT_TYPE_SELECTED, context.getContextType() );

            response.setCharacterEncoding("UTF-8");

			try {
                context.proceedResponse( view, request, response );
            } catch ( ProcessingException e ) {
                this.processError( e, view, request, response );
            }
        } catch ( DispatchException e ) {
            throw e;
        } catch ( Throwable e ) {
            throw new DispatchException( e.getMessage(), e );
        }
    }

    protected void processError( ProcessingException e, IView view, IHttpRequest request, IHttpResponse response )
            throws DispatchException {
        log.error( e.getMessage(), e );

        request.setAttribute( IHttpRequest.FAILED_TO_PROCESS, true );

        if ( view != null ) {
            view.setException(e);
        } else {
            view = new View(null, null);
            view.setException( e );
        }

        if ( this.defaultContext != null
                && this.defaultContext.doExceptionsHandling() ) {
            try {
                this.defaultContext.proceedResponse(view, request, response);
                return;
            } catch ( ProcessingException ex ) {
                throw new DispatchException( ex.getMessage(), e );
            }
        }

        if ( this.getExceptionHandler() == null ) {
            throw new DispatchException( e.getMessage(), e );
        }

		try {
        	this.getExceptionHandler().handleException( e, request, response );
		} catch ( IOException ex ) {
			throw new DispatchException( ex.getMessage(), ex );
		}
    }

    protected void checkContextRestrictions( IAction action, IView view, IHttpRequest request, IHttpResponse response )
        throws DispatchException, ConfigException {
        List<String> restrictionValues = new ArrayList<String>();
        ContextRestriction restriction = action.getClass().getAnnotation(ContextRestriction.class);
        if ( restriction != null ) {
            restrictionValues.addAll( Arrays.asList(restriction.value()) );
        } else {
            IConfig restrictionNode = this.config.get("web.contextRestriction");
            if ( restrictionNode.isNull() ) {
                return;
            }

            restrictionValues.addAll( Arrays.asList( restrictionNode.value().split(",") ) );
        }

        boolean found = false;
        for ( String restrictionValue : restrictionValues ) {
            ContextId contextId = ContextId.valueOf( restrictionValue );
            if ( contextId == null ) {
                throw new DispatchException("Context restriction references to unknown context type");
            }

            IResponseContext expected = this.getContextSwitcher().chooseContext( contextId );
            if ( expected == null ) {
               continue;
            }

            IResponseContext actualContext = this.getContextSwitcher().chooseContext( request, view );
            if ( expected.equals(actualContext) ) {
                found = true;
                break;
            }
        }

        if ( !found ) {
            throw new DispatchException("Interaction with requested method allowed only under `"
                    + StringUtils.join(restrictionValues, " ")
                    + "`  environment");
        }
    }

    protected void checkMethodRestrictions( IAction action, IView view, IHttpRequest request, IHttpResponse response )
        throws DispatchException {
        MethodRestriction restriction = action.getClass().getAnnotation(MethodRestriction.class);
        if ( restriction == null ) {
            return;
        }

        boolean found = false;
        for ( String allowedMethod : restriction.value() ) {
            if ( request.getMethod().equals(allowedMethod) ) {
                found = true;
                break;
            }
        }

        if ( !found ) {
            throw new DispatchException( request.getMethod() + "-method is not allowed by requested action");
        }
    }

    protected void checkRestrictions( IAction action, IView view, IHttpRequest request, IHttpResponse response )
        throws DispatchException, ConfigException {
        this.checkContextRestrictions(action, view, request, response);
        this.checkMethodRestrictions(action, view, request, response );
    }

	@Override
    public void dispatch( ServletConfig servletContext, IHttpRequest request, IHttpResponse response )
    	throws DispatchException {
        try {

			ViewHelper.setLocalHttpRequest(request);

            /**
             * Move this conditional check to a context in a some
             * way...
             */
        	if ( request.getRequestURI().endsWith("jsp") ) {
                return;
        	}

            IView view = this.getView(request);
            view.reset( ResetMode.TRANSIENT );

            ILayout layoutView = this.getFront().getLayout();
            if ( layoutView.getDispatchAction() != null ) {
                layoutView.getDispatchAction().setView(view);
                layoutView.getDispatchAction().process();
            }

        	String controllerName = Commons.select( request.getController(), "index" );
			if ( controllerName.isEmpty() ) {
				controllerName = "index";
			}
			request.setController(controllerName);

        	String actionName = Commons.select(request.getAction(), "index");
			if ( actionName.isEmpty() ) {
				actionName = "index";
			}
			request.setAction(actionName);

            if ( actionName == null ) {
                this.tryRedirectToView(request, response);
                return;
            }
        	
        	log.info("Requested page: " + controllerName + "/" + actionName );

            IAction action = this.getRegistry().getInstance( controllerName, actionName );
            if ( action == null ) {
                this.tryRedirectToView( request, response );
                return;
            }

            this.checkRestrictions(action, view, request, response);

            String viewPath = this.getRegistry().getViewPath(action);

            view.setViewPath( viewPath != null ? StringUtils.escapePath(viewPath) : controllerName + File.separator + actionName );
            action.setView( view );
            action.setServletConfig(servletContext);
            action.setRequest( request );
            action.setResponse( response );

            action.checkPermissions();

            this._invokeInterceptors( null, view, request, response );

            action.process();

            if ( view.getException() != null
                    && this.getContextSwitcher().chooseContext( request, view ).doExceptionsHandling() ) {
                this.processError(view.getException(), view, request, response);
                return;
            }

            if ( view.getRedirection() != null
                    && this.getContextSwitcher().chooseContext( request, view ).doRedirectionHandling() ) {
                response.sendRedirect( ViewHelper.url(view.getRedirection()) );
            }
            
            if ( response.isCommitted() ) {
            	return;
            }

            request.getSession().setAttribute("layout", this.getFront().getLayout() );
            request.getSession().setAttribute("view", view );

            this.redirectToView( view, request, response);
        } catch ( ProcessingException e ) {
            this.processError(e, null,  request, response);
        } catch ( Throwable e ) {
        	this.processError( new ProcessingException( e.getMessage(), e ), null, request, response );
        }
    }

    private void _invokeInterceptors( IResponseContext context, IView view,
                                      IHttpRequest request, IHttpResponse response )
                    throws ProcessingException {
        for ( IDispatcherInterceptor interceptor : this.getInterceptors() ) {
            interceptor.invoke( context, view, request, response );
        }
    }


}