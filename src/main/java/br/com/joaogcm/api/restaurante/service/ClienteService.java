package br.com.joaogcm.api.restaurante.service;

import java.util.Set;

import br.com.joaogcm.api.restaurante.dao.ClienteDAO;
import br.com.joaogcm.api.restaurante.dto.ClienteDTO;

public class ClienteService {

	public ClienteService() {

	}

	public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
		return new ClienteDAO().criarCliente(clienteDTO);
	}

	public ClienteDTO atualizarClientePorCodigo(Integer codigo, ClienteDTO clienteDTO) {
		return new ClienteDAO().atualizarClientePorCodigo(codigo, clienteDTO);
	}

	public boolean removerClientePorCodigo(Integer codigo) {
		return new ClienteDAO().removerClientePorCodigo(codigo);
	}

	public Set<ClienteDTO> listarTodosClientes() {
		return new ClienteDAO().listarTodosClientes();
	}

	public ClienteDTO listarClientePorCodigo(Integer codigo) {
		return new ClienteDAO().listarClientePorCodigo(codigo);
	}
}