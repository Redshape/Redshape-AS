package com.redshape.utils.beans;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jul 23, 2010
 * Time: 3:31:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BeanException extends RuntimeException {
	private static final long serialVersionUID = 867505721529125159L;

	public BeanException() {
        super();
    }

    public BeanException( String message ) {
        super(message);
    }
}