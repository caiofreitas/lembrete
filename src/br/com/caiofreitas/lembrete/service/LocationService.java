package br.com.caiofreitas.lembrete.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		new LocationChangeListener(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
