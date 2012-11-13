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

package com.redshape.plugins.meta;

import com.redshape.plugins.packagers.IPackageStarter;
import com.redshape.plugins.starters.EngineType;

/**
 * Created by IntelliJ IDEA.
 * User: Nikelin
 * Date: 09.01.12
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
class PackageStarter implements IPackageStarter {
    private EngineType engineType;
    private String version;

    PackageStarter(EngineType engineType, String version) {
        this.engineType = engineType;
        this.version = version;
    }

    @Override
    public EngineType getEngineType() {
        return this.engineType;
    }

    @Override
    public String getVersion() {
        return this.version;
    }
}
