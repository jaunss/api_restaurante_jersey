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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.joaogcm.api.restaurante.dto.PedidoDTO;
import br.com.joaogcm.api.restaurante.service.PedidoService;

@Path("/pedido")
public class PedidoEndpoint {

	@POST
	@Path("/criarPedido")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response criarPedido(PedidoDTO pedidoDTO) {
		PedidoDTO newPedidoDTO = new PedidoService().criarPedido(pedidoDTO);

		if (newPedidoDTO != null) {
			return Response.status(Response.Status.CREATED).entity(newPedidoDTO).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar o pedido.").build();
		}
	}

	@PUT
	@Path("/atualizarPedidoPorCodigo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizarPedidoPorCodigo(@QueryParam("codigo") Integer codigo, @Context PedidoDTO pedidoDTO) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do pedido é obrigatório.").build();
		}

		pedidoDTO = new PedidoService().listarPedidoPorCodigo(codigo);

		if (pedidoDTO != null) {
			PedidoDTO newPedidoDTO = new PedidoService().atualizarPedidoPorCodigo(codigo, pedidoDTO);

			if (newPedidoDTO != null) {
				return Response.status(Response.Status.OK).entity(newPedidoDTO).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar o pedido.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Pedido não encontrado.").build();
		}
	}

	@DELETE
	@Path("/removerPedidoPorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removerPedidoPorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do pedido é obrigatório.").build();
		}

		PedidoDTO pedidoDTO = new PedidoService().listarPedidoPorCodigo(codigo);

		if (pedidoDTO != null) {
			boolean isPedidoRemovido = new PedidoService().removerPedidoPorCodigo(codigo);

			if (isPedidoRemovido) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao remover o pedido.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Pedido não encontrado.").build();
		}
	}

	@GET
	@Path("/listarTodosPedidos")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodosPedidos() {
		Set<PedidoDTO> pedidosDTO = new PedidoService().listarTodosPedidos();

		return Response.status(Response.Status.OK).entity(pedidosDTO).build();
	}

	@GET
	@Path("/listarPedidoPorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarPedidoPorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do pedido é obrigatório.").build();
		}

		PedidoDTO pedidoDTO = new PedidoService().listarPedidoPorCodigo(codigo);

		if (pedidoDTO != null) {
			return Response.status(Response.Status.OK).entity(pedidoDTO).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Pedido não encontrado.").build();
		}
	}

	@GET
	@Path("/listarPedidosPorLanche")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodosPedidosPorLanche(@QueryParam("codigo") Integer codigoLanche) {
		if (codigoLanche == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do lanche é obrigatório.").build();
		}

		Set<PedidoDTO> pedidosDTO = new PedidoService().listarTodosPedidosPorLanche(codigoLanche);

		return Response.status(Response.Status.OK).entity(pedidosDTO).build();
	}
}