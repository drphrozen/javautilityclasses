package dk.au.perpos.tailing.server;

import java.net.Socket;

public abstract class SocketThread implements Runnable {

	protected final Socket clientSocket;
	protected final String name;
	private ShutdownCallback shutdownCallback;
	
	/**
	 * A thread that wraps a socket and a name for that socket. 
	 * @param clientSocket The socket to send callbacks on.
	 * @param name The name of the this socket thread.
	 * @param shutdownCallback A callback that will be called when this thread exits the process function. Null may be passed for no callback.
	 */
	public SocketThread(Socket clientSocket, String name, ShutdownCallback shutdownCallback) {
		this.clientSocket = clientSocket;
		this.name = name;
		this.shutdownCallback = shutdownCallback;
	}
	
	/**
	 * Name getter.
	 * @return The name of this socket thread.
	 */
	public String getName() {
		return name;
	}
	
	public interface ShutdownCallback {
		void shutdownCallback(SocketThread sender);
	}
	
	@Override
	public void run() {
		process();
		if(shutdownCallback != null)
			shutdownCallback.shutdownCallback(this);
	}
	
	public abstract void process();
}