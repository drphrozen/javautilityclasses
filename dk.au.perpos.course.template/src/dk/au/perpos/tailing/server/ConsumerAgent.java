package dk.au.perpos.tailing.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import dk.au.perpos.core.Consumer;
import dk.au.perpos.sensing.measurements.Measurement;
import dk.au.perpos.sensing.measurements.gps.GPSMeasurement;
import dk.au.perpos.spatialsupport.position.WGS84Position;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.AgentInfo;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;
import dk.au.perpos.tailing.TailingAgent.TargetSeen;
import dk.au.perpos.tailing.server.MessagePublisher.MessageSubscriber;

public class ConsumerAgent implements Consumer<Measurement>, MessageSubscriber<TargetSeen>, Runnable {

	private final Agent agentProto;
	private final MessagePublisher publisher;
	private final Logger log = Logger.getLogger(ConsumerAgent.class.getName());

	private final ArrayBlockingQueue<TargetSeen> targetSeenQueue = new ArrayBlockingQueue<TargetSeen>(20);
	private final ArrayBlockingQueue<Agent> agentQueue = new ArrayBlockingQueue<Agent>(20);
	
	private Agent lastAgent = null;
	
	public ConsumerAgent(String name) {
		agentProto = Agent.newBuilder().setName(name).buildPartial();
		publisher = MessagePublisher.instance;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public void consume(Measurement measurement) {
		log.info(measurement.toString());
		if(!(measurement instanceof GPSMeasurement)) return;
		GPSMeasurement gpsMeasurement = (GPSMeasurement) measurement;
		WGS84Position position = gpsMeasurement.getPosition();
		Agent tmp = Agent.newBuilder(agentProto).setPerson(Person.newBuilder()
			.setDirection(0)
			.setSpeed(0)
			.setPosition(Position.newBuilder()
				.setAltitude(position.getAltitudeOverWGS84Ellipsoid())
				.setLatitude(position.getLatitude())
				.setLongitude(position.getLongitude())
				.build())
			.build())
		.build();
		log.info("Got agent");
		agentQueue.offer(tmp);
	}

	@Override
	public void OnSignal(TargetSeen value) {
		log.info("Got targetSeen");
		targetSeenQueue.offer(value);
	}
	
	@Override
	public void run() {
		log.info("Consumer startet!");
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
			Agent agent = agentQueue.poll();
			TargetSeen targetSeen = targetSeenQueue.poll();
			if(agent != null) {
				lastAgent = agent;
				log.info("publish1");
				publisher.Publish(build(agent, targetSeen));
			} else {
				if(targetSeen != null && lastAgent != null) {
					log.info("publish2");
					publisher.Publish(build(lastAgent, targetSeen));
				}
			}
		}
		log.info("Consumer stopped!");
	}

	private ManagerMessage build(Agent agent, TargetSeen targetSeen) {
		return ManagerMessage.newBuilder()
			.addAgent(AgentInfo.newBuilder()
				.setAgent(agent)
				.setTargetSeen(targetSeen)
			.build())
		.build();
	}

}
