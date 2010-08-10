package com.vio.render;

import java.util.Collection;
import java.util.Map;

/**
 * WebCam Project
 *
 * @author nikelin
 * @project vio
 * @package com.vio.render
 * @date Mar 16, 2010
 */
public interface Renderer<T extends Renderable> {
    
    public Object render( T renderable ) throws RendererException;

    public Object render( Collection<? extends T> renderable ) throws RendererException;

}