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

package org.hibernate.shards.criteria;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;

/**
 * Event that allows the {@link CacheMode} of a {@link Criteria} to be set lazily.
 * @see Criteria#setCacheMode(CacheMode)
 *
 * @author maxr@google.com (Max Ross)
 */
class SetCacheModeEvent implements CriteriaEvent {

  // the CacheMode that we'll set on the Criteria when the event fires
  private final CacheMode cacheMode;

  /**
   * Construct a CacheModeEvent
   *
   * @param cacheMode the {@link CacheMode} we'll set on the {@link Criteria}
   * when the event fires.
   */
  public SetCacheModeEvent(CacheMode cacheMode) {
    this.cacheMode = cacheMode;
  }

  public void onEvent(Criteria crit) {
    crit.setCacheMode(cacheMode);
  }
}
