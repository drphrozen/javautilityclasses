package dk.znz.weatherunderground;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import dk.znz.weather.Station;
import dk.znz.weather.WeatherService;

public class WundergroundService implements WeatherService {

	public static final String LOOKUP_ADDRESS = "http://api.wunderground.com/auto/wui/geo/GeoLookupXML/index.xml?query=";
	public static final String LOOKUP_ADDRESS_ENCODING = "UTF-8";

	@Override
	public String getQueryDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Station lookup(double lat, double lon) throws UnsupportedEncodingException {
		String query = URLEncoder.encode(Double.toString(lat) + "," + Double.toString(lon), LOOKUP_ADDRESS_ENCODING);
		URL url = new URL(query);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();

		/* Get the XMLReader of the SAXParser we created. */
		XMLReader xr = sp.getXMLReader();

		/* Create a new ContentHandler and apply it to the XML-Reader */
		GeoLookupHandler myExampleHandler = new GeoLookupHandler();
		xr.setContentHandler(myExampleHandler);

		/* Parse the xml-data from our URL. */
		xr.parse(new InputSource(url.openStream()));
		/* Parsing has finished. */

		/* Our ExampleHandler now provides the parsed data to us. */
		ParsedExampleDataSet parsedExampleDataSet = myExampleHandler.getParsedData();

		return null;
	}

	@Override
	public ArrayList<Station> lookup(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
