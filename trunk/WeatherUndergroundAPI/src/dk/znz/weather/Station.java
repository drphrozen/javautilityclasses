package dk.znz.weather;

public abstract class Station {
	protected String city;
	protected String state;
	protected String country;
	protected String id;
	protected double latitude;
	protected double longitude;
	
	/**
	 * 
	 * @param city The city the station is in, "" if not available.
	 * @param state The state the station is in, "" if not available.
	 * @param country The country the station is in, "" if not available.
	 * @param id ID used to lookup Conditions and Forecasts for this station.
	 * @param latitude Latitude coordinate or NaN if not available. 
	 * @param longitude Longitude coordinate or NaN if not available.
	 * @see Conditions
	 * @see Forecast
	 */
	public Station(String city, String state, String country, String id, double latitude, double longitude) {
		this.city = city;
		this.state = state;
		this.country = country;
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public abstract Conditions getCurrentConditions();
	/**
	 * Retrieves a forecast from this station.
	 * @param days
	 * Number of days in the forecast.
	 * 0 is illegal,
	 * -1 is as many as possible,
	 * 1 is the same as getCurrentConditions,
	 * >1 will return a forecast of this size or more.
	 * If a service is not able to give an forecast with the given number of days, then it will act as -1 days (as many as possible).
	 * @return A forecast for the given number of days.
	 */
	public abstract Forecast getForecast(int days);

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getID() {
		return id;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
