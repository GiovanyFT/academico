package com.example.academico.window.adapter;

import java.util.List;

import com.example.academico.R;
import com.example.academico.domain.Genero;
import com.example.academico.util.ListRecyclerView;
import com.example.academico.window.activity.ListagemTurma;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ListagemTurmaAdapter extends ListRecyclerView<Genero> {
    private final List<Genero> generos;
    private final ListagemTurma activity;

    class ViewHolder extends RecyclerView.ViewHolder{

		@BindView(R.id.item_nome)
        TextView nome;
		@Nullable
		@BindView(R.id.item_qt_aluno)
        TextView qt_aluno;
		@OnClick(R.id.linha)
		public void clickItem(){
			Genero genero = generos.get(getAdapterPosition());
			activity.onTurmaAdapterClique(genero);
		}

		public ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
    }


    public ListagemTurmaAdapter(List<Genero> generos, ListagemTurma activity) {
		super();
		this.generos = generos;
		this.activity = activity;
	}


	@Override
	public int getItemCount() {
		return generos.size();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		ViewHolder holder;
		view = activity.getLayoutInflater().inflate(R.layout.linha_turma, parent, false);

		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		ViewHolder viewHolder = (ViewHolder)holder;

		// Obtendo a genero corrente
		Genero genero = generos.get(position);

		TextView nome = viewHolder.nome;
		nome.setText(genero.getNome());

		if(position % 2 == 0){
			viewHolder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.linha_par));
		} else {
			viewHolder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.linha_impar));
		}

			// Preenchendo a quantidade de alunos da genero
			TextView qt_aluno = viewHolder.qt_aluno;
			qt_aluno.setText(genero.getFilmes().size() + " filmes");


	}

    @Override
    public Genero getItem(int position) {
        return generos.get(position);
    }
}
