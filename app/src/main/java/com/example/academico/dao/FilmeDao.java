package com.example.academico.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.academico.domain.Filme;
import com.example.academico.domain.Genero;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Dao da classe Filme
public class FilmeDao extends ObjectDao{

	public FilmeDao(DataBaseCreator creator) {
		super(creator);
	}

	// Insere um filme no banco
	public long inserirFilme(Filme filme){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("nome", filme.getNome());

		// Esse tratamento é importante porque se idade for nula não é desejavél que fique com
		// o valor 0, assim, se for null (não preenchida no layout) no banco vai ficar null
		// Se foi preenchida será setado o valor preenchido
		if (filme.getAno() == null)
			values.putNull("ano");
		else
			values.put("ano", filme.getAno().intValue());
		values.put("sinopse", filme.getSinopse());
		values.put("fk_genero", filme.getGenero().getId());
		values.put("caminho_foto", filme.getFoto());
		// Quando é feita a inserção automaticamente é retornado o id
		long key = database.insert(DataBaseCreator.TABELA_FILME, null, values);
		// Que é armazenado no filme inserido
		filme.setId(key);
		database.close();
		return key;
	}

	// Altera o filme no banco
	public void alterarFilme(Filme filme){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		ContentValues values = new ContentValues();
		long id = filme.getId();
		values.put("nome", filme.getNome());
		// Esse tratamento é importante porque se idade for nula não é desejavél que fique com
		// o valor 0, assim, se for null (não preenchida no layout) no banco vai ficar null
		// Se foi preenchida será setado o valor preenchido
		if (filme.getAno() == null)
			values.putNull("ano");
		else
			values.put("ano", filme.getAno().intValue());
		values.put("sinopse", filme.getSinopse());
		values.put("fk_genero", filme.getGenero().getId());
		values.put("caminho_foto", filme.getFoto());
		database.update(DataBaseCreator.TABELA_FILME, values, "id = ?", new String[]{String.valueOf(id)});
		database.close();
	}

	// Exlcui o filme do banco de dados
	public void excluirFilme(Filme filme){
		SQLiteDatabase database = this.getCreator().getWritableDatabase();
		long id = filme.getId();
		database.delete(DataBaseCreator.TABELA_FILME, "id = ?", new String[]{String.valueOf(id)});
		database.close();
	}
	
	// Obtem os alunos de uma genero
	public List<Filme> getLista(Genero genero) {
		SQLiteDatabase database = this.getCreator().getReadableDatabase();
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM " + DataBaseCreator.TABELA_FILME +
        			  " WHERE fk_turma= " + genero.getId() + ";" ;
        Cursor c = database.rawQuery(sql, null);
        while(c.moveToNext()){
            Filme filme = new Filme(genero);
            filme.setId(new Long(c.getInt(c.getColumnIndex("id"))));
            filme.setNome(c.getString(c.getColumnIndex("nome")));
			// Se for null não faz nada se não for nulo seta o valor
			if (!c.isNull(c.getColumnIndex("ano"))){
				filme.setAno(new Integer(c.getInt(c.getColumnIndex("ano"))));
			}
            filme.setSinopse(c.getString(c.getColumnIndex("sinopse")));
			filme.setFoto(c.getString(c.getColumnIndex("caminho_foto")));
            filmes.add(filme);
        }
        c.close();
		database.close();
        return filmes;
	}
}	


