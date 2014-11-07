package br.com.caiofreitas.lembrete.service;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import br.com.caiofreitas.lembrete.actions.LembretesProximos;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


public class LocationChangeListener implements LocationListener, 
									GooglePlayServicesClient.ConnectionCallbacks, 
									GooglePlayServicesClient.OnConnectionFailedListener{
	
	private LocationClient locationClient;
	private LembretesProximos lembretesProximos;
	
	public LocationChangeListener(Context context) {
		locationClient = new LocationClient(context, this, this);
		locationClient.connect();
		lembretesProximos = new LembretesProximos(context);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		lembretesProximos.verificar(location);		
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
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
}
