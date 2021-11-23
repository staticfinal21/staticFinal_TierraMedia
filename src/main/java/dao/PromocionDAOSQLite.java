package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import tierraMedia.Atraccion;
import tierraMedia.Promocion;
import tierraMedia.PromocionAbsoluta;
import tierraMedia.PromocionAxB;
import tierraMedia.PromocionPorcentual;

public class PromocionDAOSQLite {

	public List<Promocion> findAll(List<Atraccion> atracciones) {
		List<Promocion> listaPromociones = new ArrayList<Promocion>();
		try {
			String sql = "		SELECT promociones.*, group_concat(atracciones.atraccion_id) AS 'atracciones' \r\n"
					+ "		from atracciones\r\n" + "		join atraccion_promocion\r\n"
					+ "			on atraccion_promocion.atraccion_id = atracciones.atraccion_id\r\n"
					+ "		join promociones\r\n"
					+ "			on  promociones.promocion_id = atraccion_promocion.promocion_id\r\n"
					+ "		GROUP BY promociones.promocion_id\r\n";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				listaPromociones.addAll(toPromocion(result, atracciones));
			}
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return listaPromociones;
	}

	private List<Promocion> toPromocion(ResultSet result, List<Atraccion> atracciones) {
		List<Atraccion> atraccionesDePromocion = new ArrayList<Atraccion>();
		List<Promocion> promociones = new ArrayList<Promocion>();
		try {
			String[] stringListaAtracciones = result.getString("atracciones").split(",");

			for (int i = 0; i < stringListaAtracciones.length; i++)
				atraccionesDePromocion.add(buscarAtraccion(stringListaAtracciones[i], atracciones));

			if (result.getString("tipo").equals("AxB"))
				promociones.add(new PromocionAxB(result.getInt("promocion_id"), result.getString("nombre"),
						result.getInt("cantidad_gratis"), atraccionesDePromocion));

			if (result.getString("tipo").equals("Porcentual"))
				promociones.add(new PromocionPorcentual(result.getInt("promocion_id"), result.getString("nombre"),
						result.getDouble("descuento"), atraccionesDePromocion));

			if (result.getString("tipo").equals("Absoluta"))
				promociones.add(new PromocionAbsoluta(result.getInt("promocion_id"), result.getString("nombre"),
						result.getDouble("precio"), atraccionesDePromocion));

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return promociones;
	}

	private static Atraccion buscarAtraccion(String cadena, List<Atraccion> listaAtracciones) {
		Atraccion a = null;
		for (Atraccion cadaAtraccion : listaAtracciones) {
			if (String.valueOf(cadaAtraccion.getId()).equals(cadena)) {
				a = cadaAtraccion;
			}
		}
		return a;
	}

}
