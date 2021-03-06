/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.redshape.renderer.forms.renderers.decorators;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.redshape.form.decorators.DecoratorAttribute;
import com.redshape.form.decorators.IDecorator;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDecorator implements IDecorator<Widget> {
    private Map<DecoratorAttribute, Object> attributes;

    public AbstractDecorator() {
        this( new HashMap<DecoratorAttribute, Object>() );
    }

    public AbstractDecorator( Map<DecoratorAttribute, Object> attributes ) {
        this.attributes = attributes;
    }

    protected void buildAttributes( Map<String, Object> attributes, Widget context) {
        Element contextElement = context.getElement();
        for ( String key : attributes.keySet() ) {
            if ( attributes.get(key) != null ) {
                contextElement.setAttribute(key, String.valueOf( attributes.get(key) ) );
            }
        }
    }

    @Override
    public boolean hasAttribute( DecoratorAttribute name ) {
        return this.attributes.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getAttribute( DecoratorAttribute name ) {
        return (T) this.attributes.get(name);
    }

    @Override
    public void setAttribute( DecoratorAttribute name, Object value ) {
        this.attributes.put(name, value);
    }

    @Override
    public void setAttributes( Map<DecoratorAttribute, Object> attributes ) {
        this.attributes = attributes;
    }

    @Override
    public boolean isSupported(DecoratorAttribute attribute) {
        return true;
    }
}
