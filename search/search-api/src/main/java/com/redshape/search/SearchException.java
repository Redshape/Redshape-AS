package com.redshape.search;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 29, 2010
 * Time: 3:10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchException extends Exception {
    private static final long serialVersionUID = 5953408430033011706L;

    public SearchException() {
        this(null);
    }

    public SearchException(String message) {
        this(message, null);
    }

    public SearchException( String message, Throwable throwable ) {
        super(message, throwable);
    }

}
