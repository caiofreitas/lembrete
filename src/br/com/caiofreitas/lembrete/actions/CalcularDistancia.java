package br.com.caiofreitas.lembrete.actions;

import com.google.android.gms.maps.model.LatLng;

public class CalcularDistancia {
	
	 public static final double R = 6372.8; // em kilomentros
	
	public double entre(LatLng pontoA, LatLng pontoB){
		
		double dLat = Math.toRadians(pontoB.latitude - pontoA.latitude);
        double dLon = Math.toRadians(pontoB.longitude - pontoA.longitude);
        double latA = Math.toRadians(pontoA.latitude);
        double latB = Math.toRadians(pontoB.latitude);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(latA) * Math.cos(latB);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
	}

}
