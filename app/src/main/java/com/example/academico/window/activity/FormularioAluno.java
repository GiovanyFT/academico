package com.example.academico.window.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.academico.R;
import com.example.academico.dao.GerenciadorDao;
import com.example.academico.domain.Filme;
import com.example.academico.domain.Genero;
import com.example.academico.util.GravadorImagem;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Giovany on 06/08/2015.
 */
public class FormularioAluno extends Activity {

    // Constantes para determinar se foi aberta a Galeria ou foi tirada foto
    private static final int EXPLORER=1;
    private static final int TIRA_FOTO=2;

    // Componentes visuais para exibir os dados de um aluno
    private ImageView iv_foto;
    private TextView tv_nome;
    private TextView tv_sinopse;
    private TextView tv_ano;
    private TextView tv_sexo;
    private TextView tv_telefone;
    private TextView tv_email;
    private TextView tv_endereco;

    // Filme e turma selecionados
    private Filme filme_selecionado;
    private Genero genero_selecionada;

    // Imagem selecionada (será colocada em iv_foto
    private Bitmap imagem_selecionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Associando a classe ao xml formulario_filme.xml
        setContentView(R.layout.formulario_filme);

        // Busca os objetos no xml e seta nos componentes visuais (iv_foto, ..., tv_endereco)
        obterElementosFormulario();

        // Tenta obter a turma e aluno selecionados
        Intent intent = getIntent();
        genero_selecionada = (Genero)intent.getSerializableExtra("Genero");
        filme_selecionado = (Filme)intent.getSerializableExtra("Filme");
        // Se for uma atualização de aluno, ou seja, filme_selecionado != null
        // Então seta os valores nos componentes visuais
        // Se for um aluno novo não é necessário fazer nada
        if (filme_selecionado != null){
            preencherElementosFormulario(filme_selecionado);
        }

        // Define a ToolBar
        montarToolBar();

        // Definir comportamento do botão de imagem da Galeria
        definirComportamentoBotaoExplorer();

        // Definir comportamento do botão para tirar foto
        definirComportamentoBotaoFoto();

    }

    // Definir comportamento do botão de imagem da Galeria
    private void definirComportamentoBotaoExplorer() {
        // Obtem o botão de galeria (para pegar fotos)
        Button bt_galeria = (Button)findViewById(R.id.formulario_explorer_button);
        bt_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre o aplicativo de Galeria
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, EXPLORER);

            }
        });
    }

    // Definir comportamento do botão para tirar foto
    private void definirComportamentoBotaoFoto() {
        // Obtem o botão de foto (para tirar uma foto)
        Button bt_foto = (Button)findViewById(R.id.formulario_foto_button);
        bt_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chamando a camera do Android
                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(irParaCamera,TIRA_FOTO);
            }
        });
    }

    // Quando o aplicativo de Galeria ou de Foto finalizar seu trabalho esse evento será chamado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Se houve confirmação (se foi tudo ok)
        if (resultCode == Activity.RESULT_OK) {
            // Se foi o aplicativo de galeria
            if (requestCode == EXPLORER){

                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap imagem = BitmapFactory.decodeStream(imageStream);
                    imagem_selecionada = Bitmap.createScaledBitmap(imagem, iv_foto.getWidth(), iv_foto.getHeight(), true);

                    iv_foto.setImageBitmap(imagem_selecionada);
                    iv_foto.setScaleType(ImageView.ScaleType.FIT_XY);

                } catch (FileNotFoundException erro) {

                }
            // Se foi o aplicativo de Câmera
            } else if (requestCode == TIRA_FOTO){
                Bitmap imagemFoto = (Bitmap)data.getExtras().get("data");
                imagem_selecionada = Bitmap.createScaledBitmap(imagemFoto, iv_foto.getWidth(), iv_foto.getHeight(), true);

                iv_foto.setImageBitmap(imagem_selecionada);
                iv_foto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    // Obtem os elementos do xml e seta nos componentes visuais
    private void obterElementosFormulario(){
        iv_foto = (ImageView)findViewById(R.id.formulario_foto);
        tv_nome = (TextView)findViewById(R.id.formulario_nome);
        tv_sinopse = (TextView)findViewById(R.id.formulario_sinopse);
        tv_ano = (TextView)findViewById(R.id.formulario_ano);;
    }

    // Seta os valores do filme nos objetos visuais
    private void preencherElementosFormulario(Filme filme){
        // Se o filme tem foto
        if(filme.getFoto() != null){
            // Obtem a imagem
            Bitmap imagemFoto = GravadorImagem.carregarImagem(this, filme.getFoto());
            // Define uma nova imagem de tamanho reduzido
            Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, imagemFoto.getWidth(), 300, true);

            // Seta na tela a imagem reduzida
            iv_foto.setImageBitmap(imagemFotoReduzida);
            iv_foto.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        tv_nome.setText(filme.getNome());
        tv_sinopse.setText(filme.getSinopse());
        // É importante essa proteção para que idade não seja setada para 0, ou seja, se a idade
        // não for informada deve ser mantida null
        if (filme.getAno() != null)
            tv_ano.setText(filme.getAno().intValue() + "");

    }

    // Atualiza o filme_selecionado com os valores dos objetos visuais
    private void atualizarAluno(){
        // Se há uma imagem
        if (imagem_selecionada != null) {
            // Se já existia uma imagem para aquele aluno
            if (filme_selecionado.getFoto() != null) {
                // Libera a imagem do disco (delete)
                GravadorImagem.removerImagem(this, filme_selecionado.getFoto());
            }
            // Seta a imagem no disco e guarda no filme_selecionado seu caminho (na realidade só
            // o nome da imagem mesmo, pois o caminho é um diretório padrão e oculto do aplicativo
            filme_selecionado.setFoto(GravadorImagem.salvarImagem(this, imagem_selecionada));
        }
        filme_selecionado.setNome(tv_nome.getText().toString());
        filme_selecionado.setSinopse(tv_sinopse.getText().toString());
        // É importante essa proteção para que idade não seja setada para 0, ou seja, se a idade
        // não for informada deve ser mantida null
        if(tv_ano.getText().length() != 0)
            filme_selecionado.setAno(new Integer(tv_ano.getText().toString()));
        else
            filme_selecionado.setAno(null);
        filme_selecionado.setGenero(genero_selecionada);
    }

    private void montarToolBar() {
        // Obtendo o ToolBar
        Toolbar barra_menu = (Toolbar)findViewById(R.id.tb_formulario_filme);
        // Setando o icone de navegação e seu comportamento
        barra_menu.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        barra_menu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Populando o menu menu_toolbar_formulario na ToolBar
        barra_menu.inflateMenu(R.menu.menu_toolbar_formulario);

        // Definindo os comportamentos de cada um dos itens do menu
        // (só há o menu menu_salvar)
        barra_menu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_salvar) {
                    // É uma inclusão
                    if (filme_selecionado == null) {
                        filme_selecionado = new Filme(genero_selecionada);
                        atualizarAluno();
                        GerenciadorDao.getFilmeDao().inserirFilme(filme_selecionado);
                    // É uma atualização
                    } else {
                        atualizarAluno();
                        GerenciadorDao.getFilmeDao().alterarFilme(filme_selecionado);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("turma", genero_selecionada);
                    setResult(FormularioTurma.SALVOU, intent);
                    finish();
                }
                return false;
            }
        });


    }
}
