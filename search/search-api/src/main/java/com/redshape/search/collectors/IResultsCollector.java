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

package com.redshape.search.collectors;

import com.redshape.search.index.IIndexField;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 30, 2010
 * Time: 4:04:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IResultsCollector {

    public void collect( Class<?> collectable, Map<IIndexField, Object> fields, String id )
			throws ProcessingException;

    public <T> Collection<T> getResults() throws ProcessingException;
}
