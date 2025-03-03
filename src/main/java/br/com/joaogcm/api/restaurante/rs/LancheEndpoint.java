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

import br.com.joaogcm.api.restaurante.dto.LancheDTO;
import br.com.joaogcm.api.restaurante.service.LancheService;

@Path("/lanche")
public class LancheEndpoint {

	@POST
	@Path("/criarLanche")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response criarLanche(LancheDTO lancheDTO) {
		LancheDTO newLancheDTO = new LancheService().criarLanche(lancheDTO);

		if (newLancheDTO != null) {
			return Response.status(Response.Status.CREATED).entity(newLancheDTO).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar o lanche.").build();
		}
	}

	@PUT
	@Path("/atualizarLanchePorCodigo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response atualizarLanchePorCodigo(@QueryParam("codigo") Integer codigo, LancheDTO lancheDTO) {
		if (codigo == null || lancheDTO == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("O código do lanche e/ou objeto lanche é obrigatório.").build();
		}

		LancheDTO buscarLancheDTO = new LancheService().listarLanchePorCodigo(codigo);

		if (buscarLancheDTO != null) {
			LancheDTO newLancheDTO = new LancheService().atualizarLanchePorCodigo(buscarLancheDTO.getCodigo(),
					lancheDTO);

			if (newLancheDTO != null) {
				return Response.status(Response.Status.OK).entity(newLancheDTO).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar o lanche.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Lanche não encontrado.").build();
		}
	}

	@DELETE
	@Path("/removerLanchePorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removerLanchePorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do lanche é obrigatório.").build();
		}

		LancheDTO lancheDTO = new LancheService().listarLanchePorCodigo(codigo);

		if (lancheDTO != null) {
			boolean isLancheRemovido = new LancheService().removerLanchePorCodigo(lancheDTO.getCodigo());

			if (isLancheRemovido) {
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao remover o lanche.")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Lanche não encontrado.").build();
		}
	}

	@GET
	@Path("/listarTodosLanches")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarTodosLanches() {
		Set<LancheDTO> lanchesDTO = new LancheService().listarTodosLanches();

		return Response.status(Response.Status.OK).entity(lanchesDTO).build();
	}

	@GET
	@Path("/listarLanchePorCodigo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listarLanchePorCodigo(@QueryParam("codigo") Integer codigo) {
		if (codigo == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("O código do lanche é obrigatório.").build();
		}

		LancheDTO newLancheDTO = new LancheService().listarLanchePorCodigo(codigo);

		if (newLancheDTO != null) {
			return Response.status(Response.Status.OK).entity(newLancheDTO).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("Lanche não encontrado.").build();
		}
	}
}