package dk.au.perpos.tailing.client;

import dk.au.perpos.spatialsupport.position.WGS84Position;
import dk.au.perpos.spatialsupport.position.WGS84Position.AltitudeReference;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.server.MessagePublisher;

public class Phone implements Runnable {

	private final Agent agentProto;
	private final MessagePublisher publisher = MessagePublisher.instance;

	public Phone() {
		agentProto = Agent.newBuilder().setName("007").buildPartial();
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				WGS84Position position = new WGS84Position(0,0,7, AltitudeReference.ENCODES_ALTITUDE_OVER_WGS84ELLIPSOID);
				Agent agent = Agent.newBuilder(agentProto).setPerson(Person.newBuilder()
					.setDirection(0)
					.setSpeed(0)
					.setPosition(Position.newBuilder()
						.setAltitude(position.getAltitudeOverWGS84Ellipsoid())
						.setLatitude(position.getLatitude())
						.setLongitude(position.getLongitude())
						.build())
					.build())
				.build();
				AgentInfo agentInfo = AgentInfo.newBuilder()
					.setAgent(agent)
				.build();
				publisher.Publish(ManagerMessage.newBuilder().addAgent(agentInfo).build());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
