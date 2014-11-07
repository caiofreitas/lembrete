package br.com.caiofreitas.lembrete.actions;

import java.util.List;

import android.content.Context;
import android.location.Location;
import br.com.caiofreitas.lembrete.dao.LembreteDAO;
import br.com.caiofreitas.lembrete.model.Lembrete;
import br.com.caiofreitas.lembrete.service.NotificationService;

public class LembretesProximos {
	
	private CalcularDistancia calcularDistancia;
	private NotificationService notificationService;
	private LembreteDAO dao;
	private static final int RAIO = 250;
	
	public LembretesProximos(Context context){
		notificationService = new NotificationService(context);
		calcularDistancia = new CalcularDistancia();
		dao = new LembreteDAO(context);
	}
	
	public void verificar(Location location) {
				
		List<Lembrete> lembretes = dao.getLembretes();
		dao.close();
		
		for (Lembrete lembrete : lembretes) {
		
			if(verificaProximidade(lembrete, location)) {
				notificationService.enviar(lembrete);
			}
		}
	}
	
	private boolean verificaProximidade(Lembrete lembrete, Location location) {
		double dist = calcularDistancia.WGS84ellipsoid(lembrete, location);
		return dist <= RAIO && lembrete.notificar();
	}	
}
	


