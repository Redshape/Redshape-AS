package com.redshape.servlet.core.controllers;


import com.redshape.servlet.core.Constants;
import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.core.IHttpResponse;
import com.redshape.servlet.core.controllers.registry.IControllersRegistry;
import com.redshape.servlet.views.IView;
import com.redshape.servlet.views.IViewsFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10/11/10
 * Time: 12:57 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAction implements IAction {
    private IView view;

    @Autowired( required = true )
    private IControllersRegistry registry;
    @Autowired( required = true )
    private IViewsFactory viewsFactory;

    private IHttpRequest request;
    private IHttpResponse response;

    @Override
    public void setResponse(IHttpResponse response) {
        if ( response == null ) {
            throw new IllegalArgumentException("<null>");
        }

        this.response = response;
    }

    protected IHttpResponse getResponse() {
        return this.response;
    }

    @Override
    public void setRequest( IHttpRequest request) {
        if ( request == null ) {
            throw new IllegalArgumentException("<null>");
        }

        this.request = request;
    }

    protected IHttpRequest getRequest() {
        return this.request;
    }

    public void setViewsFactory( IViewsFactory factory ) {
    	this.viewsFactory = factory;
    }
    
    protected IViewsFactory getViewsFactory() {
    	return this.viewsFactory;
    }
    
    public void setRegistry( IControllersRegistry registry ) {
    	this.registry = registry;
    }
    
    protected IControllersRegistry getRegistry() {
    	return this.registry;
    }

    public void setView( IView view ) {
        this.view = view;
    }

    protected synchronized IView getView() {
        return this.view;
    }

    protected String getViewPath() {
        Action actionMeta = this.getClass().getAnnotation( Action.class );

        String viewPath = actionMeta.view();
        if ( viewPath == null || viewPath.isEmpty() ) {
           viewPath = actionMeta.controller().concat( Constants.URI_PATH_SEPARATOR ).concat( actionMeta.name() );
        }

        return viewPath;
    }

}
