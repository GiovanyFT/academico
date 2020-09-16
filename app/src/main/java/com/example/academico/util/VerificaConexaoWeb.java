package com.example.academico.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Giovany on 19/08/2015.
 */
public class VerificaConexaoWeb {
    private Activity activity;

    public VerificaConexaoWeb(Activity activity) {
        this.activity = activity;
    }

    public  boolean verificaConexao() {
        // Obtem o gerenciador de conexão
        ConnectivityManager conectivtyManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Se a conexão está conectado
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
