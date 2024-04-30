package com.krakedev.inventarios.entidades;

public class tipoDocumento {
	private String codigo;
	private String descripcion;
	public tipoDocumento() {
		
	}
	public tipoDocumento(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public String toString() {
		return "tipoDocumento [codigo=" + codigo + ", descripcion=" + descripcion + "]";
	}
	
}
