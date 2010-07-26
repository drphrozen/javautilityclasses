package dk.znz.weatherunderground;

import dk.znz.weather.Conditions;
import dk.znz.weather.Forecast;
import dk.znz.weather.Station;

public class WundergroundStation extends Station {

	public WundergroundStation(String city, String state, String country, String id, double latitude, double longitude) {
		super(city, state, country, id, latitude, longitude);
	}

	@Override
	public Conditions getCurrentConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Forecast getForecast(int days) {
		// TODO Auto-generated method stub
		return null;
	}

}
