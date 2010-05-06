package dk.au.perpos.tailing.server;

import java.net.Socket;

public class AgentSocketThread extends SocketThread {

	public AgentSocketThread(Socket clientSocket, String name, ShutdownCallback shutdownCallback) {
		super(clientSocket, name, shutdownCallback);
	}

	@Override
	public void process() {
		
	}
}
