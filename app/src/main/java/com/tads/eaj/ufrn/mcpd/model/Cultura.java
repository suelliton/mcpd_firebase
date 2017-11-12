package com.tads.eaj.ufrn.mcpd.model;

import com.orm.SugarRecord;

public class Cultura extends SugarRecord {

	private long id;

	private String nome;

	private String descricao;

	public Cultura(long id, String nome, String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}

	public Cultura() {
	}

	@Override
	public Long getId() {
		return id;
	}



	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return "Cultura{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", descricao='" + descricao + '\'' +
				'}';
	}
}
