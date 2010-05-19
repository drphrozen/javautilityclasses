package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;

import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.server.MessagePublisher.MessageSubscriber;

public class ManagerSocketThread extends SocketThread implements MessageSubscriber<ManagerMessage> {

	private final Object sync = new Object();
	private volatile ManagerMessage message;
	
	public ManagerSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name, null);
	}

	@Override
	public void process() {
		while(true) {
			synchronized (sync) {
				try {
					sync.wait();
					message.writeDelimitedTo(clientSocket.getOutputStream());
				} catch (InterruptedException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	@Override
	public void OnSignal(ManagerMessage value) {
		synchronized (sync) {
			message = value;
			sync.notifyAll();
		}
	}
}
