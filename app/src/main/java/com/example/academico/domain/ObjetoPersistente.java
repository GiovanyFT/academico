package com.example.academico.domain;

import java.io.Serializable;

/***************************************************************
    Por padrão os objetos de domínio devem herdar de
    ObjetoPersistente para já ter um id (usado com chave
    primária auto-increment no banco de dados)

    Por padrão Objetos Persistentes são serializavés e são
    comparados pelo id (quando for necessário usar ordenação)
 ***************************************************************/
public abstract class ObjetoPersistente implements Serializable, Comparable<ObjetoPersistente> {
	// A opção por usar a classe Long se deve ao fato da possibilidade de termos id nulo, ou seja,
	// ainda não setado (quando um objeto é novo). Quando trabalhomos direto com o long, não existe
	// a possibilidade do null pois ele é um tipo primitivo e não uma classe, assim, se quiséssemos
	// usar long seria necessário determinar um valor para ter a semantica de null (-1 por exemplo)
	// isso prejudicaria a legibilidade do código
	private Long id=null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(ObjetoPersistente another) {
		return id.compareTo(another.id);
	}
}
