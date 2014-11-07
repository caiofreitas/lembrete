package br.com.caiofreitas.lembrete;


import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.com.caiofreitas.lembrete.dao.LembreteDAO;
import br.com.caiofreitas.lembrete.model.Lembrete;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("InflateParams")
public class FormularioActivity extends FragmentActivity implements 
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {

	private static final int GPS_ERRORDIALOG_REQUEST = 9001;
	GoogleMap mMap;
	private static final float DEFAULTZOOM = 15;
	LocationClient mLocationClient;
	Marker marker;
	Circle shape;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (servicesOK()) {
			setContentView(R.layout.activity_map);

			if (initMap()) {
				//	mMap.setMyLocationEnabled(true);
				mLocationClient = new LocationClient(this, this, this);
				mLocationClient.connect();
			}
			else {
				Toast.makeText(this, "Mapa não disponivel!", Toast.LENGTH_SHORT)
					.show();				
			}
		}
		else {
			setContentView(R.layout.activity_main);
		}
		
		Intent intent = getIntent();
		final Lembrete  lembreteParaEditar = (Lembrete) intent.getSerializableExtra("lembreteSelecionado");
		
		Button btn = (Button) findViewById(R.id.button_salvar);
		
		if (lembreteParaEditar != null) {
			btn.setText("Alterar");
			
			EditText tituloEditText = (EditText) findViewById(R.id.titulo);
			EditText lembreteEditText = (EditText) findViewById(R.id.lembrete);
			Switch switch1 = (Switch) findViewById(R.id.switch1);
			
			tituloEditText.setText(lembreteParaEditar.getTitulo());
			lembreteEditText.setText(lembreteParaEditar.getTexto());
			switch1.setChecked(lembreteParaEditar.notificar());

			FormularioActivity.this.setMarker("", "", 
					lembreteParaEditar.getLatitude(), lembreteParaEditar.getLongitude());
		}
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText tituloEditText = (EditText) findViewById(R.id.titulo);
				EditText lembreteEditText = (EditText) findViewById(R.id.lembrete);
				Switch switch1 = (Switch) findViewById(R.id.switch1);
				
				
				Lembrete lembrete = new Lembrete();
				lembrete.setTitulo(tituloEditText.getText().toString());
				lembrete.setTexto(lembreteEditText.getText().toString());
		
				if(marker != null) {
					lembrete.setLatitude(marker.getPosition().latitude);
					lembrete.setLongitude(marker.getPosition().longitude);
				}
				
				lembrete.setNotificar(switch1.isChecked());
				
				if (lembrete.getTitulo() == null || lembrete.getTitulo().isEmpty()) {
					tituloEditText.setError("Campo obrigatório");
					tituloEditText.setFocusable(true);
					tituloEditText.requestFocus();
				}
				
				if (lembrete.getTexto() == null || lembrete.getTexto().isEmpty()) {
					lembreteEditText.setError("Campo obrigatório");
					lembreteEditText.setFocusable(true);
					lembreteEditText.requestFocus();
				}
				
				if (marker == null) {
					Toast.makeText(FormularioActivity.this, "Selecione um ponto no mapa", Toast.LENGTH_SHORT).show();
				}
				
				if((lembrete.getTitulo() != null && !lembrete.getTitulo().isEmpty())
					&&	(lembrete.getTexto() != null || !lembrete.getTexto().isEmpty())
					&& marker != null) {
				
					LembreteDAO dao = new LembreteDAO(FormularioActivity.this);
				
					if (lembreteParaEditar == null) {
						dao.save(lembrete);		
					} else {
						lembrete.setId(lembreteParaEditar.getId());
						dao.edit(lembrete);
					}
				
					dao.close();

					finish();		
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean servicesOK() {
		int isAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		}
		else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil
					.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		}
		else {
			Toast
			.makeText(this, "Não foi possível conectar ao Google Play services", Toast.LENGTH_SHORT)
				.show();
		}
		return false;
	}

	private boolean initMap() {	
		
		if (mMap == null) {
			SupportMapFragment mapFrag =
					(SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map);
			mMap = mapFrag.getMap();

			if (mMap != null) {
				mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						View v = getLayoutInflater().inflate(R.layout.info_window, null);
						TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
						TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
						TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
						TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

						LatLng ll = marker.getPosition();

						tvLocality.setText(marker.getTitle());
						tvLat.setText("Latitude: " + ll.latitude);
						tvLng.setText("Longitude: " + ll.longitude);
						tvSnippet.setText(marker.getSnippet());

						return v;
					}
				});
				
				mMap.setOnMapClickListener(new OnMapClickListener() {
					
					@Override
					public void onMapClick(LatLng ll) {
						FormularioActivity.this.setMarker("", "", 
								ll.latitude, ll.longitude);
					}
				});

				mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

					@Override
					public void onMapLongClick(LatLng ll) {

					}
				});

				mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

					@Override
					public void onMarkerDragStart(Marker arg0) {
						removeShape();
					}

					@Override
					public void onMarkerDragEnd(Marker marker) {
						LatLng ll = marker.getPosition();						
						setMarker("", "", ll.latitude, ll.longitude);
					}

					@Override
					public void onMarkerDrag(Marker arg0) {
						// TODO Auto-generated method stub
					}
				});

			}
		}
		return (mMap != null);
	}

	private void gotoLocation(double lat, double lng,
			float zoom) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		//mMap.moveCamera(update);
		mMap.animateCamera(update);
	}

	public void geoLocate(View v) {

		EditText et = (EditText) findViewById(R.id.editText1);
		String location = et.getText().toString();
		if (location.length() == 0) {
			Toast.makeText(this, "Por favor, indique um local", Toast.LENGTH_SHORT).show();
			return;
		}

		hideSoftKeyboard(v);

		if(Geocoder.isPresent()) {
			Geocoder gc = new Geocoder(this);
			
			List<Address> list;
			try {
				list = gc.getFromLocationName(location, 1);

			
				if (list != null && !list.isEmpty()) {
				
					Address add = list.get(0);
					String locality = add.getLocality();
						Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
		
					double lat = add.getLatitude();
					double lng = add.getLongitude();
	
					gotoLocation(lat, lng, DEFAULTZOOM);
					
					setMarker(locality, add.getCountryName(), lat, lng);
				} else {
					Toast.makeText(this, "Sua pesquisa - " + location + " - não encontrou nenhuma localidade correspondente. ", Toast.LENGTH_LONG).show();
				}
		
			} catch (IOException e) {
				// 
				e.printStackTrace();
				Toast.makeText(this, "Servidor excedeu o tempo de resposta", Toast.LENGTH_LONG).show();
				
			}
			
		} else {
			Toast.makeText(this, "Serviço de geocodificação da Google não está disponíve no emulador android ", Toast.LENGTH_LONG).show();
		}

	}

	private void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.mapTypeNone:
			mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			break;
		case R.id.mapTypeNormal:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
		case R.id.mapTypeSatellite:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.mapTypeTerrain:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
		case R.id.mapTypeHybrid:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			break;
		case R.id.gotoCurrentLocation:
			gotoCurrentLocation();
			break;
		case android.R.id.home:
			finish();
			break;
			
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MapStateManager mgr = new MapStateManager(this);
		mgr.saveMapState(mMap);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MapStateManager mgr = new MapStateManager(this);
		CameraPosition position = mgr.getSavedCameraPosition();
		if (position != null) {
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
			mMap.moveCamera(update);
			//			This is part of the answer to the code challenge
			mMap.setMapType(mgr.getSavedMapType());
		}

	}

	protected void gotoCurrentLocation() {
		Location currentLocation = mLocationClient.getLastLocation();
		if (currentLocation == null) {
			Toast.makeText(this, "Localização atual não está disponivel", Toast.LENGTH_SHORT).show();
		}
		else {
			LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
			mMap.animateCamera(update);
		}

		setMarker("Localização atual", "",
				currentLocation.getLatitude(), 
				currentLocation.getLongitude());

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle arg0) {
		//		Toast.makeText(this, "Conectado ao serviço de localização", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
	}

	private void setMarker(String locality, String country, double lat, double lng) {

		LatLng ll = new LatLng(lat,lng);
		
		MarkerOptions options = new MarkerOptions()	
		.title(locality)
		.position(ll)
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker))
				//.icon(BitmapDescriptorFactory.defaultMarker())
		.anchor(.5f, .5f)
		.draggable(true);
		if (country.length() > 0) {
			options.snippet(country);
		}

		if (marker != null) {
			removeEverything();
		}

		marker = mMap.addMarker(options);
		
		shape = drawCircle(ll);
		gotoLocation(ll.latitude, ll.longitude, DEFAULTZOOM);

	}

	private Circle drawCircle(LatLng ll) {
		
		CircleOptions options = new CircleOptions()
		.center(ll)
		.radius(250)
		.fillColor(0x330000FF)
		.strokeColor(Color.BLUE)
		.strokeWidth(3);
		
		return mMap.addCircle(options);
	}

	private void removeEverything() {
		removeMarker();
		removeShape();
	}
	
	private void removeMarker() {
		if (marker != null) {
			marker.remove();
			marker = null;			
		}
	}
	
	private void removeShape(){
		if (shape != null) {
			shape.remove();
			shape = null;			
		}
	}
}
