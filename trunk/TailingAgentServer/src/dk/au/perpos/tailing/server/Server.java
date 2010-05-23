package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import dk.au.perpos.tailing.simulator.Phone;

//public class Server implements Runnable, EventHandler<ValueEventArgs<Sensor>> {
public class Server implements Runnable {

	private final ExecutorService	pool	= Executors.newFixedThreadPool(20);
	private final ServerSocket		serverSocket;
	private volatile boolean			isRunning;
//	private final SensingService sensingService;
	
	private final Logger log = Logger.getLogger(Server.class.getName());
	
//	public Server(SensingService sensingService) throws IOException {
	public Server() throws IOException {
		String strPort = System.getenv().get("TAILING_SERVER_PORT");
		int port = 15341;
		if(strPort != null) {
			try {
			port = Integer.parseInt(strPort);
			} catch (NumberFormatException e) {
			}
		}
		
		log.info("Binding server socket to " + port + "..");
		serverSocket = new ServerSocket(port);
//		this.sensingService = sensingService;
	}

	@Override
	public void run() {
		
		MessagePublisher.instance.start();
		
		SocketThreadManager socketThreadManager = new SocketThreadManager();
		log.info("The server is running..");
		
		Thread phoneSimThread = new Thread(new Phone());
		phoneSimThread.start();
		
		isRunning = true;
		while (isRunning) {
			try {
				pool.execute(socketThreadManager.newSocketThread(serverSocket.accept()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		phoneSimThread.interrupt();
		
		MessagePublisher.instance.stop();
		log.info("The server was shutdown!");
	}

	public void stop() {
		log.info("The server is shutting down..");
		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void handle(Object source, ValueEventArgs<Sensor> e) {
//		Sensor sensor = e.getValue();
//		String id = sensor.getId();
//		log.info("Added sensor with ID: " + id);
//		MeasurementProducer<Measurement> producer = sensingService.getProducer(sensor);
//		ConsumerAgent consumerAgent = new ConsumerAgent(id);
//		producer.addConsumer(consumerAgent);
//		new Thread(consumerAgent).run();
//	}
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.run();
	}
}
