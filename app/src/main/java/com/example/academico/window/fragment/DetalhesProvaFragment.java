package com.example.academico.window.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.academico.R;
import com.example.academico.domain.Prova;


public class DetalhesProvaFragment extends Fragment {

    private Prova prova;

    private TextView materia;
    private TextView data;
    private ListView topicos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Obtendo o layout definido no fragment (fragment_detalhes_prova)
        View layout = inflater.inflate(R.layout.fragment_detalhes_prova, container, false);

        // Valiação feita por segurança
        if(getArguments() != null){
            this.prova = (Prova)getArguments().getSerializable("prova");
        }

        // Buscando os objetos do fragment
        buscarComponentes(layout);
        // Preenchendo os dados do fragment
        populaCamposComDados(this.prova);
        return layout;
    }

    // Busca os componentes visuais do fragment
    private void buscarComponentes(View layout){
        this.materia = (TextView)layout.findViewById(R.id.detalhe_prova_materia);
        this.data = (TextView)layout.findViewById(R.id.detalhe_prova_data);
        this.topicos = (ListView)layout.findViewById(R.id.detalhe_prova_topicos);
    }

    public void populaCamposComDados(Prova prova){
        if(prova!=null){
            // Seta o cabeçalho
            this.materia.setText(prova.getMateria());
            this.data.setText(prova.getData());

            // Seta o conteúdo (tópicos da prova)
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                    prova.getTopicos());
            this.topicos.setAdapter(adapter);
        }
    }
}
