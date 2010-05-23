package dk.au.perpos.tailing.agent;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.Login;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.ServerMessage;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;
import dk.au.perpos.tailing.TailingAgent.Login.Type;

public class TargetSeenSender implements Runnable {

	private Socket socket = null;
	private final String imei;
	private final Context context;
	private final Agent agentProto = null;
	
	public TargetSeenSender(Context context) {
		this.context = context;
		imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
	
	public void run() {
		while(true) {
			try {
				if(socket == null) break;
				ServerMessage.newBuilder()
				.setTargetSeen(TargetSeen.newBuilder()
					.setDirection(1f)
					.setDistance(2f))
				.build().writeDelimitedTo(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void login(String hostName, int port) {
		try {
			socket = new Socket(hostName, port);
			Login login = Login.newBuilder()
				.setName(imei + "GPS")
				.setClientType(Type.Agent)
			.build();
			login.writeDelimitedTo(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			socket = null;
			e.printStackTrace();
		}
	}
	
	private ArrayBlockingQueue<ServerMessage> queue = new ArrayBlockingQueue<ServerMessage>(20); 
	
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
			.setAgent(Agent.newBuilder()
			.build())
		.build());
		
	}
}
