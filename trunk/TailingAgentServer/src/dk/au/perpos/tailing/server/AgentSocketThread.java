package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;

public class AgentSocketThread extends SocketThread {

	private boolean isRunning; 
	private final MessagePublisher publisher = MessagePublisher.instance;
	private final Logger log = Logger.getLogger(AgentSocketThread.this.getName());
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
				log.info(message.toString());
				if(message.hasAgent()) {
					agent = message.getAgent();
					publisher.Publish(ManagerMessage.newBuilder()
						.addAgent(AgentInfo.newBuilder()
							.setAgent(agent)
						.build())
						.setTimestamp(System.currentTimeMillis())
					.build());
				}
				if(message.hasTargetSeen() && agent != null) {
					publisher.Publish(ManagerMessage.newBuilder()
						.addAgent(AgentInfo.newBuilder()
							.setAgent(agent)
							.setTargetSeen(message.getTargetSeen())
						.build())
						.setTimestamp(System.currentTimeMillis())
					.build());
				}
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}
}
