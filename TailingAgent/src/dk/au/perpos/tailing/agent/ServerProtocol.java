package dk.au.perpos.tailing.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import android.location.Location;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.Login;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;
import dk.au.perpos.tailing.TailingAgent.Login.Type;

public class ServerProtocol implements Runnable {

	private final Socket	socket;
	private final Agent		agentProto;
	
	public ServerProtocol(String hostName, int port, String agentName) throws UnknownHostException, IOException {
		agentProto = Agent.newBuilder().setName(agentName).buildPartial();
		
		socket = new Socket(hostName, port);
		Login.newBuilder()
			.setName(agentName)
			.setClientType(Type.Agent)
		.build().writeDelimitedTo(socket.getOutputStream());
	}
	
	public void run() {
		while(true) {
			try {
				queue.take().writeDelimitedTo(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (InterruptedException e) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}
	
	private final ArrayBlockingQueue<ServerMessage> queue = new ArrayBlockingQueue<ServerMessage>(20); 
	
	public void sendTargetSeen(float distance, float direction) {
		queue.offer(ServerMessage.newBuilder()
			.setTargetSeen(TargetSeen.newBuilder()
				.setDistance(distance)
				.setDirection(direction)
			.build())
		.build());
	}
	
	public void sendLocation(Location location) {
		queue.offer(ServerMessage.newBuilder()
			.setAgent(Agent.newBuilder(agentProto)
				.setPerson(Person.newBuilder()
					.setDirection(location.getBearing())
					.setSpeed(location.getSpeed())
					.setPosition(Position.newBuilder()
						.setAltitude(location.getAltitude())
						.setLatitude(location.getLatitude())
						.setLongitude(location.getLongitude())
					.build())
				.build())
			.build())
		.build());
	}
}
