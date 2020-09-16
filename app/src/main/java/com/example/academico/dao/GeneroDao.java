package com.example.academico.dao;

import java.util.ArrayList;
import java.util.List;
import com.example.academico.domain.Genero;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Dao da classe Genero
public class GeneroDao extends ObjectDao {

	public GeneroDao(DataBaseCreator creator) {
		super(creator);
	}

	// Insere uma genero no banco
	public long inserirGenero(Genero genero){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nome", genero.getNome());
		// Quando é feita a inserção automaticamente é retornado o id
		long id = database.insert(DataBaseCreator.TABELA_GENERO, null, values);
		// Que é armazenado na genero inserida
		genero.setId(id);
		database.close();
		return id;
	}
	
	// Altera uma genero no banco de dados
	public void alterarGenero(Genero genero){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		ContentValues values = new ContentValues();
		long id = genero.getId();
		values.put("nome", genero.getNome());
		database.update(DataBaseCreator.TABELA_GENERO, values, "id = ?", new String[]{String.valueOf(id)});
		database.close();
	}

	// Exclui uma genero no banco de dados
	public void excluirGenero(Genero genero){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		long id = genero.getId();
		database.delete(DataBaseCreator.TABELA_GENERO, "id = ?", new String[]{String.valueOf(id)});
		database.close();
	}
	
	
	// Obtem a lista das turmas no banco de dados
	public List<Genero> getLista(){
		SQLiteDatabase database = this.getCreator().getReadableDatabase();
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT * FROM " + DataBaseCreator.TABELA_GENERO + ";" ;
        Cursor c = database.rawQuery(sql, null);
        while(c.moveToNext()){
            Genero genero = new Genero();
            genero.setId(new Long(c.getInt(c.getColumnIndex("id"))));
            genero.setNome(c.getString(c.getColumnIndex("nome")));
            generos.add(genero);
        }
        c.close();
		database.close();
        return generos;
    }
	
}
