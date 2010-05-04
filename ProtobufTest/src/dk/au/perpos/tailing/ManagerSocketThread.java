package dk.au.perpos.tailing;

import java.net.Socket;

public class ManagerSocketThread extends SocketThread {

	public ManagerSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name);
	}

	@Override
	public void run() {

	}
}
