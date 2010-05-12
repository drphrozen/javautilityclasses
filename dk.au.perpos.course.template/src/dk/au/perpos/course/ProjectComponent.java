package dk.au.perpos.course;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dk.au.perpos.course.boilerplate.ProjectComponentBase;
import dk.au.perpos.sensing.SensingService;
import dk.au.perpos.tailing.server.Server;

public class ProjectComponent extends ProjectComponentBase {

	private final Logger log = Logger.getLogger(ProjectComponent.class.getName());
	private Server server;

	@Override
	protected void activate() {
		Logger.getLogger("dk.au.perpos.components.nmeaparser.Parser").setLevel(Level.WARNING);
		
		log.info("Activated");
		// Called by the OSGi framework when all dependencies are available
		// This method must return rather quickly, so any processing must be
		// done in new threads started from here.
		
		try {
			server = new Server(sensingService);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		new Thread(server).start();
		
		final SensingService sensingService = getSensingService();
		sensingService.sensorAdded().addHandler(server);
	}

	@Override
	protected void deactivate() {
		// Called when the component is being closed.
		// Any active resources and threads must be released and stopped in this
		// method.
		
		server.stop();
	}
}
