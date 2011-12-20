package com.redshape.plugins.packagers;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: cyril
 * Date: 12/16/11
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPackageEntry {
    
    public String getName();
    
    public String getPath();
    
    public int getSize();
    
    public InputStream getInputStream();
    
    public String getMimeType();
    
    public URI getURI();

}