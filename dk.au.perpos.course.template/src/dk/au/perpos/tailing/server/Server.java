package dk.au.perpos.tailing.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import dk.au.perpos.core.events.EventHandler;
import dk.au.perpos.core.events.ValueEventArgs;
import dk.au.perpos.sensing.SensingService;
import dk.au.perpos.sensing.Sensor;
import dk.au.perpos.sensing.measurements.Measurement;
import dk.au.perpos.sensing.measurements.MeasurementProducer;

public class Server implements Runnable, EventHandler<ValueEventArgs<Sensor>> {

	private final ExecutorService	pool	= Executors.newFixedThreadPool(20);
	private final ServerSocket		serverSocket;
	private volatile boolean			isRunning;
	private final SensingService sensingService;
	
	private final Logger log = Logger.getLogger(Server.class.getName());
	
	public Server(SensingService sensingService) throws IOException {
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
		this.sensingService = sensingService;
	}

	@Override
	public void run() {
		
		MessagePublisher.instance.start();
		
		SocketThreadManager socketThreadManager = new SocketThreadManager();
		log.info("The server is running..");
		
		isRunning = true;
		while (isRunning) {
			try {
				pool.execute(socketThreadManager.newSocketThread(serverSocket.accept()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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

	@Override
	public void handle(Object source, ValueEventArgs<Sensor> e) {
		Sensor sensor = e.getValue();
		String id = sensor.getId();
		log.info("Added sensor with ID: " + id);
		MeasurementProducer<Measurement> producer = sensingService.getProducer(sensor);
		producer.addConsumer(new ConsumerAgent(id));
	}

	public static void main(String[] args) {
//		try {
//			new Server().run();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
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
