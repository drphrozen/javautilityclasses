package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;

public class AgentSocketThread extends SocketThread {

	private boolean isRunning; 
	private final MessagePublisher publisher = MessagePublisher.instance;
	private final Agent agentProto;
	
	public AgentSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name, null);
		agentProto = Agent.newBuilder().setName(name).buildPartial();
	}

	@Override
	public void process() {
		isRunning = true;
		while(isRunning) {
			try {
				ServerMessage message = ServerMessage.parseDelimitedFrom(clientSocket.getInputStream());
				if(message == null)
					break;
				if(message.hasTargetSeen()) {
					publisher.Publish(ManagerMessage.newBuilder()
						.addAgent(AgentInfo.newBuilder()
							.setAgent(Agent.newBuilder(agentProto)
								.setPerson(Person.newBuilder()
									.setDirection(0)
									.setSpeed(0)
									.setPosition(Position.newBuilder()
										.setAltitude(0)
										.setLatitude(0)
										.setLongitude(0)
									.build())
								.build())
							.build())
						.setTargetSeen(message.getTargetSeen())
						.build())
					.build());
				}
				if(message.hasAgent()) {
					// TODO!!!
					publisher.Publish(message.getAgent());
				}
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
	}
}
