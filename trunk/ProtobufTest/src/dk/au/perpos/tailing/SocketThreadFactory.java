package dk.au.perpos.tailing;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.Login;

public class SocketThreadFactory {
	public static SocketThread newSocketThread(Socket clientSocket) throws IOException {
		Login login = Login.parseDelimitedFrom(clientSocket.getInputStream());
		Login.Type type = login.getClientType();
		switch (type) {
		case Agent:
			return new AgentSocketThread(clientSocket, login.getName());
		case Manager:
			return new ManagerSocketThread(clientSocket, login.getName());
		default:
			throw new IOException("ClientType not supportet: " + type + "!");
		}
	}
}
