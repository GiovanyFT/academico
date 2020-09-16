package com.example.academico.util;

import org.json.JSONException;
import org.json.JSONStringer;
import com.example.academico.domain.Filme;

import java.util.List;

public class FilmeSONConverter {

    // Dada uma lista de filmes retorna uma String no padrão JSON
    public String toJSON(List<Filme> filmes){
        try{
            // Cria o objeto JSON
            JSONStringer jsonStringer = new JSONStringer();
            // Acrescenta a chave de lista e aluno (para dizer que é uma lista de filmes)
            jsonStringer.object().key("list").array().object().key("aluno").array();
            for(Filme filme : filmes){
                // Acrescenta cada um dos filmes no JSON
                JSONStringer objetoPessoa = jsonStringer.object();
                objetoPessoa.key("id").value(filme.getId());
                objetoPessoa.key("nome").value(filme.getNome());
                objetoPessoa.key("matricula").value(filme.getSinopse());
                objetoPessoa.key("idade").value(filme.getAno());
                objetoPessoa.key("turma").value(filme.getGenero().getId());
                objetoPessoa.key("caminho_foto").value(filme.getFoto());
                objetoPessoa.endObject();
            }
            // Fecha a lista de filmes e retorna a string no padrão JSON gerada
            return jsonStringer.endArray().endObject().endArray().endObject().toString();

        } catch (JSONException e){
            throw  new RuntimeException(e);
        }
    }
}
