package com.example.academico.window.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.academico.R;
import com.example.academico.dao.GerenciadorDao;
import com.example.academico.domain.Filme;
import com.example.academico.domain.Genero;
import com.example.academico.util.GravadorImagem;
import com.example.academico.util.Localizador;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Giovany on 18/08/2015.
 */
public class MapaFragment extends SupportMapFragment  {
    List<Filme> filmes;

    // Evento chamado após a criação do fragment
    @Override
    public void onResume() {
        super.onResume();

        // Coloca o IFES no mapa
        setIFESOnMap();

        // Obtem a genero e seus filmes
        Genero genero = (Genero)getActivity().getIntent().getExtras().getSerializable("Genero");
        filmes = GerenciadorDao.getFilmeDao().getLista(genero);

        // Colocando os filmes no mapa
        Iterator<Filme> iterator = filmes.iterator();
        while(iterator.hasNext()){
            setAlunoOnMap(iterator.next());
        }

        // Tratando o clique sobre marcações no map
        this.getMap().setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this.getActivity());
    }

    private void setAlunoOnMap(Filme filme){
        GoogleMap mapa = getMap();

        // Crio um maker para setar as opções que aparecerão no mapa
        MarkerOptions opcoes = new MarkerOptions();
        opcoes.title(filme.getNome());
        // Icone usado no mapa
        Bitmap bm;
        if(filme.getFoto() != null){
            bm = GravadorImagem.carregarImagem(getActivity(), filme.getFoto());
        } else {
            bm = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_no_image);
        }
        Bitmap bm_reduzido = Bitmap.createScaledBitmap(bm, 50, 50, true);
        // Setando a imagem como ícone a ser usado no mapa
        opcoes.icon(BitmapDescriptorFactory.fromBitmap(bm_reduzido));
        // Craindo um localizador
        Localizador local = new Localizador(getActivity());
        // Obtendo as coordenadas de um filme a partid do seu endereço
        // Se não for nulo (endereço encontrado), adiciona no map
    }

    private void setIFESOnMap() {
        GoogleMap mapa = getMap();

        // Crio um maker para setar as opções que aparecerão no mapa
        MarkerOptions opcoes = new MarkerOptions();
        opcoes.title("IFES - Campus Colatina");
        opcoes.snippet("Instituto Fedederal do Espírito Santo - Colatina");
        // Icone usado no mapa
        opcoes.icon(BitmapDescriptorFactory.fromResource(R.drawable.ifes));
        // Craindo um localizador
        Localizador local = new Localizador(getActivity());
        // Obtendo as coordenadas do endereço do IFES-Colatina
        LatLng latLng = local.getCoordenada("Avenida Neves Lima de Souza 1700, Santa Margarida, Colatina");
        if (latLng != null) {
            opcoes.position(latLng);
            centralizarNo(latLng, mapa);
            mapa.addMarker(opcoes);
        }


    }

    // Centraliza a camera do mapa nas coordenadas informadas com um zoom de 11
    private void centralizarNo(LatLng latLng, GoogleMap mapa) {
        // 11 é o zoom da camera
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

    }



}
