package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.server.MessagePublisher.MessageSubscriber;

public class ManagerSocketThread extends SocketThread implements MessageSubscriber<ManagerMessage> {

//	private final Logger log = Logger.getLogger(ManagerSocketThread.class.getName());
	private final ArrayBlockingQueue<ManagerMessage> queue = new ArrayBlockingQueue<ManagerMessage>(10);
	
	public ManagerSocketThread(Socket clientSocket, String name) {
		super(clientSocket, name, null);
		MessagePublisher.instance.addMessageSubscriber(this, ManagerMessage.class);
	}

	@Override
	public void process() {
		while(true) {
			ManagerMessage message;
			try {
//				log.info("Waiting for message");
				message = queue.take();
//				log.info("Got Message");
				message.writeDelimitedTo(clientSocket.getOutputStream());
			} catch (InterruptedException e) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		MessagePublisher.instance.removeMessageSubscriber(this, ManagerMessage.class);
	}
	
	@Override
	public void OnSignal(ManagerMessage value) {
//		log.info("OnSignal");
		queue.offer(value);
	}
}
