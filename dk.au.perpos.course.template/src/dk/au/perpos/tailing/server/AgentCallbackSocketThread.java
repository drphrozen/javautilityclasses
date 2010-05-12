package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import dk.au.perpos.tailing.TailingAgent.AgentMessage;

public class AgentCallbackSocketThread extends SocketThread {

	private ArrayBlockingQueue<AgentMessage> queue = new ArrayBlockingQueue<AgentMessage>(20, true);
	private boolean isRunning;
	
	/**
	 * A thread that handles callbacks to the agent. 
	 * @param clientSocket The socket to send callbacks on.
	 * @param name The name of the this socket thread.
	 * @param shutdownCallback 
	 */
	public AgentCallbackSocketThread(Socket clientSocket, String name, ShutdownCallback shutdownCallback) {
		super(clientSocket, name, shutdownCallback);
	}

	/**
	 * Emits an event to the Agent.
	 * @param message A message containing Target and/or Agent's.
	 * @return True on success otherwise false.
	 */
	public boolean Emit(AgentMessage message) {
		return queue.offer(message);
	}
	
	@Override
	public void process() {
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
}