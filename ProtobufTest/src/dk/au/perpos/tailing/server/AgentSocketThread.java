package dk.au.perpos.tailing.server;

import java.net.Socket;

public class AgentSocketThread extends SocketThread {

	public AgentSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name);
	}

	@Override
	public void run() {
		
	}
}
