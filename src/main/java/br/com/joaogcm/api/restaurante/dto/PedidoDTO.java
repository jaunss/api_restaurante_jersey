package br.com.joaogcm.api.restaurante.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PedidoDTO {

	private Integer codigo;
	private LocalDateTime dataPedido;
	private ClienteDTO cliente;
	private BigDecimal total;
	private String observacao;
	private Set<LancheDTO> lanches = new HashSet<LancheDTO>();

	public PedidoDTO() {

	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public LocalDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Set<LancheDTO> getLanches() {
		return lanches;
	}

	public void setLanches(Set<LancheDTO> lanches) {
		this.lanches = lanches;
	}
}