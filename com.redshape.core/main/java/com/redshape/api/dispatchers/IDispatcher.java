package com.redshape.api.dispatchers;

import com.redshape.features.IFeatureInteractor;
import com.redshape.io.protocols.core.request.IRequest;
import com.redshape.io.protocols.core.response.IResponse;
import com.redshape.server.ServerException;

/**
 * IEntity responsible on dispatching incoming requests
 * to one of the available features (@see com.redshape.features )
 *
 * Process given request and write results in a view of items
 * for Map<V,Q>.
 *
 * @author nikelin
 * @group api
 */
public interface IDispatcher<T extends IFeatureInteractor, V extends IRequest, Q extends IResponse> {
    
    public void dispatch( T requester, V request, Q response ) throws ServerException;

}