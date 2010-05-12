package dk.au.perpos.tailing.agent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import dk.au.perpos.android.PerPos;

public class TailingAgent extends Activity {
	private PerPos perpos;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.Button01).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String hostName;
				final int port;

				String[] hostPort = ((EditText) findViewById(R.id.EditText01))
								.getText().toString().split(":");
				if(hostPort.length != 2)
				{
					Toast.makeText(TailingAgent.this, "Need to have host:port!", Toast.LENGTH_LONG);
					return;
				}
				hostName = hostPort[0];
				int tmpPort = -1;
				try {
					tmpPort= Integer.parseInt(hostPort[1]); 
				} catch (NumberFormatException e) {
					Toast.makeText(TailingAgent.this, "Port could not be parsed!", Toast.LENGTH_LONG);
					return;
				}
				port = tmpPort;

				perpos = new PerPos(TailingAgent.this, hostName, port);
//				SensingService service = perpos.getSensingService();
				
				perpos.startGPS();
				perpos.startRelay();
//				perpos.startWiFi();
				
//				MeasurementProducer<Measurement> producer = null;
//				Sensor sensor = perpos.getSensingService().findFirstSensor(
//						new Any());
//				if (sensor != null) {
//					producer = perpos.getSensingService().getProducer(sensor);
//					producer.addConsumer(new Consumer<Measurement>() {
//
//						public void consume(Measurement m) {
//							if (m instanceof WiFiMeasurement) {
//								WiFiMeasurement wm = (WiFiMeasurement) m;
//								toast("Received WiFi measurement [x: "
//										+ wm.getX() + ", y: " + wm.getY()
//										+ ", z: " + wm.getZ() + ", symID: "
//										+ wm.getSymbolicID() + ", time: "
//										+ wm.getTimeOfMeasurement() + "]");
//							} else if (m instanceof GPSMeasurement) {
//								GPSMeasurement gm = (GPSMeasurement) m;
//								toast("Received GPS measurement [lat: "
//										+ gm.getPosition().getLatitude()
//										+ ", lon: "
//										+ gm.getPosition().getLongitude()
//										+ ", time: "
//										+ gm.getTimeOfMeasurement() + "]");
//							} else {
//								toast("Unknown measurement "
//										+ m.getTimeOfMeasurement());
//							}
//						}
//					});
//				}
			}
		});
	}

	protected void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
}