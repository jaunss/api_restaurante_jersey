package br.com.joaogcm.api.restaurante.service;

import java.util.Set;

import br.com.joaogcm.api.restaurante.dao.LancheDAO;
import br.com.joaogcm.api.restaurante.dto.LancheDTO;

public class LancheService {

	public LancheService() {

	}

	public LancheDTO criarLanche(LancheDTO lancheDTO) {
		return new LancheDAO().criarLanche(lancheDTO);
	}

	public LancheDTO atualizarLanchePorCodigo(Integer codigo, LancheDTO lancheDTO) {
		return new LancheDAO().atualizarLanchePorCodigo(codigo, lancheDTO);
	}

	public boolean removerLanchePorCodigo(Integer codigo) {
		return new LancheDAO().removerLanchePorCodigo(codigo);
	}

	public Set<LancheDTO> listarTodosLanches() {
		return new LancheDAO().listarTodosLanches();
	}

	public LancheDTO listarLanchePorCodigo(Integer codigo) {
		return new LancheDAO().listarLanchePorCodigo(codigo);
	}
}