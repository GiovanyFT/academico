package com.example.academico.window.activity;

import java.util.Iterator;
import java.util.List;

import com.example.academico.R;
import com.example.academico.dao.FilmeDao;
import com.example.academico.dao.GerenciadorDao;
import com.example.academico.dao.GeneroDao;
import com.example.academico.domain.Filme;
import com.example.academico.domain.Genero;
import com.example.academico.util.GravadorImagem;
import com.example.academico.window.adapter.ListagemTurmaAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

public class ListagemTurma extends Activity {

	// Lista carregada do banco e utilizada para preenchimento da interface gráfica
	private List<Genero> generos;
    // ListView que apresentará as generos na tela
	private RecyclerView lv_turma;
	// Cabeçalho da ListView, será usado para a colocação da Toolbar
    private ViewGroup header_lv_turma;

    
    // Método que determina se o layout está em landscape ou não
	// De acordo com a orientação da tela ele irá retornar verdadeiro ou falso
	// Se a orientação for retrato esse método retornará false (o arquivo bools.xml no diretório
	// values será usado)
	// Se for paisagem esse método retornará true (o arquivo bools.xml no diretório values-land
	// será usado)
    private boolean isLand(){
        return getResources().getBoolean(R.bool.isLand);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listagem_genero);

		// Criar Gerenciador Dao
		GerenciadorDao.criarGerenciadorDao(this);

		// Obtendo as generos e seus alunos
		obterTurmasAlunos();

		// Seta os dados da ListView
		carregarDadosListView();

		// Montando a ToolBar da tela
		montarToolBar();

		// Avisando que a ListView tem um menu de contexto (menu flutuante)
		registerForContextMenu(lv_turma);

		// Setando o clique sobre um item da ListView
		//definirComportamentoCliqueListView();

