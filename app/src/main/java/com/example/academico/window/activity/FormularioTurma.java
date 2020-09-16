package com.example.academico.window.activity;

import com.example.academico.R;
import com.example.academico.dao.GerenciadorDao;
import com.example.academico.domain.Genero;
import com.example.academico.util.FilmeSONConverter;
import com.example.academico.window.fragment.ListagemAlunoFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


// Para usar getSupportFragmentManager() é necessário herdar de FragmentActivity
public class FormularioTurma extends FragmentActivity {
	// TextView com os campos de uma turma
	private TextView tv_nome;

	// Genero selecionada na tela anterior (em caso de edição)
	// caso seja uma inclusão esse atributo se tornará null
	private Genero genero_selecionada;

	// "Constante" usada para definir que houve um salvamento (insert ou update)
	public static int SALVOU = 1;

	// Fragment para apresentar a listagem de alunos
	ListagemAlunoFragment alunosFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setando o xml que será usado. É importante notar a existência do arquivo
		// formulario_generoo.xml tanto na pasta layout quanto na pasta layout_land
		setContentView(R.layout.formulario_genero);

		// Seta os elementos do formulário buscando cada um deles pelo seu id
		obterElementosFormulario();

		// Obtendo a turma selecionada (se inclusão a turma será null)
		Intent intent = getIntent();
		genero_selecionada = (Genero) intent.getSerializableExtra("Genero");

		// Monta a ToolBar
		montarToolBar();

		// Monta o fragment que contem a listagem de alunos
		criarFragmentAlunos();
	}

	// Método acionado quando uma Activity acionada por essa dá um retorno, nesse caso a Activity
	// FormularioAluno. O aluno pode ter sido salvo nesse contexto onActivityResult do fragment
	// é acionado para atualizar a listagem de alunos
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FormularioTurma.SALVOU) {
			alunosFragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	// Método usado para desenhar e dar comportamento a Toolbar R.id.tb_formulario_turma
	private void montarToolBar() {
		
		// Obtendo o ToolBar
	    Toolbar barra_menu = (Toolbar)findViewById(R.id.tb_formulario_genero);
		// Definindo o icone do menu de navegação e seu comportamento (finalizar a tela)
		barra_menu.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
		barra_menu.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Aqui "inflamos" o menu para que ele apareça na Toolbar
	    barra_menu.inflateMenu(R.menu.menu_toolbar_formulario_turma);

		// Nesse método implementamos o que cada opção de menu deve fazer
	    barra_menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				// Foi clicada a opção de salvar a turma
				if (id == R.id.menu_salvar_turma) {
					// É uma inclusão
					if (genero_selecionada == null) {
						genero_selecionada = new Genero();
						atualizarTurma();
						GerenciadorDao.getGeneroDao().inserirGenero(genero_selecionada);
					}
					// É uma atualização
					else {
						atualizarTurma();
						GerenciadorDao.getGeneroDao().alterarGenero(genero_selecionada);
					}
					// Após salvar voltamos a tela anterior
					finish();
				// Se for clicada opção para incluir um novo aluno na turma
				} else if (id == R.id.menu_incluir_aluno) {
					Intent irParaFormulario = new Intent(FormularioTurma.this, FormularioAluno.class);
					// Genero ainda não salva
					// Notar que uma turma não salva (só no visual) será salva obrigatoriamente
					// quando o aluno tentar incluir o primeiro aluno
					if (genero_selecionada == null) {
						// Cria a turma, seta seus valores e salva no banco
						genero_selecionada = new Genero();
						atualizarTurma();
						GerenciadorDao.getGeneroDao().inserirGenero(genero_selecionada);
					}
					// Envia a turma para o formulário de aluno para que seja possível salvar o aluno
					// (para salvar o aluno precisamos do id da turma)
					irParaFormulario.putExtra("Genero", genero_selecionada);
					startActivityForResult(irParaFormulario, FormularioTurma.SALVOU);
				} else if (id == R.id.menu_mostar_json_aluno) {
					// Cria um objeto para converter uma lista de alunos numa String formatada (JSON)
					FilmeSONConverter alunoJSON = new FilmeSONConverter();
					String sJSON = alunoJSON.toJSON(GerenciadorDao.getFilmeDao().getLista(genero_selecionada));

					// Define e desenha a tela com o JSON criado
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FormularioTurma.this);
					alertBuilder.setTitle("JSON dos alunos da turma");
					alertBuilder.setIcon(android.R.drawable.ic_dialog_info);
					// Nessa linha que o texto de sJSON é apresentado
					alertBuilder.setMessage(sJSON);
					// Não vai ser dado nehum comportamento a esse alert
					alertBuilder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					alertBuilder.show();
				} else if (id == R.id.menu_mostrar_alunos_mapa) {
					// Cria uma Activity de Mapa de Alunos com a turma selecionada e apresenta
					// no mapa os alunos da turma selecionada
					Intent irParaFormulario = new Intent(FormularioTurma.this, MapaAlunos.class);
					irParaFormulario.putExtra("Genero", genero_selecionada);
					startActivity(irParaFormulario);
				}
				return false;
			}
		});


	}
	
	// Método usado para pegar os objetos do xml e setar nos atributos da classe
	private void obterElementosFormulario(){
		tv_nome = (TextView)findViewById(R.id.formulario_nome);
	}

	// Método usado para atualizar os atributos visuais com os dados da genero informada
	private void preencherElementosFormulario(Genero genero){
		tv_nome.setText(genero.getNome());

	}

	// Método que atualiza a turma selecionada com os dados apresentados na janela
	private void atualizarTurma(){
		genero_selecionada.setNome(tv_nome.getText().toString());
	}

	// Método que define o fragment de alunos
	private void criarFragmentAlunos() {
		// Criando o Fragment que substituirá parte da tela
		alunosFragment = new ListagemAlunoFragment();

		// Preenchendo (atualização) os elementos do formulário
		// com a turma recebida
		if (genero_selecionada != null){
            preencherElementosFormulario(genero_selecionada);

            // Passando argumentos para o fragment que será criado
            Bundle argumentos = new Bundle();
            argumentos.putSerializable("turma", genero_selecionada);
            alunosFragment.setArguments(argumentos);
        }

		// Trocando o FrameLayout da tela - framelayout_lista_alunos_turma pelo Fragment ListagemAlunoFragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.framelayout_lista_alunos_turma, alunosFragment);
		transaction.commit();
	}

}
