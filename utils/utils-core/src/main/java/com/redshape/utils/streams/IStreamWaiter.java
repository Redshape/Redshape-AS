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

package com.redshape.utils.streams;

import com.redshape.utils.TimeSpan;

import java.io.IOException;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.utils.streams
 * @date 1/31/12 {5:09 PM}
 */
public interface IStreamWaiter {

    public void setPriority( Priority priority );
    
    public void await() throws IOException;

    public void awaitUntil( TimeSpan span ) throws InterruptedException, IOException;

    public void addEventHandler( IStreamEventHandler handler );

    public void removeEventHandler( IStreamEventHandler handler );

}
