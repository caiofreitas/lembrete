package br.com.caiofreitas.lembrete.actions;

import br.com.caiofreitas.lembrete.model.Coordenada;

public class CalcularDistancia {
	
	 public static final double R = 6372.8; // em kilomentros
	
	public double entre(Coordenada pontoA, Coordenada pontoB){
		
		double dLat = Math.toRadians(pontoB.getLatitude() - pontoA.getLatitude());
        double dLon = Math.toRadians(pontoB.getLongitude() - pontoA.getLongitude());
        double latA = Math.toRadians(pontoA.getLatitude());
        double latB = Math.toRadians(pontoB.getLatitude());
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(latA) * Math.cos(latB);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
	}

}
