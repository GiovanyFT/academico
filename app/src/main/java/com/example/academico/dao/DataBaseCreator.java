package com.example.academico.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Classe usada para criar o Banco de dados e inserir os dados iniciais
public class DataBaseCreator extends SQLiteOpenHelper {
	// Constantes das tabelas que serão definidas
	protected static final String TABELA_GENERO = "GENERO";
	protected static final String TABELA_FILME = "FILME";

	// Comando de criação do banco de dados (ainda vazio)
	// O último parâmetro é muito importante, ele nos dá a versão do banco de dados
	// Vamos supor que haja uma atualização do aplicativo (com mudança no banco de dados),
	// nesse contexto, modificaríamos a versão para 2 e isso acionaria o método onUpgrade para
	// a execução do que fosse necessário para atualizar o aplicato para a nova versão de banco
	// a ser utilizada.
	// Especificamente para esse programa a mudança desse número não seria um problema, pois sempre
	// apagamos tudo e criamos novamente, mas dependendo do que se coloca no onUpgrade é possível
	// gerar inconsistências com no banco de dados
	public DataBaseCreator(Context context) {
		super(context, "db_locadora", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Criando a tabela Genero e seus dados iniciais
		database.execSQL("CREATE TABLE " + TABELA_GENERO
											+ " (id integer primary key autoincrement, "
											+   "nome text not null);");
		database.execSQL("insert into " + TABELA_GENERO
											+ " (nome) values"
											+ " ('Terror');");
		database.execSQL("insert into " + TABELA_GENERO
											+ " (nome) values"
											+ " ('Comédia');");
		database.execSQL("insert into " + TABELA_GENERO
											+ " (nome) values"
											+ " ('Romance');");
		
		// Criando a tabela Filme (notar que há uma coluna de chave estrangeira)
		database.execSQL("CREATE TABLE " + TABELA_FILME
											+ " (id integer primary key autoincrement, "
											+   "fk_turma integer, "

											+   "nome text not null, "
											+   "ano integer,"
											+   "sinopse text not null, "
											+   "caminho_foto, "
											+   "FOREIGN KEY (fk_turma) REFERENCES " + TABELA_GENERO + "(id));");
	
	}

	// Método chamado quando a uma atualização no banco de dados (lá no construtor a versão ser
	// incrementada).
	// Se a versão for decrementada o aplicativo acusará erro (não é válido fazer downgrade
	// de banco, ou seja, as versões devem sempre ser incrementadas)
	@Override
	public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
		database.execSQL("DROP TABLE IF EXISTS " + TABELA_FILME);
		database.execSQL("DROP TABLE IF EXISTS " + TABELA_GENERO);
		onCreate(database);
	}

}
