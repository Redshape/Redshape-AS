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

package com.redshape.persistence.migration.strategy.entities;

import com.redshape.persistence.migration.Action;
import com.redshape.persistence.migration.MigrationException;
import com.redshape.persistence.migration.MigrationManager;
import com.redshape.persistence.migration.Migrator;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Feb 26, 2010
 * Time: 1:11:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rollback extends AbstractEntitiesStrategy {

    public Rollback( MigrationManager manager ) {
        super(manager);
    }

    protected void processMigrators( Migrator[] migrators, int from, int to ) throws MigrationException {
        int delta = from - to;
        int version = from;

        Collection<Class<?>> processed = new Vector<Class<?>>();
        int activations = 0;
        while( activations++ < migrators.length && activations != delta ) {
            for ( Migrator migrator : migrators ) {
                if ( ( migrator.getVersion() >= version && !processed.contains( migrator.getClass() )  ) && !migrator.isReductant( Action.ROLLBACK ) ) {
                    processed.add( migrator.getClass() ); 
                    migrator.rollback();
                    break;
                }
            }

            version -= 1;
        }
    }

    public boolean isAffectable( int version, int from, int to ) {
        return from > to && version < from && version > to;
    }
}
