package dk.au.perpos.tailing.server;

import java.net.Socket;

public class ManagerSocketThread extends SocketThread {

	public ManagerSocketThread(Socket clientSocket, String name, ShutdownCallback shutdownCallback) {
		super(clientSocket, name, shutdownCallback);
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
}
