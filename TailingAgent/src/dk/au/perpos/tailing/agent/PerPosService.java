package dk.au.perpos.tailing.agent;

import dk.au.perpos.android.PerPos;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PerPosService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private PerPos perpos = null;
	public static String PERPOS_PORT = "PERPOS_PORT";
	public static String PERPOS_HOSTNAME = "PERPOS_HOSTNAME";
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
}
