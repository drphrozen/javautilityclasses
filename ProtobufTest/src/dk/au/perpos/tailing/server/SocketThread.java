package dk.au.perpos.tailing.server;

import java.net.Socket;

public abstract class SocketThread implements Runnable {

	protected final Socket clientSocket;
	protected final String name;
	
	/**
	 * A thread that wraps a socket and a name for that socket. 
	 * @param clientSocket The socket to send callbacks on.
	 * @param name The name of the this socket thread.
	 */
	public SocketThread(Socket clientSocket, String name) {
		this.clientSocket = clientSocket;
		this.name = name;
	}
	
	/**
	 * Name getter.
	 * @return The name of this socket thread.
	 */
	public String getName() {
		return name;
	}
}
