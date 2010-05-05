package dk.au.perpos.tailing.server;

import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.AgentMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.Target;

public class AgentSocketThread extends SocketThread {

	public AgentSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name);
	}

	@Override
	public void run() {
		AgentMessage.Builder agentBuilder = AgentMessage.newBuilder();
		double direction = 0;
		double altitude = 0;
		double longitude = 0;
		double latitude = 0;
		double speed = 0;
		agentBuilder.setTarget(
				Target.newBuilder()
					.setPer(Person.newBuilder()
						.setDirection(direction)
						.setPos(Position.newBuilder()
							.setAltitude(altitude)
							.setLongitude(longitude)
							.setLatitude(latitude)
							.build())
						.setSpeed(speed)
						.build())
					.build());
	}
}
