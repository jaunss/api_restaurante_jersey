package br.com.joaogcm.api.restaurante.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.com.joaogcm.api.restaurante.connection.ConexaoBancoDeDados;
import br.com.joaogcm.api.restaurante.dto.LancheDTO;

public class LancheDAO {

	public LancheDAO() {

	}

	public LancheDTO criarLanche(LancheDTO lancheDTO) {
		LancheDTO newLancheDTO = null;

		String sql = "INSERT INTO lanche (nome, descricao_conteudo, preco) VALUES (?, ?, ?)";

		try (Connection conn = ConexaoBancoDeDados.getConexao();
				PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, lancheDTO.getNome());
			ps.setString(2, lancheDTO.getDescricao_conteudo());
			ps.setBigDecimal(3, lancheDTO.getPreco());

			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					while (rs.next()) {
						int codigoGerado = rs.getInt(1);

						// objeto lancheDTO gerado
						newLancheDTO = listarLanchePorCodigo(codigoGerado);
					}
				}
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível criar o lanche: " + e.getMessage());
		}

		return newLancheDTO;
	}

	public LancheDTO atualizarLanchePorCodigo(Integer codigo, LancheDTO lancheDTO) {
		if (lancheDTO.getNome() == null && lancheDTO.getDescricao_conteudo() == null && lancheDTO.getPreco() == null) {
			throw new IllegalArgumentException(
					"Pelo menos um campo (nome, descricao_conteudo, preco) deve ser fornecido para atualização.");
		}

		String sql = "UPDATE lanche SET ";
		
		List<Object> parametros = new ArrayList<Object>();
		boolean isCampoAdicionado = false;
		
		if (lancheDTO.getNome() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " nome = ? ";
			parametros.add(lancheDTO.getNome());
			isCampoAdicionado = true;
		}

		if (lancheDTO.getDescricao_conteudo() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " descricao_conteudo = ? ";
			parametros.add(lancheDTO.getDescricao_conteudo());
			isCampoAdicionado = true;
		}

		if (lancheDTO.getPreco() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " preco = ? ";
			parametros.add(lancheDTO.getPreco());
			isCampoAdicionado = true;
		}

		sql += " WHERE codigo = ? ";
		parametros.add(codigo);

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}

			if (ps.executeUpdate() > 0) {
				return listarLanchePorCodigo(codigo);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível atualizar o lanche: " + e.getMessage());
		}
	}

	public boolean removerLanchePorCodigo(Integer codigo) {
		String sql = "DELETE FROM lanche WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível remover o lanche: " + e.getMessage());
		}
	}

	public Set<LancheDTO> listarTodosLanches() {
		Set<LancheDTO> lanchesDTO = new LinkedHashSet<LancheDTO>();

		String sql = "SELECT * FROM lanche";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					LancheDTO lancheDTO = new LancheDTO();
					lancheDTO.setCodigo(rs.getInt("CODIGO"));
					lancheDTO.setNome(rs.getString("NOME"));
					lancheDTO.setDescricao_conteudo(rs.getString("DESCRICAO_CONTEUDO"));
					lancheDTO.setPreco(rs.getBigDecimal("PRECO"));

					lanchesDTO.add(lancheDTO);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar todos os lanches: " + e.getMessage());
		}

		return lanchesDTO;
	}

	public LancheDTO listarLanchePorCodigo(Integer codigo) {
		LancheDTO lancheDTO = null;

		String sql = "SELECT * FROM lanche WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lancheDTO = new LancheDTO();
					lancheDTO.setCodigo(rs.getInt("CODIGO"));
					lancheDTO.setNome(rs.getString("NOME"));
					lancheDTO.setDescricao_conteudo(rs.getString("DESCRICAO_CONTEUDO"));
					lancheDTO.setPreco(rs.getBigDecimal("PRECO"));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar o lanche: " + e.getMessage());
		}

		return lancheDTO;
	}

	public Set<LancheDTO> listarLanchesPorPedido(Integer codigo) {
		Set<LancheDTO> lanchesDTO = new LinkedHashSet<LancheDTO>();

		String sql = "SELECT * FROM lanche l, pedido p, pedido_lanche pl " + "WHERE pl.codigo_lanche = l.codigo "
				+ "AND pl.codigo_pedido = p.codigo " + "AND p.codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					LancheDTO lancheDTO = new LancheDTO();
					lancheDTO.setCodigo(rs.getInt("CODIGO"));
					lancheDTO.setNome(rs.getString("NOME"));
					lancheDTO.setDescricao_conteudo(rs.getString("DESCRICAO_CONTEUDO"));
					lancheDTO.setPreco(rs.getBigDecimal("PRECO"));

					lanchesDTO.add(lancheDTO);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar os lanches do pedido: " + e.getMessage());
		}

		return lanchesDTO;
	}
}