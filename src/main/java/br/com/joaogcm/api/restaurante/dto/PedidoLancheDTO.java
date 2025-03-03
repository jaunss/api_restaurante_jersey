package br.com.joaogcm.api.restaurante.dto;

public class PedidoLancheDTO {

	private Integer codigoPedido;
	private Integer codigoLanche;

	public PedidoLancheDTO() {

	}

	public Integer getCodigoPedido() {
		return codigoPedido;
	}

	public void setCodigoPedido(Integer codigoPedido) {
		this.codigoPedido = codigoPedido;
	}

	public Integer getCodigoLanche() {
		return codigoLanche;
	}

	public void setCodigoLanche(Integer codigoLanche) {
		this.codigoLanche = codigoLanche;
	}
}