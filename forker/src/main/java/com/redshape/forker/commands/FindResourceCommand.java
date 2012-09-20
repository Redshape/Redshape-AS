package com.redshape.forker.commands;

import com.redshape.forker.AbstractForkCommand;
import com.redshape.forker.AbstractForkCommandResponse;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.forker.commands
 * @date 1/31/12 {4:33 PM}
 */
public final class FindResourceCommand {
    
    public static class Request extends AbstractForkCommand {
        public static final long ID = Request.class.getCanonicalName().hashCode();

        private String path;
        
        public Request( String path ) {
            super( Request.ID, Response.ID );
        }

        public String getPath() {
            return this.path;
        }
        
        @Override
        public void readFrom(DataInputStream input) throws IOException {
            this.path = input.readUTF();
        }

        @Override
        public void writeTo(DataOutputStream output) throws IOException {
            output.writeUTF( this.path );
        }
    }

    public static class Response extends AbstractForkCommandResponse {
        public static final long ID = Response.class.getCanonicalName().hashCode();

        private byte[] data;

        public Response(Status status) {
            super(ID, status);
        }

        public byte[] getData() {
            return this.data;
        }

        @Override
        public void readFrom(DataInputStream stream) throws IOException {
            ByteArrayOutputStream result = new ByteArrayOutputStream();

            int count;
            byte[] data = new byte[0xffff];
            while ( ( count = stream.readUnsignedShort() ) > 0 ) {
                stream.readFully(data, 0, count);
                result.write( data, 0, count );
            }

            this.data = data;
        }

        @Override
        public void writeTo(DataOutputStream stream) throws IOException {
            stream.writeInt(this.data.length);
            stream.write( this.data );
        }
    }
}
