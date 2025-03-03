package br.com.joaogcm.api.restaurante.service;

import java.util.Set;

import br.com.joaogcm.api.restaurante.dao.PedidoDAO;
import br.com.joaogcm.api.restaurante.dto.PedidoDTO;

public class PedidoService {

	public PedidoService() {

	}

	public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
		return new PedidoDAO().criarPedido(pedidoDTO);
	}

	public PedidoDTO atualizarPedidoPorCodigo(Integer codigo, PedidoDTO pedidoDTO) {
		return new PedidoDAO().atualizarPedidoPorCodigo(codigo, pedidoDTO);
	}

	public boolean removerPedidoPorCodigo(Integer codigo) {
		return new PedidoDAO().removerPedidoPorCodigo(codigo);
	}

	public Set<PedidoDTO> listarTodosPedidos() {
		return new PedidoDAO().listarTodosPedidos();
	}

	public PedidoDTO listarPedidoPorCodigo(Integer codigo) {
		return new PedidoDAO().listarPedidoPorCodigo(codigo);
	}

	public Set<PedidoDTO> listarTodosPedidosPorLanche(Integer codigoLanche) {
		return new PedidoDAO().listarTodosPedidosPorLanche(codigoLanche);
	}
}