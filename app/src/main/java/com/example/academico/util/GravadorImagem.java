package com.example.academico.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Giovany on 07/08/2015.
 */
public class GravadorImagem {

    // Salva uma imagem no diretório padrão oculto do aplicativo
    public static String salvarImagem(Context ctx, Bitmap imagem) {
        String nomeArquivo = null;
        try {
            // O nome do arquivo será gerado pegando o tempo atual
            nomeArquivo = System.currentTimeMillis() + "";
            // A extensão será .png
            FileOutputStream out = ctx.openFileOutput(nomeArquivo + ".png",
                    Context.MODE_PRIVATE);
            // Aqui a o armazenamento
            imagem.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Esse retorno é fundamental, pois através desse nome que será possível recuperar o arquivo
        return nomeArquivo;
    }

    // Retorna a imagem como nome "nome" do diretório oculto do aplicativo
    public static Bitmap carregarImagem(Context ctx, String nome)  {
        Bitmap imagem = null;
        try{
            // Abre o arquivo
            FileInputStream in = ctx.openFileInput(nome + ".png");
            // Decodifica a imagem
            imagem = BitmapFactory.decodeStream(in);
            // Libera a entrada
            in = null;
        } catch (Exception erro){
        }
        // Retorna a imagem
        return imagem;
    }

    // Dado o nome do arquivo o exclui do diretório oculto do aplicativo
    public static void removerImagem(Context ctx, String nome){
        ctx.deleteFile(nome + ".png");
    }
}
