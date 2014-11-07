package br.com.caiofreitas.lembrete.actions;

import android.location.Location;
import br.com.caiofreitas.lembrete.model.Lembrete;

import com.google.android.gms.maps.model.LatLng;

public class CalcularDistancia {
	
	public static final double R = 6372.8; // em kilomentros
	
	
	public double haversine(LatLng pontoA, LatLng pontoB){
		
		double dLat = Math.toRadians(pontoB.latitude - pontoA.latitude);
        double dLon = Math.toRadians(pontoB.longitude - pontoA.longitude);
        double latA = Math.toRadians(pontoA.latitude);
        double latB = Math.toRadians(pontoB.latitude);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(latA) * Math.cos(latB);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
	}
	
	public double WGS84ellipsoid(Lembrete lembrete, Location location) {
		
		Location lembreteLocation = new Location("");
		lembreteLocation.setLatitude(lembrete.getLatitude());
		lembreteLocation.setLongitude(lembrete.getLongitude());
		double dist = lembreteLocation.distanceTo(location);		
		
		return dist;
	}

}
