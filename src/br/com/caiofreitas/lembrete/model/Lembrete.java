package br.com.caiofreitas.lembrete.model;

import android.R.string;

public class Lembrete {

	private Coordenada coordenada;
	private string texto;
	
	public Coordenada getCoordenada() {
		return coordenada;
	}
	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}
	public string getTexto() {
		return texto;
	}
	public void setTexto(string texto) {
		this.texto = texto;
	}
}
