package com.example.academico.domain;

import java.util.ArrayList;
import java.util.List;


public class Prova extends ObjetoPersistente {

    private String data;
    private String materia;
    private String descricao;
    private List<String> topicos = new ArrayList<String>();

    public Prova(String data, String materia) {
        this.data = data;
        this.materia = materia;
    }

    @Override
    public String toString() {
        return this.materia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public List<String> getTopicos() {
        return topicos;
    }

    public void setTopicos(List<String> topicos) {
        this.topicos = topicos;
    }
}
