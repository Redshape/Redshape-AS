package com.redshape.migration.renderers.mysql;

import com.redshape.migration.components.FieldType;
import com.redshape.migration.renderers.MySQLRenderer;
import com.redshape.migration.renderers.engine.MySQLRenderersFactory;
import com.redshape.render.RendererException;
import com.redshape.render.RenderersFactory;
import com.redshape.render.TargetEntity;

/**
 * WebCam Project
 *
 * @author nikelin
 * @project vio
 * @package com.vio.migration.renderers.mysql
 * @date Apr 6, 2010
 */
@TargetEntity( entity = FieldType.class )
public class FieldTypeRenderer extends MySQLRenderer<FieldType> {

    public String render( FieldType type ) throws RendererException {
        try {
            RenderersFactory factory = RenderersFactory.getFactory(MySQLRenderersFactory.class);

            StringBuilder builder = new StringBuilder();
            builder.append( type.getType().name() )
                   .append(" ");

            if ( type.getLength() != null && type.getDecimalLength() != null ) {
                builder.append( "(" )
                       .append( type.getLength() )
                       .append( "," )
                       .append( type.getDecimalLength() )
                       .append( ")" );
            } else if ( type.getLength() != null ) {
                builder.append( "(" )
                       .append( type.getLength() )
                       .append( ")" );
            }

            builder.append(" ");

            for ( String option : type.getOptions() ) {
                builder.append(option)
                       .append(" ");
            }

            return builder.toString();
        } catch ( Throwable e ) {
            throw new RendererException();
        }
    }

}