	    // Incluir uma nova turma
	    definirComportamentoBotaoIncluirNovaTurma();
	    
	}

	/*
							Possibilita a criação de um menu de contexto

	 A linha: registerForContextMenu(lv_turma);
	 identifica a existência de um menu de contexto associado a lv_turma, assim, quando uma
	 linha da ListView lv_turma for pressionada por um tempo (curto intervalo) será aberto
	 o menu de contexto
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		// Obtêm-se a turma selecionada a partir da linha clicada em lv_turma
		ListagemTurmaAdapter turmaAdapter = (ListagemTurmaAdapter)lv_turma.getAdapter();
		final Genero genero_selecionada = (Genero) turmaAdapter.getItem(info.position);

		// A ListView começa os dados em 1 e não em 0, como as listas (List) começam em 0
		// é necessário fazer esse ajuste
		final int posicao = info.position-1;

		// Criando o menu de exclusão e seu comportamento
		MenuItem excluir = menu.add("Excluir");
		definirComportamentoExcluir(genero_selecionada, excluir, posicao);

	}

	// Define o que será implementado quando o usuário clicar sobre um item da ListView lv_turma e
	// selecionar o menu "Excluir"
	private void definirComportamentoExcluir(final Genero genero_selecionada, MenuItem excluir, final int posicao) {
		excluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Definindo um AlertDialog.Builder para construir um alert de opções para o usuário
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListagemTurma.this);
				alertBuilder.setTitle("Excluir");
				alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
				alertBuilder.setMessage("Deseja mesmo excluir? Isso irá apagar a turma e todos os seus alunos");
				// Comportamento para quando o usuário marca "Sim", ou seja, deseja mesmo exluir
				// a turma
				alertBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						List<Filme> filmes = genero_selecionada.getFilmes();
						FilmeDao filmeDao = GerenciadorDao.getFilmeDao();
						Filme filme;
						int qt_aluno = filmes.size();
						// Excluindo cada um dos filmes da turma, se o filme tiver uma foto
						// armazenada ela é apagada (para liberar espaço)
						for (int i = 0; i < qt_aluno; i++) {
							filme = filmes.remove(0);
							filmeDao.excluirFilme(filme);
							if (filme.getFoto() != null)
								GravadorImagem.removerImagem(ListagemTurma.this, filme.getFoto());
						}
						// Após excluir os filmes é possível excluir a turma
						GeneroDao generoDao = GerenciadorDao.getGeneroDao();
						generoDao.excluirGenero(genero_selecionada);
						generos.remove(posicao);
						carregarDadosListView();
					}
				});
				// Se o usuário escolher "Não" nada será feito
				alertBuilder.setNegativeButton("Não", null);
				alertBuilder.show();

				return false;
			}
		});

	}

	// Define o que será feito quando houver um clique "rápido" sobre uma linha da ListView
/*	private void definirComportamentoCliqueListView() {
		lv_turma.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				// Obtem-se a turma selecionada e chama-se a janela de edição de turma
				// passando a turma como "extra"
				Genero turma = (Genero) list.getItemAtPosition(position);
				Intent irParaFormulario = new Intent(ListagemTurma.this, FormularioTurma.class);
				irParaFormulario.putExtra("Genero", turma);
				startActivity(irParaFormulario);
			}
		});
	}
	*/


	public void onTurmaAdapterClique(Genero genero){
		Intent irParaFormulario = new Intent(ListagemTurma.this, FormularioTurma.class);
		irParaFormulario.putExtra("Genero", genero);
		startActivity(irParaFormulario);
	}

	// Definine o que será feito quando o usuário clica no botão "+"
	private void definirComportamentoBotaoIncluirNovaTurma() {
		Button bt_incluir_turma = (Button)findViewById(R.id.incluir_turma_floating_button);
	    bt_incluir_turma.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// A janela de edição de turma é acionada
				Intent irParaFormulario = new Intent(ListagemTurma.this, FormularioTurma.class);
				startActivity(irParaFormulario);

			}
		});
	}

	// Seta a lista de generos (generos) na ListView (lv_turma)
	private void carregarDadosListView() {
        // Setando o adapter para a construção de cada linha da ListView
		lv_turma = (RecyclerView)findViewById(R.id.lista_turma);
		ListagemTurmaAdapter adapter = new ListagemTurmaAdapter(generos, this);
        lv_turma.setAdapter(adapter);
		lv_turma.setLayoutManager(new GridLayoutManager(this, 3));//new LinearLayoutManager(this));
	}

	// Obtem as generos e seus alunos do banco
	private void obterTurmasAlunos() {
		generos = GerenciadorDao.getGeneroDao().getLista();
		Iterator<Genero> itTurma = generos.iterator();
		while(itTurma.hasNext()){
			Genero genero = itTurma.next();
			genero.setFilmes(GerenciadorDao.getFilmeDao().getLista(genero));
		}
	}

	// Define a ToolBar da tela
	// A Toolbar nessa tela será colocada no header da ListView (lv_turma)
	private void montarToolBar() {
		
        // Setando o Toolbar no aplicativo via LayoutInflater
		// A Toolbar ficará no header da ListView
      //  LayoutInflater inflater = getLayoutInflater();
	 //   header_lv_turma = (ViewGroup)inflater.inflate(R.layout.toolbar_listagem_genero, lv_turma, false);
	//    lv_turma.addHeaderView(header_lv_turma);
	    
		// Obtendo o ToolBar
	    Toolbar barra_menu = (Toolbar)findViewById(R.id.tb_principal);
	    barra_menu.setTitle("Listagem de Turmas");
	    barra_menu.setTitleTextColor(Color.WHITE);
	    
	    // No formato retrato aparece o subtítulo
		if (!isLand()){
		    barra_menu.setSubtitle("Nome e Curso");
		    barra_menu.setSubtitleTextColor(Color.WHITE);	    	
	    }

		// Definindo o botão de navegação para sair da aplicação
		barra_menu.setNavigationIcon(R.drawable.ic_file_excel_box_white_24dp);
		barra_menu.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Definido os demais menus e suas implementações quando acionados
		barra_menu.inflateMenu(R.menu.menu_toolbar_listagem_turma);
		barra_menu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				// A clicar no menu "Buscar Alunos" será acionada a tela ListagemAlunoTurma
				if (id == R.id.menu_buscar_alunos) {
					Intent irParaFormulario = new Intent(ListagemTurma.this, ListagemAlunoTurma.class);
					startActivity(irParaFormulario);
				}
				return false;
			}
		});
	}
    
    
    @Override
	protected void onRestart() {
		super.onRestart();
		// Obtendo as generos e seus alunos
		obterTurmasAlunos();
		// Reconstruindo o ListView
		this.carregarDadosListView();
	}

}
