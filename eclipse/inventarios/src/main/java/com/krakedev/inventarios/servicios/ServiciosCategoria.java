package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.CategoriasBDD;
import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.tipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("categoria")
public class ServiciosCategoria {
	@Path("crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crearProducto(Categoria categoria) {
		CategoriasBDD prodBDD = new CategoriasBDD();
		try {
			prodBDD.insertar(categoria);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("actualizar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizarProducto(Categoria categoria) {
		CategoriasBDD prodBDD = new CategoriasBDD();
		try {
			prodBDD.actualizar(categoria);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("recuperar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerClientes(){
		CategoriasBDD pro = new CategoriasBDD();
		ArrayList<Categoria> tipoD = null;
		try {
			tipoD = pro.recuperarCategoria();
			return Response.ok(tipoD).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
