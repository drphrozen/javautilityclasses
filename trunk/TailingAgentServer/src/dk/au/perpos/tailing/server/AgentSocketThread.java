package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;

public class AgentSocketThread extends SocketThread {

	private boolean isRunning; 
	private final MessagePublisher publisher = MessagePublisher.instance;
	private Agent agent = null;
	
	public AgentSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name, null);
	}

	@Override
	public void process() {
		isRunning = true;
		while(isRunning) {
			try {
				ServerMessage message = ServerMessage.parseDelimitedFrom(clientSocket.getInputStream());
				if(message == null)
					break;
				if(message.hasTargetSeen() && agent != null) {
					publisher.Publish(ManagerMessage.newBuilder()
						.addAgent(AgentInfo.newBuilder()
							.setAgent(agent)
						.setTargetSeen(message.getTargetSeen())
						.build())
					.build());
				}
				if(message.hasAgent()) {
					agent = message.getAgent();
					publisher.Publish(agent);
				}
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}
}
