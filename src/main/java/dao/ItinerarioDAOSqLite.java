package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import tierraMedia.Atraccion;
import tierraMedia.Ofertable;
import tierraMedia.Promocion;
import tierraMedia.Usuario;

public class ItinerarioDAOSqLite {

	public List<Integer> findAtraccionesItinerario(Usuario usuario) {
		List<Integer> listaAtraccionesID = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT itinerario.*\r\n" + "FROM itinerario\r\n" + "join usuarios\r\n"
					+ "	on itinerario.usuario_id = ?";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, usuario.getId());
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getInt(4) != 0) {
					listaAtraccionesID.add(result.getInt(4));
				}
			}
		} catch (Exception e) {
			throw new MissingDataException(e);
		}

		return listaAtraccionesID;
	}

	public List<Integer> findPromocionesItinerario(Usuario usuario) {
		List<Integer> listaPromocionesID = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT itinerario.*\r\n" + "FROM itinerario\r\n" + "join usuarios\r\n"
					+ "	on itinerario.usuario_id = ?";
			Connection connection = ConnectionProvider.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, usuario.getId());
			ResultSet result = statement.executeQuery();

			while (result.next()) {

				if (result.getInt("promocion_id") != 0) {
					listaPromocionesID.add(result.getInt("promocion_id"));
				}
			}
		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return listaPromocionesID;
	}

	public List<Ofertable> findItinerario(Usuario usuario, List<Atraccion> atracciones, List<Promocion> promociones) {
		List<Ofertable> itinerario = new ArrayList<>();

		for (Integer atraccion_id : findAtraccionesItinerario(usuario))
			for (Atraccion cadaAtraccion : atracciones) {
				if (atraccion_id == cadaAtraccion.getId()) {
					itinerario.add(cadaAtraccion);
				}
			}

		for (Integer promo_id : findPromocionesItinerario(usuario))
			for (Promocion cadaPromo : promociones) {
				if (promo_id == cadaPromo.getId()) {
					itinerario.add(cadaPromo);
				}
			}
		return itinerario;
	}

	public int insertarItinerario(Usuario usuario, Ofertable oferta) {
		int rows = 0;
		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "INSERT INTO itinerario (usuario_id, promocion_id, atraccion_id) VALUES(?,?,?)  ";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, usuario.getId());

			if (oferta.soyPromocion()) {
				statement.setInt(2, oferta.getId());
			} else {
				statement.setInt(3, oferta.getId());
			}
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

	public int eliminarItinerario(Usuario usuario, Ofertable oferta) {
		int rows = 0;
		try {
			Connection connection = ConnectionProvider.getConnection();
			String sql = "DELETE FROM itinerario WHERE usuario_id = ? AND (promocion_id = ? or promocion_id IS NULL) AND (atraccion_id = ? or atraccion_id IS NULL)  ";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, usuario.getId());

			if (oferta.soyPromocion()) {
				statement.setInt(2, oferta.getId());
			} else {
				statement.setInt(3, oferta.getId());
			}
			rows = statement.executeUpdate();

		} catch (Exception e) {
			throw new MissingDataException(e);
		}
		return rows;
	}

}
