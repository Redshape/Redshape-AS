package com.redshape.daemon.services;

import org.apache.log4j.Logger;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.rmi.server.RMIServerSocketFactory;

public class ServerFactory implements RMIServerSocketFactory, Serializable {
	private static final long serialVersionUID = 1430493102430287284L;

	private static final Logger log = Logger.getLogger( ServerFactory.class );
	
	//TODO
	private String host;
	private Integer maxConnections;
	
	public ServerFactory( String host, Integer maxConnections ) {
		this.host = host;
		this.maxConnections = maxConnections;
	}
	
	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		log.info("Starting repository on host: " + this.host + ":" + port );
		InetAddress address = InetAddress.getByName( this.host );
		ServerSocket socket = ServerSocketFactory.getDefault()
		  .createServerSocket();
        socket.bind( new InetSocketAddress(address, port) );

        return socket;
	}
	
}
