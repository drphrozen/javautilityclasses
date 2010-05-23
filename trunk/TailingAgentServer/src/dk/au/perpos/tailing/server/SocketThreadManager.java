package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import dk.au.perpos.tailing.TailingAgent.Login;

public class SocketThreadManager {

	private final Logger log = Logger.getLogger(SocketThreadManager.class.getName());

	public SocketThread newSocketThread(Socket clientSocket) throws IOException, NullPointerException {
		log.info("Login attempt from: " + clientSocket.getInetAddress());
		Login login = Login.parseDelimitedFrom(clientSocket.getInputStream());
		Login.Type type = login.getClientType();
		String name = login.getName();
		log.info("Processing a " + type + " named " + name + "!");
		switch (type) {
		case Agent:
			return new AgentSocketThread(clientSocket, name);
		case Manager:
			return new ManagerSocketThread(clientSocket, name);
		default:
			throw new IOException("ClientType not supportet: " + type + "!");
		}
	}

}
