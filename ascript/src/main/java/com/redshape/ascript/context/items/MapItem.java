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

package com.redshape.ascript.context.items;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.context.IEvaluationContextItem;
import com.redshape.utils.ILambda;

import java.lang.reflect.Method;
import java.util.Map;

public class MapItem implements IEvaluationContextItem {
	private Map<String,?> map;
	
	public MapItem( Map<String,?> map ) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue(String name) throws EvaluationException {
		return (V) this.map.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.map;
	}
	
	@Override
	public <T> ILambda<T> getMethod( String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		throw new IllegalArgumentException("Restricted operation on map");
	}
	
}
