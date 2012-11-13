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

import com.google.gwt.user.client.ui.Widget;
import com.redshape.form.IFormItem;
import com.redshape.form.decorators.DecoratorAttribute;
import com.redshape.form.decorators.IDecorator;

import java.util.Arrays;
import java.util.List;

public class ComposedDecorator extends AbstractDecorator {
	private List<IDecorator<Widget>> decorators;

	public ComposedDecorator( IDecorator<Widget>[] decorators ) {
		this( Arrays.asList(decorators) );
	}
	
	public ComposedDecorator( List<IDecorator<Widget>> decorators ) {
		this.decorators = decorators;
	}
	
	@Override
	public Widget decorate(IFormItem item, Widget data) {
		for ( IDecorator<Widget> decorator : decorators ) {
			data = decorator.decorate( item, data );
		}
		
		return data;
	}

	@Override
	public boolean isSupported(DecoratorAttribute attribute) {
		return false;
	}
}
