package com.example.academico.window.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
// Se a classe Fragment é importada desse pacote ela mantem a compatibilidade com versões antigas do
// Android
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import com.example.academico.R;
import com.example.academico.domain.Prova;
import com.example.academico.window.activity.ListagemProvas;


public class ListaProvasFragment extends Fragment {
    private ListView listViewProvas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtendo o layout definido no fragment (fragment_lista_provas)
        View layoutProvas = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        // Criando as provas (com seus tópicos) que serão apresentadas
        List<Prova> provas = criarListaProvas();

        // Buscando o listView e setando a lista de provas (será usado o padrão simple_list_item_1
        // para a exibição das provas
        this.listViewProvas = (ListView)layoutProvas.findViewById(R.id.lista_provas_listview);
        this.listViewProvas.setAdapter(new ArrayAdapter<Prova>(getActivity(), android.R.layout.simple_list_item_1, provas));

        // Definindo o comportamento de um clique sobre um elemento do listView (uma prova)
        this.listViewProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Prova selecionada = (Prova)adapter.getItemAtPosition(position);

                // Obtem a activity desse fragment
                ListagemProvas listagemProvas = (ListagemProvas)getActivity();
                // Solicita a essa activity para atualizar a Prova Selecionada (isso irá refazer
                // o layout da Activity, afetando os fragments internos - ListaProvasFragment e
                // DetalhesProvaFragment
                listagemProvas.atualizarProvaSelecionada(selecionada);

            }
        });

        return layoutProvas;
    }

    @NonNull
    private List<Prova> criarListaProvas() {
        // Criando 2 provas na mão e inserirndo na lista
        Prova prova1 = new Prova("20/06/2015", "Matemática");
        prova1.setTopicos(Arrays.asList("Álgebra Linear", "Cálculo", "Estatística"));

        Prova prova2 = new Prova("25/07/2015", "Português");
        prova2.setTopicos(Arrays.asList("Complemento Nominal", "Orações Subordinadas", "Análise sintática"));

        return Arrays.asList(prova1, prova2);
    }


}
