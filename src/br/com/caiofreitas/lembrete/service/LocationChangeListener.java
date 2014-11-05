package br.com.caiofreitas.lembrete.service;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class LocationChangeListener implements LocationListener, 

GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	
	private Context context;
	private LocationClient locationClient;
	
	public LocationChangeListener(Context context) {
		this.context = context;
		locationClient = new LocationClient(context, this, this);
		locationClient.connect();
	}
	
	@Override
	public void onLocationChanged(Location location) {
//		String msg = location.getLatitude() + ", " + location.getLongitude();
//		Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
		NotificationService notificationService = new NotificationService(context);
		notificationService.notificar(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
//		Toast.makeText(this.context, "Conectado ao servico de localizacao"
//				, Toast.LENGTH_SHORT).show();
		
		LocationRequest request = LocationRequest.create();
		request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		request.setInterval(25000);
		request.setFastestInterval(1000);
		locationClient.requestLocationUpdates(request, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
