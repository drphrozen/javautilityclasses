package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.server.MessagePublisher.MessageSubscriber;

public class ManagerCallbackSocketThread extends SocketThread implements MessageSubscriber<ManagerMessage> {

	private volatile boolean isRunning;
	private final Object sync = new Object();
	private final Logger log = Logger.getLogger(ManagerCallbackSocketThread.class.getName()); 

	public ManagerCallbackSocketThread(Socket clientSocket, String name, ShutdownCallback shutdownCallback) {
		super(clientSocket, name, shutdownCallback);
		MessagePublisher.instance.addMessageSubscriber(this, ManagerMessage.class);
	}

	public ManagerCallbackSocketThread(Socket clientSocket, String name) {
		this(clientSocket, name, null);
	}

	@Override
	public void process() {
		isRunning = true;
		try {
			synchronized(sync) {
				while(isRunning) {
					sync.wait();
				}
			}
		} catch (InterruptedException e) {
		}
		isRunning = false;
		MessagePublisher.instance.removeMessageSubscriber(this, ManagerMessage.class);
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void OnSignal(ManagerMessage value) {
		log.info("OnSignal..\n" + value);
		try {
			value.writeDelimitedTo(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			isRunning = false;
			sync.notifyAll();
		}
	}

}
