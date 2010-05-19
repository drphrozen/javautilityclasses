package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;

public class AgentSocketThread extends SocketThread {

	private boolean isRunning; 
	
	public AgentSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name, null);
	}

	@Override
	public void process() {
		isRunning = true;
		while(isRunning) {
			try {
				ServerMessage message = ServerMessage.parseDelimitedFrom(clientSocket.getInputStream());
				if(message.hasTargetSeen()) {
					TargetSeen targetSeen = message.getTargetSeen();
					MessagePublisher.instance.Publish(targetSeen);
				}
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}
}
