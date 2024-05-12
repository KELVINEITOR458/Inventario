package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.tipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class CategoriasBDD {
	public void insertar(Categoria categoria) throws KrakeDevException{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into categorias(nombre, categoria_padre) "
					+ "values(?,?)");
			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			ps.executeUpdate();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar categoria. Detalle: " + e.getMessage());
		}
		
	}
	
	public void actualizar(Categoria categoria) throws KrakeDevException{
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("update categorias set nombre=?, categoria_padre=? "
					+ "where codigo_cat =" + categoria.getCodigo());
			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			ps.executeUpdate();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar categoria. Detalle: " + e.getMessage());
		}
		
	}
	
	public ArrayList<Categoria> recuperarCategoria() throws KrakeDevException {
		ArrayList<Categoria> tipoD = new ArrayList<Categoria>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Categoria tp = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("select * from categorias");
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigo = rs.getInt("codigo_cat");
				String nombre = rs.getString("nombre");
				
				tp = new Categoria(codigo,nombre);
				tipoD.add(tp);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle: " + e.getMessage());
		}

		return tipoD;
	}
}
