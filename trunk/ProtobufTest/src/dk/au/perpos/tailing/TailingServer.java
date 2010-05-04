package dk.au.perpos.tailing;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TailingServer implements Runnable {

	private final ExecutorService	pool	= Executors.newFixedThreadPool(20);
	private final ServerSocket		serverSocket;
	private volatile boolean			isRunning;

	public TailingServer() throws IOException {
		serverSocket = new ServerSocket(15339);
	}

	@Override
	public void run() {
		System.out.println("The server is running..");
		isRunning = true;
		while (isRunning) {
			try {
				pool.execute(SocketThreadFactory.newSocketThread(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("The server was shutdown!");
	}

	public void stop() {
		System.out.println("The server is shutting down..");
		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
