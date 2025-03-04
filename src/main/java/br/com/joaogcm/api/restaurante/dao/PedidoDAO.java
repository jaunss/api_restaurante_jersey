package br.com.joaogcm.api.restaurante.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import br.com.joaogcm.api.restaurante.connection.ConexaoBancoDeDados;
import br.com.joaogcm.api.restaurante.dto.ClienteDTO;
import br.com.joaogcm.api.restaurante.dto.LancheDTO;
import br.com.joaogcm.api.restaurante.dto.PedidoDTO;

public class PedidoDAO {

	public PedidoDAO() {

	}

	public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
		PedidoDTO newPedidoDTO = null;

		String sql = "INSERT INTO pedido (data_pedido, cliente_id, total, observacao) VALUES (?, ?, ?, ?)";
		String sql2 = "INSERT INTO pedido_lanche (codigo_pedido, codigo_lanche) VALUES (?, ?)";

		try (Connection conn = ConexaoBancoDeDados.getConexao();
				PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setTimestamp(1, Timestamp.valueOf(pedidoDTO.getDataPedido()));
			ps.setInt(2, pedidoDTO.getCliente().getCodigo());
			ps.setBigDecimal(3, pedidoDTO.getTotal());
			ps.setString(4, pedidoDTO.getObservacao());

			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					while (rs.next()) {
						int codigoGerado = rs.getInt(1);

						if (!pedidoDTO.getLanches().isEmpty()) {
							try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
								for (LancheDTO lancheDTO : pedidoDTO.getLanches()) {
									ps2.setInt(1, codigoGerado);
									ps2.setInt(2, lancheDTO.getCodigo());

									ps2.addBatch();
								}

								ps2.executeBatch();
							} catch (SQLException e) {
								conn.rollback();
								throw new RuntimeException(
										"Não foi possível adicionar os lanches ao pedido: " + e.getMessage());
							}
						}

						newPedidoDTO = listarPedidoPorCodigo(codigoGerado);
					}
				}
			} else {
				conn.rollback();
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível criar o pedido: " + e.getMessage());
		}

		return newPedidoDTO;
	}

	public PedidoDTO atualizarPedidoPorCodigo(Integer codigo, PedidoDTO pedidoDTO) {
		String sql = " UPDATE pedido SET ";

		List<Object> parametros = new ArrayList<Object>();
		boolean isCampoAdicionado = false;

		if (pedidoDTO.getDataPedido() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " data_pedido = ? ";
			parametros.add(Timestamp.valueOf(pedidoDTO.getDataPedido()));
			isCampoAdicionado = true;
		}

		if (pedidoDTO.getCliente() != null && pedidoDTO.getCliente().getCodigo() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " cliente_id = ? ";
			parametros.add(pedidoDTO.getCliente().getCodigo());
			isCampoAdicionado = true;
		}

		if (pedidoDTO.getTotal() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " total = ? ";
			parametros.add(pedidoDTO.getTotal());
			isCampoAdicionado = true;
		}

		if (pedidoDTO.getObservacao() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " observacao = ? ";
			parametros.add(pedidoDTO.getObservacao());
			isCampoAdicionado = true;
		}

		sql += " WHERE codigo = ? ";
		parametros.add(codigo);

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}

			if (ps.executeUpdate() > 0) {
				return listarPedidoPorCodigo(codigo);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível atualizar o pedido: " + e.getMessage());
		}
	}

	public boolean removerPedidoPorCodigo(Integer codigo) {
		String sql = "DELETE FROM pedido WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível remover o pedido: " + e.getMessage());
		}
	}

	public Set<PedidoDTO> listarTodosPedidos() {
		Set<PedidoDTO> pedidosDTO = new LinkedHashSet<PedidoDTO>();

		String sql = "SELECT * FROM pedido";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					PedidoDTO pedidoDTO = new PedidoDTO();
					pedidoDTO.setCodigo(rs.getInt("CODIGO"));
					pedidoDTO.setDataPedido(rs.getTimestamp("DATA_PEDIDO").toLocalDateTime());

					ClienteDTO clienteDTO = new ClienteDTO();
					clienteDTO.setCodigo(rs.getInt("CLIENTE_ID"));
					pedidoDTO.setCliente(clienteDTO);

					pedidoDTO.setTotal(rs.getBigDecimal("TOTAL"));
					pedidoDTO.setObservacao(rs.getString("OBSERVACAO"));

					pedidoDTO.setLanches(new LancheDAO().listarLanchesPorPedido(pedidoDTO.getCodigo()));

					pedidosDTO.add(pedidoDTO);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar todos os pedidos: " + e.getMessage());
		}

		return pedidosDTO;
	}

	public PedidoDTO listarPedidoPorCodigo(Integer codigo) {
		PedidoDTO pedidoDTO = null;

		String sql = "SELECT * FROM pedido WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					pedidoDTO = new PedidoDTO();
					pedidoDTO.setCodigo(rs.getInt("CODIGO"));
					pedidoDTO.setDataPedido(rs.getTimestamp("DATA_PEDIDO").toLocalDateTime());

					ClienteDTO clienteDTO = new ClienteDTO();
					clienteDTO.setCodigo(rs.getInt("CLIENTE_ID"));
					pedidoDTO.setCliente(clienteDTO);

					pedidoDTO.setTotal(rs.getBigDecimal("TOTAL"));
					pedidoDTO.setObservacao(rs.getString("OBSERVACAO"));

					pedidoDTO.setLanches(new LancheDAO().listarLanchesPorPedido(codigo));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar o lanche: " + e.getMessage());
		}

		return pedidoDTO;
	}

	public Set<PedidoDTO> listarTodosPedidosPorLanche(Integer codigoLanche) {
		Set<PedidoDTO> pedidosDTO = new LinkedHashSet<PedidoDTO>();

		String sql = "SELECT * FROM pedido p, lanche l, pedido_lanche pl " + "WHERE pl.codigo_pedido = p.codigo "
				+ "AND pl.codigo_lanche = l.codigo " + "AND l.codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigoLanche);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					PedidoDTO pedidoDTO = new PedidoDTO();
					pedidoDTO = new PedidoDTO();
					pedidoDTO.setCodigo(rs.getInt("CODIGO"));
					pedidoDTO.setDataPedido(rs.getTimestamp("DATA_PEDIDO").toLocalDateTime());

					ClienteDTO clienteDTO = new ClienteDTO();
					clienteDTO.setCodigo(rs.getInt("CLIENTE_ID"));
					pedidoDTO.setCliente(clienteDTO);

					pedidoDTO.setTotal(rs.getBigDecimal("TOTAL"));
					pedidoDTO.setObservacao(rs.getString("OBSERVACAO"));

					pedidoDTO.setLanches(new LancheDAO().listarLanchesPorPedido(pedidoDTO.getCodigo()));

					pedidosDTO.add(pedidoDTO);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar todos os pedidos pelo lanche: " + e.getMessage());
		}

		return pedidosDTO;
	}
}