package dk.znz.weather;

import java.util.ArrayList;
import java.util.Collection;

public class Forecast extends ArrayList<Conditions> {

	private static final long serialVersionUID = 1L;
	
	private final Station station;
	
	public Forecast(Station station, Collection<? extends Conditions> c) {
		super(c);
		this.station = station;
	}
	
	public Station getStation() {
		return station;
	}
}
