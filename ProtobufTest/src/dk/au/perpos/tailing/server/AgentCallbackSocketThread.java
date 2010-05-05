package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import dk.au.perpos.tailing.TailingAgent.AgentMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.Target;
import dk.au.perpos.tailing.TailingAgent.Agent;

public class AgentCallbackSocketThread extends SocketThread {

	private ArrayBlockingQueue<AgentMessage> queue = new ArrayBlockingQueue<AgentMessage>(20, true);
	private boolean isRunning;
	
	/**
	 * A thread that handles callbacks to the agent. 
	 * @param clientSocket The socket to send callbacks on.
	 * @param name The name of the this socket thread.
	 */
	public AgentCallbackSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name);
	}

	@Override
	public void run() {
		isRunning = true;
		while(isRunning) {
			try {
				AgentMessage message = queue.take();
				message.writeDelimitedTo(clientSocket.getOutputStream());
			} catch (IOException e) {
				isRunning = false;
				e.printStackTrace();
			} catch (InterruptedException e) {
				isRunning = false;
			}
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Emits an event to the Agent.
	 * @param target A Target to emit.
	 * @param agents Agent's to emit.
	 * @return True on success otherwise false.
	 */
	public boolean Emit(Target target, Iterable<? extends Agent> agents) {
		return queue.offer(AgentMessage.newBuilder().setTarget(target).addAllAgents(agents).build());
	}
	
	/**
	 * Emits an event to the Agent.
	 * @param target A Target to emit.
	 * @return True on success otherwise false.
	 */
	public boolean Emit(Target target) {
		return queue.offer(AgentMessage.newBuilder().setTarget(target).build());
	}
	
	/**
	 * Emits an event to the Agent.
	 * @param agents Agent's to emit.
	 * @return True on success otherwise false.
	 */
	public boolean Emit(Iterable<? extends Agent> agents) {
		return queue.offer(AgentMessage.newBuilder().addAllAgents(agents).build());
	}

	/**
	 * Emits an event to the Agent.
	 * @param message A message containing Target and/or Agent's.
	 * @return True on success otherwise false.
	 */
	public boolean Emit(AgentMessage message) {
		return queue.offer(message);
	}
	
	/**
	 * Creates a Target.
	 * @return A new Target.
	 */
	public Target createTarget(Person person) {
		return Target.newBuilder()
			.setPerson(person)
			.build();
	}
	
	/**
	 * Creates a Person. 
	 * @param direction The direction in which the target is moving.
	 * @param speed The speed at which the target is moving.
	 * @param altitude The altitude portion of the targets position. 
	 * @param longitude The longitude portion of the targets position.
	 * @param latitude The latitude portion of the targets position.
	 * @return A new Person.
	 */
	public Person newPerson(double direction, double speed, double altitude, double longitude, double latitude) {
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
	public Agent createAgent(String name, Person person) {
		return Agent.newBuilder()
			.setName(name)
			.setPerson(person)
			.build();
	}
}