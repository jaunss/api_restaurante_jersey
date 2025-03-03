package br.com.joaogcm.api.restaurante.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBancoDeDados {

	private static Connection conn = null;
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	public static synchronized Connection getConexao() {
		Properties properties = new Properties();

		try {
			if (conn == null) {
				ClassLoader classLoader = ConexaoBancoDeDados.class.getClassLoader();

				properties.load(classLoader.getResourceAsStream("database.properties"));

				String urlConexao = properties.getProperty("URL");
				String usuario = properties.getProperty("USUARIO");
				String senha = properties.getProperty("SENHA");

				Class.forName(DRIVER);

				conn = DriverManager.getConnection(urlConexao, usuario, senha);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Não foi possível recuperar os dados de conexão com o banco de dados: " + e.getMessage());
		}

		return conn;
	}

	public void fecharConexao(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void fecharPreparedStatement(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void fecharResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}