package com.redshape.plugins.loaders;

import com.redshape.plugins.LoaderException;
import com.redshape.plugins.loaders.resources.IPluginResource;

import java.net.URI;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.plugins.loaders
 * @date 10/11/11 12:16 PM
 */
public interface IPluginsLoader {

    /**
     * Load plugin by a given URI
     * @param path
     * @throws LoaderException
     */
	public IPluginResource load( URI path ) throws LoaderException;

}