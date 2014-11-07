package br.com.caiofreitas.lembrete.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import br.com.caiofreitas.lembrete.FormularioActivity;
import br.com.caiofreitas.lembrete.dao.LembreteDAO;
import br.com.caiofreitas.lembrete.model.Lembrete;

public class NotificationService {

	private Context context;
	
	public NotificationService(Context context){
		this.context = context;
	}
	
	public void enviar(Lembrete lembrete) {
		
		Intent intent = new Intent(context, FormularioActivity.class);
		intent.putExtra("lembreteSelecionado", lembrete);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context);
		builder.setAutoCancel(true);
		builder.setContentTitle(lembrete.getTitulo());
		builder.setContentText(lembrete.getTexto());
		builder.setSmallIcon(br.com.caiofreitas.lembrete.R.drawable.ic_launcher);
		builder.setLights(0xff0000ff, 300, 1000);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		builder.setSound(alarmSound);	
		builder.setContentIntent(pIntent);
	
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify( 8, notification);
		
		LembreteDAO dao = new LembreteDAO(context);
		dao.desativar(lembrete);
	}
}
