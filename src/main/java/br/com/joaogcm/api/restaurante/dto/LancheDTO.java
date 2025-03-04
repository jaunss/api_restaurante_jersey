package br.com.joaogcm.api.restaurante.dto;

import java.math.BigDecimal;

public class LancheDTO {

	private Integer codigo;
	private String nome;
	private String descricao_conteudo;
	private BigDecimal preco;

	public LancheDTO() {

	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao_conteudo() {
		return descricao_conteudo;
	}

	public void setDescricao_conteudo(String descricao_conteudo) {
		this.descricao_conteudo = descricao_conteudo;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
}