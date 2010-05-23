package dk.au.perpos.tailing.server;

import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;

public class MessageWrapper {
	
	/**
	 * Creates a Person. 
	 * @param direction The direction in which the target is moving.
	 * @param speed The speed at which the target is moving.
	 * @param altitude The altitude portion of the targets position. 
	 * @param longitude The longitude portion of the targets position.
	 * @param latitude The latitude portion of the targets position.
	 * @return A new Person.
	 */
	public static Person newPerson(double direction, double speed, double altitude, double longitude, double latitude) {
		return Person.newBuilder()
			.setDirection(direction)
			.setSpeed(speed)
			.setPosition(Position.newBuilder()
				.setAltitude(altitude)
				.setLongitude(longitude)
				.setLatitude(latitude)
				.build())
			.build();
	}
	
	/**
	 * Creates an Agent.
	 * @param name The name of the Agent.
	 * @param person The person.
	 * @return A new Agent.
	 */
	public static Agent createAgent(String name, Person person) {
		return Agent.newBuilder()
			.setName(name)
			.setPerson(person)
			.build();
	}
}
