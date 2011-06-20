package com.redshape.servlet.resources.types;

import com.redshape.utils.Commons;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 17.06.11
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class Style {
    public static String DEFAULT_MEDIA = "all";

    private String type;
    private String href;
    private String media;

    public Style( String type, String href, String media ) {
        this.type = type;
        this.href = href;
        this.media = media;
    }

    public String getType() {
        return type;
    }

    public String getHref() {
        return href;
    }

    public String getMedia() {
        return media;
    }

    @Override
    public int hashCode() {
        return ( this.getHref() + Commons.select(this.getType(), "" ) ).hashCode();
    }

    @Override
    public boolean equals( Object object ) {
        if ( !( object instanceof Style ) ) {
            return false;
        }

        return ( (Style) object ).getHref().equals( this.getHref() )
                && ( (Style) object ).getType().equals( this.getType() );
    }
}