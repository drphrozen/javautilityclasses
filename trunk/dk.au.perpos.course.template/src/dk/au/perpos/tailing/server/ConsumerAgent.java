package dk.au.perpos.tailing.server;

import java.util.logging.Logger;

import dk.au.perpos.core.Consumer;
import dk.au.perpos.sensing.measurements.Measurement;
import dk.au.perpos.sensing.measurements.gps.GPSMeasurement;
import dk.au.perpos.spatialsupport.position.WGS84Position;
import dk.au.perpos.tailing.TailingAgent.Agent;
import dk.au.perpos.tailing.TailingAgent.ManagerMessage;
import dk.au.perpos.tailing.TailingAgent.Person;
import dk.au.perpos.tailing.TailingAgent.Position;

public class ConsumerAgent implements Consumer<Measurement> {

	private final Agent agentProto;
	private final MessagePublisher publisher;
	private final Logger log = Logger.getLogger(ConsumerAgent.class.getName());

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
		publisher.Publish(ManagerMessage.newBuilder().setAgent(tmp).build());
	}

}
