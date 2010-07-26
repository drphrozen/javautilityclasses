package dk.znz.weatherunderground;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import dk.znz.weather.Station;

public class GeoLookupHandler extends DefaultHandler {

	private class StationBuilder {
		public String city;
		public String state;
		public String country;
		public String id;
		public double distance;
		public double lon;
		public double lat;
		
		public StationBuilder() {
			reset();
		}
		
		public void reset() {
			city = "";
			state = "";
			country = "";
			id ="";
			distance = Double.MAX_VALUE;
			lon = Double.NaN;
			lat = Double.NaN;
		}

		public WundergroundStation build() {
			return new WundergroundStation(city, state, country, id, lat, lon);
		}
		
		public double getDistance(double currentLatitude, double currentLongitude) {
			if(lat != Double.NaN && lon != Double.NaN &&
					currentLatitude != Double.NaN && currentLongitude != Double.NaN) {
				return distance(lat, lon, currentLatitude, currentLongitude, 'K');
			} else {
				return distance;
			}
		}
		
		private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit == 'K') {
		    dist = dist * 1.609344;
		  } else if (unit == 'N') {
		  	dist = dist * 0.8684;
		    }
		  return (dist);
		}

		private double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
		}

		private double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
		}
	}

	private static final String NEARBY = "nearby_weather_stations";
	private static final String STATION = "station";
	private boolean isInsideNearby = false;
	private boolean isInsideStation = false;
	private String currentElement = "";
	private final StationBuilder stationBuilder = new StationBuilder();
	private final SortedMap<Double, WundergroundStation> wundergroundStations = new TreeMap<Double, WundergroundStation>();
	private final double current_latitude;
	private final double current_longitude;
	
	public GeoLookupHandler(double latitude, double longitude) {
		current_latitude = latitude;
		current_longitude = longitude;
	}
	
	public GeoLookupHandler() {
		current_latitude = Double.NaN;
		current_longitude = Double.NaN;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (isInsideNearby) {
			if (isInsideStation) {
				currentElement = localName;
			} else if (localName.equals(STATION)) {
				isInsideStation = true;
				stationBuilder.reset();
			}
		} else if (localName.equals(NEARBY))
			isInsideNearby = true;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (isInsideNearby) {
			if (localName.equals(NEARBY)) {
				isInsideNearby = false;
			} else if (isInsideStation && localName.equals(STATION)) {
				isInsideStation = false;
				wundergroundStations.put(stationBuilder.getDistance(current_latitude, current_longitude), stationBuilder.build());
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(isInsideStation) {
			try {
				if(currentElement.equals("city")) stationBuilder.city = String.copyValueOf(ch, start, length);
				else if(currentElement.equals("state")) stationBuilder.state = String.copyValueOf(ch, start, length);
				else if(currentElement.equals("country")) stationBuilder.country = String.copyValueOf(ch, start, length);
				else if(currentElement.equals("icao") || currentElement.equals("id")) stationBuilder.id = String.copyValueOf(ch, start, length);
				else if(currentElement.equals("distance_km")) stationBuilder.id = String.copyValueOf(ch, start, length);
				else if(currentElement.equals("lat")) stationBuilder.lat = Double.parseDouble(String.copyValueOf(ch, start, length));
				else if(currentElement.equals("lon")) stationBuilder.lon = Double.parseDouble(String.copyValueOf(ch, start, length));
			} catch (NumberFormatException e) {
				// skip lat/lon parsing errors
			}
		}
	}
	
	public ArrayList<WundergroundStation> getWundergroundStations() {
		return wundergroundStations;
		// TODO: return sorted list
		wundergroundStations.values()
	}
}
