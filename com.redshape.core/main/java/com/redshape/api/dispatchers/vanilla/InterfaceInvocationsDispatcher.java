package com.redshape.api.dispatchers.vanilla;

import com.redshape.api.ApiException;
import com.redshape.features.*;
import com.redshape.exceptions.ErrorCode;
import com.redshape.io.protocols.vanilla.request.IApiRequest;
import com.redshape.io.protocols.core.request.RequestType;
import com.redshape.io.protocols.vanilla.response.IApiResponse;
import com.redshape.persistence.entities.requesters.IRequester;
import com.redshape.server.ServerException;

import org.apache.log4j.*;

public class InterfaceInvocationsDispatcher implements IVanillaDispatcher<IRequester, IApiRequest, IApiResponse> {
	private static final Logger log = Logger.getLogger( InterfaceInvocationsDispatcher.class );

    public RequestType getDispatchingType() {
        return RequestType.INTERFACE_INVOKE;
    }

	public void dispatch( IRequester requester, IApiRequest invoke, IApiResponse response ) throws ServerException {
        try {
            log.info("Calling action " + invoke.getAspectName() + " of " + invoke.getFeatureName() + " interface...");
            IFeatureAspect featureAspect = FeaturesRegistry.getDefault().getFeatureAspect( invoke.getFeatureName(), invoke.getAspectName() );
            if ( featureAspect == null ) {
                throw new ApiException( ErrorCode.EXCEPTION_REQUEST_METHOD_NOT_EXISTS );
            }

            if ( !requester.canInteract( featureAspect ) ) {
                throw new ApiException( ErrorCode.EXCEPTION_ACCESS_DENIED );
            }

            for ( String parameter : invoke.getParams().keySet() ) {
                featureAspect.setAttribute( parameter, invoke.getParam(parameter) );
            }

            if ( !featureAspect.isValid() ) {
                throw new ApiException( ErrorCode.EXCEPTION_ACCESS_DENIED );
            }

            try {
                IInteractionResult result = featureAspect.interact( requester );
                if ( result != null ) {
                    for ( String name : invoke.hasParam("waiting") ? invoke.getMap("waiting").keySet() : result.getAttributes().keySet() ) {
                        response.addParam( name, result.getAttribute(name) );
                    }
                }
            } catch ( InteractionException e ) {
                response.addError( e );
            }
        } catch ( ApiException e ) {
            log.error( e.getMessage(), e );
            response.addError( e );
        }
	}

}
