package com.redshape.ui.data.loaders;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10.01.11
 * Time: 23:39
 * To change this template use File | Settings | File Templates.
 */
public class LoaderException extends Exception {

    public LoaderException() {
        super();
    }

    public LoaderException( String message ) {
        super(message);
    }

    public LoaderException( String message, Throwable e ) {
        super(message, e );
    }

}
