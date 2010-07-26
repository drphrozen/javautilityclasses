package dk.znz.weather;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public interface WeatherService {
	/**
	 * Get a weather station based on the current location.
	 * @param lat GPS Latitude
	 * @param lon GPS Longitude
	 * @return The nearest weather station.
	 * @throws UnsupportedEncodingException 
	 */
	Station lookup(double lat, double lon) throws UnsupportedEncodingException;
	
	/**
	 * @return A description of the query that the end user will see.
	 */
	String getQueryDescription();
	
	/**
	 * Lookup a station based on a query.
	 * @param query A service specific query for a Station use {@link #getQueryDescription()}
	 * @return A list of stations best matching the query. Should be sorted so that the first entry is the best match.  
	 * @see Station
	 */
	ArrayList<Station> lookup(String query);
}
