package com.example.academico.window.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.academico.R;
import com.example.academico.domain.Genero;
import com.example.academico.util.VerificaConexaoWeb;
import com.example.academico.window.fragment.MapaFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Giovany on 18/08/2015.
 */
public class MapaAlunos extends FragmentActivity implements OnMarkerClickListener{
    private Genero genero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Há apenas um framelayout em mapa_alunos, esse framelayout será substituído por um
        // fragment e o mapa com os alunos será apresentado
        setContentView(R.layout.mapa_alunos);

        // Recebendo a genero
        Intent intent = getIntent();
        genero = (Genero) intent.getSerializableExtra("Genero");

        // Classe criada para verificar se há conexão com a web
        VerificaConexaoWeb conexaoWeb = new VerificaConexaoWeb(this);
        // Se houver, chama o fragment de mapas para preencher a tela
        if (conexaoWeb.verificaConexao()){
            // Criando o Fragment e passando a genero como parâmetro
            MapaFragment mapaFragment = new MapaFragment();
            // Passando a genero como parâmetro para o fragment de mapas
            Bundle argumentos = new Bundle();
            argumentos.putSerializable("Genero", genero);
            mapaFragment.setArguments(argumentos);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mapa_alunos, mapaFragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(this, "Conexão de internet DESLIGADA", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        String name= marker.getTitle();

        if (name.equals("IFES - Campus Colatina"))
        {
            Intent mostrarSite = new Intent(Intent.ACTION_VIEW);
            String site = "www.ifes.edu.br";
            if(!site.startsWith("http://")){
                site = "http://" + site;
            }
            mostrarSite.setData(Uri.parse(site));
            startActivity(mostrarSite);
            return true;
        }
        // Com o false o Map exibe as informações básicas
        return false;

    }
}
