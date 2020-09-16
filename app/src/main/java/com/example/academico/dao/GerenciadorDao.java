package com.example.academico.dao;

import android.content.Context;

/**
 * Created by Giovany on 06/08/2015.
 */
// Classe criada para gerenciar os Dao da aplicação
public class GerenciadorDao {
    private static GeneroDao generoDao;
    private static FilmeDao filmeDao;

    // Na primeira activity da aplicação deve-se chamar esse método para garantir a existência
    // de todos os Daos e do próprio banco de dados
    public static void criarGerenciadorDao(Context context){
        // Criando o database e os DAOs
        DataBaseCreator creator = new DataBaseCreator(context);
        generoDao = new GeneroDao(creator);
        filmeDao = new FilmeDao(creator);
    }

    public static GeneroDao getGeneroDao() {
        return generoDao;
    }

    public static FilmeDao getFilmeDao() {
        return filmeDao;
    }

}
