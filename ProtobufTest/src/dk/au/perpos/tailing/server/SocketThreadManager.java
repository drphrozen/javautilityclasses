package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dk.au.perpos.tailing.TailingAgent.AgentMessage;
import dk.au.perpos.tailing.TailingAgent.Login;
import dk.au.perpos.tailing.server.SocketThread.ShutdownCallback;

public class SocketThreadManager implements ShutdownCallback {
	
	
	private final Set<AgentCallbackSocketThread> set = Collections.synchronizedSet(new HashSet<AgentCallbackSocketThread>(10));
	
	public SocketThread newSocketThread(Socket clientSocket) throws IOException {
		Login login = Login.parseDelimitedFrom(clientSocket.getInputStream());
		Login.Type type = login.getClientType();
		String name = login.getName();
		System.out.println("Processing a " + type + " named " + name + "!");
		switch (type) {
		case Agent:
			return new AgentSocketThread(clientSocket, name, null);
		case AgentCallback:
			AgentCallbackSocketThread socketThread = new AgentCallbackSocketThread(clientSocket, name, this);
			set.add(socketThread);
			return socketThread;
		case Manager:
			return new ManagerSocketThread(clientSocket, name, null);
		default:
			throw new IOException("ClientType not supportet: " + type + "!");
		}
	}

	@Override
	public void shutdownCallback(SocketThread sender) {
		if(sender != null)
			set.remove(sender);
	}
	
	public void Emit(AgentMessage message) {
		synchronized(set) {
      Iterator<AgentCallbackSocketThread> i = set.iterator(); // Must be in the synchronized block
      while (i.hasNext())
        i.next().Emit(message);
		}
	}
}
