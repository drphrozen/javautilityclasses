package dk.au.perpos.tailing.agent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import android.location.Location;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.Login;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;
import dk.au.perpos.tailing.TailingAgent.Login.Type;

public class ServerProtocol implements Runnable {

	private final Agent agentProto;
	private Socket socket;
	private OutputStream output;
	
	public ServerProtocol(String hostName, int port, String agentName) throws FileNotFoundException {
		agentProto = Agent.newBuilder().setName(agentName).buildPartial();
		
		try {
			socket = new Socket(hostName, port);
			output = socket.getOutputStream();
			Login.newBuilder()
				.setName(agentName)
				.setClientType(Type.Agent)
			.build().writeDelimitedTo(output);
		} catch (UnknownHostException e) {
			socket = null;
			output = new FileOutputStream("/sdcard/Manager" + System.currentTimeMillis());
			e.printStackTrace();
		} catch (IOException e) {
			socket = null;
			output = new FileOutputStream("/sdcard/Manager" + System.currentTimeMillis());
			e.printStackTrace();
		}		
	}
	
	public void run() {
		while(true) {
			try {
				if(socket == null) {
					ServerMessage message = queue.take();
					if(message.hasAgent())
						ManagerMessage.newBuilder()
							.addAgent(AgentInfo.newBuilder()
								.setAgent(message.getAgent())
							.build())
							.setTimestamp(System.currentTimeMillis())
						.build().writeDelimitedTo(output);
				} else {
					queue.take().writeDelimitedTo(output);
				}
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
