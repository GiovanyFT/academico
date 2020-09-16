package com.example.academico.window.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
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
import com.example.academico.util.GravadorImagem;
import com.example.academico.window.activity.FormularioAluno;
import com.example.academico.window.activity.FormularioTurma;
import com.example.academico.window.adapter.ListagemAlunoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giovany on 06/08/2015.
 */
public class ListagemAlunoFragment extends Fragment {

    private ListView listViewAlunos;
    private Genero genero;
    ListagemAlunoAdapter adapter;
    List<Filme> filmes;


    // Método acionado na criação do Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Carregar os filmes da genero selecionada e setar no ListView para a exibição
        return carregarListaAlunosTurma(inflater, container);
    }

    // Método acionado quando o formulário de aluno é fechado, ou seja, quando ocorreu alteração
    // é necessário recarregar a lista de filmes e atualizar a listView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == FormularioTurma.SALVOU) {
            this.genero = (Genero)data.getExtras().getSerializable("genero");
            carregarDadosListView();
        }
    }

    // Recarregando a listView de filmes
    private void carregarDadosListView() {
        filmes = GerenciadorDao.getFilmeDao().getLista(this.genero);
        adapter = new ListagemAlunoAdapter(filmes, getActivity());
        this.listViewAlunos.setAdapter(adapter);
    }

    @NonNull
    private View carregarListaAlunosTurma(LayoutInflater inflater, ViewGroup container) {
        // Obtendo a genero e seus filmes
        // Se existe genero
        if(getArguments() != null){
            this.genero = (Genero)getArguments().getSerializable("genero");
            filmes = GerenciadorDao.getFilmeDao().getLista(this.genero);
        // Se a genero não foi informada (ainda não salva no banco)
        // Obviamente se a genero ainda não foi salva não tem filmes no banco (chave estrangeira)
        } else {
            filmes = new ArrayList<>();
        }

        // Montando o listView de filmes
        adapter = new ListagemAlunoAdapter(filmes, getActivity());
        View layoutAlunos = inflater.inflate(R.layout.fragment_listagem_filme_genero, container, false);
        this.listViewAlunos = (ListView)layoutAlunos.findViewById(R.id.fragment_lista_alunos_listview);
        this.listViewAlunos.setAdapter(adapter);

        // Definindo o comportamento do clique sobre um item do listView
        this.listViewAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                // Obtem o aluno e genero selecioandos
                Filme filme_selecionado = (Filme)adapter.getItemAtPosition(position);
                Genero genero_selecionada = filme_selecionado.getGenero();

                // Abre o formulário de aluno passando o aluno e sua genero
                Intent irParaFormulario = new Intent(getActivity(), FormularioAluno.class);
                irParaFormulario.putExtra("Filme", filme_selecionado);
                irParaFormulario.putExtra("Genero", genero_selecionada);
                startActivityForResult(irParaFormulario, FormularioTurma.SALVOU);
            }
        });

        // Avisando que a ListView tem um menu de contexto (menu flutuante)
        registerForContextMenu(listViewAlunos);

        return layoutAlunos;
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        final Filme filme_selecionado = (Filme) listViewAlunos.getAdapter().getItem(info.position);

        // Criando o menu de envio de email e seu coportamento
        MenuItem email = menu.add("Enviar email");
        definirComportamentoEnviarEmail(filme_selecionado, email);

        // Definindo o comportamento de SM

        // Abrir site do IFES
        MenuItem site = menu.add("Abrir site do IFES");
        abrirSiteIFES(site);

        // Criando o menu de exclusão e seu comportamento
        MenuItem excluir = menu.add("Excluir");
        definirComportamentoExcluir(filme_selecionado, excluir);

    }

    // Método usado para chamar o aplicativo de email
    private void definirComportamentoEnviarEmail(Filme filme_selecionado, MenuItem email) {
        Intent intentmail = new Intent(Intent.ACTION_SEND);
        intentmail.setType("message/rfc822");
        // Definindo o email do destinatário
        // As próximas 2 linhas não são necessárias, estão presentes apenas para mostrar como
        // setar um texto no assunto e no corpo de email
        intentmail.putExtra(Intent.EXTRA_SUBJECT, "");
        intentmail.putExtra(Intent.EXTRA_TEXT, "");
        // Abre o aplicativo de email
        email.setIntent(intentmail);
    }

    // Método usado para chamar o aplicativo de SMS

    // Método utilizado para excluir um aluno
    private void definirComportamentoExcluir(final Filme filme_selecionado, MenuItem excluir) {
        excluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("Excluir");
                alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                alertBuilder.setMessage("Deseja mesmo excluir o aluno selecionado? ");
                alertBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtendo filmeDao e excluindo o aluo selecionado do banco
                        FilmeDao filmeDao = GerenciadorDao.getFilmeDao();
                        filmeDao.excluirFilme(filme_selecionado);
                        // Se ele tinha foto, apague a foto registrada no aplicativo
                        if (filme_selecionado.getFoto() != null)
                            GravadorImagem.removerImagem(getActivity(), filme_selecionado.getFoto());
                        // Recarregando a listView de filmes
                        carregarDadosListView();
                    }
                });
                alertBuilder.setNegativeButton("Não", null);
                alertBuilder.show();

                return false;
            }
        });
    }

    // Método usado para acionar o navegador com o endereço informado (site)
    private void abrirSiteIFES(MenuItem msite) {
        Intent mostrarSite = new Intent(Intent.ACTION_VIEW);
        String site = "www.ifes.edu.br";
        if(!site.startsWith("http://")){
            site = "http://" + site;
        }
        mostrarSite.setData(Uri.parse(site));
        msite.setIntent(mostrarSite);
    }
}
