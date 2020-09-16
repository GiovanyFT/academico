package com.example.academico.domain;

import java.util.List;

public class Genero extends ObjetoPersistente {
	private String nome;
	private List<Filme> filmes;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Filme> getFilmes() {
		return filmes;
	}
	public void setFilmes(List<Filme> filmes) {
		this.filmes = filmes;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
