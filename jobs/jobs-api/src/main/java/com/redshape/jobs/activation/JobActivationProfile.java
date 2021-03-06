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

package com.redshape.jobs.activation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nikelin
 * @date 22/04/11
 * @package com.redshape.jobs.activation
 */
public class JobActivationProfile implements IJobActivationProfile {
	private Map<ActivationAttribute, Object> attributes = new HashMap<ActivationAttribute, Object>();
	private JobActivationType type;

	public JobActivationProfile() {
		this( JobActivationType.SINGLE );
	}

	public JobActivationProfile( JobActivationType type ) {
		this.type = type;
	}

	@Override
	public <V> V getAttribute( ActivationAttribute attribute ) {
		return (V) this.attributes.get(attribute);
	}

	@Override
	public void setAttribute( ActivationAttribute attribute, Object value ) {
		this.attributes.put( attribute, value );
	}

	@Override
	public Map<ActivationAttribute, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public JobActivationType getActivationType() {
		return this.type;
	}

    @Override
    public String toString() {
        return this.type.name();
    }
}