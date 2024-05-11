package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws KrakeDevException{
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;
		
		Date fechaActual = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_pedido(proveedor, fecha, estado)"
					+ "values(?,?,?);", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");
		
			ps.executeUpdate();
			
			
			rsClave = ps.getGeneratedKeys();
			if(rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}
			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			DetallePedido det;
			for(int i=0; i<detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet=con.prepareStatement("insert into detalle_pedido(cabecera_pedido, producto, cantidad, subtotal, cantidad_recibida)"
						+ "values(?,?,?,?,?)");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				
				psDet.setInt(5, 0);
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(4, subtotal);
				psDet.executeUpdate();
			}
			
			
			
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al crear pedido. Detalle: " + e.getMessage());
		}
	}
	
	public void recibir(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psHis = null;

		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime()); 
		
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("update cabecera_pedido set estado='R' where numero = ? " );
			ps.setInt(1, pedido.getCodigo());
		
			ps.executeUpdate();
			
			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			DetallePedido det;
			for(int i=0; i<detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet=con.prepareStatement("update detalle_pedido set cantidad_recibida = ?, subtotal = ? "
						+ "where codigo = ?");
				psDet.setInt(1, det.getCantidadRecibida());
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadRecibida());
				BigDecimal subtotal = pv.multiply(cantidad);
				psDet.setBigDecimal(2, subtotal);
				psDet.setInt(3, det.getCodigo());
				psDet.executeUpdate();
				
				psHis = con.prepareStatement("insert into historial_stock(fecha, referencia, producto, cantidad)"
						+ "values(?,?,?,?)");
				psHis.setTimestamp(1, fechaHoraActual);
				psHis.setString(2, "PEDIDO " + pedido.getCodigo());
				psHis.setInt(3, det.getProducto().getCodigo());
				psHis.setInt(4, det.getCantidadRecibida());
				
				System.out.println("Se crea historial");
				psHis.executeUpdate();
				
				
			}
			
			
			
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al crear pedido. Detalle: " + e.getMessage());
		}
	}
	
	public void insertarVentas(Pedido pedido) throws KrakeDevException{
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement psDet = null;
        PreparedStatement psCv = null;
        ResultSet rsClave = null;
        int codigoCabeceraVentas = 0;
        
        Date fechaActual = new Date();
        Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime()); 
        
        try {
            con = ConexionBDD.obtenerConexion();
            ps = con.prepareStatement("insert into cabecera_ventas(fecha, total_sin_iva, iva, total) " 
                    + "values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, fechaHoraActual);
            ps.setBigDecimal(2, BigDecimal.ZERO); 
            ps.setBigDecimal(3, BigDecimal.ZERO); 
            ps.setBigDecimal(4, BigDecimal.ZERO); 
        
            ps.executeUpdate();
            
            rsClave = ps.getGeneratedKeys();
            if(rsClave.next()) {
                codigoCabeceraVentas = rsClave.getInt(1);
            }
            
            ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
            for(DetallePedido det : detallesPedido) {
                psDet=con.prepareStatement("insert into detalle_ventas(cabecera_ventas,codigo_producto,cantidad,precio_venta,subtotal,subtotal_iva)"
                        + "values(?,?,?,?,?,?)");
                psDet.setInt(1, codigoCabeceraVentas);
                psDet.setInt(2, det.getProducto().getCodigo());
                psDet.setInt(3, det.getCantidadSolicitada());
                psDet.setBigDecimal(4, det.getProducto().getPrecioVenta());
                BigDecimal pv = det.getProducto().getPrecioVenta();
                BigDecimal cantidad = new BigDecimal(det.getCantidadRecibida());
                BigDecimal subtotal = pv.multiply(cantidad);
                double subtotalIvaDouble;
                BigDecimal subtotalIva;
                psDet.setBigDecimal(5, subtotal);
                if(det.getProducto().isTieneIVA()) {
                    double subtotalDouble = subtotal.doubleValue();
                    double ivaDet = subtotalDouble * 0.12; 
                    subtotalIvaDouble = subtotalDouble + ivaDet;
                    subtotalIva = BigDecimal.valueOf(subtotalIvaDouble);
                } else {
                    subtotalIva = subtotal;
                }
                psDet.setBigDecimal(6, subtotalIva);
                psDet.executeUpdate();
            }
            
            BigDecimal totalSinIva = BigDecimal.ZERO;
            BigDecimal iva = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (DetallePedido detalle : detallesPedido) {
                BigDecimal precioVenta = detalle.getProducto().getPrecioVenta();
                BigDecimal cantidad = new BigDecimal(detalle.getCantidadRecibida());
                BigDecimal subtotal = precioVenta.multiply(cantidad);

                totalSinIva = totalSinIva.add(subtotal);

                if (detalle.getProducto().isTieneIVA()) {
                    double ivaDet = subtotal.doubleValue() * 0.12;
                    iva = iva.add(BigDecimal.valueOf(ivaDet));
                }
            }

            total = totalSinIva.add(iva);

            psCv = con.prepareStatement("update cabecera_ventas set total_sin_iva = ?, iva = ?, total = ? where codigo = ?");
            psCv.setBigDecimal(1, totalSinIva);
            psCv.setBigDecimal(2, iva);
            psCv.setBigDecimal(3, total);
            psCv.setInt(4, codigoCabeceraVentas);
            psCv.executeUpdate();
            
            for(DetallePedido det : detallesPedido) {
                psCv = con.prepareStatement("insert into historial_stock(fecha, referencia, producto, cantidad)"
                        + "values(?,?,?,?)");
                psCv.setTimestamp(1, fechaHoraActual);
                psCv.setString(2, "VENTA " + pedido.getCodigo()); 
                psCv.setInt(3, det.getProducto().getCodigo());
                psCv.setInt(4, -det.getCantidadRecibida()); 
                psCv.executeUpdate();
            }

            System.out.println("Se crea historial");

        } catch (KrakeDevException e) {
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new KrakeDevException("Error al crear pedido. Detalle: " + e.getMessage());
        } 
    }
	

}
