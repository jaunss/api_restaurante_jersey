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
import br.com.joaogcm.api.restaurante.dto.ClienteDTO;

public class ClienteDAO {

	public ClienteDAO() {

	}

	public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
		ClienteDTO newClienteDTO = null;

		String sql = "INSERT INTO cliente (nome, email, telefone, cpf, senha) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = ConexaoBancoDeDados.getConexao();
				PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, clienteDTO.getNome());
			ps.setString(2, clienteDTO.getEmail());
			ps.setString(3, clienteDTO.getTelefone());
			ps.setString(4, clienteDTO.getCpf());
			ps.setString(5, clienteDTO.getSenha());

			if (ps.executeUpdate() > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					while (rs.next()) {
						int codigoGerado = rs.getInt(1);

						newClienteDTO = listarClientePorCodigo(codigoGerado);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível criar o cliente: " + e.getMessage());
		}

		return newClienteDTO;
	}

	public ClienteDTO atualizarClientePorCodigo(Integer codigo, ClienteDTO clienteDTO) {
		if (clienteDTO.getNome() == null && clienteDTO.getEmail() == null && clienteDTO.getTelefone() == null) {
			throw new IllegalArgumentException(
					"Pelo menos um campo (nome, email, telefone) deve ser fornecido para atualização.");
		}

		String sql = "UPDATE cliente SET ";

		List<Object> parametros = new ArrayList<Object>();
		boolean isCampoAdicionado = false;

		if (clienteDTO.getNome() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " nome = ? ";
			parametros.add(clienteDTO.getNome());
			isCampoAdicionado = true;
		}

		if (clienteDTO.getEmail() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " email = ? ";
			parametros.add(clienteDTO.getEmail());
			isCampoAdicionado = true;
		}

		if (clienteDTO.getTelefone() != null) {
			if (isCampoAdicionado) {
				sql += ", ";
			}

			sql += " telefone = ? ";
			parametros.add(clienteDTO.getTelefone());
			isCampoAdicionado = true;
		}

		sql += " WHERE codigo = ? ";
		parametros.add(codigo);

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < parametros.size(); i++) {
				ps.setObject(i + 1, parametros.get(i));
			}

			if (ps.executeUpdate() > 0) {
				return listarClientePorCodigo(codigo);
			} else {
				return null;
			}

		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível atualizar o cliente: " + e.getMessage());
		}
	}

	public boolean removerClientePorCodigo(Integer codigo) {
		String sql = "DELETE FROM cliente WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigo);

			if (ps.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível remover o cliente: " + e.getMessage());
		}
	}

	public Set<ClienteDTO> listarTodosClientes() {
		Set<ClienteDTO> clientesDTO = new LinkedHashSet<ClienteDTO>();

		String sql = "SELECT * FROM cliente";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ClienteDTO clienteDTO = new ClienteDTO();
					clienteDTO.setCodigo(rs.getInt("CODIGO"));
					clienteDTO.setNome(rs.getString("NOME"));
					clienteDTO.setEmail(rs.getString("EMAIL"));
					clienteDTO.setTelefone(rs.getString("TELEFONE"));

					clientesDTO.add(clienteDTO);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar todos os clientes: " + e.getMessage());
		}

		return clientesDTO;
	}

	public ClienteDTO listarClientePorCodigo(int codigoGerado) {
		ClienteDTO clienteDTO = null;

		String sql = "SELECT * FROM cliente WHERE codigo = ?";

		try (Connection conn = ConexaoBancoDeDados.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, codigoGerado);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					clienteDTO = new ClienteDTO();
					clienteDTO.setCodigo(rs.getInt("CODIGO"));
					clienteDTO.setNome(rs.getString("NOME"));
					clienteDTO.setEmail(rs.getString("EMAIL"));
					clienteDTO.setTelefone(rs.getString("TELEFONE"));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Não foi possível listar o cliente: " + e.getMessage());
		}

		return clienteDTO;
	}
}