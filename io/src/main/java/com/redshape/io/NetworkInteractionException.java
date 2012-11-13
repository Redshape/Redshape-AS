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

package com.redshape.io;


import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 11/3/10
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkInteractionException extends IOException {
	private static final long serialVersionUID = 9148204335399558919L;

	public NetworkInteractionException() {
        this(null);
    }

    public NetworkInteractionException( String message ) {
        this( message, null );
    }

	public NetworkInteractionException( String message, Throwable e ) {
		super(message, e);
	}

}
