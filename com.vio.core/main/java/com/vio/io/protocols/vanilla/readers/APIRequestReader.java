package com.vio.io.protocols.vanilla.readers;

import com.vio.io.protocols.vanilla.request.InterfaceInvoke;
import com.vio.io.protocols.vanilla.hyndrators.IApiRequestHydrator;
import com.vio.io.protocols.core.readers.IRequestReader;
import com.vio.io.protocols.core.readers.ReaderException;
import com.vio.io.protocols.core.sources.input.BufferedInput;
import com.vio.io.protocols.vanilla.request.IAPIRequest;
import org.apache.log4j.Logger;

/**
 * WebCam Project
 *
 * @author nikelin
 * @project vio
 * @package com.vio.api.io.readers
 * @date Apr 1, 2010
 */
public class APIRequestReader implements IRequestReader<BufferedInput, IAPIRequest> {
    private static final Logger log = Logger.getLogger( APIRequestReader.class );
    private IApiRequestHydrator hydrator;

    public APIRequestReader( IApiRequestHydrator hydrator ) {
        this.hydrator = hydrator;
    }

    public IApiRequestHydrator getHydrator() {
        return this.hydrator;
    }

    @Override
    public IAPIRequest readRequest( BufferedInput source ) throws ReaderException {
        try {
            String data = source.readLine();
            if ( data != null && !data.isEmpty() ) {
                return InterfaceInvoke.buildRequest( data, this.getHydrator() );
            }

            return null;
        } catch (Throwable e ) {
            log.info( e.getMessage(), e );
            throw new ReaderException();
        }
    }
}