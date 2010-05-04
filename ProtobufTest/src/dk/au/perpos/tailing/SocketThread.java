package dk.au.perpos.tailing;

import java.net.Socket;

public abstract class SocketThread implements Runnable {

	protected final Socket clientSocket;
	protected final String name;
	
	public SocketThread(Socket clientSocket, String name) {
		this.clientSocket = clientSocket;
		this.name = name;
	}
}
