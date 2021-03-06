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

package com.redshape.net.ssh.connection.capabilities;

import com.redshape.net.connection.ConnectionException;
import com.redshape.net.connection.capabilities.IConsoleCapabilitySupport;
import com.redshape.net.ssh.connection.SshConnectionSupport;
import com.redshape.utils.system.processes.ISystemProcess;
import com.redshape.utils.system.scripts.IScriptExecutor;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Signal;
import net.schmizz.sshj.transport.TransportException;

import java.io.*;

/**
 * @author nikelin
 * @date 16:35
 */
public class ConsoleCapabilitySupport implements IConsoleCapabilitySupport {

    public class ProcessAdapter implements ISystemProcess {
        private Session.Command command;

        public ProcessAdapter(Session.Command command) {
            this.command = command;
        }

        @Override
        public String readStdError() throws IOException {
            return command.getExitErrorMessage();
        }

        @Override
        public String readStdInput() throws IOException {
            BufferedReader reader = new BufferedReader( new InputStreamReader(command.getInputStream() ) );
            String tmp;
            StringBuilder builder = new StringBuilder();
            while ( null != ( tmp = reader.readLine() ) ) {
                builder.append(tmp);
            }

            return builder.toString();
        }

        @Override
        public InputStream getInputStream() {
            return command.getInputStream();
        }

        @Override
        public InputStream getErrorStream() {
            return command.getErrorStream();
        }

        @Override
        public OutputStream getOutputStream() {
            return command.getOutputStream();
        }

        @Override
        public boolean isSuccessful() throws IOException {
            return command.getExitStatus() == 0;
        }

        @Override
        public int waitFor() throws InterruptedException {
            return 0;
        }

        @Override
        public int exitValue() {
            return command.getExitStatus();
        }

        @Override
        public void destroy() {
            try {
                command.signal(Signal.TERM);
            } catch ( TransportException e ) {
                throw new RuntimeException( e.getMessage(), e );
            }
        }

        @Override
        public int getPID() {
            return command.getID();
        }
    }

    private SshConnectionSupport connection;

    public ConsoleCapabilitySupport( SshConnectionSupport connection ) {
        this.connection = connection;
    }

    protected SshConnectionSupport getConnection() {
        return connection;
    }

    @Override
    public void execute(IScriptExecutor executor) throws ConnectionException {
        try {
            Session session = this.getConnection()
                                  .asRawConnection()
                                    .startSession();

            executor.execute(
                new ProcessAdapter(
                    session.exec( executor.getExecCommand() )
                )
            );
        } catch ( IOException e ) {
            throw new ConnectionException( e.getMessage(), e );
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
