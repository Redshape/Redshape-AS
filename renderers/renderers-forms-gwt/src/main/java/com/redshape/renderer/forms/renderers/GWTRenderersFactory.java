package com.redshape.renderer.forms.renderers;

import com.google.gwt.core.client.GWT;
import com.redshape.form.IForm;
import com.redshape.form.fields.*;
import com.redshape.renderer.AbstractRenderersFactory;
import com.redshape.renderer.IRenderer;
import com.redshape.renderer.forms.renderers.fields.*;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.renderer.forms.renderers
 * @date 2/17/12 {2:01 PM}
 */
public class GWTRenderersFactory extends AbstractRenderersFactory {

    public GWTRenderersFactory() {
        super();
        
        this.init();
    }

    @Override
    public String getFactoryId() {
        return "Renderers.Forms.GWT";
    }

    @Override
    public <T, V> IRenderer<T, V> forEntity(Class<T> object) {
        Class<? extends IRenderer<?, ?>> rendererClazz = this.entities.get(object);
        if ( rendererClazz == null ) {
            return null;
        }
        
        IRenderer<T, V> renderer = (IRenderer<T, V>) this.renderers.get( rendererClazz );
        if ( renderer != null ) {
            return renderer;
        }
        
        this.renderers.put( rendererClazz, renderer = GWT.create( rendererClazz ) );

        if ( renderer instanceof StandardGWTFormRenderer ) {
            ((StandardGWTFormRenderer) renderer).setRenderersFactory(this);
        }

        return renderer;
    }

    protected void init() {
        this.addRenderer(IForm.class, StandardGWTFormRenderer.class );
        this.addRenderer(CheckboxField.class, GWTCheckboxFieldRenderer.class );
        this.addRenderer(TextAreaField.class, GWTTextAreaFieldRenderer.class );
        this.addRenderer(InputField.class, GWTInputFieldRenderer.class );
        this.addRenderer(SelectField.class, GWTSelectFieldRenderer.class );
        this.addRenderer(RadioGroupField.class, GWTRadioGroupFieldRenderer.class );
        this.addRenderer(CheckboxGroupField.class, GWTCheckboxGroupFieldRenderer.class );
    }
}
