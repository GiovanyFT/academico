package com.example.academico.window.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.academico.R;
import com.example.academico.dao.FilmeDao;
import com.example.academico.dao.GerenciadorDao;
import com.example.academico.domain.Filme;
import com.example.academico.domain.Genero;
import com.example.academico.window.adapter.ListagemAlunoTurmaAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Giovany on 11/08/2015.
 */
public class ListagemAlunoTurma extends Activity {
    private List<Genero> generos;
    private List<Filme> filmes;

    ListView lista;
    private ViewGroup footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_filme_genero);

        // Obtendo os filmes com suas generos setadas
        carregarDadosListView();

        // Montando a Toolbar
        montarToolBar();

        // Fazendo aparecer a lista de generos para uma possível troca da turma
        definirComportamentoCliqueListView();
    }

    // Montando a Toolbar
    private void montarToolBar() {

        // Obtendo o inflater, ele será colocado no footer (base) do listView (lista)
        LayoutInflater inflater = getLayoutInflater();
        footer = (ViewGroup)inflater.inflate(R.layout.toolbar_listagem_filme, lista, false);
        lista.addFooterView(footer);

        // Obtendo a toolbar
        Toolbar barra_menu = (Toolbar)findViewById(R.id.tb_listagem_aluno);
        // Definindo o botão de navegação para sair da aplicação
        barra_menu.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        barra_menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inflando um menu na Toolbar
        barra_menu.inflateMenu(R.menu.menu_toolbar_listagem_aluno_turma);
        barra_menu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_ver_provas) {
                    // Cria a Activity de Provas e conteúdos (Mestre/Detalhe)
                    Intent irParaFormulario = new Intent(ListagemAlunoTurma.this, ListagemProvas.class);
                    startActivity(irParaFormulario);
                }
                return false;
            }
        });
    }

    private void carregarDadosListView() {

        // Obtendo os filmes com suas generos setadas
        // Primeiro pegamos as generos e depois os filmes de cada turma inserindo-os numa
        // lista geral. Assim, temos a lista total de filmes e cada aluno tem sua turma
        // setada
        filmes = new ArrayList<>();
        generos = GerenciadorDao.getGeneroDao().getLista();
        Iterator<Genero> itTurma = generos.iterator();
        while(itTurma.hasNext()){
            Genero genero = itTurma.next();
            List<Filme> alunosTurma = GerenciadorDao.getFilmeDao().getLista(genero);
            genero.setFilmes(alunosTurma);
            filmes.addAll(alunosTurma);
        }

        // Aqui promovemos a ordenação dos filmes pelo id (ordem pela qual foram criados)
        // indendente da turma
        Collections.sort(filmes);


        // Setando o adapter para a construção de cada linha da ListView
        lista = (ListView)findViewById(R.id.lista_filme);
        ListagemAlunoTurmaAdapter adapter = new ListagemAlunoTurmaAdapter(filmes, this);
        lista.setAdapter(adapter);
    }

    // Quando uma linha da listVeiw for clicada
    private void definirComportamentoCliqueListView() {
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> list, View view, final int position, long id) {

                // Primeiramente será gerado um vetor de todas as generos possíveis
                // (será exibido o toString de cada turma para que o usuário possa
                // idnetificá-las)
                final String[] s_turmas = new String[generos.size()];
                for(int i = 0; i< generos.size(); i++){
                    s_turmas[i] = generos.get(i).toString();
                }

                AlertDialog.Builder definidorEscolhasPossiveis = new AlertDialog.Builder(ListagemAlunoTurma.this);
                definidorEscolhasPossiveis.setTitle("Turmas");
                definidorEscolhasPossiveis.setItems(s_turmas, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // Como a ordem das generos na lista é a mesma ordem em que foram exibidas,
                        // consigo pegar a turma escolhida com facilidade, basta usar a posição
                        // selecionada no alert de generos (item) que conseguirei pegar a turma
                        // que se encontra na mesma posição em generos
                        Genero genero_escolhida = generos.get(item);
                        // Para pegar o aluno selecionado basta pegar a linha selecionada na
                        // listView (position), pois a ordem dos filmes é a mesma ordem que foram
                        // exibidos no listView
                        Filme filme_selecionado = filmes.get(position);
                        // Modifico então a turma do aluno
                        filme_selecionado.setGenero(genero_escolhida);
                        // Atualizo o aluno no banco
                        FilmeDao filmeDao = GerenciadorDao.getFilmeDao();
                        filmeDao.alterarFilme(filme_selecionado);
                        // Recarrego toda a lista
                        carregarDadosListView();

                    }
                });
                AlertDialog janelaEscolhasPossiveis = definidorEscolhasPossiveis.create();
                janelaEscolhasPossiveis.show();

            }
        });

    }
}
