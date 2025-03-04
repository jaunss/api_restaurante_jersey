package br.com.joaogcm.api.restaurante.rs;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.joaogcm.api.restaurante.dto.ClienteDTO;
import br.com.joaogcm.api.restaurante.service.ClienteService;

@Path("/cliente")
public class ClienteEndpoint {

	@POST
	@Path("/criarCliente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response criarCliente(ClienteDTO clienteDTO) {
		ClienteDTO newClienteDTO = new ClienteService().criarCliente(clienteDTO);

		if (newClienteDTO != null) {
			return Response.status(Response.Status.CREATED).entity(newClienteDTO).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar o cliente.").build();
		}
	}

	@PUT
	@Path("/atualizarClientePorCodigo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizarClientePorCodigo(@QueryParam("codigo") Integer codigo, ClienteDTO clienteDTO) {
		if (codigo == null || clienteDTO == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("O código do cliente e/ou objeto cliente é obrigatório.").build();
		}

		ClienteDTO buscarClienteDTO = new ClienteService().listarClientePorCodigo(codigo);

		if (buscarClienteDTO != null) {
			ClienteDTO newClienteDTO = new ClienteService().atualizarClientePorCodigo(buscarClienteDTO.getCodigo(),
					clienteDTO);

			if (newClienteDTO != null) {
				return Response.status(Response.Status.OK).entity(newClienteDTO).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar o cliente.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Cliente não encontrado.").build();
		}
	}

	@DELETE
	@Path("/removerClientePorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removerClientePorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do cliente é obrigatório.").build();
		}

		ClienteDTO clienteDTO = new ClienteService().listarClientePorCodigo(codigo);

		if (clienteDTO != null) {
			boolean isClienteRemovido = new ClienteService().removerClientePorCodigo(clienteDTO.getCodigo());

			if (isClienteRemovido) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao remover o cliente.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Cliente não encontrado.").build();
		}
	}

	@GET
	@Path("/listarTodosClientes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodosClientes() {
		Set<ClienteDTO> clientesDTO = new ClienteService().listarTodosClientes();

		return Response.status(Response.Status.OK).entity(clientesDTO).build();
	}

	@GET
	@Path("/listarClientePorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarClientePorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do cliente é obrigatório.").build();
		}

		ClienteDTO clienteDTO = new ClienteService().listarClientePorCodigo(codigo);

		if (clienteDTO != null) {
			return Response.status(Response.Status.OK).entity(clienteDTO).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Cliente não encontrado.").build();
		}
	}
}