import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class Program {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Guice.createInjector() takes your Modules, and returns a new Injector
		 * instance. Most applications will call this method exactly once, in
		 * their main() method.
		 */
		Injector injector = Guice.createInjector(new SensorModule());

		/*
		 * Now that we've got the injector, we can build objects.
		 */
		Sensor sensor = injector.getInstance(Sensor.class);
	}
}

interface Driver {
	int getId();
}

class Sensor implements Driver {

	@Inject
	public Sensor() {
	}

	@Override
	public int getId() {
		return 0;
	}
}

class SensorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Driver.class).to(Sensor.class);
	}
}