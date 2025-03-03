package br.com.joaogcm.api.restaurante.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.joaogcm.api.restaurante.rs.ClienteEndpoint;
import br.com.joaogcm.api.restaurante.rs.LancheEndpoint;
import br.com.joaogcm.api.restaurante.rs.PedidoEndpoint;

@ApplicationPath("rs")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		classes.add(ClienteEndpoint.class);
		classes.add(PedidoEndpoint.class);
		classes.add(LancheEndpoint.class);

		return classes;
	}
}