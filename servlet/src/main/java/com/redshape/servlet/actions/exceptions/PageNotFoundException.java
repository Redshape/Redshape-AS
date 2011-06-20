package com.redshape.servlet.actions.exceptions;

import com.redshape.servlet.core.controllers.ProcessingException;

public class PageNotFoundException extends ProcessingException {
	private static final long serialVersionUID = -6373951826775646458L;

	public PageNotFoundException() {
		this(null);
	}
	
	public PageNotFoundException( String message ) {
		this(message, null);
	}
	
	public PageNotFoundException( String message, Throwable e ) {
		super(message, e);
	}
	
}