package br.com.joaogcm.api.restaurante.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.joaogcm.api.restaurante.rs.ClienteEndpoint;
import br.com.joaogcm.api.restaurante.rs.LancheEndpoint;
import br.com.joaogcm.api.restaurante.rs.PedidoEndpoint;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@ApplicationPath("rs")
@OpenAPIDefinition(
	info = @Info(
			title = "Documentação da API REST Restaurante Caseiro",
			version = "1.1.0",
			description = "Documentação da API REST Restaurante Caseiro com JAX-RS (Jersey) e Swagger"),
	servers = {
			@Server(url = "/",
					description = "Servidor local")
			}
	)
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		classes.add(ClienteEndpoint.class);
		classes.add(PedidoEndpoint.class);
		classes.add(LancheEndpoint.class);
		classes.add(OpenApiResource.class);

		return classes;
	}
}