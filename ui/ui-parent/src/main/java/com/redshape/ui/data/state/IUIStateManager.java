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

package com.redshape.ui.data.state;

import com.redshape.ui.application.events.IEventDispatcher;

import java.util.Date;
import java.util.Map;

/**
 * UI states managing entity.
 *
 * Responsible for backup and restoring UI data and structure.
 *
 * @author root
 * @date 07/04/11
 * @package com.redshape.ui.state
 */
public interface IUIStateManager extends IEventDispatcher {

	/**
	 * Revert to N-1 version
	 * @throws StateException
	 */
	public void restore() throws StateException;

    /**
         * Backup state to persistent using applied objects flusher and increase current revision by one.
         */
    public void backup() throws StateException;

    /**
         * Revert current state to given version. If version exists loads it through specified unmarshalling manager
         * otherwise throught StateException.
         * @param rev
         */
    public void restore( Integer rev ) throws StateException;

    /**
         * Return information about current state version ( 0 - if never saved before )
         * @return
        */
    public Integer getRevision();

    /**
         * Get all revisions
         *
         * @return
         */
    public Map<Date, Integer> getRevisions();

    /**
         * Method to enable period state backup
         * @param value
         */
    public void setPeriodicEnabled( boolean value );


	/**
	 *Method to change versioning mode state ( on/off )
	 * @param value
	 */
	public void setDoVersioning( boolean value );

	/**
 	 * Indicates versioning mode state
	 * @return
	 */
	public boolean doVersioning();

    /**
         * Indicates periodic backups processing state
         * @return
         */
    public boolean isPeriodicEnabled();

    /**
         * Specify interval of periodic backups if their enabled
         * @param value
         */
    public void setPeriodicInterval( int value );

    /**
         * @return
         */
    public int getPeriodicInterval();

    /**
         * Change flushing location
         * @param location
         */
    public void setLocation( String location );

    public boolean isFeatureEnabled( UIStateFeature feature );

    public void enableFeature( UIStateFeature feature );

    public void disableFeature( UIStateFeature feature );

}
