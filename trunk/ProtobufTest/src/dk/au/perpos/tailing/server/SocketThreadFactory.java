package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.Login;

public class SocketThreadFactory {
	public static SocketThread newSocketThread(Socket clientSocket) throws IOException {
		Login login = Login.parseDelimitedFrom(clientSocket.getInputStream());
		Login.Type type = login.getClientType();
		String name = login.getName();
		System.out.println("Processing a " + type + " named " + name + "!");
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
