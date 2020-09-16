package com.example.academico.window.activity;



import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.example.academico.R;
import com.example.academico.domain.Prova;
import com.example.academico.window.fragment.DetalhesProvaFragment;
import com.example.academico.window.fragment.ListaProvasFragment;

// É necessário o uso de FragmenteActivity para ter acesso ao método getSupportFragmentManager()
public class ListagemProvas extends FragmentActivity {
    // Método que determina se o layout está em landscape ou não
    private boolean isLand(){
        return getResources().getBoolean(R.bool.isLand);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provas);

        // Ao usar um FramentTransaction ou são feitas todas as trocas ou não é feito nada
        // O commit irá tentar fazer as trocas propostas
        // É importante notar que tudo é feito em função da orientação do layout (retrato ou
        // paisagem
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(isLand()){
            // Troca os FrameLayout provas_lista e  provas_detalhes
            // pelos Fragments ListaProvasFragment e DetalhesProvaFragment
            transaction
                    .replace(R.id.provas_lista, new ListaProvasFragment())
                    .replace(R.id.provas_detalhes, new DetalhesProvaFragment());
        } else {
            // Troca o FrameLayout provas_view pelo Fragment ListaProvasFragment
            transaction.replace(R.id.provas_view, new ListaProvasFragment());
        }
        transaction.commit();
    }

    /*
    Esse método tem o objetivo de, dada uma prova, montar o fragment de detalhe, ou seja,
    o fragment dos tópicos de uma prova.

    Para a orientação paisagem (land) busca-se o fragment desejado (de detalhe) e atualiza-se
    sua visualização com os dados da nova prova selecionada.

    Para a orientação retrato é feita uma atualizaçao do fragment usado na tela (R.id.provas_view),
    para a apresentação dos tópicos da prova selecionada

    A linha transaction.addToBackStack(null); é necessária para que da tela de tópicos voltemos para
    a tela de provas (com as informações sobre as provas). Sem essa linha a aplicação volta para a
    tela que acionou ListagemProvas, ou seja, ao voltar de um detalhe não conseguiríamos visualizar
    o mestre e sim quem o acionou
     */
    public void atualizarProvaSelecionada(Prova prova){
        FragmentManager manager = getSupportFragmentManager();

        if(isLand()){
            DetalhesProvaFragment detalhesProva
                    = (DetalhesProvaFragment)manager.findFragmentById(R.id.provas_detalhes);
            detalhesProva.populaCamposComDados(prova);
        } else {
            Bundle argumentos = new Bundle();
            argumentos.putSerializable("prova", prova);

            DetalhesProvaFragment detalhesProva = new DetalhesProvaFragment();
            detalhesProva.setArguments(argumentos);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.provas_view, detalhesProva);

            transaction.addToBackStack(null);

            transaction.commit();

        }
    }
}
