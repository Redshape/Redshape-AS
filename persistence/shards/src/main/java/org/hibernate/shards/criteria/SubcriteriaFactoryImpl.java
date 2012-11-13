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

import org.hibernate.Criteria;
import org.hibernate.shards.session.ShardedSessionException;

/**
 * Concrete implementation of the {@link org.hibernate.shards.criteria.SubcriteriaFactory} interface.
 * Used to lazily create {@link org.hibernate.impl.CriteriaImpl.Subcriteria}
 * @see Criteria#createCriteria(String)
 * @see Criteria#createCriteria(String, int)
 * @see Criteria#createCriteria(String, String)
 * @see Criteria#createCriteria(String, String, int)
 *
 * @author maxr@google.com (Max Ross)
 */
public class SubcriteriaFactoryImpl implements SubcriteriaFactory {

  private enum MethodSig {
    ASSOCIATION,
    ASSOCIATION_AND_JOIN_TYPE,
    ASSOCIATION_AND_ALIAS,
    ASSOCIATION_AND_ALIAS_AND_JOIN_TYPE
  }

  // used to tell us which overload of createCriteria to invoke
  private final MethodSig methodSig;

  // the association we'll pass to createCriteria
  private final String association;

  // the join type we'll pass to createCriteria.  Can be null.
  private final int joinType;

  // the alias we'll pass to createCriteria.  Can be null.
  private final String alias;

  /**
   * Construct a SubcriteriaFactoryImpl
   *
   * @param methodSig used to tell us which overload of createCriteria to invoke
   * @param association the association we'll pass to createCriteria
   * @param joinType the join type we'll pass to createCriteria.  Can be null.
   * @param alias the alias we'll pass to createCriteria.  Can be null.
   */
  private SubcriteriaFactoryImpl(
      MethodSig methodSig,
      String association,
      /*@Nullable*/ int joinType,
      /*@Nullable*/ String alias) {
    this.methodSig = methodSig;
    this.association = association;
    this.joinType = joinType;
    this.alias = alias;
  }

  /**
   * Construct a SubcriteriaFactoryImpl
   *
   * @param association the association we'll pass to createCriteria
   */
  public SubcriteriaFactoryImpl(String association) {
    this(MethodSig.ASSOCIATION, association, 0, null);
  }

  /**
   * Construct a SubcriteriaFactoryImpl
   *
   * @param association the association we'll pass to createCriteria
   * @param joinType the join type we'll pass to createCriteria
   */
  public SubcriteriaFactoryImpl(String association, int joinType) {
    this(MethodSig.ASSOCIATION_AND_JOIN_TYPE, association, joinType, null);
  }

  /**
   * Construct a SubcriteriaFactoryImpl
   *
   * @param association the association we'll pass to createCriteria
   * @param alias the alias we'll pass to createCriteria
   */
  public SubcriteriaFactoryImpl(String association, String alias) {
    this(MethodSig.ASSOCIATION_AND_ALIAS, association, 0, alias);
  }

  /**
   * Construct a SubcriteriaFactoryImpl
   *
   * @param association the association we'll pass to createCriteria
   * @param alias the alias we'll pass to createCriteria
   * @param joinType the join type we'll pass to createCriteria
   */
  public SubcriteriaFactoryImpl(String association, String alias, int joinType) {
    this(MethodSig.ASSOCIATION_AND_ALIAS_AND_JOIN_TYPE, association, joinType, alias);
  }

  public Criteria createSubcriteria(Criteria parent, Iterable<CriteriaEvent> events) {
    // call the right overload to actually create the Criteria
    Criteria crit;
    switch (methodSig) {
      case ASSOCIATION:
        crit = parent.createCriteria(association);
        break;
      case ASSOCIATION_AND_JOIN_TYPE:
        crit = parent.createCriteria(association, joinType);
        break;
      case ASSOCIATION_AND_ALIAS:
        crit = parent.createCriteria(association, alias);
        break;
      case ASSOCIATION_AND_ALIAS_AND_JOIN_TYPE:
        crit = parent.createCriteria(association, alias, joinType);
        break;
      default:
        throw new ShardedSessionException(
            "Unknown constructor type for subcriteria creation: " + methodSig);
    }
    // apply the events
    for(CriteriaEvent event : events) {
      event.onEvent(crit);
    }
    return crit;
  }
}
