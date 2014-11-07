package br.com.caiofreitas.lembrete;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Toast;
import br.com.caiofreitas.lembrete.dao.LembreteDAO;
import br.com.caiofreitas.lembrete.model.Lembrete;
import br.com.caiofreitas.lembrete.service.LocationService;

public class ListagemLembretesActivity extends Activity {

	private ListView lista;
	private Lembrete lembrete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_lembretes);
		
		Intent intent = new Intent(this, LocationService.class);
    	startService(intent);
		
				
		lista = (ListView)findViewById(R.id.lista);
		
		registerForContextMenu(lista);
		
		popularLista();
		
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Lembrete lembrete = (Lembrete) parent.getItemAtPosition(position);
				
				Intent intent = new Intent(ListagemLembretesActivity.this, FormularioActivity.class);
				intent.putExtra("lembreteSelecionado", lembrete);
				startActivity(intent);
			}
		});
		
		lista.setOnItemLongClickListener(new OnItemLongClickListener(){

			

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				lembrete = (Lembrete) parent.getItemAtPosition(position);
				
				
//				Toast.makeText(ListagemLembretes.this, "Click na longo posicao " 
//				+ position, Toast.LENGTH_SHORT).show();
				return false;
			}
			
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		MenuItem remover = menu.add("Remover");
		remover.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				LembreteDAO dao = new LembreteDAO(ListagemLembretesActivity.this);
				dao.remover(lembrete);
				dao.close();
				
				popularLista();
				
				return false;
			}
		});
		
		//menu.add("Ver no mapa");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listagem_lembretes, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int itemClicado = item.getItemId();
		
		switch (itemClicado) {
		case R.id.novo:
			Intent it = new Intent(this, FormularioActivity.class);
			startActivity(it);
			break;

		default:
			break;
		}
			
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		popularLista();
	}
	
	private void popularLista(){
		LembreteDAO dao = new LembreteDAO(this);
		List<Lembrete> lembretes = dao.getLembretes();
		int layout = android.R.layout.simple_list_item_1;
		ArrayAdapter<Lembrete> adapter = new ArrayAdapter<Lembrete>(this, layout , lembretes);
		lista.setAdapter(adapter);
	}
}
