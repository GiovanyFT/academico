package com.example.academico.domain;

public class Filme extends ObjetoPersistente {


	private String nome;
	private String sinopse;
	private Integer ano;
	private Genero genero;
	private String foto;
	
	
	public Filme(Genero genero) {
		this.genero = genero;
		this.ano = null;
	}

	public Filme() {
		this.ano = null;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSinopse() {
		return sinopse;
	}
	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Genero getGenero() {
		return genero;
	}
	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public String toString() {
		return this.nome + " - " + this.getNome();
	}
}
