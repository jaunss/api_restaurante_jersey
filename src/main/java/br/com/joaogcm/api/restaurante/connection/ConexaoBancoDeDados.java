package br.com.joaogcm.api.restaurante.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConexaoBancoDeDados {

	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static HikariDataSource hikariDataSource = null;

	static {
		Properties properties = new Properties();

		try {
			ClassLoader classLoader = ConexaoBancoDeDados.class.getClassLoader();

			properties.load(classLoader.getResourceAsStream("database.properties"));

			String urlConexao = properties.getProperty("URL");
			String usuario = properties.getProperty("USUARIO");
			String senha = properties.getProperty("SENHA");

			HikariConfig hikariConfig = new HikariConfig();
			hikariConfig.setJdbcUrl(urlConexao);
			hikariConfig.setUsername(usuario);
			hikariConfig.setPassword(senha);
			hikariConfig.setDriverClassName(DRIVER);
			hikariConfig.setMaximumPoolSize(10);
			hikariConfig.setMinimumIdle(2);
			hikariConfig.setIdleTimeout(30000);
			hikariConfig.setMaxLifetime(1800000);
			hikariConfig.setConnectionTimeout(30000);

			hikariDataSource = new HikariDataSource(hikariConfig);
		} catch (Exception e) {
			throw new RuntimeException(
					"Não foi possível recuperar os dados de conexão com o banco de dados: " + e.getMessage());
		}
	}

	public static Connection getConexao() throws SQLException {
		return hikariDataSource.getConnection();
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