package com.example.academico.window.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.academico.R;
import com.example.academico.domain.Filme;
import com.example.academico.util.GravadorImagem;

import java.util.List;

/**
 * Created by Giovany on 06/08/2015.
 */
public class ListagemAlunoAdapter extends BaseAdapter {
    private final List<Filme> filmes;
    private final Activity activity;


    public ListagemAlunoAdapter(List<Filme> filmes, Activity activity) {
        super();
        this.filmes = filmes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return filmes.size();
    }

    @Override
    public Object getItem(int position) {
        return filmes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filmes.get(position).getId();
    }

    // Método usado para definir uma linha com informações de um aluno
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtendo o layout de uma linha (representa um filme)
        View view = activity.getLayoutInflater().inflate(R.layout.linha_filme, parent, false);

        // Obtendo o filme corrente
        Filme filme = filmes.get(position);

        // Setando os elementos da linha com os valores do filme corrente (que está sendo montado o layout)
        TextView nome = (TextView)view.findViewById(R.id.item_filme_nome);
        nome.setText(filme.getNome());

        // Setando a imagem em tamanho reduzido através dos dados do filme
        Bitmap bm;
        if(filme.getFoto() != null){
            // Se o filme tem imagem pegar a imagem gravada
            bm = GravadorImagem.carregarImagem(activity, filme.getFoto());
        } else {
            // Se não tem usar a imagem padrão ic_no_image
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
        }
        Bitmap bm_reduzido = Bitmap.createScaledBitmap(bm, 75, 75, true);
        // Desalocando o objeto Bitmap (liberação de memória)
        bm.recycle();


        ImageView imagem = (ImageView)view.findViewById(R.id.item_filme_foto);
        imagem.setImageBitmap(bm_reduzido);

        return view;
    }

}
