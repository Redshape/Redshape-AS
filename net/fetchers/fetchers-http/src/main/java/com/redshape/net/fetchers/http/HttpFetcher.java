package com.redshape.net.fetchers.http;

import com.redshape.net.fetchers.AbstractFetcher;
import com.redshape.net.fetchers.FetcherException;
import com.redshape.net.fetchers.IResponse;
import com.redshape.net.fetchers.charset.ICharsetConverter;
import com.redshape.utils.IEnum;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Nov 9, 2010
 * Time: 4:21:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpFetcher extends AbstractFetcher<HttpFetcher.Attribute> {
    private static final Logger log = Logger.getLogger( HttpFetcher.class );
    public static ICharsetConverter<?> DEFAULT_CHARSET_CONVERTER;
    private SimpleTransformer transformer;

    public enum Attribute implements IEnum<String> {
        USER_AGENT,
        VALIDATE
    }

    public HttpFetcher() {
        this(AbstractFetcher.DEFAULT_TIMEOUT);
    }

    public HttpFetcher( Integer timeout ) {
        super(timeout);

        this.transformer = new SimpleTransformer();
        this.setAttribute( Attribute.VALIDATE, false );
        this.setAttribute( Attribute.USER_AGENT, "Mozilla");
        this.setCharsetConverter( DEFAULT_CHARSET_CONVERTER );
    }

    @Override
    public IResponse post( String address, Map<String, String> params ) throws FetcherException {
        try {
            Connection connection = Jsoup.connect( address );
            connection.data( params );
            connection.method(Connection.Method.POST);

            Connection.Response response = connection.execute();

            return new HttpResponse( response );
        } catch ( IOException e ) {
            throw new FetcherException( e.getMessage() );
        }
    }

    public Document fetchData( String data ) {
        return this.transformer.transform(  Jsoup.parse(data) );
    }

    @Override
    public Document fetchDocument( String address ) throws FetcherException, IOException {
        try {
            String data = this.fetch(address);

            return this.transformer.transform( Jsoup.parse( data ) );
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
            throw new IOException( e.getMessage() );
        }
    }

    @Override
    public String fetch( String address ) throws FetcherException, IOException {
        if ( this.getFilter() != null && !this.getFilter().filter(address) ) {
            throw new IOException("Wrong address given");
        }

        String data = Jsoup.connect(address)
                    .userAgent( this.getString( Attribute.USER_AGENT ) )
                    .timeout( this.getTimeout() )
                    .execute()
                        .body();

        if (  this.getBoolean( Attribute.VALIDATE ) == true ) {
            try {
                this.fetchData(data);
            } catch ( Throwable e ) {
                log.error( e.getMessage(), e );
                throw new IOException();
            }
        }

        return data;
    }

}
