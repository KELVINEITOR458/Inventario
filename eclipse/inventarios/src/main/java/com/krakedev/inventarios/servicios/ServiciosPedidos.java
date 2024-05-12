package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.PedidosBDD;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("pedidos")
public class ServiciosPedidos {
	
	@Path("registrar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crearPedido(Pedido pedido) {
		PedidosBDD pedBDD = new PedidosBDD();
		try {
			pedBDD.insertar(pedido);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("recibir")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recibirPedido(Pedido pedido) {
		PedidosBDD pedBDD = new PedidosBDD();
		try {
			pedBDD.recibir(pedido);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("buscar/{subcadena}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("subcadena")String subcadena){
		PedidosBDD pe = new PedidosBDD();
		ArrayList<Pedido> pedidos = null;
		try {
			pedidos = pe.buscar(subcadena);
			return Response.ok(pedidos).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
