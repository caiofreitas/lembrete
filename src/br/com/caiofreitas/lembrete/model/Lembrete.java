package br.com.caiofreitas.lembrete.model;

import java.io.Serializable;

public class Lembrete implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7479992691033165922L;
	private Long id;
	private String texto;
	private String titulo;
	private double latitude;
	private double longitude;
	private boolean notificar;
	
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
		
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

	@Override
	public String toString() {
		
//		String coordenada = String.format("%.4f", this.latitude) 
//		+ ", " + String.format("%.4f",this.longitude);
		
		return this.titulo + " - " + this.texto;//" (" + coordenada + ")" ;
	}

	public boolean notificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}
	
}
