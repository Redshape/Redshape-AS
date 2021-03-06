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

package com.redshape.persistence.dao.query;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: cwiz
 * Date: 19.11.10
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public interface IQueryHolder {

	public void init() throws QueryBuilderException;
	
    public boolean isQueryExists( String name );

    public Collection<IQuery> getQueries() throws QueryBuilderException;

    public Collection<IQuery> getQueries(com.redshape.persistence.entities.IEntity entity) throws QueryBuilderException;

    public IQuery findQuery(String queryName) throws QueryBuilderException;

}
