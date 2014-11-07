package br.com.caiofreitas.lembrete.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.caiofreitas.lembrete.model.Lembrete;

public class LembreteDAO extends SQLiteOpenHelper{

	
	private static final int VERSION = 3;
	private static final String DATA_BASE_NAME = "Lembretes";

	public LembreteDAO(Context context) {
		super(context, DATA_BASE_NAME, null, VERSION);
		
	}

	public void save(Lembrete lembrete) {
		ContentValues values = new ContentValues();
		
		values.put("titulo", lembrete.getTitulo());
		values.put("texto", lembrete.getTexto());
		values.put("latitude", lembrete.getLatitude());
		values.put("longitude", lembrete.getLongitude());
		values.put("notificar", lembrete.notificar());
		
		getWritableDatabase().insert("Lembrete", null, values);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String ddl = "CREATE TABLE Lembrete (id INTEGER PRIMARY KEY, titulo TEXT NOT NULL" +
				", texto TEXT NOT NULL, latitude REAL NOT NULL" +
				", longitude REAL NOT NULL, notificar INTEGER NUT NULL  ); ";
		db.execSQL(ddl);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String ddl = "DROP TABLE IF EXISTS Lembrete";
		db.execSQL(ddl);
		
		this.onCreate(db);
	}

	public List<Lembrete> getLembretes() {

		String[] colunas = { "id", "titulo", "texto", "latitude", "longitude", "notificar"};
		
		Cursor cursor = getWritableDatabase().query("Lembrete", colunas, null, null, null, null, null);
		
		ArrayList<Lembrete> lembretes = new ArrayList<Lembrete>();
		
		while (cursor.moveToNext()) {
			Lembrete lembrete = new Lembrete();
			lembrete.setId(cursor.getLong(0));
			lembrete.setTitulo(cursor.getString(1));
			lembrete.setTexto(cursor.getString(2));	
			lembrete.setLatitude(cursor.getDouble(3));
			lembrete.setLongitude(cursor.getDouble(4));
			
			int a = cursor.getInt(5);
			lembrete.setNotificar(a == 1);
			
			lembretes.add(lembrete);
		}
		
		return lembretes;
	}

	public void desativar(Lembrete lembrete) {
		lembrete.setNotificar(false);
		edit(lembrete);
	}
	
	public void remover(Lembrete lembrete) {
		String[] args = { lembrete.getId().toString() };
		getWritableDatabase().delete("Lembrete", "id=?", args );
	}

	public void edit(Lembrete lembrete) {
		ContentValues values = new ContentValues();
		
		values.put("titulo", lembrete.getTitulo());
		values.put("texto", lembrete.getTexto());
		values.put("latitude", lembrete.getLatitude());
		values.put("longitude", lembrete.getLongitude());
		values.put("notificar", lembrete.notificar());
		
		String[] args = { lembrete.getId().toString() };
		getWritableDatabase().update("Lembrete", values, "id=?", args ); 	
	}
}
