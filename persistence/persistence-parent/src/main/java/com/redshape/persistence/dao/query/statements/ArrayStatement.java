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

package com.redshape.persistence.dao.query.statements;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 1/23/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayStatement implements IArrayStatement {
    private IStatement[] statements;
    
    public ArrayStatement(IStatement... statements) {
        this.statements = statements;
    }

    @Override
    public int getSize() {
        return this.statements.length;
    }

    @Override
    public IStatement getStatement( int statement ) {
        if ( statement < 0 || statement > this.statements.length ) {
            throw new ArrayIndexOutOfBoundsException(statement);
        }

        return this.statements[statement];
    }

    @Override
    public IStatement[] getStatements() {
        return this.statements;
    }
    
}
