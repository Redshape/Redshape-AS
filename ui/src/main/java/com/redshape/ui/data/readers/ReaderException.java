package com.redshape.ui.data.readers;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10.01.11
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class ReaderException extends Exception {

    public ReaderException() {
        super();
    }

    public ReaderException( String message ) {
        super(message);
    }

    public ReaderException( String message, Throwable e ) {
        super( message, e );
    }

}
