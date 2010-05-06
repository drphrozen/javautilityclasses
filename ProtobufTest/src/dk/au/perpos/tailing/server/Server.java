package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

	private final ExecutorService	pool	= Executors.newFixedThreadPool(20);
	private final ServerSocket		serverSocket;
	private volatile boolean			isRunning;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(15339);
	}

	@Override
	public void run() {
		
		MessagePublisher.instance.start();
		
		SocketThreadManager socketThreadManager = new SocketThreadManager();
		System.out.println("The server is running..");
		
		isRunning = true;
		while (isRunning) {
			try {
				pool.execute(socketThreadManager.newSocketThread(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MessagePublisher.instance.stop();
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
	
	public static void main(String[] args) {
		try {
			new Server().run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		MessagePublisher dataStore = new MessagePublisher();
//		Thread dataStoreThread = new Thread(dataStore);
//		dataStoreThread.start();
//		dataStore.addMessageSubscriber(new MessageSubscriber<Msg>() {
//			@Override
//			public void OnSignal(Msg value) {
//				System.out.println("YEah: " + value);
//			}
//		}, Msg.class);
//		dataStore.addMessageSubscriber(new MessageSubscriber<Msg>() {
//			@Override
//			public void OnSignal(Msg value) {
//				System.out.println("Ohhh: " + value);
//			}
//		}, Msg.class);
//		dataStore.addMessageSubscriber(new MessageSubscriber<Reply>() {
//			@Override
//			public void OnSignal(Reply value) {
//				System.out.println("Wee: " + value);
//			}
//		}, Reply.class);
//		dataStore.Publish(Msg.newBuilder().setId(2).build());
//		dataStore.Publish(Reply.newBuilder().setAnswer(Answer.Ok).setText("Buuuh").build());
//		try {
//			Thread.sleep(1000);
//			dataStoreThread.interrupt();
//			dataStoreThread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
