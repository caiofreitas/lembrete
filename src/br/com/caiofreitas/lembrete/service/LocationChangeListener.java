package br.com.caiofreitas.lembrete.service;

import br.com.caiofreitas.lembrete.model.Coordenada;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationChangeListener implements LocationListener {
	
	public LocationChangeListener(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 50, this);
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
		Coordenada coordenadaCorrente = new Coordenada(location.getLatitude(), location.getLongitude());
		
		Log.i("Latitude: ", String.valueOf(coordenadaCorrente.getLatitude()));
		Log.i("Longitude: ", String.valueOf(coordenadaCorrente.getLongitude()));
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		
		Location A = new Location("");
		A.setLatitude(-22.471654);
		A.setLongitude(-43.160215);
		
		Location B = new Location("");
		B.setLatitude(-22.529298);
		B.setLongitude(-43.216882);
		
		System.out.println( A.distanceTo(B));
	}

}
