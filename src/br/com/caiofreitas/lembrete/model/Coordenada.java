package br.com.caiofreitas.lembrete.model;

public class Coordenada {
	
	public Coordenada(){
		
	}
	
	public Coordenada(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private double latitude;
	private double longitude;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	
}