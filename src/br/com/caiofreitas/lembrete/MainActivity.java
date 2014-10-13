package br.com.caiofreitas.lembrete;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.caiofreitas.lembrete.service.LocationService;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;


public class MainActivity extends FragmentActivity {

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	  super.onCreate(savedInstanceState);
    	
    	if (servicesOK()) {
    		Toast.makeText(this, "Conctado ao google play", Toast.LENGTH_SHORT).show();
    		 setContentView(R.layout.activity_map);
    	
    	} else {
    		setContentView(R.layout.activity_main);
           
    		String[] lembretes = {"Comprar café", "Devolver Livro", "Sacar dinheiro" };
            
            int layout = android.R.layout.simple_list_item_1;
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout, lembretes);
            
            ListView lista = (ListView) findViewById(R.id.lista);
            
            lista.setAdapter(adapter);
            
            lista.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				
    				Toast.makeText(MainActivity.this, "Clique na posição: " + position , Toast.LENGTH_SHORT).show();
    				
    			}
            });
    		
            lista.setOnItemLongClickListener(new OnItemLongClickListener(){

    			@Override
    			public boolean onItemLongClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				Toast.makeText(MainActivity.this, "Clique longo em: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
    				return true;
    			}
            });       
    	}
    	  	
    	
      
        
        startService(new Intent(MainActivity.this, LocationService.class));
        

    }
    
    public boolean servicesOK() {
    	int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	
    	if (isAvailable == ConnectionResult.SUCCESS){
    		return true;
    	}
    	else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
    		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
    		dialog.show();
    	}
    	else {
    		Toast.makeText(this, "Erro ao conectar ao google play", Toast.LENGTH_SHORT).show();
    	}
    	return false;
    }
}
